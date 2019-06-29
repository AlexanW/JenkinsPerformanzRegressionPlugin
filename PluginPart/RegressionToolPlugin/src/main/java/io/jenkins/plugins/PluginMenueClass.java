package io.jenkins.plugins;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

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

public class PluginMenueClass extends BuildWrapper{
    
    private String pfadZuBasen;
    
    private String pfadZuBuilds;
    
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
    
    @Override
    public Environment setUp(AbstractBuild build,
            Launcher launcher,
            BuildListener listener) {
        return new Environment() {
            
            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener)
                    throws IOException, InterruptedException {
                listener.getLogger().print(pfadZuBasen + " HIER IST DER PFAD ZU DEN BASEN------------------------------\n");
                
                listener.fatalError("HierIstEtwasSchiefgegangen.\n");
                listener.fatalError( "TestName: -- "+build.getProject().getName() + "\n");
                listener.fatalError( "TeatPlace: -- "+ build.getProject().getRootProject().getRootDir() + "\n");
                listener.fatalError( "TeatPlace: -- "+ build.getProject().getRootProject().getRootDir() + "\n");
                
                File fileDif = new File(build.getProject().getRootProject().getRootDir().getPath() + "/builds");
                if (fileDif != null) {
                    File[] tempList = fileDif.listFiles(); 
                    if (tempList != null) {
                        for (File f: tempList) {
                            if (f.isDirectory()) {
                                File[] tempListT =  f.listFiles();
                                if (tempListT != null) {
                                    for (File a : tempListT) {
                                        listener.getLogger().print(a.getPath() + "................................_______________________________________________________\n");
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
            return "TestNameHier";
        }
        
        public FormValidation doCreateBase(@QueryParameter("pfadZuBasen") final String pfadZuBasen) {
            FormValidation valid = FormValidation.ok("pfadZuBasen");
            
            File file = new File("text.txt");
            try {
                file.createNewFile();
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            valid = FormValidation.ok(file.getAbsolutePath() +" 000000000000000000000000000000000000000");
            
            return valid;
        }
        
    }
}
