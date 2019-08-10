package testDatenTypen;

public class RegressionTestResult {
    /**
     * Der Status der Tests die ausgefuehrt wurden.
     */
    private Status resutlDerTests;
    /**
     * Die Nachricht fuer den Logger.
     */
    private String nachricht = "";
    
    public Status getResutlDerTests() {
        return resutlDerTests;
    }
    public void setResutlDerTests(Status resutlDerTests) {
        this.resutlDerTests = resutlDerTests;
    }
    public String getNachricht() {
        return nachricht;
    }
    public void addTextZuNachricht(String nachricht) {
        StringBuffer temp = new StringBuffer(this.nachricht);
        temp.append(nachricht);
        this.nachricht = temp.toString();
    }
    
    public void addResultZuResult(RegressionTestResult resultToAdd) {
        this.addTextZuNachricht( resultToAdd.getNachricht());
        this.setResutlDerTests(resultToAdd.getResutlDerTests());
    }
}
