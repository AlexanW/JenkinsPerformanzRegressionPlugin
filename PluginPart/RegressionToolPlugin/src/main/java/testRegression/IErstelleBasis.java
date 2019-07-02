package testRegression;

import testDatenTypen.IBasis;

/**
 * Dises Interface definiert welche Methoden eine Klasse benoetigt, die eine 
 * Art der IBasis erstellen moechte.
 * @author Alexander Weber
 *
 */
public interface IErstelleBasis {
    /**
     * Jede Basis sollte eine Quelle haben, in der die Dateien zum erstellen
     * liegen. Dann sollte festgelegt werden wo die Basis hingeschrieben werden
     * soll. Es soll moeglich sein einen Tolleranzwert fuer die Basis anzugeben.
     * @param targetJUnitResutls Der Ordner in dem Alle ResultDateien liegen,
     * die zum erstellen benoetigt werden.
     * @param targetBasis Der Ort, an den die erstellte Basis geschrieben wird.
     * @param tolleranz
     */
    public IBasis erstelleBasisOhneMessungen(String targetJUnitResutls, String targetBasis
            , double tolleranz, int anzahlTests, double step_size);
}
