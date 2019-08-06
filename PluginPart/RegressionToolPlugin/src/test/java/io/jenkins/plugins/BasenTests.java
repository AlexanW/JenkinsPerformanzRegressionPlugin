package io.jenkins.plugins;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import leseDaten.ILeseBasis;
import leseDaten.LeseBasis;
import testDatenTypen.Basis;
import testDatenTypen.BasisMitTests;
import testDatenTypen.IBasis;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.RegressionTestResult;
import testDatenTypen.Status;
import testRegression.ErstelleBasis;
import testRegression.IErstelleBasis;
import testRegression.TestVergleichArten;

public class BasenTests {

    //@Test
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
        TestVergleichArten.vergleicheTests((ITestObjektGruppe)neueBasis,
                (ITestObjektGruppe)alteBasis, erwarteteRegression,
                "src/main/resources/TestDaten");
        System.out.println(result.getNachricht());
    }
    @Test
    public void testeEinlesen() {
        IBasis basisLesen = null;
        IBasis basisLesen2 = null;
        LeseBasis lese = new LeseBasis();
        try {
            //src/main/resources/TestDaten/Basen/Alt.txt
            basisLesen = lese.leseObjektIBasisEin("F:\\Uni\\Jenkins\\jobs\\TestingProjekt\\basen/Alt.txt");
            basisLesen2 = lese.leseObjektIBasisEin("F:\\Uni\\Jenkins\\jobs\\TestingProjekt\\basen/Neu.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (basisLesen != null) {
            System.out.println(basisLesen);
            System.out.println(basisLesen2);
        }
    }
    //@Test
    public void erstelleBasis() {
        IErstelleBasis erstelle = new ErstelleBasis();
        try {
            IBasis basis = erstelle.erstelleBasis("src/main/resources/TestDaten/builds"
                    , "src/main/resources/TestDaten/Basen"
                    , 0.0, 5, 100, "junitResult.xml"
                    , new PrintStream("src/main/resources/TestDaten/builds/auswertungen.txt"));
            IBasis basisLesen = null;
            LeseBasis lese = new LeseBasis();
            basisLesen = lese.leseObjektIBasisEin("src/main/resources/TestDaten/Basen/Alt.txt");
            System.out.println("Alt: " + basisLesen.toString());
            IBasis basisLesen1 = null;
            basisLesen1 = lese.leseObjektIBasisEin("src/main/resources/TestDaten/Basen/Neu.txt");
            System.out.println("Neu: " + basisLesen1.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Test
    public void testeBasenRegressionsTest() {
            IBasis basisAlt = new Basis("Test", 1.873, 2.3, 1.9, 0.2, null, 40);
            IBasis basisNeu = new Basis("Test", 1.982, 2.2, 1.8, 0.2, null, 40);
            Status status = Status.NEUTRAL;
            status = TestVergleichArten.vergleicheBasen(basisAlt, basisNeu, 0.005);
            System.out.println("Status ist: " + status);
//            status = TestVergleichArten.vergleicheBasen((BasisMitTests)basisAlt, (BasisMitTests) basisNeu, 1.0, 0.005);
//            System.out.println("Status nach APPACE THINS: " + status); 
    }
}
