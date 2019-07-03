package leseDaten;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import testDatenTypen.TestAuslastungen;
/**
 * Diese Klasse enthält Methoden mit denen die RAM und CPU 
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
	 */
	public static  List<TestAuslastungen> readAuslastung (String target) {
	    List <TestAuslastungen> loads = new ArrayList<TestAuslastungen>();
	    try {
	      
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
                    loads.add(new TestAuslastungen(Double.parseDouble(temp[1])
                            , Double.parseDouble(temp[2])
                            , new Timestamp(Long.parseLong(temp[0]))));   
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return loads;
	}
	/**
	 * Test Mainmethode.
	 * @param args
	 */
	public static void main(String[] args) {
        readAuslastung("Data/SysLoadData/ProzessValues.txt");
    }
}
