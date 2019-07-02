package leseDaten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import testDatenTypen.ITestWerte;
import testDatenTypen.TestWerte;

public class LeseSchreibeTestWerte {
    
    
    
    public static void schreibeTestWerte (String pfad, ITestWerte testwerte) throws FileNotFoundException, IOException {
        File file = new File(pfad);
        if (!file.exists()) {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(pfad));
            stream.writeObject(testwerte);            
        }
    }
    
    public static ITestWerte leseTestWerte (String pfad) throws FileNotFoundException, IOException, ClassNotFoundException {
        ITestWerte tempWerte = new TestWerte();
        File file = new File(pfad);
        if (file.exists()) {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(pfad));
            tempWerte = (ITestWerte)stream.readObject();            
        }
        return tempWerte;
    }
}
