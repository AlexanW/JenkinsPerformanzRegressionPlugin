package testDatenTypen;

import java.io.Serializable;
import java.util.HashMap;
/**
 * Diese Klasse stellt ein besondere Version einer Basis da, diese Version
 * enthaellt alle Tests die zu dieser Basis gehoeren.
 * @author Alexander
 *
 */
public abstract class BasisMitTests implements IBasis, ITestObjektGruppe, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8617089885956255701L;
    /**
     * Eine Liste die alle Tests der Suit enthaehlt, die fuer die Erstellung 
     * verwendet wurden. Die Werte der Tests entsprechen den durchschnittlichen
     * Werten der Test ueber alle verwendeten TestValues.
     */
    public HashMap<String,ITest> avarageTests;
    /**
     * 
     * @param avarageTests Die Liste mit allen Tests dieser Basis.
     */
    public BasisMitTests(HashMap<String, ITest> avarageTests) {
        this.avarageTests = avarageTests;
    }
    /**
     * Default Konstruktor.
     */
    public BasisMitTests() {        
    }
    
    public void setAvarageTests(HashMap<String, ITest> avarageTests) {
        this.avarageTests = avarageTests;
    }
    
    public HashMap<String, ITest> getTests() {
        return avarageTests;
    }
    
    public String getTestsAlsString() {
        StringBuffer buffer = new StringBuffer();
            for (ITest t : avarageTests.values()) {
                buffer.append(t.toString());
            }
        return buffer.toString();
    }
}
