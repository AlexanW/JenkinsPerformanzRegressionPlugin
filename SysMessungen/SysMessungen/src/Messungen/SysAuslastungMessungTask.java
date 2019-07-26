package Messungen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.uni_hildesheim.sse.system.GathererFactory;
/**
 * Eine Klasse die ,die TimerTask zum Messen von CPU und RAM auslastung definiert.
 * @author Alexander Weber
 *
 */
public class SysAuslastungMessungTask extends TimerTask {
    /**
     * Der Dateiname für die Auslastungsdateien. Wird um einen Counter ergänzt.
     */
    private static final String DATEINAME = "Auslastungen";
    /**
     * Ein Dateforamt umd die akutelle Systemzeit zu bestimmen.
     */
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"
            + ".SSS");
    /**
     * FileStream um die gesammelten Daten in eine Datei zu schreiben.
     */
    private FileOutputStream stream;
    /**
     * Der Timer, der diese Task ausfürht. Wird verwendet um im Fall von 
     * Exceptionss diesen Prozess zu beenden.
     */
    private Timer timer;
    /**
     * Die Differenz zwischen der Systemzeit und der Zeit der JUnitResult Datei
     * die Jenkins generiert.
     */
    private long zeitOffset;
    /**
     * Der Ordner in dem die Datein liegen sollen.
     */
    private File zielFolder;
    /**
     * Die maximale Speicherzeit bevor Daten geloescht werden, in millisekunden.
     */
    private double maxSpeicherZeit;
    /**
     * Die Summe der aktuelle gespeicherten Daten.
     */
    private double aktuelleSpeichersumme;
    /**
     * Counter bis zum Dateiwechsel.
     */
    private double aktuelleSpeicherdauerDatei;
    /**
     * Die Intervallzeit.
     */
    private double steps;
    /**
     * Dieser Int wird an den Dateinamen angehaengt um Dateien zu unterschieden.
     */
    private int dateienCounter;
    /**
     * Diese Task erstellt oder Überschreibt eine Datei names 
     * "ProzessValues.txt". In dieser Werden die Ergebnisse der Gatherer 
     * gesammelt, und für die Auswertung bereitgestellt.
     * @throws FileNotFoundException Sollte die Datei in die geschrieben werden
     *  soll nicht gefunden werden.
     */
    public SysAuslastungMessungTask(Timer timer, String pfad, long zeitOffset,
            int maxSpeicherZeit, double intervall) 
            throws FileNotFoundException {
        super();
        this.zeitOffset = zeitOffset;
        zielFolder = new File(pfad);
        stream = new FileOutputStream(zielFolder.getAbsoluteFile() + "/" + DATEINAME + "0.txt", true);
        dateienCounter = 0;
        this.timer = timer;
        steps = intervall;
        aktuelleSpeichersumme = 0;
        this.maxSpeicherZeit = TimeUnit.HOURS.toMillis(maxSpeicherZeit);
    }
    /**
     * Im Run werden die Daten gesammelt und in eine, im Konstruktor definierte
     *  Datei geschrieben.
     */
    @Override
    public void run() {
            try {
                if (aktuelleSpeichersumme >= maxSpeicherZeit) { 
                    loescheAeltesteDatei(zielFolder.getAbsolutePath());
                }
                erneuereStream();
                /**
                 * memoryGatherer gibt Byte Werte zurück, daher werden diese 
                 * hier durch 1000000000.0 geteilt  um sie auf GB zu bringen.
                 * processorGatherer gibt prozentuale auslastungen zurück
                 * Die Werte werden hier in einer .txt geschriebven, um sie 
                 * händisch nachvollziehen zu können
                 * Per Date wird hier die Uhrzeit der Messergebnisse genommen.
                 * Trennung um die Auswertung zu erleichertern geschieht
                 *  per "|" und "\n"
                 */
                stream.write(
                        ((System.currentTimeMillis() - zeitOffset)
                                + ";" + GathererFactory.getProcessorDataGatherer().getCurrentSystemLoad() 
                                // Teilen durch 1000000000.0 für GB.
                                + ";" + (GathererFactory.getMemoryDataGatherer().getCurrentMemoryUse() / 1000000000.0) 
                                + ";" + timeFormat.format((System.currentTimeMillis() - zeitOffset)) + "\n")
                        .getBytes("UTF-8"));
                aktuelleSpeichersumme += steps;
                aktuelleSpeicherdauerDatei += steps;
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                timer.cancel();
            }
    }
    /**
     * Entfernt die aelsteste Auslastungsdatei aus dem Ordner.
     * @param pfad Der Pfad des Ordners aus dem die Datei entfernt werden soll.
     */
    private void loescheAeltesteDatei (String pfad) {
        File file = new File(pfad);
        boolean dateiGeloescht = false;
        File[] files = file.listFiles();
        /*
         * Sortiere die Files aufsteigent nach lastModified.
         */
        Arrays.sort(files, (a,b) -> (Long.compare(a.lastModified(), b.lastModified())));
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(DATEINAME) && !dateiGeloescht) {
                dateiGeloescht = files[i].delete();
                aktuelleSpeichersumme -= TimeUnit.HOURS.toMillis(1);
            }
        }
    }
    private void erneuereStream() {
        try {
            stream.close();
            //System.out.println("aktuell: " + aktuelleSpeicherdauerDatei + " Vergleich : " + TimeUnit.MINUTES.toMillis(1));
            if (aktuelleSpeicherdauerDatei >= TimeUnit.HOURS.toMillis(1)) {
                if (TimeUnit.HOURS.toMillis(dateienCounter+1) == maxSpeicherZeit) {
                    dateienCounter = 0;
                } else {
                    dateienCounter++;
                }
                //System.out.println("Wechsel Datei zu " + dateienCounter + " bei " + aktuelleSpeichersumme);
                stream = new FileOutputStream(zielFolder.getAbsolutePath() + "/" 
                        + DATEINAME + dateienCounter + ".txt", true);
                aktuelleSpeicherdauerDatei = 0;
            } else {
                //System.out.println("Files: " + dateienCounter);
                stream = new FileOutputStream(zielFolder.getAbsolutePath() + "/" 
                        + DATEINAME + dateienCounter + ".txt", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
