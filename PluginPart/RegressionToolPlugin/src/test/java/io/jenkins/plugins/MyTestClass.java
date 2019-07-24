package io.jenkins.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import leseDaten.LeseBasis;
import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.IBasis;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;
import testRegression.TestVergleichArten;

public class MyTestClass {
    
    public void testBaseCreation() {
        //Relativer Pfad zu TestDaten "src/main/resources/TestDaten"
        File resultFile = new File("src/main/resources/TestDaten/ResultDateien/100/junitResult.xml");
        File resultFile1 = new File("src/main/resources/TestDaten/ResultDateien/101/junitResult.xml");
        File resultFile2 = new File("src/main/resources/TestDaten/ResultDateien/102/junitResult.xml");
        if (resultFile.exists() && resultFile1.exists() && resultFile2.exists()) {
            ITestWerte werte = LeseJUnitResults.leseTestsXML(resultFile.getAbsolutePath(), 100);
            ITestWerte werte1 = LeseJUnitResults.leseTestsXML(resultFile1.getAbsolutePath(), 100);
            ITestWerte werte2 = LeseJUnitResults.leseTestsXML(resultFile2.getAbsolutePath(), 100);
            if (werte != null) {
                try {
                    werte.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/Auslastungen/TestDateiH11M04S10DH11M05S10.txt"));
                    werte1.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/Auslastungen/TestDateiH11M07S40DH11M07S53.txt"));
                    werte2.setTestAuslastungen(LeseCPUundRAM.readAuslastung("src/main/resources/TestDaten/Auslastungen/TestDateiH11M15S09DH11M17S51.txt"));
                    if (werte != null && werte1 != null && werte2 != null) {
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                "src/main/resources/TestDaten/ResultDateien/100/testWerte.txt", werte);
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                "src/main/resources/TestDaten/ResultDateien/101/testWerte.txt", werte1);
                        LeseSchreibeTestWerte.schreibeTestWerte(
                                "src/main/resources/TestDaten/ResultDateien/102/testWerte.txt", werte2);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(werte);
                System.out.println(werte1);
                System.out.println(werte2);
            }
        }
        IErstelleBasis erstelleBasis = new ErstelleBasis();
        try {
            IBasis basis = erstelleBasis.erstelleBasis("src/main/resources/TestDaten/ResultDateien"
                    , "src/main/resources/TestDaten/Basen", 0.0, 2, 100, "junitResult.xml"
                    , new PrintStream("src/main/resources/TestDaten/Logger.txt"));
            if (basis != null) {
                System.out.println(basis);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    public void testeEinlesen() {
        try {
            List<ITestWerte> werte = 
                    LeseJUnitResults.getJUnitResultDateiAusBuilds
                    ("src/main/resources/TestDaten", 1, 100, "junitResult.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testeBasenTestRegression() {
        RegressionTestResult result = new RegressionTestResult();
        double erwarteteRegression = 0.1;
       LeseBasis read = new LeseBasis();
        IBasis neueBasis = null;
        IBasis alteBasis = null;
        try {
            neueBasis = read.leseObjektIBasisEin("src/main/resources/TestDaten/Basen/Neu.txt");
            alteBasis = read.leseObjektIBasisEin("src/main/resources/TestDaten/Basen/Alt.txt");
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        result.addTextZuNachricht(TestVergleichArten.vergleicheTests((ITestObjektGruppe)alteBasis, (ITestObjektGruppe)neueBasis, erwarteteRegression));
        System.out.println(result.getNachricht());
    }
}
