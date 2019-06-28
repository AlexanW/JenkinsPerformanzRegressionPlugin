package io.jenkins.plugins;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;

public class PluginMenueClass extends BuildWrapper{
    
    private String pfadZuBasen;
    
    private String pfadZuBuilds;
    
    @DataBoundConstructor
    public PluginMenueClass(String pfadZuBasen, String pfadZuBuilds) {
        this.pfadZuBasen = pfadZuBasen;
        this.pfadZuBuilds = pfadZuBuilds;
        System.out.println("Testausgabe.");
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
        
    }
}
