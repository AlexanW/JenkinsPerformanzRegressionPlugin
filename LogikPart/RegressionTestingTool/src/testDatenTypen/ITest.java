package testDatenTypen;

import java.util.List;

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
}
