package testRegression;

import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;

public interface ITestVergleich {
    
   public RegressionTestResult vergleicheBasen (IBasis basisNeu, IBasis basisAlt
           , double erwarteteRegression, double alph);
    
   public RegressionTestResult vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
           ,double erwarteteRegressiona);
}
