package testRegression;

import testDatenTypen.Basis;
import testDatenTypen.IBasis;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.ITestWerte;
import testDatenTypen.Status;
import testDatenTypen.TestWerte;

public class TestVergleichen implements ITestVergleich{
    /**
     * 
     */
    @Override
    public String vergleicheBasen(IBasis neueBasis, IBasis alteBasis
            , double erwarteteRegression, double alpha) {
        Status status = Status.NEUTRAL;
        String result = "";
        status = TestVergleichArten.vergleicheBasen(alteBasis, neueBasis, erwarteteRegression, alpha);
        if (status == Status.GROESSER && neueBasis instanceof ITestObjektGruppe && alteBasis instanceof ITestObjektGruppe) {
            result = TestVergleichArten.vergleicheTests((ITestObjektGruppe)alteBasis, (ITestObjektGruppe)neueBasis, erwarteteRegression);
        }
        result += status.toString();
        return result;
    }
    
    /**
     * 
     */
    @Override
    public String vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
            ,double erwarteteRegression) {
        Status regressionGefunden = Status.NEUTRAL;
        String result = "";
                regressionGefunden = TestVergleichArten.testWerteAuserhalbDerGrenzen(
                        testWerte, basis, erwarteteRegression);
                /*
                 * Wenn die durchschnittliche Laufzeit auserhabl der Grenzen liegt
                 * wird davon ausgegangen, dass es sich um Performanz
                 * Regression oder einen Ausreisser handelt.
                 */
                if (regressionGefunden == Status.GROESSER) {
                    if (basis instanceof ITestObjektGruppe) {
                        result += TestVergleichArten.vergleicheTestsAuslastungen((ITestObjektGruppe)basis, testWerte);
                    }
                    if (result.isEmpty()) {
                        result += "Die Tests fallen aus den Grenzen der Basis."
                                + "Daher wird angenommen, dass es sich um Regression "
                                + "handelt.\nUntergrenze: " 
                                + basis.getUntergrenze() + " Obergrenze: "
                                + basis.getObergrenze()  + " TestZeit: " 
                                + testWerte.getScore() 
                                + " Bei einer Tolleranz um die mittlere "
                                + "Laufzeit von: " 
                                + basis.getTolleranz() 
                                + " (0.0 bedeutet, dass die Grenzen durch"
                                + "min und max Werte aus X Messungen sind) \n";
                        if (basis instanceof ITestObjektGruppe) {
                            result += TestVergleichArten.vergleicheTests(testWerte, (ITestObjektGruppe)basis 
                                    , erwarteteRegression);
                        }
                    }
                } else if (regressionGefunden == Status.KLEINER) {
                    /*
                     * Warnung, dass die Tests schneller als erwartet liefen und
                     * es probleme geben koennte.
                     */
                    result += "Die Tests wurden schneller abgeschlossen"
                            + " als erwarte, dies koennte auf fehlerhaftes "
                            + "Verhalten beim Testen deuten.";
                } else {
                    regressionGefunden = Status.IM_BEREICH;
                    result += "Es scheint keine Regression vorzuliegen. " + regressionGefunden.toString() ;
                }
        return result;
    }
}