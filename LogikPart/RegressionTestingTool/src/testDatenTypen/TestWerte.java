package testDatenTypen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Diese Klasse stellt eine Testsuit dar. Sie Enth�lt alle Test mit Laufzeit und ggf. Fehlermeldung.
 * @author Alexander Weber
 *
 */
public class TestWerte implements ITestWerte {
    /**
     * Die Schrittlaenge der SystemMessungen.
     */
    private static final double STEP_SIZE = 0.100;
	/**
	 * HashMap f�r das Speichern von Testzeiten im Verbund mit einem Namen und der Auslastung.
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
	 * Diese Methode erlaub es einzelne Tests den TestValues hinzuzuf�gen.
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
        this.testAuslastungen = matchMessungenZuTests(testAuslastungen);
    }
	
	private List <TestAuslastungen> matchMessungenZuTests (List <TestAuslastungen> testAuslastungen) {
	    List <TestAuslastungen> tempList = new ArrayList<TestAuslastungen>();
	    boolean startGefunden = false;
	    double zaehlerZumEnde = 0;
	    //Gehe sicher, dass die Zeiten in steigender Reihenfolge sind.
	    testAuslastungen.sort((a,b) -> Long.compare(a.getTimeStamp().getTime(),b.getTimeStamp().getTime()));
	    
	    //Result Zeitstempel yyyy-mm-ddThh:mm:ss split by T um nur Zeit zu bekommen
	    String testStart = timestamp.split("T")[1];
	    SimpleDateFormat formateTime = new SimpleDateFormat("hh:mm:ss");
	    //Score+STEP_SIZE stellen sicher, dass die letzte Messun nach dem Letzen Test liegt.
	    for (int i = 0; i < testAuslastungen.size() && zaehlerZumEnde < (score + STEP_SIZE); i++) {
	        System.out.println("TestFormateTimeThingy: " + formateTime.format(testAuslastungen.get(i)) + " Start = " + testStart);
	        if (testStart.equals(formateTime.format(testAuslastungen.get(i))) || startGefunden) {
	            startGefunden = true;
	            tempList.add(testAuslastungen.get(i));
	            zaehlerZumEnde += STEP_SIZE;
	        }
	    }
	    
	    return tempList;
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
