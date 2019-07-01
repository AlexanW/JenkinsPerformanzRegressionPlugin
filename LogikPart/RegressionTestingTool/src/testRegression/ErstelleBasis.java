package testRegression;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import leseDaten.LeseJUnitResults;
import testDatenTypen.Test;
import testDatenTypen.Basis;
import testDatenTypen.IBasis;
import testDatenTypen.ITest;
import testDatenTypen.ITestWerte;

public class ErstelleBasis implements IErstelleBasis {
    /**
     * 
     */
    private static FileOutputStream stream;
    /**
     * 
     */
    private static ObjectOutputStream oStream;
    /**
     * Diese Methode erstellt eine Basis, auf Basis aller jUnitDateien in dem 
     * targetJUnitReult Ordner.
     * @param targetJUnitResutls der Zielordner, in dem die jUnitResult Dateien
     * fuer dei Erstellung liegen.
     * @param targetBasis Der Zielordner, in den die Basis geschrieben werden.
     * Dabei ist der Name nicht festzulegen.
     * @param tolleranz Eine Tolleranzgrenze. Sie ist prozentual von dem 
     * Mittelwert aus. Wenn statt einer 
     * Tolleranzgrenze die min. bzw max Werte aller verwendeten 
     * jUnitResultDateien nemen soll so sollte sie 0.0 sein.
     * Eingabe von Prozent in 0.xx Fomat.
     */
    public IBasis erstelleBasis(String targetJUnitResutls,String targetBasis 
            ,double tolleranz, int anzahlTests) {
        List<ITestWerte> werte = 
                LeseJUnitResults.getJUnitResultDateiAusBuilds(targetJUnitResutls, anzahlTests);
        if (werte.size() == 0) {
            werte =  
                LeseJUnitResults.getJUnitResultDatei(targetJUnitResutls);
        }
        IBasis basis = null;
        double avarageLaufzeit = getAvarageLaufzeit(werte);
        if (!enthaeltFehlschlag(werte)) {
            basis = new Basis(werte.get(0).getName(),avarageLaufzeit
                    , getMinLaufzeit(werte, tolleranz, avarageLaufzeit )
                    , getMaxLaufzeit(werte, tolleranz, avarageLaufzeit)
                    ,berecheneVarianz(werte, tolleranz, avarageLaufzeit)
                    ,getDurchschnitTests(werte), werte.get(0).getTests().values().size());
            //Schreibe die generierte Basis in den Ordner fuer die Basen.
            bestimmeNameUndSchreibeBasis(basis, targetBasis);
        } else {
            //Warnung das die Zeiten nicht stimmen koennen!!
        }
        return basis;
    }
    
    private boolean schreibeBasis(IBasis basis, String targetBasis) {
        boolean geschrieben = false;
        try {
            oStream = new ObjectOutputStream(new FileOutputStream(targetBasis));
            oStream.writeObject(basis);
            geschrieben = true;
        } catch (IOException e){
            e.printStackTrace();
        }    
        return geschrieben;
    }
    /**
     * Bestimmt den Namen einer Basis, wenn es eine "Neu" schon gibt wird diese
     * in Alt umbenannt.
     * @param targetBasis
     * @return
     */
    private boolean bestimmeNameUndSchreibeBasis(IBasis basis, String targetBasis) {
        boolean geschrieben = false;
        File file = new File(targetBasis + "/Neu.txt");
        if (file.exists()) {
            File fileAlt = new File(targetBasis + "/Alt.txt");
            file.renameTo(fileAlt);
        }
        geschrieben = schreibeBasis(basis, file.getAbsolutePath());
        return geschrieben;
    }
    
    private boolean schribePlainTextBasis (IBasis basis, String targetBasis) {
        boolean geschrieben = false;
        try {
            /*
             * Hier wird an den Namen der Datei Basis noch der Name der 
             * TestSuit angehaengt.
             */
            stream = new FileOutputStream(targetBasis + basis.getName() + ".txt");
            stream.write(basis.toString().getBytes());
        } catch (IOException exc) {
            exc.printStackTrace();
        }  
        return geschrieben;
    }
    
    private boolean enthaeltFehlschlag (List<ITestWerte> werte) {
        boolean enthaeltFehlschlag = false;
        for (ITestWerte w : werte ) {
            if (w.getEnthaeltFehlschag()) {
                enthaeltFehlschlag = true;
            }
        }
       return enthaeltFehlschlag;
    }
    private double getAvarageLaufzeit(List<ITestWerte> werte) {
        double sum = 0;
        for (ITestWerte t : werte) {
            sum += t.getScore();
        }
        return sum / werte.size();
    }
    private double getMinLaufzeit(List<ITestWerte> werte, double tolleranz
            , double avarage) {
        //Init des Minimusm mit dem Mittelwer. 
        double min = avarage;
        for (ITestWerte t : werte) {
            if (min > t.getScore() && 
                    ((t.getScore() > 
                    (avarage * (1-tolleranz)) || (1- tolleranz == 1)))) {
                min = t.getScore();
            }
        }
        /*
         * Solle es keine Untergrenze im Tolleranzbereich geben so wird
         * die Untergrenze durch den Durchschnitt * (1 - tolleranz) bestimmt. 
         */
        if (min == avarage) {
            min = avarage * (1 - tolleranz);
        }
        return min;
    }
    
    private double getMaxLaufzeit(List<ITestWerte> values, double tolleranz
            , double avarage) {
        double max = 0;
        for (ITestWerte t : values) {
            if (max < t.getScore() &&
                    ((t.getScore() < 
                    (avarage * (1+tolleranz)) || (1- tolleranz == 1)))) {
                max = t.getScore();
            }
        }
        /*
         * Solle es keine Obergrenze im Tolleranzbereich geben so wird die
         * Untergrenze durch den Durchschnitt * (1 + tolleranz) bestimmt. 
         */
        if (max == 0.0) {
            max = avarage * (1 + tolleranz);
        }
        return max;
    }
    
    private double berecheneVarianz(List<ITestWerte> werte, double tolleranz
            , double avarage) {
        double temp = 0;
        for (ITestWerte t : werte) {
            temp += Math.pow((t.getScore() - avarage), 2);
        }
        temp = temp /werte.size(); 
        System.out.println(temp);
        return temp;
    }
    /**
     * Berechnet den Durchschnitt fuer alle Tests der Basis. Auch fuer Tests
     * , die nicht in allen TestWerten enthalten sind.
     * @param werte
     * @return
     */
    private HashMap<String, ITest> getDurchschnitTests (List<ITestWerte> werte) {
        HashMap<String, ITest> druchschnitTests = new HashMap<String, ITest>();
        List<String> namen = getAlleTestNamenDerBasis(werte);
        Test tempTest = new Test();
        
        for (String n : namen) {
            tempTest.setName(n);
            double tempSum = 0.0;
            int i = 0;
            
            for (ITestWerte e : werte) {
               if (e.getTests().get(n) != null) {
                   tempSum += e.getTests().get(n).getScore();
                   i++;
               }
            }
            tempTest.setScore(tempSum/i);
            druchschnitTests.put(tempTest.getName(), tempTest);
        }
        return druchschnitTests;
    }
    /**
     * Diese Methode sucht alle Namen von Tests die in einer Menge an
     * TestWerten enthalten sind, unabhaengig davon ob sie einmal oder
     * mehrmals vorkommen.
     * @param werte
     * @return
     */
    private List<String> getAlleTestNamenDerBasis (List<ITestWerte> werte) {
        List<String> namen = new ArrayList<String>();
        for (ITestWerte e : werte) {
            for (ITest t : e.getTests().values()) {
                namen.add(t.getName());
            }
        }
        return namen;
    }
    public static void main(String[] args) {
        ErstelleBasis basis = new ErstelleBasis();
        basis.erstelleBasis("Data/jUnitResults/", "Data/Basen/", 0.2, 5);
    }
}
