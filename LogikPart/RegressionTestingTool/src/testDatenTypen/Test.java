package testDatenTypen;

import java.util.List;

/**
 * Eine Klasse, die die Testzeiten und die entsprechenden Auslastungen bindet.
 * 
 * @author Alexander Weber
 *
 */
public class Test implements ITest {
	/**
	 * Die Dauer des Tests
	 */
	private double score;
	/**
	 * Ob es Komplikationen gab.
	 */
	private boolean istFehlgeschlagen;
	/**
	 *	Der Name des Tests.
	 */
	private String name;
	/**
	 * 
	 */
	private List<TestAuslastungen> auslastung;
	/**
	 * Der Getter fuer den Namen dieses Tests.
	 * @return Den Namen des Tests.
	 */
	public String getName() {
		return name;
	}
	@Override
	public void setScore(double score) {
	    this.score = score;
	}
	/**
	 * Setter ob der Test fehlschlug.
	 * @param istFehlgeschlagen Boolean ob der Tests fehlgeschlagen ist.
	 */
	@Override
	public void setIstFehlgeschlagen(boolean istFehlgeschlagen) {
        this.istFehlgeschlagen =istFehlgeschlagen;
    }
	/**
	 * Setter fuer den Namen des Tests.
	 * @param name Der Name des Tests.
	 */
	@Override
	public void setName(String name) {
        this.name = name;
    }
	/**
	 * toString Methode einens Tests, sie enthaelt alle Attribute.
	 */
	public String toString() {
	    return "Name: " + name + ", Dauer: " + score;
	}
	@Override
	public boolean getIstFehlgeschlagen() {
	    return istFehlgeschlagen;
	}
	@Override
	public double getScore() {
        return score;
    }
    @Override
    public void setTestAuslastungen(List<TestAuslastungen> testAuslastungen) {
        this.auslastung = testAuslastungen;
        
    }
}
