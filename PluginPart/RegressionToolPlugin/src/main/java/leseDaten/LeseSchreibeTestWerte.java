package leseDaten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.ObjectInputStream;

import testDatenTypen.ITestWerte;
import testDatenTypen.TestWerte;

public class LeseSchreibeTestWerte {
    
    
    
    public static void schreibeTestWerte (String pfad, ITestWerte testwerte) throws FileNotFoundException, IOException {
        File file = new File(pfad);
        if (file.getParentFile().exists()) {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(pfad));
            stream.writeObject(testwerte);    
            stream.close();
        }
    }
    
    public static ITestWerte leseTestWerte (String pfad) throws FileNotFoundException, IOException, ClassNotFoundException {
        ITestWerte tempWerte = new TestWerte();
        File file = new File(pfad);
        if (file.exists()) {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(pfad));
            tempWerte = (ITestWerte)stream.readObject();    
            stream.close();
        }
        return tempWerte;
    }
    
    public static void schreibeErgebnisse(String pfad, String text) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(pfad + "/auswertung.txt"), true);
            stream.write(text.getBytes("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
