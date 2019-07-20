package testDatenTypen;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Diese Klasse Verbindet die CPU und die RAM auslastung an einem fixen Zeitpunkt.
 * 
 * @author Alexander Weber
 *
 */
public class TestAuslastungen implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -7232136932852731144L;
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
    public TestAuslastungen(Timestamp timeStamp, double cpuAuslastung
            , double ramAuslastung) {
        this.ramAuslastung = ramAuslastung;
        this.cpuAuslastung = cpuAuslastung;
        this.timeStamp = new Timestamp(timeStamp.getTime());
    }
    public double getCpuAuslastung() {
        return cpuAuslastung;
    }
    public double getRamAuslastung() {
        return ramAuslastung;
    }
    public Timestamp getTimeStamp() {
        return new Timestamp(timeStamp.getTime());
    }
    
    @Override
    public String toString() {
        return "CPU: " + cpuAuslastung + " RAM: " + ramAuslastung + " Time: " + timeStamp;
    }
}
