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
import testDatenTypen.IBasis;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;
import testRegression.ITestVergleich;
import testRegression.TestVergleichen;

public class PluginMenueClass extends BuildWrapper{
    
    private String pfadZuBasen;
    
    private String pfadZuBuilds;
    
    private boolean erstelleBasis;
    
    private int anzahlAnVergangenenBuilds;
    
    private double tolleranzFuerBasen;
    
    private double tolleranzFuerBasenVergleich;
    
    private boolean pruefeRegression;
    
    private boolean vergleicheBasis;
    
    @DataBoundConstructor
    public PluginMenueClass(String pfadZuBasen, String pfadZuBuilds) {
        this.pfadZuBasen = pfadZuBasen;
        this.pfadZuBuilds = pfadZuBuilds;
    }
    
    public String getPfadZuBasen() {
        return pfadZuBasen;
    }
    
    public String getPfadZuBuilds() {
        return pfadZuBuilds;
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
    
    
    @Override
    public Environment setUp(AbstractBuild build,
            Launcher launcher,
            BuildListener listener) {
      
        boolean enthaeltBasisDir = false;
        
        //Dir des Projektjobs: RootDir=Buildnumber, Parant1=Builds, Parent2=Projekt.
        File file = build.getRootDir().getParentFile().getParentFile();
        if (file.isDirectory()) {
            for (String s: file.list()) {
                if (s.equals("basen")) {
                   enthaeltBasisDir = true; 
                }
            }
            if(!enthaeltBasisDir) {
              File tempFile = new File (file.getAbsolutePath() + "/basen");
              tempFile.mkdir();
            }
            if (erstelleBasis) {
                IErstelleBasis basis = new ErstelleBasis();
                //Erster abschnitt: JUnitResults, Zweiter Part: Basen Dir
                basis.erstelleBasis(build.getRootDir().getParent(), file.getAbsolutePath() + "/basen", tolleranzFuerBasen, anzahlAnVergangenenBuilds);
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
                ITestVergleich verlgeich = new TestVergleichen();
                verlgeich.vergleicheBasen(basisNeu, basisAlt, tolleranzFuerBasenVergleich);
            }
        }
 
        return new Environment() {      
            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener)
                    throws IOException, InterruptedException {
                File fileDif = new File(build.getProject().getRootProject().getRootDir().getPath() + "/builds");
                if (fileDif != null) {
                    File[] tempList = fileDif.listFiles(); 
                    if (tempList != null) {
                        for (File f: tempList) {
                            if (f.isDirectory()) {
                                File[] tempListT =  f.listFiles();
                                if (tempListT != null) {
                                    for (File a : tempListT) {
                                        //listener.getLogger().print(a.getPath() + "................................_______________________________________________________\n");
                                    }
                                }
                            }
                        }
                    }
                }
                listener.getLogger().print("");
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
