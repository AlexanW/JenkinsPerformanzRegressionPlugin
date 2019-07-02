package testDatenTypen;

import java.util.List;

/**
 * Dieses Interface definiert ein Grundgeruest fuer die Klassen,
 * die, die Werte einer TestSuit einlesen.
 * @author Alexander Weber
 *
 */
public interface ITestWerte extends ITestObjektGruppe{
    
    public boolean getEnthaeltFehlschag();
    
    public String getName();
    
    public String getTimeStapm(); 
    
    public void setName(String name);
     
    public void addTest(ITest test);
    
    public void setTimestamp(String time);
    
    public void setScore(double score);
    
    public void setTestAuslastungen(List<TestAuslastungen> testAuslastungen);
}
