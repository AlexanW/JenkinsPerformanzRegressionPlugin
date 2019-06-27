package io.jenkins.plugins;

import org.kohsuke.stapler.DataBoundConstructor;

public class PluginMenueClass {
    
    private String pfadZuBasen;
    
    @DataBoundConstructor
    public PluginMenueClass(String pfadZuBasen) {
        this.pfadZuBasen = pfadZuBasen;
    }
    
    public String getPfadZuBasen() {
        return pfadZuBasen;
    }
}
