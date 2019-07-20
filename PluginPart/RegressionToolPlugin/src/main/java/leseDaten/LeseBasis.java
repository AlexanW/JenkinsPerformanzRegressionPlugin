package leseDaten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import testDatenTypen.Basis;
import testDatenTypen.IBasis;

/**
 * Diese Klasse enthaelt Methoden um eine Basis einzulesen.
 * @author Alexander Weber
 *
 */
public class LeseBasis implements ILeseBasis {
    /**
     * Inputstrem zum Einlesen der Daten.
     */
//    private BufferedReader stream;
    /**
     * Inputstrem zum Einlesen der Daten.
     */
    private ObjectInputStream oStream;
    
    public LeseBasis(String pfad) throws FileNotFoundException, IOException {
            oStream = new ObjectInputStream(new FileInputStream(pfad));
//            stream = new BufferedReader(new FileReader(pfad));
    }
    
    
    public LeseBasis() {
    }
    
//    public IBasis leseBasisEin () {
//        IBasis basis = new Basis();
//        try {
//            while(stream.ready()) {
//                String tempString = stream.readLine();
//                if (tempString != null) {
//                    String[] attribut = tempString.split(":");
//                    basis.setBasisAttribute(attribut);   
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return basis;
//    }
    /**
     * 
     * @param pfad
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
