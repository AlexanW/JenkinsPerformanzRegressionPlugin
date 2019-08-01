package testRegression;

import testDatenTypen.IBasis;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;
import testDatenTypen.Status;

public class TestVergleichen implements ITestVergleich{
    /**
     * 
     */
    @Override
    public RegressionTestResult vergleicheBasen(IBasis neueBasis, IBasis alteBasis
            , double erwarteteRegression, double alpha, String pfad) {
        RegressionTestResult result = new RegressionTestResult();
        result.setResutlDerTests(TestVergleichArten.vergleicheBasen(alteBasis, neueBasis, erwarteteRegression, alpha));
        if (result.getResutlDerTests() == Status.GROESSER) {
            result.addTextZuNachricht("Die neue Basis weist dabei eine Regression im Vergleich zu der alten auf."
                    + " Es galt eine erwartete Regression von " + erwarteteRegression);
            if (neueBasis instanceof ITestObjektGruppe && alteBasis instanceof ITestObjektGruppe) {
                TestVergleichArten.vergleicheTests((ITestObjektGruppe)alteBasis,
                        (ITestObjektGruppe)neueBasis,
                        ((alteBasis.getObergrenze() - alteBasis.getScore())/alteBasis.getScore()),
                        pfad);
            }
        } else {
            result.addTextZuNachricht("Keine Regression ausserhalb der " + (erwarteteRegression*100) + "% Grenze"
                    + "fuer Regression.");
        }
        return result;
    }
    
    /**
     * 
     */
    @Override
    public RegressionTestResult vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
            ,double erwarteteRegression, String pfad, double auslastungTolleranz) {
        RegressionTestResult result = new RegressionTestResult();
        result.setResutlDerTests(TestVergleichArten.testWerteAuserhalbDerGrenzen(
                testWerte, basis, erwarteteRegression));
            /*
             * Wenn die durchschnittliche Laufzeit auserhabl der Grenzen liegt
             * wird davon ausgegangen, dass es sich um Performanz
             * Regression oder einen Ausreisser handelt.
             */
            if (result.getResutlDerTests() == Status.GROESSER) {
                if (basis instanceof ITestObjektGruppe) {
                    result.setResutlDerTests(TestVergleichArten.vergleicheTestsAuslastungen(
                                    (ITestObjektGruppe)basis,
                                    testWerte, auslastungTolleranz, pfad));
                    TestVergleichArten.vergleicheTests(testWerte, 
                            (ITestObjektGruppe)basis, 
                            ((basis.getObergrenze() - basis.getScore())/basis.getScore()),
                            pfad);
                }
                if (result.getResutlDerTests() != Status.AUSREISSER) {
                    result.addTextZuNachricht("Die Tests fallen aus den Grenzen der Basis."
                            + "Daher wird angenommen, dass es sich um Regression "
                            + "handelt.\nUntergrenze: " 
                            + basis.getUntergrenze() + " Obergrenze: "
                            + basis.getObergrenze()  + " TestZeit: " 
                            + testWerte.getScore() 
                            + " Bei einem Intervall um die mittlere "
                            + "Laufzeit von maximal: " 
                            + basis.getTolleranz() 
                            + " (0.0 bedeutet, dass die Grenzen "
                            + "min und max Werte aus den Verwendeten Messungen sind) \n");
                }
            } else if (result.getResutlDerTests() == Status.KLEINER) {
                /*
                 * Warnung, dass die Tests schneller als erwartet liefen und
                 * es probleme geben koennte.
                 */
                result.addTextZuNachricht("Die Tests wurden schneller abgeschlossen"
                        + " als erwarte, dies koennte auf fehlerhaftes "
                        + "Verhalten beim Testen deuten.");
            } else {
                result.setResutlDerTests(Status.IM_BEREICH);
                result.addTextZuNachricht("Es scheint keine Regression vorzuliegen.");
            }
        return result;
    }
}