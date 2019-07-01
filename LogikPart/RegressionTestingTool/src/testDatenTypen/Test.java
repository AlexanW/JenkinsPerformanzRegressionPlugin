package testDatenTypen;

import java.io.Serializable;
import java.util.List;

/**
 * Eine Klasse, die die Testzeiten und die entsprechenden Auslastungen bindet.
 * 
 * @author Alexander Weber
 *
 */
public class Test implements ITest, Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 7408901150396168510L;
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
	private double minCPU;
	   /**
     * 
     */
    private double maxCPU;
    /**
     * 
     */
    private double avarageCPU;
    /**
     * 
     */
    private double minRAM;
       /**
     * 
     */
    private double maxRAM;
    /*
     * 
     */
    private double avarageRAM;
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
    @Override
    public List<TestAuslastungen> getTestAuslastungen() {
        return auslastung;
    }  
    public double getMinCPU() {
        return minCPU;
    }
    public void setMinCPU(double minCPU) {
        this.minCPU = minCPU;
    }
    public double getMaxCPU() {
        return maxCPU;
    }
    public void setMaxCPU(double maxCPU) {
        this.maxCPU = maxCPU;
    }
    public double getAvarageCPU() {
        return avarageCPU;
    }
    public void setAvarageCPU(double avarageCPU) {
        this.avarageCPU = avarageCPU;
    }
    public double getMinRAM() {
        return minRAM;
    }
    public void setMinRAM(double minRAM) {
        this.minRAM = minRAM;
    }
    public double getMaxRAM() {
        return maxRAM;
    }
    public void setMaxRAM(double maxRAM) {
        this.maxRAM = maxRAM;
    }
    public double getAvarageRAM() {
        return avarageRAM;
    }
    public void setAvarageRAM(double avarageRAM) {
        this.avarageRAM = avarageRAM;
    }
    public String toString () {
        String output = "Testname: " + name + " Score: " + score + " IstFehlgeschlagen: " + istFehlgeschlagen + "\n+ Werte: ";
//        for (TestAuslastungen a : auslastung) {
//            output += a.toString();        
//       }    
       return output;
    }
}
