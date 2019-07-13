package leseDaten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import testDatenTypen.ITest;
import testDatenTypen.ITestWerte;
import testDatenTypen.Test;
import testDatenTypen.TestWerte;

/**
 * Diese Klasse liest jUnit Result Dateien ein und Speichter dabei fuer jeden Test den Namen und die dauer.
 * @author Alex
 *
 */
public class LeseJUnitResults {
    public final static String JUNIT_DATAEINAME = "junitResult.xml";
    public final static String TESTWERTE_DATEINAME = "testWerte.res"; 
	/**
	 * XMLReaderTrial
	 */
	public static ITestWerte leseTestsXML(String pfad, double step_size) {
	    ITestWerte results = new TestWerte(step_size);
	    XMLEventReader reader = null;
	    InputStream in = null;
	    try {
	        //Erstellen eines XMLReaders um die jUnitResultDateien einzulesen.
	        XMLInputFactory factory = XMLInputFactory.newInstance();
	        System.out.println("XML in Question: " + pfad + "/" + JUNIT_DATAEINAME);
	        in = new FileInputStream(pfad + "/" + JUNIT_DATAEINAME);
	        reader = factory.createXMLEventReader(in);
	        //Schritt fuer Schritt druchlaufen der Datei.
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    switch (start.getName().toString()) {
                    case "time": 
                        results.setScore(Double.parseDouble(reader.nextEvent().asCharacters().getData()));
                        break;
                    case "timestamp":
                        results.setTimestamp(reader.nextEvent().asCharacters().getData());
                        break;
                    case "case":
                        results.addTest(leseTestFall(reader));
                        break;
                    case "name":
                        results.setName(reader.nextEvent().asCharacters().getData());
                        break;
                    default:
                        break;
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        } finally {
 
        }
        try {
            if (in != null) {
                in.close();
            }
            if( reader != null) {
                reader.close();                
            }
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }  
	    //results.setTestAuslastungen(LeseCPUundRAM.readAuslastung("Data/SysLoadData/ProzessValues.txt"));
	    
		return results;
	}
	/**
	 * Eine Klasse, die einen Case aus einer jUnitResultDatei einliest und in einen Test schreibt.
	 * 
	 */
	private static ITest leseTestFall (XMLEventReader reader) {
	    ITest temp = new Test();
	    try {
            XMLEvent event = reader.nextEvent();
            do {
                /**
                 * Diese Schleife ueberschreitet alle nicht relevanten Tags, bis zu dem naechsten Starttag.
                 */                
                do {
                    event = reader.nextEvent();
                } while (!event.isStartElement());
                
                setTestModi(event, temp, reader);
                //System.out.println(temp.toString());
                /**
                 * Da hiervor die Character eingelesen werden, ist der naechste Schritt ein EndElement.
                 */
                event = reader.nextEvent();
                /**
                 * Die Bedingung stellt sicher, dass am Ende eines Case Blocks wieder in die aeussere Klasse geht.
                 * failedSince ist immner der letzte endblock in einem Case Block.
                 */
            } while (!(event.isEndElement() && event.asEndElement().getName().toString().equals("failedSince")));

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
	    return temp;
	}	
	/**
	 * Diese Methode liest eine Characterzeile ein und setzt das entsprechende Attribut fuer den Test.
	 * @param event Das aktuell eingelesene Event.
	 * @param temp Der temporaere Test, welcher in die TestValues eingefuegt wird. 
	 * @param reader Der XMLReader der die Datei einliest.
	 */
	private static void setTestModi (XMLEvent event, ITest temp, XMLEventReader reader) {
        /**
         * An dieser Stelle wird ein Character Event eingelesen, welches die Daten enthaelt die gesucht sind.
         * Dabei werden von {duration,className,testName,skipped,failedSince} duration, testName und failedSince verwendet.
         */
	    try {
	        switch (event.asStartElement().getName().toString()) {
	        case "duration" :
	            event = reader.nextEvent();
	            temp.setScore(Double.parseDouble(event.asCharacters().getData()));
	            break;
	        case "testName" :
	            event = reader.nextEvent();
	            temp.setName(event.asCharacters().getData());
	            break;
	        case "failedSince" :
	            event = reader.nextEvent();
	            temp.setIstFehlgeschlagen( Boolean.parseBoolean(event.asCharacters().getData()));
	            break;
	        default:
	            break;
	        }
	    } catch (XMLStreamException exc) {
	        exc.printStackTrace();
	    }
	}
    /**
     * Diese Methode startet den Vorgang des Einlesens aller JUnitResultDatein
     * die bereitgestellt sind.
     * @param pfad Der Zielordner in dem die Resultdateien Liegen.
     */
    public static List<ITestWerte> getJUnitResultDatei(String pfad, double step_size) {
        List<ITestWerte> values = new ArrayList<ITestWerte>();
        File file = new File(pfad);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                values.add(LeseJUnitResults.leseTestsXML(f.getAbsolutePath(), step_size));
            }
        }
        return values;
    }   
    /**
     * Diese Methode startet den Vorgang des Einlesens aller JUnitResultDatein
     * die bereitgestellt sind.
     * @param pfad Der "builds" Ordner inerhalb eines Jenkins projekts in dem die "build" Folders Liegen.
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws FileNotFoundException 
     */
    public static List<ITestWerte> getJUnitResultDateiAusBuilds(String pfad, int useResults , double step_size) 
            throws FileNotFoundException, ClassNotFoundException, IOException {
        List<ITestWerte> values = new ArrayList<ITestWerte>();
        File file = new File(pfad);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                //Fuer den Fall, dass mehr angegeben wurde, als vorhanden ist, oder 0;
                files = entferneNichtBuildDatein(files);
                if (useResults > files.length || useResults == 0) {
                    useResults = files.length;
                }
                //Sortiert die Files nach absteigender Buildnummer.
                Arrays.sort(files, (a,b) -> (Long.compare(b.lastModified(), a.lastModified())));
                for ( File f : files) {
                    System.out.println(f.getName());
                }
                //Start bei 1 damit der Ordner des aktuellen Runs nicht beachtet wird.
                if (files.length > 1) {
                    for (int i = 1; i <= useResults; i++) {
                        File tempTestWerteFile = new File(files[i].getAbsolutePath() + "/" + TESTWERTE_DATEINAME);
                        if (tempTestWerteFile.exists()) {
                            values.add(LeseSchreibeTestWerte.leseTestWerte(tempTestWerteFile.getAbsolutePath()));
                        } else {
                            File tempFile = new File(files[i].getAbsolutePath() + "/" + JUNIT_DATAEINAME);
                            if (tempFile.exists()) {
                                values.add(leseTestsXML(files[i].getAbsolutePath(), step_size));
                            } else {
                                //Erhoehen der zu verwenden Results falls nicht genug JUnitReulsts gefunden wurden.
                                if (useResults < files.length - 1) {
                                    useResults++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return values;
    }
    
    /**
     * Diese Methode entfernt alle nicht Build folders aus einem "builds" Dir.
     * @param files
     * @return
     */
    private static File[] entferneNichtBuildDatein (File[] files) {
        int removedFiles = 0;
        //Testet ob es sich um einen Ordner nur mit Nummer handelt und entfernt ihn sonst.
        for (int i = 0; i < files.length; i++) {
            try {
                Double.parseDouble(files[i].getName());
            } catch (NumberFormatException e) {
                files[i] = null;
                removedFiles++;
            }
        }
        File[] tempFiles = new File[files.length - removedFiles];
        int i = 0;
        for (File f : files) {
            if (f != null && i < tempFiles.length) {
                tempFiles[i] = f;
                i++;
            }
        }
        return tempFiles;
    }
    
	/**
	 * Test Main Methdoe.
	 * @param args
	 */
	public static void main(String[] args) {
	    getJUnitResultDatei("Data/jUnitResults", 100);
		//getJUnitResultDateiAusBuilds("F:\\Uni\\Jenkins\\jobs\\TestingProjekt\\builds", 5);
	}
}
