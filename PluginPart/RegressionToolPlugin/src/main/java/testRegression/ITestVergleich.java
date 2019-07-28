package testRegression;

import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;

public interface ITestVergleich {
    
   public RegressionTestResult vergleicheBasen (IBasis basisNeu, IBasis basisAlt
           , double erwarteteRegression, double alpha, String pfad);
    
   public RegressionTestResult vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
           ,double erwarteteRegressiona, String pfad, double auslastungTolleranz);
}
