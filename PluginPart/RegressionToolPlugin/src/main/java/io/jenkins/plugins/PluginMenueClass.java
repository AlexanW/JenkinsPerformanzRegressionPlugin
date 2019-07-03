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
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import leseDaten.LeseBasis;
import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.TestWerte;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;
import testRegression.ITestVergleich;
import testRegression.TestVergleichen;

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
    
    private String jUnitDateiName;
    
    @DataBoundConstructor
    public PluginMenueClass(String pfadZuBasen, String pfadZuBuilds, String pfadZuCPUundRAM, 
            boolean erstelleBasis, int anzahlAnVergangenenBuilds, double tolleranzFuerBasen, 
            double tolleranzFuerBasenVerlgeich, boolean pruefeRegression, boolean vergleicheBasis,
            double timerIntervall, String jUnitDateiName) {
        this.pfadZuBasen = pfadZuBasen;
        this.pfadZuBuilds = pfadZuBuilds;
        this.pfadZuCPUundRAM = pfadZuCPUundRAM;
        this.erstelleBasis = erstelleBasis;
        this.anzahlAnVergangenenBuilds = anzahlAnVergangenenBuilds;
        this.tolleranzFuerBasen = tolleranzFuerBasen;
        this.tolleranzFuerBasenVergleich = tolleranzFuerBasenVerlgeich;
        this.pruefeRegression = pruefeRegression;
        this.vergleicheBasis = vergleicheBasis;
        this.timerIntervall = timerIntervall;
        this.jUnitDateiName = jUnitDateiName;
    }
    
    public String getPfadZuBasen() {
        return pfadZuBasen;
    }
    
    public String getPfadZuBuilds() {
        return pfadZuBuilds;
    }
    
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
        listener.getLogger().print("-----Starte RegressionTest-----");
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
              File tempFile = new File (file.getAbsolutePath() + "/basen");
              basenDirErstellt = tempFile.mkdir();
            }
            if (erstelleBasis && basenDirErstellt) {
                IErstelleBasis basis = new ErstelleBasis();
                //Erster abschnitt: JUnitResults, Zweiter Part: Basen Dir
                if (pfadZuBasen.isEmpty()) {
                    pfadZuBasen =  file.getAbsolutePath() + "/basen";
                }
                if (pfadZuBuilds.isEmpty()) {
                    pfadZuBuilds = build.getRootDir().getParent();
                }
                basis.erstelleBasisOhneMessungen(pfadZuBuilds, pfadZuBasen, tolleranzFuerBasen, anzahlAnVergangenenBuilds, timerIntervall);
            }
            if (vergleicheBasis) {
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
                }

                if (basisAlt != null && basisNeu != null) {
                    ITestVergleich verlgeich = new TestVergleichen();
                    String result = verlgeich.vergleicheBasen(basisNeu, basisAlt, tolleranzFuerBasenVergleich, 0.05);
                    listener.getLogger().print(result);
                }

            }
        }
 
        return new Environment() {      
            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener)
                    throws IOException, InterruptedException {
                File file = new File(build.getRootDir() + "/" + jUnitDateiName + ".xml");
                ITestWerte tests = new TestWerte();
                if (file.exists()) {
                    tests = LeseJUnitResults.leseTestsXML(file.getAbsolutePath(), timerIntervall);
                    tests.setTestAuslastungen(LeseCPUundRAM.readAuslastung(pfadZuCPUundRAM));
                } else {
                    listener.getLogger().print("Die jUnitResult.xml ist nocht nicht verfuegbar.");   
                }
                TestVergleichen vergleichen = new TestVergleichen();
                LeseBasis lese = new LeseBasis();
                String testResultString = vergleichen.vergleicheBasisMitWerten(tests, 
                        lese.leseObjektIBasisEin(file.getAbsolutePath() + "/basen/Neu.txt"), 0.0);
                listener.getLogger().print(testResultString);
                LeseSchreibeTestWerte.schreibeTestWerte(
                        build.getRootDir().getAbsolutePath() + "/" + TESTWERTE_DATEINAME, tests);
                return super.tearDown(build, listener);
            }
        };
    }
    
    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor{

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            // TODO Auto-generated method stub
            return true;
        }
        @Override
        public String getDisplayName() {
            return "Teste Performanz Regression";
        }
        
        public FormValidation doCreateBase(@QueryParameter("pfadZuBasen") final String pfadZuBasen) throws IOException, InterruptedException {
            FormValidation valid = FormValidation.ok("pfadZuBasen");
            return valid;
        }
        
    }
}
