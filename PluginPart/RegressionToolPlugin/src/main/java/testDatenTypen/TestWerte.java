package testDatenTypen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Diese Klasse stellt eine Testsuit dar. Sie Enthaelt alle Test mit Laufzeit und ggf. Fehlermeldung.
 * @author Alexander Weber
 *
 */
public class TestWerte implements ITestWerte, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5293564103144812233L;
    /**
     * Die Schrittlaenge der SystemMessungen.
     */
    private double step_size;
	/**
	 * LinkedHashMap fuer das Speichern von Testzeiten im Verbund mit einem Namen und der Auslastung.
	 * Dabei wird die Reihenfolge der Tests gewaehrleistet.
	 */
	private LinkedHashMap<String, ITest> tests = new LinkedHashMap<String, ITest>();
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
	 * Diese Methode erlaub es einzelne Tests den TestValues hinzuzufuegen.
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
	        //Hier wird angenommen, dass es min. eine Messungen pro Sekunde gibt und diese somit gematcht werden koennen.
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
	    if (testAuslastungen.size() > 0) {
	        for (ITest t : tests.values()) {
	            if (t.getScore() < step_size) {
	                if ((int)(scoreSumme/step_size) < testAuslastungen.size()) {
	                    setAuslastungenFuerTests(t, testAuslastungen.get((int)(scoreSumme/step_size)));
	                }
	            } else {
	                List<TestAuslastungen> auslatungen = new ArrayList<TestAuslastungen>();
	                for (int i = (int)(scoreSumme/step_size); i <= ((int)((t.getScore() + scoreSumme)/step_size)) && i < testAuslastungen.size(); i++) {
	                    if (i < testAuslastungen.size()) {
	                        auslatungen.add(testAuslastungen.get(i));
	                    }
	                }
	                setAuslatungenFuerTests(t, auslatungen);
	            }
	            scoreSumme += t.getScore();
	        }
	    }
	}
	/**
	 * Diese Methode nimmt eine Liste von Auslasutngen entgegen und fuegt diese 
	 * zu den Test dieser TestWerte Sammlung hinzu.
	 * @param test
	 * @param auslastungen
	 */
	private void setAuslatungenFuerTests(ITest test, List<TestAuslastungen> auslastungen) {
	    //Ein Test hat einen min, max und Avaraga Wert, diese werden initial mit den ersten
	    //der Liste gesetzt, so dass sie abgeglichen werden koennen.
        if (auslastungen != null && auslastungen.size() > 0) {
            double max = auslastungen.get(0).getCpuAuslastung();
            double min = auslastungen.get(0).getCpuAuslastung();
            double avarage = 0;
            
            for (TestAuslastungen t : auslastungen) {
                if (t.getCpuAuslastung() > max) {
                    max = t.getCpuAuslastung();
                }
                if (t.getCpuAuslastung() < min) {
                    min = t.getCpuAuslastung();
                }
                avarage += t.getCpuAuslastung();
            }  
            test.setAvarageCPU(avarage/auslastungen.size());
            test.setMinCPU(min);
            test.setMaxCPU(max);
            
            max = auslastungen.get(0).getRamAuslastung();
            min = auslastungen.get(0).getRamAuslastung();
            avarage = 0;
            for (TestAuslastungen t : auslastungen) {
                if (t.getRamAuslastung() > max) {
                max = t.getRamAuslastung();
                }
                if (t.getRamAuslastung() < min) {
                    min = t.getRamAuslastung();
                }
                avarage += t.getRamAuslastung();
            }  
            test.setAvarageRAM(avarage / auslastungen.size());
            test.setMinRAM(min);
            test.setMaxRAM(max);
        }   
    }
	
	private void setAuslastungenFuerTests(ITest test, TestAuslastungen auslastung) {
	    test.setAvarageCPU(auslastung.getCpuAuslastung());
	    test.setMinCPU(auslastung.getCpuAuslastung());
	    test.setMaxCPU(auslastung.getCpuAuslastung());
	    test.setAvarageRAM(auslastung.getRamAuslastung());
	    test.setMinRAM(auslastung.getRamAuslastung());
	    test.setMaxRAM(auslastung.getRamAuslastung());
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
	    StringBuffer buffer = new StringBuffer("Name: " + name  + " \n" + "Score: "  + score + " \n");
//	    for ( ITest t : tests.values()) {
//	        buffer.append(t.toString());
//	    }
	    return buffer.toString();
	}
	
    public String getTestsAlsString() {
        StringBuffer buffer = new StringBuffer();
            for (ITest t : tests.values()) {
                buffer.append(t.toString());
            }
        return buffer.toString();
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