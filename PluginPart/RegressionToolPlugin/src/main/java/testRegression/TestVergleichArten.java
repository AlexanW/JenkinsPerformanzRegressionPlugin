package testRegression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import leseDaten.LeseSchreibeTestWerte;
import testDatenTypen.BasisMitTests;
import testDatenTypen.IBasis;
import testDatenTypen.ITest;
import testDatenTypen.ITestObjektGruppe;
import testDatenTypen.ITestWerte;
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
        for(ITest t : testWerte.getTests().values()) {
            if (!basis.getTests().containsKey(t.getName())) {
                testNurInWerten.add(t);
            }
        }
        result.append("Regressierte Tests bei " + erwarteteRegression + " Tolleranz : \n");
        for (ITest t : regressierteTests) {
            result.append(t.getName() + " mit: " + t.getScore() +"\n");
        }
        result.append("Tests die nur in der neuen Datei sind: \n");
        for (ITest t : testNurInWerten) {
            result.append(t.getName() + " mit: " + t.getScore() +  "\n");
        }
        result.append( "Tests die nur in der Basis sind: ");
        for (ITest t : testNurInBasis) {
            result.append(t.getName() + " mit: " + t.getScore() +  "\n");
        }
        LeseSchreibeTestWerte.schreibeErgebnisse(pfad, result.toString());
    }
    /**
     * 
     */
    public static void vergleicheTestsAuslastungen(ITestObjektGruppe basis,
            ITestObjektGruppe testObjektGruppe, double auslastungTolleranz, 
            String pfad) {
        Status status = Status.NEUTRAL;
        List<ITest> zuhoheAuslatungCPU = new ArrayList<ITest>();
        List<ITest> zuhoheAuslatungRAM = new ArrayList<ITest>();
        
        for (ITest t: testObjektGruppe.getTests().values()) {
            if (basis.getTests().get(t.getName()) != null) {
                if (t.getAvarageCPU() > (basis.getTests().get(t.getName()).getAvarageCPU() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungCPU.add(t);
                } else if (t.getMaxCPU() > (basis.getTests().get(t.getName()).getMaxCPU() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungCPU.add(t);
                }
                if (t.getAvarageRAM() > (basis.getTests().get(t.getName()).getAvarageRAM() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungRAM.add(t);
                } else if (t.getMaxRAM() > (basis.getTests().get(t.getName()).getMaxRAM() * (1 + auslastungTolleranz))) {
                    zuhoheAuslatungRAM.add(t);
                }
            }
        }
        StringBuffer result = new StringBuffer();
        result.append( "Tests die im Vergleich zu ihrem Durchschnittswert in der Basis"
                + " zu hohe CPU Auslastung zeigen (mehr als " 
                + (auslastungTolleranz *100)+ "% hoeher im Druchschnitt oder im"
                + " Maximalwert):\n  "+  status.toString() +"  \n");
        for (ITest t: zuhoheAuslatungCPU) {
            result.append(t.getName());
        }
        result.append("Tests die im Vergleich zu ihrem Durchschnittswert in der Basis"
                + " zu hohe RAM Auslastung zeigen (mehr als " 
                + (auslastungTolleranz *100) + "% hoeher im Druchschnitt"
                + " oder im Maximalwert):\n  "+  status.toString() +"  \n");
        for (ITest t: zuhoheAuslatungRAM) {
            result.append(t.getName());
        }
        LeseSchreibeTestWerte.schreibeErgebnisse(pfad, result.toString());
    }
    
    /**
     * Eine Methode die zwei Basen miteinander vergleicht. Dazu wird ein t-Test
     * verwendet. Dieser vergleicht 2 Sets miteinander und sagt mit 
     * @param alteBasis
     * @param neueBasis
     * @return
     */
    public static Status vergleicheBasen(BasisMitTests alteBasis, BasisMitTests neueBasis, double erwarteteRegression, double alpha) {
        Status status = Status.NEUTRAL;
        TTest test = new TTest();
        /*
         * Sollte die Ho Hypothese, dass die Means gleich sind abgelehnt werden dann gilt, 
         * dass es keien statistischen Hinweis darauf gibt, dass die Means gleich sind.
         * Dabei gibt es eine Fehlkategoriesierung von aphla%, hier 5%.
         * OneSided alpha*2; -> hier Mean 1 kleiner als mean2 -> Regression.
         */
        boolean h0Rejectet = test.tTest(ITestsZuArry(alteBasis.getTests().values(), erwarteteRegression),
                ITestsZuArry(neueBasis.getTests().values()), (2*alpha));
        if (!h0Rejectet) {
            status = Status.GROESSER;
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
    public static Status vergleicheBasen(IBasis alteBasis, IBasis neueBasis, double erwarteteRegression, double alpha) {
        Status status = Status.NEUTRAL;
        double sp = Math.sqrt((((alteBasis.getAnzahlTests()-1) * Math.pow(alteBasis.getVarianz(), 2))
                + ((neueBasis.getAnzahlTests()-1) * Math.pow(neueBasis.getVarianz(), 2)))
                /(alteBasis.getAnzahlTests() + neueBasis.getAnzahlTests() - 2));
        double t0 = ((alteBasis.getScore() - neueBasis.getScore())
                /(sp 
                * Math.sqrt(((1/alteBasis.getAnzahlTests()) + (1/neueBasis.getAnzahlTests())))));
        TDistribution dist = new TDistribution(alteBasis.getAnzahlTests()+ neueBasis.getAnzahlTests()-2, alpha);
        double pvalue = dist.getSupportUpperBound();
        
        boolean h0Rejectet = t0 > pvalue;
        if (!h0Rejectet) {
            status = Status.GROESSER;
        }
        return status;
    }
    /**
     * 
     */
    private static double[] ITestsZuArry(Collection<ITest> tests) {
        double[] doubleArray = new double[tests.size()];
        int i = 0;
        for (ITest t: tests) {
            doubleArray[i] = t.getScore();
            i++;
        }
        return doubleArray;
    }
    /**
     * 
     */
    private static double[] ITestsZuArry(Collection<ITest> tests, double erwarteteRegression) {
        double[] doubleArray = new double[tests.size()];
        int i = 0;
        for (ITest t: tests) {
            doubleArray[i] = t.getScore() * (1 + erwarteteRegression);
            i++;
        }
        return doubleArray;
    }
}
