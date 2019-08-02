package io.jenkins.plugins;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.ITestWerte;

public class ReadJUnitDateiTestClass {
    
    @Test
    public void testeEinlesenAlle() {
        try {
            List<ITestWerte> werte = 
                    LeseJUnitResults.getJUnitResultDateiAusBuilds
                    ("src/main/resources/TestDaten/builds", 10, 100, "junitResult.xml");;
            assertTrue(werte.size() == 9);
            for (ITestWerte t : werte) {
                System.out.println(t.getScore());
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testeEinzelnEinlesen() {
        ITestWerte werte = LeseJUnitResults.leseTestsXML("src/main/resources/TestDaten/TesteEinzelneResultDatei/junitResult.xml", 100);
        List<String> namen = new ArrayList<String>();
        namen.add("de.uni_hildesheim.sse.vil.rt.tests.RampUpTest.rampUpTest");
        namen.add("net.ssehub.easy.instantiation.ant.ExecutionTests.testAnt");
        assertTrue(werte.getTimeStapm().equals("2019-07-31T13:15:26"));
        assertTrue(werte.getScore() == 100.135994);
    }
    
    @Test
    public void testeErstennenUndLesenVonTestWerten() {
        //Relativer Pfad zu TestDaten "src/main/resources/TestDaten"
        File resultFile = new File("src/main/resources/TestDaten/TesteMatchenAuslastungenResults/ResultDateien/100/junitResult.xml");
        File resultFile1 = new File("src/main/resources/TestDaten/TesteMatchenAuslastungenResults/ResultDateien/101/junitResult.xml");
        File resultFile2 = new File("src/main/resources/TestDaten/TesteMatchenAuslastungenResults/ResultDateien/102/junitResult.xml");
        if (resultFile.exists() && resultFile1.exists() && resultFile2.exists()) {
            ITestWerte werte = LeseJUnitResults.leseTestsXML(resultFile.getAbsolutePath(), 100);
            ITestWerte werte1 = LeseJUnitResults.leseTestsXML(resultFile1.getAbsolutePath(), 100);
            ITestWerte werte2 = LeseJUnitResults.leseTestsXML(resultFile2.getAbsolutePath(), 100);
          System.out.println(werte);
          System.out.println(werte1);
          System.out.println(werte2.getTestsAlsString());
          
            if (werte != null) {
                try {
                    werte.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/TesteMatchenAuslastungenResults/Auslastungen/TestDateiH11M04S10DH11M05S10.txt"));
                    werte1.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/TesteMatchenAuslastungenResults/Auslastungen/TestDateiH11M07S40DH11M07S53.txt"));
                    werte2.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/TesteMatchenAuslastungenResults/Auslastungen/TestDateiH11M15S09DH11M17S51.txt"));
                    System.out.println(werte2.getTestsAlsString());
                    if (werte != null && werte1 != null && werte2 != null) {
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                "src/main/resources/TestDaten/TesteMatchenAuslastungenResults/ResultDateien/100/testWerte.txt", werte);
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                "src/main/resources/TestDaten/TesteMatchenAuslastungenResults/ResultDateien/101/testWerte.txt", werte1);
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                "src/main/resources/TestDaten/TesteMatchenAuslastungenResults/ResultDateien/102/testWerte.txt", werte2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}
