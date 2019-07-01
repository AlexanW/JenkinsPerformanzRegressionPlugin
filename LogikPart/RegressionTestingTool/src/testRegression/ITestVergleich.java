package testRegression;

import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.Status;

public interface ITestVergleich {
    
   public String vergleicheBasen (IBasis basisNeu, IBasis basisAlt
           , double erwarteteRegression, double alph);
    
   public String vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
           ,double erwarteteRegressiona);
}
