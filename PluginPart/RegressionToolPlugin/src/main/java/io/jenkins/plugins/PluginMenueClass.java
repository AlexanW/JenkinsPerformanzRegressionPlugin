package io.jenkins.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.tools.ant.taskdefs.TempFile;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import jenkins.model.StandardArtifactManager;

import leseDaten.LeseJUnitResults;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;

public class PluginMenueClass extends BuildWrapper{
    
    private String pfadZuBasen;
    
    private String pfadZuBuilds;
    
    private boolean erstelleBasis;
    
    private int anzahlAnVergangenenBuilds;
    
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
                basis.erstelleBasis(build.getRootDir().getParent(), file.getAbsolutePath() + "/basen", 0.0, anzahlAnVergangenenBuilds);
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
            
            File file = new File("jobs/");
            FilePath filepaht = new FilePath(file);
            
            try {
                //file.createNewFile();
                valid = FormValidation.ok(filepaht.getRemote() +" 00\n00" + filepaht.list().get(0).getRemote());
            } catch (IOException e) {
                e.printStackTrace();
               //valid = FormValidation.ok(filepaht.getRemote() +" 00\n00" + filepaht.getParent());
                valid = FormValidation.ok(filepaht.getRemote() +" 00\n00" + filepaht.list().get(0).getRemote() + "asdfdafg" + e.getMessage());
            }
            return valid;
        }
        
    }
}
