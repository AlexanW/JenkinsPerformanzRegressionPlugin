package testDatenTypen;

import java.util.HashMap;

public interface ITestObjektGruppe extends ITestObjekt {
    
    public HashMap<String, ITest> getTests();
    
    public String getTestsAlsString();
}
