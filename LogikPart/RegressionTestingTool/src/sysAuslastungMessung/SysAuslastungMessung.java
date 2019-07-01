package sysAuslastungMessung;

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
    public static void startMeasurement() {
        timer = new Timer();
        try {
            timer.schedule(new SysAuslastungMessungTask(timer), 10, 500);
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
        startMeasurement();
        bestimmeDatei("Dummy");
    }
}
