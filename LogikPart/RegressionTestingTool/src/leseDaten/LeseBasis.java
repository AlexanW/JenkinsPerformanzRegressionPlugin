package leseDaten;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    public IBasis leseObjektIBasisEin () {
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
        LeseBasis lese = new LeseBasis("Data/Basen/tests.eu.qualimaster.AllTests.txt");
        IBasis basis = (Basis)lese.leseObjektIBasisEin();
        System.out.println(basis.toString());
    }
}
