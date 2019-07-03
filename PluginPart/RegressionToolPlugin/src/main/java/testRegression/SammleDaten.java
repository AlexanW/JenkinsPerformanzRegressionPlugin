package testRegression;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import leseDaten.LeseJUnitResults;
import testDatenTypen.ITestWerte;

/**
 * Diese Klasse enthaelt Methoden zum Sammeln von Daten.
 */
public class SammleDaten {
    /**
     * Diese Methode startet den Vorgang des Einlesens aller JUnitResultDatein 
     * die bereitgestellt sind.
     * @param pfad Der Zielordner in dem die Resultdateien Liegen.
     */
    public static List<ITestWerte> getJUnitResultDatei(String pfad) {
        List<ITestWerte> values = new ArrayList<ITestWerte>();
        File file = new File(pfad);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                //values.add(LeseJUnitResults.leseTestsXML(f.getAbsolutePath()));
            }   
        }
        return values;
    }
}
