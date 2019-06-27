package sysAuslastungMessung;
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
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");
    /**
     * FileStream um die gesammelten Daten in eine Datei zu schreiben.
     */
    private FileOutputStream stream;
    /**
     * Der Timer, der diese Task ausf�rht. Wird verwendet um im Fall von Exceptionss diesen Prozess zu beenden.
     */
    private Timer timer;
    /**
     * Diese Task erstellt oder �berschreibt eine Datei names "ProzessValues.txt".
     * In dieser Werden die Ergebnisse der Gatherer gesammelt, und f�r die Auswertung bereitgestellt.
     * @throws FileNotFoundException Sollte die Datei in die geschrieben werden soll nicht gefunden werden.
     */
    public SysAuslastungMessungTask(Timer timer) throws FileNotFoundException {
        super();
        stream = new FileOutputStream(new File("Data/SysLoadData/ProzessValues.txt"));
        this.timer = timer;
    }
    /**
     * Im Run werden die Daten gesammelt und in eine, im Konstruktor definierte Datei geschrieben.
     */
    @Override
    public void run() {
            try {
                /**
                 * memoryGatherer gibt Byte Werte zur�ck, daher werden diese hier durch 1000000000.0 geteilt 
                 * um sie auf GB zu bringen.
                 * processorGatherer gibt prozentuale auslastungen zur�ck
                 * Die Werte werden hier in einer .txt geschriebven, um sie h�ndisch nachvollziehen zu k�nnen
                 * Per Date wird hier die Uhrzeit der Messergebnisse genommen.
                 * Trennung um die Auswertung zu erleichertern geschieht per "|" und "\n"
                 */
                stream.write(
                        (System.currentTimeMillis()
                                + ";" + GathererFactory.getProcessorDataGatherer().getCurrentSystemLoad() 
                                + ";" + (GathererFactory.getMemoryDataGatherer().getCurrentMemoryUse()/1000000000.0) 
                                + ";" + timeFormat.format(System.currentTimeMillis()) + "\n")
                        .getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                timer.cancel();
            }
    }
}
