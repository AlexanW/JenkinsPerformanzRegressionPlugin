package testDatenTypen;

import java.util.HashMap;
/**
 * Ein TestObjekt welches eine Menge an Tests enthaelt.
 * @author Alexander
 *
 */
public interface ITestObjektGruppe extends ITestObjekt {
    
    public HashMap<String, ITest> getTests();
    
    public String getTestsAlsString();
}
