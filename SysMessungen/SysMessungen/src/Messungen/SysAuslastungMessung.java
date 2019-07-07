package Messungen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;

public class SysAuslastungMessung {
    /**
     * Der Timer, der fuer das Ausfuehren dieser Aufgabe zustaendig ist.
     */
    private static Timer timer;
    /**
     * Der Pfad der Dateien in die, die Messdaten geschrieben werden.
     * Dabei fehlt die Nummer der Datei und die .txt Endung, beides wird
     * bei dem Start einer Task hinzugefügt.
     */
    private final static String PATH = "Data/SysLoadData/ProzessValues";
    /**
     * Dieser Methode stellt den Timer ein uns startet den Messvorgang. Sollte die 
     * TimerTask eine Exception werfen, so wird der Timer beendet.
     */
    public static void startMeasurementTimer(String pfad, int steps) {
        timer = new Timer();
        try {
            timer.schedule(new SysAuslastungMessungTask(timer, pfad), 10, steps);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            timer.cancel();
        }
    }
    /**
     * Beendet die TimerTask, die fuer das Messen zustaendig ist.
     */
    public static void endMeasurement() {
        timer.cancel();
    }
    /**
     * Eine Methode um festzulegen in welche ProzessValues Datei geschrieben wird.
     * Dabei wird darauf geachtet, dass es eine max. Anzahl X gibt.
     */
    private static String bestimmeDatei(String name) {
        return PATH + name +"txt";
    }
    
    public static void main(String[] args) {
        System.out.println("Bitte einen Modus und einen Zielordner eingeben.");
        if (args.length > 1  ) {
            switch(args[0]) {
            case "timer":
                if (args.length > 2) {
                    File testFile = new File(args[1]);
                    startMeasurementTimer(args[1], Integer.parseInt((args[2])));
                    //TO DO END TIMER?
                    System.out.println("Bitte eine eingabe Taetigen um das Programm zu beenden.");
                    endMeasurement();
                } else {
                    System.out.println("Ein Timer benoetigt eine Tickrate in Hundertstelsekunden.");
                }
                break;
            case "single":
                
                break;
            default:
                System.out.println("Die erste Eingabe ist kein bekanntest Kommando (\"timer\" oder \"singel\")");
                break;
            }
        } else {
            System.out.println("Die Eingabe sollte einen Modus (\"timer\" oder \"single\""
                    + " und eine Zieldatei beinhalten.");
        }
        startMeasurementTimer("Data/SysLoadData/ProzessValues.txt", 100);
        bestimmeDatei("Dummy");
    }
}
