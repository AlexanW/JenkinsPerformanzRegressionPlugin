package io.jenkins.plugins;

import java.io.IOException;

import org.junit.Test;

import leseDaten.LeseCPUundRAM;
import leseDaten.LeseJUnitResults;
import testDatenTypen.ITest;
import testDatenTypen.ITestWerte;

public class TestFuerTestWerte {
    @Test
    public void addAuslastung() {
        ITestWerte werte = LeseJUnitResults.leseTestsXML("src/main/resources/TestDaten/TestAuslastung/junitResult.xml", 100);
        System.out.println(werte);
        for (ITest t : werte.getTests().values() ) {
            System.out.println(t);
        }
        try {
            werte.setTestAuslastungen(LeseCPUundRAM.readAuslastungen("src/main/resources/TestDaten/TestAuslastung", 10.0));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(werte);
        for (ITest t : werte.getTests().values() ) {
            System.out.println(t);
        }
    }
}
