package testRegression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.distribution.TDistribution;

import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.IBasis;
import testDatenTypen.ITest;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.ITestWerte;
import testDatenTypen.RegressionTestResult;
import testDatenTypen.Status;

public class TestVergleichArten {
  
    /**
     * Testet ob die durchschnittliche Laufzeit der TestSuit auserhalb der 
     * Grenzen der Basis liegt. Wenn dies der Fall ist wird von Performanz 
     * Regression oder Ausreissern ausgegangen.
     * @param testWerte Die TestWerte die mit dieser Basis verglichen werden
     * sollen.
     * @param erwarteteRegression prozentuale erwartete Regression. Wenn keine
     * Erwartet wird sollte sie 0.0 sein. Format: 0.xx.
     * @return Ob die durchschnittliche Laufzeit der TestSuit auserhalb der
     * Grenzen liegt.
     */
    public static Status testWerteAuserhalbDerGrenzen (ITestWerte testWerte
            , IBasis basis, double erwarteteRegression) {
        Status status = Status.NEUTRAL;
        /*
         * Wird 0 wenn keine Regression erwartet wird, hat daher dann keine
         * Auswirkung auf die Auswertung. Alles andere Rechnet die erwartete Regression
         * als prozentualen Anteil aus und rechnet diesen auf die Grenzen drauf.
         * So entsteht eine Verschiebung nach oben.
         */
        double passeGrenzenAnErwarteteRegAn = 
                basis.getScore() * erwarteteRegression;
        if (testWerte.getScore() > basis.getObergrenze() + 
                passeGrenzenAnErwarteteRegAn) {
            status = Status.GROESSER;
        } else if (testWerte.getScore() < basis.getUntergrenze() +
                passeGrenzenAnErwarteteRegAn) {
            status = Status.KLEINER;
        } else {
            status = Status.IM_BEREICH;
        }
        return  status;
    }
    /**
     * Diese Methode fuegt der Meldung ueber Regression die Namen der Tests
     * hinzu, die sich verschlaechtert haben. Als Grad fuer diese 
     * Verschlechterun gilt die erwartete Regression, ist diese 0.0 
     * so wird 0.2 festgelegt.
     * @param testWerte
     * @param basis
     * @param erwarteteRegression
     */
    public static void vergleicheTests(ITestObjektGruppe testWerte, ITestObjektGruppe basis 
            ,double erwarteteRegression, String pfad) {
        StringBuffer result = new StringBuffer();
        //Liste mit allen Tests in dem ersten und zweiten Objekt die sich verschlechtert haben.
        List<ITest> regressierteTests = new ArrayList<ITest>();
        List<ITest> testNurInWerten = new ArrayList<ITest>();
        List<ITest> testNurInBasis = new ArrayList<ITest>();
        System.out.println("Erwartete Regressoion fuer vergleich " + erwarteteRegression);
        for (ITest t :basis.getTests().values()) {
            if (testWerte.getTests().containsKey(t.getName())) {
                //Trotz erwarteteer Regression sollte hier eine weitere tolleranz eingebaut werden.
                if ((t.getScore() * (1 + erwarteteRegression)) <
                        testWerte.getTests().get(t.getName()).getScore()) {
                    regressierteTests.add(testWerte.getTests().get(t.getName()));
                }
            } else {
                testNurInBasis.add(t);
            }
        }
        //Erstellt die Auswertungen.
        for(ITest t : testWerte.getTests().values()) {
            if (!basis.getTests().containsKey(t.getName())) {
                testNurInWerten.add(t);
            }
        }
        result.append("Regressierte Tests bei " + (erwarteteRegression * 100) + "% Tolleranz : \n");
        for (ITest t : regressierteTests) {
            result.append(t.getName() + " Score: " + t.getScore());
            if (basis.getTests().get(t.getName()) != null) {
                result.append(" Bais Test Score: " + basis.getTests().get(t.getName()).getScore() + "\n");
            } else {
                result.append("/n");
            }
        }
        if (testNurInWerten.size() > 0) {
            result.append("Tests die nur in der neuen Datei sind: \n");
            for (ITest t : testNurInWerten) {
                result.append(t.getName() + " mit: " + t.getScore() +  "\n");
            }
        }
        if (testNurInBasis.size() > 0) {
            result.append( "Tests die nur in der Basis sind: ");
            for (ITest t : testNurInBasis) {
                result.append(t.getName() + " mit: " + t.getScore() +  "\n");
            }
        }
        LeseSchreibeTestWerte.schreibeErgebnisse(pfad + "/auswertung.txt", result.toString(), true);
    }
    /**
     * Vergleicht die Auslastungen von Tests in zwei Testobjektgruppen.
     */
    public static Status vergleicheTestsAuslastungen(ITestObjektGruppe alteGruppe,
            ITestObjektGruppe neueGruppe, double auslastungTolleranz, 
            String pfad) {
        List<ITest> zuhoheAuslatungCPU = new ArrayList<ITest>();
        List<ITest> zuhoheAuslatungRAM = new ArrayList<ITest>();
        /*
         * Dieser Bereich wird nur ausgefuehrt wenn die Messungen ausserhalb des Rahmens liegen.
         */
        Status status = Status.GROESSER;
        for (ITest t: neueGruppe.getTests().values()) {
            if (alteGruppe.getTests().get(t.getName()) != null) {
                if (t.getAvarageCPU() > (alteGruppe.getTests().get(t.getName()).getAvarageCPU() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungCPU.add(t);
                } else if (t.getMaxCPU() > (alteGruppe.getTests().get(t.getName()).getMaxCPU() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungCPU.add(t);
                }
                if (t.getAvarageRAM() > (alteGruppe.getTests().get(t.getName()).getAvarageRAM() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungRAM.add(t);
                } else if (t.getMaxRAM() > (alteGruppe.getTests().get(t.getName()).getMaxRAM() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungRAM.add(t);
                }
            }
        }
        StringBuffer result = new StringBuffer();
        result.append( "Tests die im Vergleich zu ihrem Durchschnittswert in der Basis"
                + " zu hohe CPU Auslastung zeigen (mehr als " 
                + (auslastungTolleranz *100)+ "% hoeher im Druchschnitt oder im"
                + " Maximalwert):\n  ");
        for (ITest t: zuhoheAuslatungCPU) {
            result.append(t.getName() + " CPU Mean: " + t.getAvarageCPU() 
                + " CPU Max: " + t.getMaxCPU() + "\n");
        }
        result.append("Tests die im Vergleich zu ihrem Durchschnittswert in der Basis"
                + " zu hohe RAM Auslastung zeigen (mehr als " 
                + (auslastungTolleranz *100) + "% hoeher im Druchschnitt"
                + " oder im Maximalwert):\n");
        for (ITest t: zuhoheAuslatungRAM) {
            result.append(t.getName() + " RAM Mean: " + t.getAvarageRAM() 
            + " RAM Max: " + t.getMaxRAM() + "\n");
        }
        LeseSchreibeTestWerte.schreibeErgebnisse(pfad + "/auswertung.txt", result.toString(), true);
        if (zuhoheAuslatungCPU.size() > 0 || zuhoheAuslatungRAM.size() > 0) {
            status = Status.AUSREISSER;
        }
        return status;
    }
    /**
     * Eine Methode die zwei Basen miteinander vergleicht. Dazu wird ein t-Test
     * verwendet. Dieser vergleicht 2 Sets miteinander und sagt mit aus ob der 
     * Durchschnitt der alten Basis kleiner ist als der der Neuen.
     * Hier mussten die einzelnen Komponenten berechenet werden, da diese 
     * Basen kein Zugriff auf die Menge an Tests haben aus denen sie bestehen.
     * @param alteBasis
     * @param neueBasis
     * @return
     */
    public static RegressionTestResult vergleicheBasen(IBasis alteBasis, IBasis neueBasis, double alpha) {
        RegressionTestResult result = new RegressionTestResult();
        double spUpperPartOld = (alteBasis.getAnzahlTests()-1) * Math.pow(alteBasis.getVarianz(), 2);
        double spUpperPartNew = (neueBasis.getAnzahlTests()-1) * Math.pow(neueBasis.getVarianz(), 2);
        double spUpperPart = spUpperPartNew + spUpperPartOld;
        double spLowerPart = alteBasis.getAnzahlTests() + neueBasis.getAnzahlTests() - 2;
        double sp = Math.sqrt(spUpperPart / spLowerPart);
        double difBasenAvarage = neueBasis.getScore() - alteBasis.getScore();
        double tOLowerSqrt = Math.sqrt((1.0/alteBasis.getAnzahlTests()) + (1.0 /neueBasis.getAnzahlTests()));
        double tOLowerHalf = sp * tOLowerSqrt;
        double t0 = difBasenAvarage / tOLowerHalf;
        TDistribution dist = new TDistribution(alteBasis.getAnzahlTests() + neueBasis.getAnzahlTests() - 2);
        double tDistWert = dist.inverseCumulativeProbability(1 - 0.05);
        
        System.out.println("t0: " + t0 + " pVALUE:" + tDistWert);
        boolean h0Rejectet = t0 > tDistWert;
        result.addTextZuNachricht("t Statistik:" + t0 + " p-Wert:"
                + tDistWert + " Freiheitsgrade: " 
                + (neueBasis.getAnzahlTests() 
                        + alteBasis.getAnzahlTests() - 2) + "\n");
        
        if (h0Rejectet) {
            result.setResutlDerTests(Status.GROESSER);
        }
        return result;
    }
}
