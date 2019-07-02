package testDatenTypen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private double step_size = 0.100;
	/**
	 * LinkedHashMap f�r das Speichern von Testzeiten im Verbund mit einem Namen und der Auslastung.
	 * Dabei wird die Reihenfolge der Tests gewaehrleistet.
	 */
	private LinkedHashMap<String, ITest> tests = new LinkedHashMap<String, ITest>();
	/**
	 * Eine HashMap die, die Uhrzeit der Auslastung mit der Auslastung verbindet. 
	 */
	private List <TestAuslastungen> testAuslastungen =
	        new ArrayList<TestAuslastungen>();
	/**
	 * 
	 */
	public TestWerte(double step_size) {
	    // Durch 1000 geteilt um die Schrittgroesse anzupassen.
	    this.step_size = step_size/1000;
    }
	/**
	 * 
	 */
	public TestWerte() {
    }
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
        addMessungenZuTests();
    }
	/**
	 * Diese Methode nimmt eine Menge an TestAuslastungen und matcht den Start
	 * der Tests mit einem Punkt in den testAuslastungen
	 * @param testAuslastungen
	 * @return
	 */
	private List <TestAuslastungen> matchMessungenZuTests (List <TestAuslastungen> testAuslastungen) {
	    List <TestAuslastungen> tempList = new ArrayList<TestAuslastungen>();
	    boolean startGefunden = false;
	    double zaehlerZumEnde = 0;
	    //Gehe sicher, dass die Zeiten in steigender Reihenfolge sind.
	    testAuslastungen.sort((a,b) -> Long.compare(a.getTimeStamp().getTime(),b.getTimeStamp().getTime()));
	    //timestamp = "2019-04-10T21:17:20";
	    //Result Zeitstempel yyyy-mm-ddTHH:mm:ss split by T um nur Zeit zu bekommen
	    String testStart = timestamp.split("T")[1];
	    SimpleDateFormat formateTime = new SimpleDateFormat("HH:mm:ss");
	    //Score+STEP_SIZE stellen sicher, dass die letzte Messun nach dem Letzen Test liegt.
	    for (int i = 0; i < testAuslastungen.size() && zaehlerZumEnde < (score + step_size); i++) {
	        
	        if (testStart.equals(formateTime.format(testAuslastungen.get(i).getTimeStamp().getTime())) || startGefunden) {
	            //System.out.println( formateTime.format(testAuslastungen.get(i).getTimeStamp().getTime()) + " STARTUP ASD");
	            startGefunden = true;
	            tempList.add(testAuslastungen.get(i));
	            zaehlerZumEnde += step_size;
	        }
	    }
	    return tempList;
	}
	
	private void addMessungenZuTests() {
	    double scoreSumme = 0;
	    for (ITest t : tests.values()) {
	        if (t.getScore() < step_size) {
	            setAuslatungenFuerTests(t, testAuslastungen.get((int)(scoreSumme*10)));
	            scoreSumme += t.getScore();
	        } else {
	            List<TestAuslastungen> auslatungen = new ArrayList<TestAuslastungen>();
	            for (int i = ((int)(scoreSumme*10)); i <=  (int)((scoreSumme + t.getScore())*10); i++) {
	                if (i < testAuslastungen.size()) {
	                    auslatungen.add(testAuslastungen.get(i));
	                }
	            }
	            setAuslatungenFuerTests(t, auslatungen);
	            scoreSumme += t.getScore();
	        }
	    }
	}
	
	private void setAuslatungenFuerTests(ITest test, List<TestAuslastungen> auslatungen) {
        double max = auslatungen.get(0).getCpuAuslastung();
        double min = auslatungen.get(0).getCpuAuslastung();
        double avarage = 0;
        
        for (TestAuslastungen t : auslatungen) {
            if (t.getCpuAuslastung() > max) {
                max = t.getCpuAuslastung();
            }
            if (t.getCpuAuslastung() < min) {
                min = t.getCpuAuslastung();
            }
            avarage += t.getCpuAuslastung();
        }  
        test.setAvarageCPU(avarage/auslatungen.size());
        test.setMinCPU(min);
        test.setMaxCPU(max);
        
        max = auslatungen.get(0).getRamAuslastung();
        min = auslatungen.get(0).getRamAuslastung();
        avarage = 0;
        for (TestAuslastungen t : auslatungen) {
            if (t.getRamAuslastung() > max) {
            max = t.getRamAuslastung();
            }
            if (t.getRamAuslastung() < min) {
                min = t.getRamAuslastung();
            }
            avarage += t.getRamAuslastung();
        }  
        test.setAvarageRAM(avarage / auslatungen.size());
        test.setMinRAM(min);
        test.setMaxRAM(max);
    }
	
	private void setAuslatungenFuerTests(ITest test, TestAuslastungen auslastung) {
	    test.setAvarageCPU(auslastung.getCpuAuslastung());
	    test.setMinCPU(auslastung.getCpuAuslastung());
	    test.setMaxCPU(auslastung.getCpuAuslastung());
	    test.setAvarageRAM(auslastung.getRamAuslastung());
	    test.setMinRAM(auslastung.getRamAuslastung());
	    test.setMaxRAM(auslastung.getRamAuslastung());
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