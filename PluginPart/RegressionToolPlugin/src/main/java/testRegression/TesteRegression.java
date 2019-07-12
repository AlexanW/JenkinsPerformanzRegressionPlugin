package testRegression;

import java.util.List;

import leseDaten.LeseJUnitResults;
import testDatenTypen.IBasis;
import testDatenTypen.ITestWerte;

/**
 * Diese Klasse enthaelt Methoden um mithilfe von Basen und 
 * TestWerten Performanz Regression aufzudecken.
 * Diese Klasse bezieht sich dabei auf die Klassen Basis und TestWerte.
 * @author Alexander Weber
 *
 */
public class TesteRegression {
    
    public static void main(String[] args) {
        /*
         * Diese Methode sollte allein getestet werden, da sie beendet sein 
         * sollte bevor die andern Methoden beginnen.
         */
        //SysAuslastungMessung.startMeasurement();
//        IErstelleBasis erstelleBasis = new ErstelleBasis();
//        IBasis basis = erstelleBasis
//                .erstelleBasis("Data/jUnitResults/", "Data/Basen/", 0.2, 5, 100);
//        List<ITestWerte> werte = LeseJUnitResults
//                .getJUnitResultDatei("Data/jUnitResultVergleich/", 100);
//        TestVergleichen testverleich = new TestVergleichen();
//        String result = testverleich.vergleicheBasisMitWerten(werte.get(0), basis, 0.0); 
//        System.out.println(testverleich.vergleicheBasen(basis, basis, 0.0, 0.05) + result);
    }
}