 package testDatenTypen;

import java.util.HashMap;

/**
 * Eine Basis gegen die einzelne TestValues getestet werden können.
 * Sie wir aus einer Menge an TestValues erstellt. 
 * 
 * @author Alexander Weber
 *
 */
public class Basis extends BasisMitTests {
    /**
     * 
     */
    private static final long serialVersionUID = -4575802343264513418L;
    /**
     * Die Tolleranz um den Durchschnitt herrum.
     */
    private double tolleranz;
    /**
     * Der Name der TestSuit, zu der diese Basis gehoert.
     */
    private String name;
    /**
     * Die durchschnittliche Laufzeit alle fuer die Erstellung 
     * verwendeten TestValues.
     */
    private double avarageLaufzeit;
    /**
     * Die Untergrenze der Laufzeit aller TestValues.
     */
    private double untergrenze;
    /**
     * Die Obergrenze der Laufzeit aller TestValues. 
     */
    private double obergrenze;
    /**
     * Die Varianz der Laufzeit.
     */
    private double varianz;
    /**
     * Die Anzahl der verwendeten Tests.
     */
    private int anzahlTests;
    
    /**
     * Der Konstruktor fuer eine Basis, der alle Attribute setzt.
     * @param avarageLaufzeit
     * @param minLaufzeit
     * @param maxLaufzeit
     * @param varianz
     */
    public Basis(String name, double avarageLaufzeit, double minLaufzeit,
            double maxLaufzeit, double varianz, HashMap<String, ITest> avarageTests,
            int anzahlTests ) {
        super(avarageTests);
        this.name = name;
        this.avarageLaufzeit = avarageLaufzeit;
        this.untergrenze = minLaufzeit;
        this.obergrenze = maxLaufzeit;
        this.varianz = varianz;
        this.avarageTests = avarageTests;
        this.anzahlTests = anzahlTests;
    }
    /**
     * Default Konstruktor fuer eine Basis.
     */
    public Basis() {
        super();
    }
    
    public void setMaxLaufzeit(double maxLaufzeit) {
        this.obergrenze = maxLaufzeit;
    }
    public void setMinLaufzeit(double minLaufzeit) {
        this.untergrenze = minLaufzeit;
    }
    public void setAvarageLaufzeit(double avarageLaufzeit) {
        this.avarageLaufzeit = avarageLaufzeit;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setVarianz(double varianz) {
        this.varianz = varianz;
    }
    public double getVarianz() {
        return varianz;
    }
    public double getTolleranz() {
        return tolleranz;
    }
    public double getUntergrenze() {
        return untergrenze;
    }
    public double getObergrenze() {
        return obergrenze;
    }
    public double getScore() {
        return avarageLaufzeit;
    }
    public String getName() {
        return name;
    }
    
    /**
     * Standartabweichung als Wurzel der Varianz.
     * @return
     */
    public double getStandartabweichung() {
        return Math.sqrt(varianz);
    }
    /**
     * Gibt die Anzahl der Test zurueckt, die zuer erstellung verwendet wurden.
     */
    public int getAnzahlTests() {
        return this.anzahlTests;
    }
    /**
     * Bekommt Attribute als ein String Array.
     * Muster:  <AttributName>:<AttributWert>
     * @param attribut
     */
    @Override
    public void setBasisAttribute(String[] attribut) {
        switch (attribut[0]) {
            case "Name":
                this.name = (attribut[1]);
                break;
            case "AvarageLaufzeit":
                this.avarageLaufzeit = (Double.parseDouble(attribut[1]));
                break;
            case "Untergrenze":
                this.untergrenze = (Double.parseDouble(attribut[1]));
                break;
            case "Obergrenze":
                this.obergrenze = (Double.parseDouble(attribut[1]));
                break;
            case "Varianz":
                this.varianz = (Double.parseDouble(attribut[1]));
                break;
        }
    }
    /**
     * Diese toString Methode wird verwendet um die Basis als .txt zu speichern.
     */
    @Override
    public String toString() {
        String outPut = "Name:" + name + "\n" 
                + "AvarageLaufzeit: " + avarageLaufzeit + "\n" 
                + "Obergrenze:" + obergrenze + "\n" 
                + "Untergrenze:" + untergrenze + "\n" 
                + "Varianz:" + varianz + "\n";
        for (ITest t : getAvarageTests().values()) {
            outPut += (t.toString());
        }
        
        return outPut;
    }
}