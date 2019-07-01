package testDatenTypen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Diese Klasse stellt eine Testsuit dar. Sie Enthält alle Test mit Laufzeit und ggf. Fehlermeldung.
 * @author Alexander Weber
 *
 */
public class TestWerte implements ITestWerte {
	/**
	 * HashMap für das Speichern von Testzeiten im Verbund mit einem Namen und der Auslastung.
	 */
	private HashMap<String, ITest> tests = new HashMap<String, ITest>();
	/**
	 * Eine HashMap die, die Uhrzeit der Auslastung mit der Auslastung verbindet. 
	 */
	private List <TestAuslastungen> testAuslastungen =
	        new ArrayList<TestAuslastungen>();
	/**
	 * Das Datum und die Uhrzeit des Tests.
	 */
	private String timestamp;
	/**
     * Die Gesammtdauer aller Tests dieser Klasse.
     */
	private double score;
	/**
	 * Der Name der TestSuit, deren Ergebnisse in diesem Objekt sind.
	 */
	private String name;
	/**
	 * Diese Methode erlaub es einzelne Tests den TestValues hinzuzufügen.
	 */
	public void addTest(ITest test) {
		tests.put(test.getName(), test);
	}	
	@Override
	public String getTimeStapm() {
	    return timestamp;
	}
	/**
	 * Setter fuer die Zeit zu der die Tests druchgefuehrt wurden.
	 */
	public void setTimestamp(String timestamp) {
	    this.timestamp = timestamp;
	}
	/**
	 * Setter fuer die Gesamtdauer der TestSuit.
	 */
	public void setScore(double gesamtdauer) {
	        this.score = gesamtdauer;   
	}
	/**
	 * Setter fuer den Namen der TestSuit.
	 */
	public void setName(String name) {
        this.name = name;
    }
	/**
	 * Setter fuer die Auslastung weahrend dieses Tests.
	 */
	public void setTestAuslastungen (List <TestAuslastungen> testAuslastungen) {
        this.testAuslastungen = testAuslastungen;
    }
	/**
	 * Getter fuer die Gesamtdauert.
	 * @return Die Gesamtdauer der TestSuit.
	 */
	public double getScore() {
        return score;
    }
	public String getName() {
        return name;
    }
	/**
	 * 
	 * @return
	 */
	public HashMap<String, ITest> getTests() {
        return tests;
    }
	/**
	 * Eine Imlpementierung der toString Methode, diese Enthaelt alle Attribute. 
	 */
	public String toString() {
	    return Double.toString(score);
	}
	public boolean getEnthaeltFehlschag () {
	    boolean enthaeltFehlschlag = false;
	    for (ITest t : tests.values()) {
	        if (t.getIstFehlgeschlagen()) {
	            enthaeltFehlschlag = true;
	        }
	    }
	    return enthaeltFehlschlag;
	}
}
