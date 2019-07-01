package testDatenTypen;
/**
 * Dieses Interface definiert ein Grundgeruest fuer die Klassen,
 * die, die Werte einer TestSuit einlesen.
 * @author Alexander Weber
 *
 */

import java.sql.Timestamp;

public interface ITestWerte extends ITestObjektGruppe{
    
    public boolean getEnthaeltFehlschag();
    
    public String getName();
    
    public String getTimeStapm();
}
