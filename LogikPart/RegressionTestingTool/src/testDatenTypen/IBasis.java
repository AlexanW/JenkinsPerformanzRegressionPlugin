package testDatenTypen;

public interface IBasis extends ITestObjekt {
    /**
     * Diese toString Methode wird dazu verwendet die Basis als Text zu speichern.
     * @return
     */
    public String toString();
    /**
     * Gibt eine untere Grenze für Aktzeptierte Tests an.
     */
    public double getUntergrenze();
    /**
     * Gibt eine obere Grenze für Aktzeptierte Tests an.
     */
    public double getObergrenze();  
    /**
     * Die Anzahl der Tests die zum erstellen dieser Basis verwendet wurden.
     */
    public int getAnzahlTests();
    /**
     * Die Standartabweichung des Scores.
     * @return
     */
    public double getStandartabweichung();
    /**
     * Die Varianz des Scores
     */
    public double getVarianz();
    /**
     * 
     */
    public double getTolleranz();
    /**
     * Der Name der Basis.
     */
    public String getName();
    /**
     * Diese Methode bekommt ein StringArray mit [0] = <Attributname>
     * und [1] = <Attributwert>
     */
    public void setBasisAttribute(String[] attribut);
}
