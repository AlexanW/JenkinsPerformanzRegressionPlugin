package leseDaten;

import java.io.FileNotFoundException;
import java.io.IOException;

import testDatenTypen.IBasis;

/**
 * Dieses Interface definiert die Eigenschaften die eine 
 * Klasse zum Einlesen einer Basis benoetigt.
 * @author Alexander
 *
 */
public interface ILeseBasis {
    
    public IBasis leseObjektIBasisEin (String pfad)throws FileNotFoundException, IOException, ClassNotFoundException;
}
