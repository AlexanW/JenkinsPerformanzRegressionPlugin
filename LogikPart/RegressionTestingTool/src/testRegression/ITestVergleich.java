package testRegression;

import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;
import testDatenTypen.Status;

public interface ITestVergleich {
    
   public Status vergleicheBasen (IBasis basisNeu, IBasis basisAlt
           , double erwarteteRegression);
    
   public Status vergleicheBasisMitWerten(ITestWerte testWerte, IBasis basis 
           ,double erwarteteRegression);
}
