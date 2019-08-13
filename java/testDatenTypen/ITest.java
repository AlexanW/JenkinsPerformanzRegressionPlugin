package testDatenTypen;

import java.util.List;
/**
 * Diese Klasse stellt das Geruest fuer einen Test dar. 
 * @author Alexander
 *
 */
public interface ITest extends ITestObjekt{
    
    public String getName();
    
    public boolean getIstFehlgeschlagen();
    
    public void setScore(double score);
    
    public void setName(String name);
    
    public void setIstFehlgeschlagen(boolean istFehlgeschlagen);
    /**
     * Setter fuer die Auslastung weahrend dieses Tests. 
     */
    public void setTestAuslastungen (List <TestAuslastungen> testAuslastungen);
    
    public List<TestAuslastungen> getTestAuslastungen ();
    
    public String toString ();
    
    public double getMinCPU();
    
    public void setMinCPU(double minCPU);
    
    public double getMaxCPU();
    
    public void setMaxCPU(double maxCPU);
    
    public double getAvarageCPU();
    
    public void setAvarageCPU(double avarageCPU);
    
    public double getMinRAM();
    
    public void setMinRAM(double minRAM);
    
    public double getMaxRAM();
    
    public void setMaxRAM(double maxRAM);
    
    public double getAvarageRAM();
    
    public void setAvarageRAM(double avarageRAM);
}

