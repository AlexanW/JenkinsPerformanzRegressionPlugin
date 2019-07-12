package io.jenkins.plugins;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

public class PuluginMenuePostBuild extends Recorder{

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
    public PuluginMenuePostBuild (boolean pruefeRegression, String pfadZuCPUundRAM, 
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
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
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
