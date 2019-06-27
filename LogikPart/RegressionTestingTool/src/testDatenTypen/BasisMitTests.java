package testDatenTypen;

import java.util.HashMap;
import java.util.List;

public abstract class BasisMitTests implements IBasis, ITestObjektGruppe{
    /**
     * Eine Liste die alle Tests der Suit enthaehlt, die fuer die Erstellung 
     * verwendet wurden. Die Werte der Tests entsprechen den durchschnittlichen
     * Werten der Test ueber alle verwendeten TestValues.
     */
    public HashMap<String,ITest> avarageTests;
    
    public BasisMitTests(HashMap<String, ITest> avarageTests) {
        this.avarageTests = avarageTests;
    }
    public BasisMitTests() {
        
    }
    
    public void setAvarageTests(HashMap<String, ITest> avarageTests) {
        this.avarageTests = avarageTests;
    }
    
    public HashMap<String, ITest> getTests() {
        return avarageTests;
    }
}
