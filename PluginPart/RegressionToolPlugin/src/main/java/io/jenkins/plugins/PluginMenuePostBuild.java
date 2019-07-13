package io.jenkins.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    
    private double tolleranzFuerBasen;
    
    private double tolleranzFuerBasenVergleich;
    
    private double timerIntervall;
    
    private boolean pruefeRegression;
    
    private boolean vergleicheBasis;
    
    private double aplhaWert;
    
    private String jUnitDateiName;
    
    @DataBoundConstructor
    public PluginMenuePostBuild (boolean pruefeRegression, String pfadZuCPUundRAM, 
            boolean erstelleBasis, int anzahlAnVergangenenBuilds, double tolleranzFuerBasen, 
            double tolleranzFuerBasenVergleich, double aplhaWert, boolean vergleicheBasis,
            double timerIntervall, String jUnitDateiName) {
//        this.pfadZuBasen = pfadZuBasen;
//        this.pfadZuBuilds = pfadZuBuilds;
        this.pfadZuCPUundRAM = pfadZuCPUundRAM;
        this.erstelleBasis = erstelleBasis;
        this.anzahlAnVergangenenBuilds = anzahlAnVergangenenBuilds;
        this.tolleranzFuerBasen = tolleranzFuerBasen;
        this.tolleranzFuerBasenVergleich = tolleranzFuerBasenVergleich;
        this.pruefeRegression = pruefeRegression;
        this.vergleicheBasis = vergleicheBasis;
        this.aplhaWert = aplhaWert;
        this.timerIntervall = timerIntervall;
        this.jUnitDateiName = jUnitDateiName;
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
    
    public double getTolleranzFuerBasen() {
        return tolleranzFuerBasen;
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
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        boolean enthaeltBasisDir = false;
        boolean basenDirErstellt = true;
        
        //Erster abschnitt: JUnitResults, Zweiter Part: Basen Dir
        pfadZuBasen =  build.getRootDir().getParentFile().getParent() + "/basen";
        pfadZuBuilds = build.getRootDir().getParent();
        listener.getLogger().print("-----Starte RegressionTest-----\n");
        //Dir des Projektjobs: RootDir=, Parant1=Builds, Parent2=Projekt.
        File file = build.getRootDir().getParentFile().getParentFile();
        if (file.isDirectory()) {
            String[] tempString =  file.list();
            if (tempString != null) {
                for (String s: tempString) {
                    if (s.equals("basen")) {
                       enthaeltBasisDir = true; 
                    }
                }
            }
            if(!enthaeltBasisDir) {
              listener.getLogger().print("-----Erstelle Ordner fuer Basen-----\n"
                      + "in " + file.getAbsolutePath() + "/basen\n");
              File tempFile = new File (file.getAbsolutePath() + "/basen");
              basenDirErstellt = tempFile.mkdir();
            }
            if (erstelleBasis && basenDirErstellt) {
                IErstelleBasis basis = new ErstelleBasis();
                listener.getLogger().print("-----------------------------------\n"
                        + "Erstelle Basen mit den Pfaden: \n"
                        + "Die Build Ordner mit den Result Dateien:" + pfadZuBuilds + "\n"
                        + "Der Ort an dem die Basen des Tests gespeichert werden:"+ pfadZuBasen +"\n"
                        + "Die maximale Schwankung wird als:" + tolleranzFuerBasen + " festgelegt\n"
                        + "Es werden die letzen " + anzahlAnVergangenenBuilds + " verwendet \n"
                        + "Mit einem Timerintervall von: "  + timerIntervall + " \n"
                        + "-------------------------------------------\n");
                IBasis erstellteBasis = basis.erstelleBasis(pfadZuBuilds, pfadZuBasen, 
                        tolleranzFuerBasen, anzahlAnVergangenenBuilds,
                        timerIntervall);
                if (erstellteBasis != null) {
                    listener.getLogger().print("Die erstellte Basis:\n"
                            + erstellteBasis.toString());
                } else {
                    listener.getLogger().print("Es konnte keine Basis erstellt werden.");
                }
            }
            if (vergleicheBasis) {
                listener.getLogger().print("Vergleiche die letzen zwei Basen miteinander.\n");
                IBasis basisNeu = null;
                IBasis basisAlt = null;
                LeseBasis lese = new LeseBasis();
                try {
                    basisNeu = lese.leseObjektIBasisEin(file.getAbsolutePath() + "/basen/Neu.txt");
                    basisAlt = lese.leseObjektIBasisEin(file.getAbsolutePath() + "/basen/Alt.txt");
                } catch (FileNotFoundException e) {
                    listener.getLogger().print("!Eine der Basen existiert nicht!" + e.getMessage());
                } catch (IOException e) {
                    listener.getLogger().print("!Ein Fehler beim Einlesen ist geschehen!" + e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (basisAlt != null && basisNeu != null) {
                    ITestVergleich verlgeich = new TestVergleichen();
                    RegressionTestResult result = verlgeich.vergleicheBasen(basisNeu, basisAlt, tolleranzFuerBasenVergleich, aplhaWert);
                    listener.getLogger().print(result.getNachricht());
                }

            }
            if (pruefeRegression) {
                listener.getLogger().print("Suche die JUnit Datei in: " + build.getRootDir() + "/" + jUnitDateiName + ".xml \n");
                File fileUnit = new File(build.getRootDir().getAbsolutePath());
                if (fileUnit.exists()) {
                    ITestWerte tests = LeseJUnitResults.leseTestsXML(fileUnit.getAbsolutePath(), timerIntervall);
                    if (tests != null) {
                        tests.setTestAuslastungen(LeseCPUundRAM.readAuslastung(pfadZuCPUundRAM));
                        listener.getLogger().print(tests.toString() + "\n");
                        ITestVergleich vergleichen = new TestVergleichen();
                        LeseBasis lese = new LeseBasis();
                        RegressionTestResult testResultString = new RegressionTestResult();
                        IBasis tempBasis = null;
                        try {
                            tempBasis = lese.leseObjektIBasisEin(file.getAbsolutePath() + "/basen/Neu.txt");
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        if (tempBasis != null) {
                            listener.getLogger().print("Beginne Vergleich von neuen Testergebnissen und der neusten Basis.\n"
                                    + "Dabei gilt eine erwartete Regression von " + (tolleranzFuerBasenVergleich != 0 ? tolleranzFuerBasenVergleich : 0.2) + "\n"
                                    + "Verglichen werden \n" + tempBasis.toString() + "und \n"
                                    + tests.toString());
                                testResultString = vergleichen.vergleicheBasisMitWerten(tests, 
                                       tempBasis, tolleranzFuerBasenVergleich);
                            if (testResultString.getResutlDerTests() == Status.GROESSER) {
                                build.setResult(Result.FAILURE);
                            } else if (testResultString.getResutlDerTests() == Status.KLEINER) {
                                //Unstable fuer einen Run der nicht gesichert.
                                build.setResult(Result.UNSTABLE);
                            }
                        }
                        listener.getLogger().print(testResultString.getNachricht());
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                build.getRootDir().getAbsolutePath() + "/" + TESTWERTE_DATEINAME, tests);
                    }
                } else {
                    listener.getLogger().print("Die jUnitResult.xml ist nocht nicht verfuegbar.");  
                }   
            }
        }
        return true;
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
            return "Teste Performanz Regression POST";
        }    
    }
}
