package testRegression;

import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;
/**
 * Diese Klasse wird zum vergleichen von Basen und TestWerten verwendet, 
 * dieses Geruest muss mit Testverfahren befuellt werden.
 * @author Alexander
 *
 */
public interface ITestVergleich {
    
   public RegressionTestResult vergleicheBasen (IBasis basisNeu, IBasis basisAlt
           ,double alpha, String pfad);
    
   public RegressionTestResult vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
           ,double erwarteteRegressiona, String pfad, double auslastungTolleranz);
}
