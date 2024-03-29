package Messungen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import de.uni_hildesheim.sse.system.GathererFactory;

public class SysAuslastungMessung {
    /**
     * Der Timer, der fuer das Ausfuehren dieser Aufgabe zustaendig ist.
     */
    private static Timer timer;
    /**
     * Die Millisekunden die eine Stunde ausmachen.
     */
    private static final long stunde = TimeUnit.HOURS.toMillis(1);
//    /**
//     * Der Pfad der Dateien in die, die Messdaten geschrieben werden.
//     * Dabei fehlt die Nummer der Datei und die .txt Endung, beides wird
//     * bei dem Start einer Task hinzugef�gt.
//     */
//    private final static String PATH = "Data/SysLoadData/ProzessValues";
    /**
     * Dieser Methode stellt den Timer ein uns startet den Messvorgang. Sollte 
     * die TimerTask eine Exception werfen, so wird der Timer beendet.
     */
    public static void startMeasurementTimer(String pfad, int steps, 
            double zeitOffset, int maxSpeicherZeit) {
        timer = new Timer();
        try {
            timer.schedule(new SysAuslastungMessungTask(timer, pfad, 
                    (long)(zeitOffset* stunde), maxSpeicherZeit, steps), 10, 
                    steps);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            timer.cancel();
        }
    }
    /**
     * 
     */
    public static void messePerformanz(String pfad, double zeitOffset) {
        DateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(new File(pfad), true);
            stream.write(
                    ((System.currentTimeMillis() - (zeitOffset * stunde))
                            + ";" + GathererFactory.getProcessorDataGatherer().getCurrentSystemLoad() 
                            // Teilen durch 1000000000.0 f�r GB.
                            + ";" + (GathererFactory.getMemoryDataGatherer().getCurrentMemoryUse() / 1000000000.0) 
                            + ";" + timeFormat.format((System.currentTimeMillis() - (zeitOffset * stunde))) + "\n")
                    .getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        File folder = null;
        if (args.length > 1  ) {
            File testFile = new File(args[1]); 
            //Stellt sicher, dass der Pfad existiert. Prueft dabei nicht die eigenliche Datei.
            if (testFile.exists()) { 
                 folder = new File(testFile.getAbsolutePath() + "/Auslastungen");
                 if (!folder.exists()) {
                     folder.mkdir();
                 } else {
                     /**
                      * Sollte beim Start der Ordner existieren und der 
                      * CleanUpHook diesen nicht ordentliche geleert haben
                      * so wird es hier erneut versucht.
                      */
                     MessnungenUtils.loescheAuslastugen(folder.getAbsolutePath(), "Auslastungen");
                 }
                switch(args[0]) {
                case "timer":
                    if (testFile.exists() && !testFile.isDirectory()) {
                        testFile.delete();
                    }
                    if (args.length > 3) {                        
                        startMeasurementTimer(folder.getAbsolutePath(),
                                Integer.parseInt(args[4]), 
                                Double.parseDouble(args[2]),
                                Integer.parseInt(args[3]));
                    } else {
                        System.out.println("Ein Timer benoetigt eine Tickrate in Hundertstelsekunden.");
                    }
                    break;
                case "single":
                    messePerformanz(args[1], Integer.parseInt(args[3]));
                    break;
                default:
                    System.out.println("Die erste Eingabe ist kein bekanntest Kommando (\"timer\" oder \"singel\")");
                    break;
                }
            } else {
                System.out.println("Der Ordner " + testFile.getAbsolutePath() + " existiert nicht.");
            }       
        } else {
            System.out.println("Die Eingabe sollte einen Modus (\"timer\" oder \"single\")"
                    + " und eine Zieldatei beinhalten. \n"
                    + "<Modus> <Pfad> <ZeitOffset> <maxSpeicherZeit> <timer>*");
        }   
        Runtime.getRuntime().addShutdownHook(new CleanUpHook(folder.getAbsolutePath(), "Auslastungen"));
    }
}
