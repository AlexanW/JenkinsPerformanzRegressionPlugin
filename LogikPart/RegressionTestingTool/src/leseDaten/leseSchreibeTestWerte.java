package leseDaten;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import testDatenTypen.ITestWerte;

public class leseSchreibeTestWerte {
    
    
    
    public static void schreibeTestWerte (String pfad, ITestWerte testwerte) throws FileNotFoundException, IOException {
        File file = new File(pfad);
        if (!file.exists()) {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(pfad));
            stream.writeObject(testwerte);            
        }
    }
}
