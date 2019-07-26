package leseDaten;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import testDatenTypen.TestAuslastungen;
/**
 * Diese Klasse enthaelt Methoden mit denen die RAM und CPU 
 * Auslastung wieder eingelesen werden kann.
 * 
 * @author Alexander Weber
 *
 */
public class LeseCPUundRAM {
	/**
	 * Inputstrem zum Einlesen der Daten.
	 */
	private static BufferedReader stream;
	   /**
     * Inputstrem zum Einlesen der Daten.
     */
    private static InputStreamReader inStream;
	/*
	 * private static SimpleDateFormat sdf = 
	 * new SimpleDateFormat("yyyy-mm-dd'T'hh:m");
	 */
	/**
	 * Diese Datei liest eine der ProzessValues Datei ein. 
	 * Struktur: <timestamp>;<CPUAuslasrung>;<RAMAuslastung>;<Date>
	 * TimeStamp hat die Uhrzeit mit Millisekunden, CPU Auslastung in 
	 * Prozent und RAM Auslastung in GB. Date dient nur der Leserlichkeit,
	 * dieser Part kann zum Ende hin entfernt werden.
	 * @return Eine HashMap Key ist dabei die Zeit und Value sind RAM und 
	 * CPU Auslastung.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static  List<TestAuslastungen> readAuslastung (String target) throws IOException, InterruptedException {
	    int dummyCounter = 0;
        List <TestAuslastungen> loads = new ArrayList<TestAuslastungen>();
        Path copiedTo = Paths.get(target + "Copy");
        File tempFile = new File(target);
        if (tempFile.exists()) {
            while (!tempFile.canRead() && dummyCounter < 10) {
                Thread.sleep(10);
                System.out.println("Cant Read " + dummyCounter);
            }
            if(tempFile.canRead()) {
                Files.copy(tempFile.toPath(), copiedTo, StandardCopyOption.REPLACE_EXISTING);
            }
            target+= "Copy";
            File copiedFile = new File(target);
            System.out.println("Target " + copiedFile.getAbsolutePath());
            System.out.println("Exist " + copiedFile.exists());
            System.out.println("Copied " + copiedFile.canRead());
            if (copiedFile.exists() && copiedFile.canRead()) {
                FileInputStream reader = new FileInputStream(new File(target));
                inStream =  new InputStreamReader(reader, "UTF-8"); 
                stream = new BufferedReader(inStream);
                while (stream.ready()) {
                    /**
                     * Splitted die Daten der Date anahnd des ";" in die drei
                     * Komponenten.
                     */
                    String tempString = stream.readLine();
                    if (tempString != null) {
                        String[] temp = tempString.split(";");
                        loads.add(new TestAuslastungen(new Timestamp(Long.parseLong(temp[0]))
                                , Double.parseDouble(temp[1])
                                , Double.parseDouble(temp[2])));   
                    }
                }
                stream.close();
                if (!copiedFile.delete()) {
                    System.out.println("Die Datei konnte nicht geloescht werden.");
                }
            }
        }
	    return loads;
	}
	
	public static  List<TestAuslastungen> readAuslastungen (String target,
	        double score) throws IOException, InterruptedException {
        List <TestAuslastungen> loads = new ArrayList<TestAuslastungen>();
        File folder = new File(target);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            //Sortiere Files nach absteigender letzter Bearbeitung.
            Arrays.sort(files, (a,b) -> (Long.compare(b.lastModified(), a.lastModified())));
            for (int i = 0; i < files.length && 
                    i <= TimeUnit.MILLISECONDS.toHours((long)(score * 1000)); i++) {
                System.out.println("SCHLEIFENLAUF READ TESTS");
                if (files[i].getName().contains("Auslastungen")) {
                    loads.addAll(readAuslastung(files[i].getAbsolutePath()));
                }
            }
        }
        System.out.println("MENGE AND DATEN " + loads.size());
        return loads;
	}
	/**
	 * Test Mainmethode.
	 * @param args
	 */
	public static void main(String[] args) {
        try {
            readAuslastung("Data/SysLoadData/ProzessValues.txt");
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
