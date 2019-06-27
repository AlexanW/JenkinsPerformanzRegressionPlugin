package testDatenTypen;

import java.sql.Timestamp;

/**
 * Diese Klasse Verbindet die CPU und die RAM auslastung an einem fixen Zeitpunkt.
 * 
 * @author Alexander Weber
 *
 */
public class TestAuslastungen {
    /**
     * Die RAM Auslastung in GB.
     */
    private double ramAuslastung;
    /**
     * Die CPU Auslastung in Prozent.
     */
    private double cpuAuslastung;
    /**
     * Die Uhrzeit zu der der Test durchgefuehrt wurde als String. 
     */
    private Timestamp timeStamp;
    /**
     * Ein Konstruktor der alle Attribute der Klasse setzt.
     */
    public TestAuslastungen(double ramAuslastung, double cpuAuslastung
            , Timestamp timeStamp) {
        this.ramAuslastung = ramAuslastung;
        this.cpuAuslastung = cpuAuslastung;
        this.timeStamp = timeStamp;
    }
    public double getCpuAuslastung() {
        return cpuAuslastung;
    }
    public double getRamAuslastung() {
        return ramAuslastung;
    }
    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}
