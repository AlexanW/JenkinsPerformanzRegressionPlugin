package io.jenkins.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import leseDaten.LeseBasis;
import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;
import testDatenTypen.Status;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;
import testRegression.ITestVergleich;
import testRegression.TestVergleichen;

public class PluginMenuePostBuild extends Recorder{

    public final static String TESTWERTE_DATEINAME = "testWerte.txt"; 
    
    private String pfadZuBasen;
    
    private String pfadZuBuilds;
    
    private String pfadZuCPUundRAM;
    
    private boolean erstelleBasis;
    
    private int anzahlAnVergangenenBuilds;
    
    private double tolleranzFuerSchwankungenBasen;
    
    private double tolleranzFuerTestVergleich;
    
    private double tolleranzFuerBasenVergleich;
    
    private double timerIntervall;
    
    private boolean pruefeRegression;
    
    private boolean vergleicheBasis;
    
    private double aplhaWert;
    
    private String jUnitDateiName;
    
    @DataBoundConstructor
    public PluginMenuePostBuild (boolean pruefeRegression, String pfadZuCPUundRAM, 
            boolean erstelleBasis, int anzahlAnVergangenenBuilds, double tolleranzFuerSchwankungenBasen, 
            double tolleranzFuerBasenVergleich, double aplhaWert, boolean vergleicheBasis,
            double timerIntervall, String jUnitDateiName, double tolleranzFuerTestVergleich) {
//        this.pfadZuBasen = pfadZuBasen;
//        this.pfadZuBuilds = pfadZuBuilds;
        this.pfadZuCPUundRAM = pfadZuCPUundRAM;
        this.erstelleBasis = erstelleBasis;
        this.anzahlAnVergangenenBuilds = anzahlAnVergangenenBuilds;
        this.tolleranzFuerSchwankungenBasen = tolleranzFuerSchwankungenBasen;
        this.tolleranzFuerBasenVergleich = tolleranzFuerBasenVergleich;
        this.pruefeRegression = pruefeRegression;
        this.vergleicheBasis = vergleicheBasis;
        this.aplhaWert = aplhaWert;
        this.timerIntervall = timerIntervall;
        this.jUnitDateiName = jUnitDateiName;
        this.tolleranzFuerTestVergleich = tolleranzFuerTestVergleich;
    }
    
//    public String getPfadZuBasen() {
//        return pfadZuBasen;
//    }
//    
//    public String getPfadZuBuilds() {
//        return pfadZuBuilds;
//    }
    
    public double getTimerIntervall() {
        return timerIntervall;
    }
    
    public int getAnzahlAnVergangenenBuilds() {
        return anzahlAnVergangenenBuilds;
    }
    
    public boolean getErstelleBasis() {
        return erstelleBasis;
    }
    
    public boolean getPruefeRegression() {
        return pruefeRegression;
    }
    
    public double getTolleranzFuerSchwankungenBasen() {
        return tolleranzFuerSchwankungenBasen;
    }
    
    public boolean getVergleicheBasis() {
        return vergleicheBasis;
    }
    
    public double getAplhaWert() {
        return aplhaWert;
    }
    
    public double getTolleranzFuerBasenVergleich() {
        return tolleranzFuerBasenVergleich;
    }
    
    public String getPfadZuCPUundRAM() {
        return pfadZuCPUundRAM;
    }
    
    public String getJUnitDateiName ( ) {
        return jUnitDateiName;
    }
    
    public double getTolleranzFuerTestVergleich () {
        return tolleranzFuerTestVergleich;
    }
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        boolean enthaeltBasisDir = false;
        boolean basenDirErstellt = true;
        
        //Erster abschnitt: JUnitResults, Zweiter Part: Basen Dir
        pfadZuBasen =  build.getRootDir().getParentFile().getParent();
        pfadZuBuilds = build.getRootDir().getParent();
        if (!jUnitDateiName.contains(".xml")) {
            StringBuffer buffer = new StringBuffer(jUnitDateiName);
            buffer.append(".xml");
            jUnitDateiName = buffer.toString();
        }
        listener.getLogger().print("-----Starte RegressionTest-----\n" + jUnitDateiName);
        //Dir des Projektjobs: RootDir=, Parant1=Builds, Parent2=Projekt.
        File file = new File(pfadZuBasen);
        if (file.exists() && file.isDirectory()) {
            String[] tempString =  file.list();
            if (tempString != null) {
                for (String s: tempString) {
                    if (s.equals("basen")) {
                       enthaeltBasisDir = true; 
                       pfadZuBasen = file.getAbsolutePath() + "/basen";
                    }
                }
            }
            if(!enthaeltBasisDir) {
              listener.getLogger().print("-----Erstelle Ordner fuer Basen-----\n"
                      + "in " + pfadZuBasen + "/basen\n");
              File tempFile = new File (pfadZuBasen + "/basen");
              basenDirErstellt = tempFile.mkdir();
              if (basenDirErstellt) {
                  pfadZuBasen = tempFile.getAbsolutePath();
              }
            }
            //Wenn ein Build bis hier hin schon fehlgeschlagen ist, machen die Test keinen Sinn.
            if (build.getResult() != Result.FAILURE && build.getResult() != Result.ABORTED) {
                if (erstelleBasis && basenDirErstellt) {
                    erstelleBasen(listener.getLogger());
                }
                if (vergleicheBasis) {
                    vergleicheBasen(listener.getLogger());
                }
                if (pruefeRegression) {
                    pruefeRegression(build,  listener.getLogger()); 
                }
            }
        }
        return true;
    }
    
    public void erstelleBasen(PrintStream logger) {
        IErstelleBasis basis = new ErstelleBasis();
        if (timerIntervall == 0) {
            timerIntervall = 100;
        }
        logger.print("-----------------------------------\n"
                + "Erstelle Basen mit den Pfaden: \n"
                + "Die Build Ordner mit den Result Dateien:" + pfadZuBuilds + "\n"
                + "Der Ort an dem die Basen des Tests gespeichert werden:"+ pfadZuBasen +"\n"
                + "Die maximale Schwankung wird als:" + tolleranzFuerSchwankungenBasen + " festgelegt\n"
                + "Es werden die letzen " + anzahlAnVergangenenBuilds + " verwendet \n"
                + "Mit einem Timerintervall von: "  + timerIntervall + " \n"
                + "-------------------------------------------\n");
        IBasis erstellteBasis = basis.erstelleBasis(pfadZuBuilds, pfadZuBasen, 
                tolleranzFuerSchwankungenBasen, anzahlAnVergangenenBuilds,
                timerIntervall, jUnitDateiName, logger);
        if (erstellteBasis != null) {
            logger.print("Die erstellte Basis:\n"
                    + erstellteBasis.toString());
        } else {
            logger.print("Es konnte keine Basis erstellt werden.\n");
        }
        logger.print("-----------------------------------\n");
    }
    
    public void vergleicheBasen (PrintStream logger) {
        logger.print("Vergleiche die letzen zwei Basen miteinander.\n");
        IBasis basisNeu = null;
        IBasis basisAlt = null;
        LeseBasis lese = new LeseBasis();
        try {
            basisNeu = lese.leseObjektIBasisEin(pfadZuBasen + "/Neu.txt");
            basisAlt = lese.leseObjektIBasisEin(pfadZuBasen +  "/Alt.txt");
        } catch (FileNotFoundException e) {
            logger.print("!Eine der Basen existiert nicht!" + e.getMessage());
        } catch (IOException e) {
            logger.print("!Es ist ein Fehler beim Einlesen geschehen!" + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (basisAlt != null && basisNeu != null) {
            ITestVergleich verlgeich = new TestVergleichen();
            RegressionTestResult result = verlgeich.vergleicheBasen(basisNeu, basisAlt, tolleranzFuerBasenVergleich, aplhaWert);
            logger.print(result.getNachricht());
        }
    }
    
    public void pruefeRegression (AbstractBuild<?, ?> build, PrintStream logger) throws IOException {
        logger.print("Suche die JUnit Datei in: " + build.getRootDir() + "/" + jUnitDateiName +"\n");
        File fileUnit = new File(build.getRootDir().getAbsolutePath()+ "/" + jUnitDateiName);
        if (fileUnit.exists()) {
            ITestWerte tests = LeseJUnitResults.leseTestsXML(fileUnit.getAbsolutePath(), timerIntervall);
            if (tests != null) {
                try {
                    logger.print("Suche Auslastungen in: " + pfadZuCPUundRAM);
                    tests.setTestAuslastungen(LeseCPUundRAM.readAuslastung(pfadZuCPUundRAM));
                } catch (InterruptedException e1) {
                    logger.print("Fehler beim kopieren der Datei.");
                    e1.printStackTrace();
                }
                logger.print(tests.toString() + "\n");
                ITestVergleich vergleichen = new TestVergleichen();
                LeseBasis lese = new LeseBasis();
                RegressionTestResult testResultString = new RegressionTestResult();
                IBasis tempBasis = null;
                    try {
                        tempBasis = lese.leseObjektIBasisEin(pfadZuBasen + "/Neu.txt");
                    } catch (ClassNotFoundException e ) {
                        e.printStackTrace();
                    } catch (FileNotFoundException exc) {
                        logger.print("Es konnte keine Basis fuer einen Vergleich gefunden werden.");
                    }
                if (tempBasis != null) {
                    logger.print("Beginne Vergleich von neuen Testergebnissen und der neusten Basis.\n"
                            + "Dabei gilt eine erwartete Regression von " + tolleranzFuerTestVergleich + "\n"
                            + "Verglichen werden \n" + tempBasis.toString() + "und \n"
                            + tests.toString());
                        testResultString = vergleichen.vergleicheBasisMitWerten(tests, 
                               tempBasis, tolleranzFuerTestVergleich);
                    if (testResultString.getResutlDerTests() == Status.GROESSER) {
                        build.setResult(Result.FAILURE);
                    } else if (testResultString.getResutlDerTests() == Status.KLEINER) {
                        //Unstable fuer einen Run der nicht gesichert.
                        build.setResult(Result.UNSTABLE);
                    }
                } else {
                    logger.print("Fuer diesen Job wurde keine Basis in"
                            + " " + pfadZuBasen + "gefunden");  
                }
                logger.print(testResultString.getNachricht());
                LeseSchreibeTestWerte.schreibeTestWerte(
                        build.getRootDir().getAbsolutePath() + "/" + TESTWERTE_DATEINAME, tests);
            }
        } else {
            logger.print("Die jUnitResult.xml ist nocht nicht verfuegbar.");  
        } 
    }
    
    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher>{

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> arg0) {
            return true;
        }
        @Override
        public String getDisplayName() {
            return "Teste Performanz Regression";
        }    
    }
}
