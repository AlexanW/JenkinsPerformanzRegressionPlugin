package leseDaten;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import testDatenTypen.IBasis;

/**
 * Diese Klasse enthaelt Methoden um eine Basis einzulesen.
 * @author Alexander
 *
 */
public class LeseBasis implements ILeseBasis {
    /**
     * Inputstrem zum Einlesen der Daten.
     */
    private ObjectInputStream oStream;
    
    public LeseBasis() {
    }
    /**
     * Liest eine Basis per ObjektInputStream ein.
     * @param pfad Der komplette Pfad zu der Basis.
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public IBasis leseObjektIBasisEin (String pfad) throws FileNotFoundException,
        IOException, ClassNotFoundException {
        oStream = new ObjectInputStream(new FileInputStream(pfad));
        IBasis basis = null;
        basis = (IBasis)oStream.readObject();
        return basis;
    }
}
