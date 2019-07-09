package io.jenkins.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import leseDaten.LeseBasis;
import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;
import testDatenTypen.Status;
import testDatenTypen.TestWerte;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;
import testRegression.ITestVergleich;
import testRegression.TestVergleichen;
import testRegression.TesteRegression;

public class PluginMenueClass extends BuildWrapper{
    
    public final static String TESTWERTE_DATEINAME = "testWerte.res"; 
    
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
    public PluginMenueClass(boolean pruefeRegression, String pfadZuCPUundRAM, 
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
    public Environment setUp(AbstractBuild build,
            Launcher launcher,
            BuildListener listener) {
      
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
                    RegressionTestResult result = verlgeich.vergleicheBasen(basisNeu, basisAlt, tolleranzFuerBasenVergleich, 0.05);
                    listener.getLogger().print(result);
                }

            }
        }
 
        return new Environment() {      
            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener)
                    throws IOException, InterruptedException {
                if (pruefeRegression) {
                    listener.getLogger().print("Suche die JUnit Datei in: " + build.getRootDir() + "/" + jUnitDateiName + ".xml \n");
                    File file = new File(build.getRootDir() + "/" + jUnitDateiName + ".xml");
                    if (file.exists()) {
                        ITestWerte tests = LeseJUnitResults.leseTestsXML(file.getAbsolutePath(), timerIntervall);
                        tests.setTestAuslastungen(LeseCPUundRAM.readAuslastung(pfadZuCPUundRAM));
                        TestVergleichen vergleichen = new TestVergleichen();
                        LeseBasis lese = new LeseBasis();
                        RegressionTestResult testResultString = new RegressionTestResult();
                        try {
                            testResultString = vergleichen.vergleicheBasisMitWerten(tests, 
                                    lese.leseObjektIBasisEin(file.getAbsolutePath() + "/basen/Neu.txt"), 0.0);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (testResultString.getResutlDerTests() == Status.GROESSER) {
                            build.setResult(Result.FAILURE);
                        } else if (testResultString.getResutlDerTests() == Status.KLEINER) {
                            //Unstable fuer einen Run der nicht gesichert.
                            build.setResult(Result.UNSTABLE);
                        }
                        listener.getLogger().print(testResultString);
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                build.getRootDir().getAbsolutePath() + "/" + TESTWERTE_DATEINAME, tests);
                    } else {
                        listener.getLogger().print("Die jUnitResult.xml ist nocht nicht verfuegbar.");  
                    }   
                }
                return super.tearDown(build, listener);
            }
        };
    }
    
    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor{

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }
        @Override
        public String getDisplayName() {
            return "Teste Performanz Regression";
        }       
    }
}
