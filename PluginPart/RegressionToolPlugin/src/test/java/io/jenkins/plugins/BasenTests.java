package io.jenkins.plugins;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import leseDaten.LeseBasis;
import testDatenTypen.IBasis;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.RegressionTestResult;
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
}
