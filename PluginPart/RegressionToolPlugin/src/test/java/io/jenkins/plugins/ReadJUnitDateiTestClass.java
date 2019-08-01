package io.jenkins.plugins;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import leseDaten.LeseJUnitResults;
import testDatenTypen.ITest;
import testDatenTypen.ITestWerte;

public class ReadJUnitDateiTestClass {
    @Test
    public void testeEinlesenAlle() {
        try {
            List<ITestWerte> werte = 
                    LeseJUnitResults.getJUnitResultDateiAusBuilds
                    ("src/main/resources/TestDaten", 1, 100, "junitResult.xml");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testeEinzelnEinlesen() {
        ITestWerte werte = LeseJUnitResults.leseTestsXML("src/main/resources/TestDaten/TesteEinzelneResultDatei/junitResult.xml", 100);
        System.out.println(werte.getName() + " Werte: " + werte.getScore() 
            + " TimeStamp: " + werte.getTimeStapm());
        int countTests = 0;
        for (ITest t : werte.getTests().values() ) {
            System.out.println("Test: " + t.getName());
            countTests++;
        }
        System.out.println("Es gibt Tests: " + countTests);
    }
}
