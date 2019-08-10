package leseDaten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.ObjectInputStream;

import testDatenTypen.IBasis;
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
    
    public static void schreibeErgebnisse(String pfad, String text, boolean append) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(pfad), append);
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
    
    private static boolean schreibeBasis(IBasis basis, String targetBasis) {
        boolean geschrieben = false;
        ObjectOutputStream oStream = null;
        try {
            oStream = new ObjectOutputStream(new FileOutputStream(targetBasis));
            oStream.writeObject(basis);
            geschrieben = true;
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (oStream != null) {
                try {
                    oStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return geschrieben;
    }
    
    /**
     * Bestimmt den Namen einer Basis, wenn es eine "Neu" schon gibt wird diese
     * in Alt umbenannt.
     * @param targetBasis
     * @return
     */
    public static boolean bestimmeNameUndSchreibeBasis(IBasis basis, String targetBasis) {
        boolean geschrieben = false;
        File file = new File(targetBasis + "/Neu.txt");
        if (file.exists()) {
            Path copiedTo = Paths.get(targetBasis + "/Alt.txt");
            try {
                Files.copy(file.toPath(), copiedTo, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        geschrieben = schreibeBasis(basis, file.getAbsolutePath());
        System.out.println("file Wurde nach neu geschrieben : " + geschrieben);
        return geschrieben;
    }
}
