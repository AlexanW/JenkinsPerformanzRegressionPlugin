package testRegression;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import leseDaten.LeseJUnitResults;
import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.Test;
import testDatenTypen.Basis;
import testDatenTypen.IBasis;
import testDatenTypen.ITest;
import testDatenTypen.ITestWerte;

public class ErstelleBasis implements IErstelleBasis {
    /**
     * Diese Methode erstellt eine Basis, auf Basis aller jUnitDateien in dem 
     * targetJUnitReult Ordner.
     * @param targetJUnitResutls der Zielordner, in dem die jUnitResult Dateien
     * fuer dei Erstellung liegen.
     * @param targetBasis Der Zielordner, in den die Basis geschrieben werden.
     * Dabei ist der Name nicht festzulegen.
     * @param tolleranz Eine Tolleranzgrenze. Sie ist prozentual von dem 
     * Mittelwert aus. Wenn statt einer 
     * Tolleranzgrenze die min. bzw max Werte aller verwendeten 
     * jUnitResultDateien nemen soll so sollte sie 0.0 sein.
     * Eingabe von Prozent in 0.xx Fomat.
     */
    public IBasis erstelleBasis(String targetJUnitResutls,String targetBasis 
            ,double tolleranz, int anzahlTests, double step_size
            , String jUnitDateiName,double minTolleranzFuerGrenzen , PrintStream logger) {
        
        
        List<ITestWerte> werte = new ArrayList<ITestWerte>();
        try {
            werte = LeseJUnitResults.getJUnitResultDateiAusBuilds(targetJUnitResutls,
                    anzahlTests, step_size, jUnitDateiName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IBasis basis = null;
        double avarageLaufzeit = getAvarageLaufzeit(werte);
        if (!enthaeltFehlschlag(werte) && werte.size() >= 1) {
            basis = new Basis(werte.get(0).getName(),avarageLaufzeit
                    , getMinLaufzeit(werte, tolleranz, avarageLaufzeit, minTolleranzFuerGrenzen )
                    , getMaxLaufzeit(werte, tolleranz, avarageLaufzeit, minTolleranzFuerGrenzen)
                    ,berecheneVarianz(werte, avarageLaufzeit)
                    ,getDurchschnitTests(werte), werte.size());
            //Schreibe die generierte Basis in den Ordner fuer die Basen.
            LeseSchreibeTestWerte.bestimmeNameUndSchreibeBasis(basis, targetBasis);
        } else {
            if (enthaeltFehlschlag(werte)) {
                logger.print("Unter den jUnit Dateien befindet sich mindestens "
                        + "eine mit einem Fehlgeschlagenen Test. Es wurde daher"
                        + " keine Basis erstellt.");
            }
        }
        return basis;
    }
      
    private boolean enthaeltFehlschlag (List<ITestWerte> werte) {
        boolean enthaeltFehlschlag = false;
        for (ITestWerte w : werte ) {
            if (w.getEnthaeltFehlschag()) {
                enthaeltFehlschlag = true;
            }
        }
       return enthaeltFehlschlag;
    }
    
    private double getAvarageLaufzeit(List<ITestWerte> werte) {
        double sum = 0;
        for (ITestWerte t : werte) {
            System.out.println("Erstelle Score: "+ t.getScore());
            sum += t.getScore();
        }
        System.out.println("Durchschintt: " + sum/werte.size());
        return sum / werte.size();
    }
    
    private double getMinLaufzeit(List<ITestWerte> werte, double tolleranz
            , double avarage,  double minTolleranzGrenze) {
        //Init des Minimusm mit der minimalen Grenze nach unten 
        double min = avarage * (1 - minTolleranzGrenze);
        for (ITestWerte t : werte) {
            if (tolleranz == 0) {
                if (min > t.getScore()) {
                    min = t.getScore();
                }
                /*
                 * Sollte dies der Fall sein und das Maximum kleier als die 
                 * maximale Tolleranz so wird verlichen ob der Score groesser
                 * ist als die maximale Tolleranz.
                 */
            } else if (min > avarage * (1 - tolleranz)) {
                if (t.getScore() > avarage * (1 - tolleranz)) {
                    if (min > t.getScore()) {
                        min = t.getScore();
                    }
                    //So wird max auf die maximale Tolleranz gesetzt.
                } else {
                    min = avarage * (1 - tolleranz);
                }
            }
        }
        return min;
    }
    
    private double getMaxLaufzeit(List<ITestWerte> values, double tolleranz
            , double avarage, double minTolleranzGrenze) {
        //Setzt das Maximum auf den minimal erwarteten Wert.
        double max = avarage * (1 + minTolleranzGrenze);
        for (ITestWerte t : values) {
            //Unterscheided ob es eine maximale Tolleranz gibt.
            if (tolleranz == 0) {
                if (max < t.getScore()) {
                    max = t.getScore();
                }
                /*
                 * Sollte dies der Fall sein und das Maximum kleier als die 
                 * maximale Tolleranz so wird verlichen ob der Score groesser
                 * ist als die maximale Tolleranz.
                 */
            } else if (max < avarage * (1 + tolleranz)) {
                if (t.getScore() < avarage * (1 + tolleranz)) {
                    if (max < t.getScore()) {
                        max = t.getScore();
                    }
                    //So wird max auf die maximale Tolleranz gesetzt.
                } else {
                    max = avarage * (1 + tolleranz);
                }
            }
        }
        return max;
    }
    
    private double berecheneVarianz(List<ITestWerte> werte
            , double avarage) {
        double temp = 0;
        for (ITestWerte t : werte) {
            temp += Math.pow((t.getScore() - avarage), 2);
        }
        temp = temp /werte.size(); 
        return temp;
    }
    /**
     * Berechnet den Durchschnitt fuer alle Tests der Basis. Auch fuer Tests
     * , die nicht in allen TestWerten enthalten sind.
     * @param werte
     * @return
     */
    private HashMap<String, ITest> getDurchschnitTests (List<ITestWerte> werte) {
        HashMap<String, ITest> druchschnitTests = new HashMap<String, ITest>();
        List<String> namen = getAlleTestNamenDerBasis(werte);
        for (String n : namen) {
            Test tempTest = new Test();
            tempTest.setName(n);
            double tempSum = 0.0;
            double tempCpuAvarageSum = 0.0;
            double tempCpuMinSum = 0.0;
            double tempCpuMaxSum = 0.0;
            double tempRamAvarageSum = 0.0;
            double tempRamMinSum = 0.0;
            double tempRamMaxSum = 0.0;
            int i = 0;
            int iAuslastungenCounter = 0;
            
            for (ITestWerte e : werte) {
               if (e.getTests().get(n) != null) {
                   tempSum += e.getTests().get(n).getScore();
                   /*
                    * Annahme, dass die AvarageRAM nur 0 ist wenn es keine
                    * Werte gibt.
                    */
                   if (e.getTests().get(n).getAvarageRAM() != 0) {
                       tempCpuAvarageSum += e.getTests().get(n).getAvarageCPU();
                       tempCpuMaxSum += e.getTests().get(n).getMaxCPU();
                       tempCpuMinSum += e.getTests().get(n).getMinCPU();
                       tempRamAvarageSum += e.getTests().get(n).getAvarageRAM();
                       tempRamMaxSum += e.getTests().get(n).getMaxRAM();
                       tempRamMinSum += e.getTests().get(n).getMinRAM();
                       iAuslastungenCounter++; 
                   }
                   i++;
               }
            }
            tempTest.setScore(tempSum/i);
            tempTest.setAvarageCPU(tempCpuAvarageSum/iAuslastungenCounter);
            tempTest.setMinCPU(tempCpuMinSum/iAuslastungenCounter);
            tempTest.setMaxCPU(tempCpuMaxSum/iAuslastungenCounter);
            tempTest.setAvarageRAM(tempRamAvarageSum/iAuslastungenCounter);
            tempTest.setMinRAM(tempRamMinSum/iAuslastungenCounter);
            tempTest.setMaxRAM(tempRamMaxSum/iAuslastungenCounter);
            druchschnitTests.put(tempTest.getName(), tempTest);
        }
        return druchschnitTests;
    }
    /**
     * Diese Methode sucht alle Namen von Tests die in einer Menge an
     * TestWerten enthalten sind, unabhaengig davon ob sie einmal oder
     * mehrmals vorkommen.
     * @param werte
     * @return
     */
    private List<String> getAlleTestNamenDerBasis (List<ITestWerte> werte) {
        List<String> namen = new ArrayList<String>();
        for (ITestWerte e : werte) {
            for (ITest t : e.getTests().values()) {
                if (!namen.contains(t.getName())) {
                    namen.add(t.getName());
                }
            }
        }
        return namen;
    }
}
