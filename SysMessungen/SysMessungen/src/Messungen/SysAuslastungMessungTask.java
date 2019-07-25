package Messungen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import de.uni_hildesheim.sse.system.GathererFactory;
/**
 * Eine Klasse die ,die TimerTask zum Messen von CPU und RAM auslastung definiert.
 * @author Alexander Weber
 *
 */
public class SysAuslastungMessungTask extends TimerTask {
    /**
     * Ein Dateforamt umd die akutelle Systemzeit zu bestimmen.
     */
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS");
    /**
     * FileStream um die gesammelten Daten in eine Datei zu schreiben.
     */
    private FileOutputStream stream;
    /**
     * Der Timer, der diese Task ausfürht. Wird verwendet um im Fall von Exceptionss diesen Prozess zu beenden.
     */
    private Timer timer;
    /**
     * 
     */
    private long zeitOffset;
    /**
     * 
     */
    private File zielFile;
    /**
     * Diese Task erstellt oder Überschreibt eine Datei names "ProzessValues.txt".
     * In dieser Werden die Ergebnisse der Gatherer gesammelt, und für die Auswertung bereitgestellt.
     * @throws FileNotFoundException Sollte die Datei in die geschrieben werden soll nicht gefunden werden.
     */
    public SysAuslastungMessungTask(Timer timer, String pfad, long zeitOffset) throws FileNotFoundException {
        super();
        this.zeitOffset = zeitOffset;
        zielFile = new File(pfad);
        stream = new FileOutputStream(zielFile);
        this.timer = timer;
    }
    /**
     * Im Run werden die Daten gesammelt und in eine, im Konstruktor definierte Datei geschrieben.
     */
    @Override
    public void run() {
            try {
                /**
                 * memoryGatherer gibt Byte Werte zurück, daher werden diese hier durch 1000000000.0 geteilt 
                 * um sie auf GB zu bringen.
                 * processorGatherer gibt prozentuale auslastungen zurück
                 * Die Werte werden hier in einer .txt geschriebven, um sie händisch nachvollziehen zu können
                 * Per Date wird hier die Uhrzeit der Messergebnisse genommen.
                 * Trennung um die Auswertung zu erleichertern geschieht per "|" und "\n"
                 */
                stream.write(
                        ((System.currentTimeMillis() - zeitOffset)
                                + ";" + GathererFactory.getProcessorDataGatherer().getCurrentSystemLoad() 
                                // Teilen durch 1000000000.0 für GB.
                                + ";" + (GathererFactory.getMemoryDataGatherer().getCurrentMemoryUse() / 1000000000.0) 
                                + ";" + timeFormat.format((System.currentTimeMillis() - zeitOffset)) + "\n")
                        .getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
                timer.cancel();
            }
    }
    @Override
    public boolean cancel() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!zielFile.delete()) {
            System.out.println("Die Datei " + zielFile.getAbsolutePath() + ""
                    + " konnte nicht geloescht werden.");
        }
        return super.cancel();
    }
    
}
