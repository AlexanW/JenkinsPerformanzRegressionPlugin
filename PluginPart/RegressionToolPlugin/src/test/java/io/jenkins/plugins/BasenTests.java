package io.jenkins.plugins;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import leseDaten.LeseBasis;
import testDatenTypen.IBasis;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.RegressionTestResult;
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
    //@Test
    public void testeEinlesen() {
        IBasis basisLesen = null;
        LeseBasis lese = new LeseBasis();
        try {
            basisLesen = lese.leseObjektIBasisEin("src/main/resources/TestDaten/Basen/Alt.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (basisLesen != null) {
            System.out.println(basisLesen);
        }
    }
    @Test
    public void erstelleBasis() {
        IErstelleBasis erstelle = new ErstelleBasis();
        try {
            IBasis basis = erstelle.erstelleBasis("src/main/resources/TestDaten/builds"
                    , "src/main/resources/TestDaten/Basen"
                    , 0.0, 5, 100, "junitResult.xml"
                    , new PrintStream("src/main/resources/TestDaten/builds/auswertungen.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
