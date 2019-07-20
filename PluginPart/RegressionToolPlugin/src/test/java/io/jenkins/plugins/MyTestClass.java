package io.jenkins.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.ITestWerte;

public class MyTestClass {
    @Test
    public void testBaseCreation() {
        //Relativer Pfad zu TestDaten "src/main/resources/TestDaten"
        File resultFile = new File("src/main/resources/TestDaten/ResultDateien/100/junitResult.xml");
        if (resultFile.exists()) {
            ITestWerte werte = LeseJUnitResults.leseTestsXML(resultFile.getAbsolutePath(), 100);
            if (werte != null) {
                try {
                    werte.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/Auslastungen/TestDateiH11M04S10DH11M05S10.txt"));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(werte.toString());
                try {
                    LeseSchreibeTestWerte.schreibeTestWerte(
                            "src/main/resources/TestDaten/ResultDateien/100/testWerte.txt", werte);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        File resultFile1 = new File("src/main/resources/TestDaten/ResultDateien/101/junitResult.xml");
        if (resultFile1.exists()) {
            ITestWerte werte = LeseJUnitResults.leseTestsXML(resultFile1.getAbsolutePath(), 100);
            if (werte != null) {
                try {
                    werte.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/Auslastungen/TestDateiH11M07S40DH11M07S53.txt"));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(werte.toString());
                try {
                    LeseSchreibeTestWerte.schreibeTestWerte(
                            "src/main/resources/TestDaten/ResultDateien/101/testWerte.txt", werte);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        File resultFile2 = new File("src/main/resources/TestDaten/ResultDateien/102/junitResult.xml");
        if (resultFile2.exists()) {
            ITestWerte werte = LeseJUnitResults.leseTestsXML(resultFile2.getAbsolutePath(), 100);
            if (werte != null) {
                try {
                    werte.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/Auslastungen/TestDateiH11M15S09DH11M17S51.txt"));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(werte.toString());
                try {
                    LeseSchreibeTestWerte.schreibeTestWerte(
                            "src/main/resources/TestDaten/ResultDateien/102/testWerte.txt", werte);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
