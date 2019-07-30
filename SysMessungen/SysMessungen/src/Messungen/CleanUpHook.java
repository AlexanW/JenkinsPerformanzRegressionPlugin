package Messungen;

public class CleanUpHook extends Thread{
    /**
     * Der Ordner der geleert werden soll.
     */
    private String pfadZuZielOrdner;
    /**
     * Der Name der Dateien die geloescht werden sollen.
     */
    private String dateiName;
    
    public CleanUpHook(String pfadZuZielOrdner, String dateiName) {
        this.pfadZuZielOrdner = pfadZuZielOrdner;
        this.dateiName = dateiName;
    }
    
    @Override
    public void run() {
        MessnungenUtils.loescheAuslastugen(pfadZuZielOrdner, dateiName);
    }
    
}
