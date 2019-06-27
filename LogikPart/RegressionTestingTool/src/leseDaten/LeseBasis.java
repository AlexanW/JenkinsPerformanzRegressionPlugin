package leseDaten;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    
    public LeseBasis(String pfad) {
        try {
            stream = new BufferedReader(new FileReader(pfad));
        } catch (FileNotFoundException e) {
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
    

    /**
     * TestMainMethode.
     * @param args
     */
    public static void main(String[] args) {
        LeseBasis lese = new LeseBasis("Data/Basen/Basetest.net.ssehub.easy.reasoning.sseReasoner.AllTests.txt");
        Basis basis = (Basis)lese.leseBasisEin();
        System.out.println(basis.toString());
    }
}
