package leseDaten;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private BufferedReader stream;
    
    /**
     * Inputstrem zum Einlesen der Daten.
     */
    private ObjectInputStream oStream;
    
    public LeseBasis(String pfad) {
        try {
            oStream = new ObjectInputStream(new FileInputStream(pfad));
            stream = new BufferedReader(new FileReader(pfad));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public LeseBasis() {
    }
    
    public IBasis leseBasisEin () {
        IBasis basis = new Basis();
        try {
            while(stream.ready()) {
                String[] attribut = stream.readLine().split(":");
                basis.setBasisAttribute(attribut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basis;
    }
    /**
     * 
     * @param pfad
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public IBasis leseObjektIBasisEin (String pfad) throws FileNotFoundException, IOException {
        oStream = new ObjectInputStream(new FileInputStream(pfad));
        IBasis basis = null;
        try {
            basis = (IBasis)oStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basis;
    }
    

    /**
     * TestMainMethode.
     * @param args
     */
    public static void main(String[] args) {
        LeseBasis lese = new LeseBasis();
        IBasis basis = new Basis();;
        try {
            basis = (Basis)lese.leseObjektIBasisEin("Data/Basen/tests.eu.qualimaster.AllTests.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(basis.toString());
    }
}
