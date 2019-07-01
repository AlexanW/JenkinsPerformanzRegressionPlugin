package testRegression;

import testDatenTypen.Basis;
import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.Status;
import testDatenTypen.TestWerte;

public class TestVergleichen implements ITestVergleich{
    /**
     * 
     */
    @Override
    public String vergleicheBasen(IBasis basisNeu, IBasis basisAlt
            , double erwarteteRegression, double alpha) {
        Status status = Status.NEUTRAL;
        String result = "";
        TestVergleichArten.vergleicheBasen(basisAlt, basisNeu, erwarteteRegression, alpha);
        
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
                    //ToDo hier Check auf Auslastungen. Problem : How to measure.
                    result = "Die Tests fallen aus den Grenzen der Basis."
                            + "Daher wird angenommen, dass es sich um Regression "
                            + "handelt.\nUntergrenze: " 
                            + ((Basis)basis).getUntergrenze() + " Obergrenze: "
                            + ((Basis)basis).getObergrenze()  + " TestZeit: " 
                            + ((TestWerte)testWerte).getScore() 
                            + " Bei einer Tolleranz um die mittlere "
                            + "Laufzeit von: " 
                            + ((Basis)basis).getTolleranz() 
                            + " (0.0 bedeutet, dass die Grenzen durch"
                            + "min und max Werte aus X Messungen sind)";
                    TestVergleichArten.vergleicheTests((TestWerte)testWerte, (Basis)basis 
                            , erwarteteRegression);
                } else if (regressionGefunden == Status.KLEINER) {
                    /*
                     * Warnung, dass die Tests schneller als erwartet liefen und
                     * es probleme geben könnte.
                     */
                    result = "Die Tests wurden schneller abgeschlossen"
                            + " als erwarte, dies koennte auf fehlerhaftes "
                            + "Verhalten beim Testen deuten.";
                } else {
                    regressionGefunden = Status.IM_BEREICH;
                    result = "Es scheint keine Regression vorzuliegen.";
                }
        return result;
    }
}