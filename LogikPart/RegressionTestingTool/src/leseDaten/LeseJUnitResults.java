package leseDaten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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
 * Diese Klasse liest jUnit Result Dateien ein und Speichter dabei f�r jeden Test den Namen und die dauer.
 * @author Alex
 *
 */
public class LeseJUnitResults {
	/**
	 * XMLReaderTrial
	 */
	public static ITestWerte leseTestsXML(String pfad) {
        TestWerte results = new TestWerte();
	    try {
	        //Erstellen eines XMLReaders um die jUnitResultDateien einzulesen.
	        XMLInputFactory factory = XMLInputFactory.newInstance();
	        InputStream in = new FileInputStream(pfad);
	        XMLEventReader reader = factory.createXMLEventReader(in);
	        //Schritt fuer Schritt druchlaufen der Datei.
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    switch (start.getName().toString()) {
                    case "time": 
                        results.setScore(Double.parseDouble(reader.nextEvent().asCharacters().getData()));
                        //System.out.println(results);
                        break;
                    case "timestamp":
                        results.setTimestamp(reader.nextEvent().asCharacters().getData());
                        //System.out.println(results.toString());
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
        }
	    results.setTestAuslastungen(LeseCPUundRAM.readAuslastung("Data/SysLoadData/ProzessValues.txt"));
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
         * An dieser Stelle wird ein Character Event eingelesen, welches die Daten enth�lt die gesucht sind.
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
    public static List<ITestWerte> getJUnitResultDatei(String pfad) {
        List<ITestWerte> values = new ArrayList<ITestWerte>();
        File file = new File(pfad);
        for (File f : file.listFiles()) {
            values.add(LeseJUnitResults.leseTestsXML(f.getAbsolutePath()));
        }
        return values;
    }
	/**
	 * Test Main Methdoe.
	 * @param args
	 */
	public static void main(String[] args) {
		getJUnitResultDatei("Data/jUnitResults/");
	}
}
