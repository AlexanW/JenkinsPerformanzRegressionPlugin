package Messungen;

import java.io.File;

public class MessnungenUtils {
    
    public static void loescheAuslastugen(String pfadZuZielOrdner, String dateiName) {
        boolean alleGeloescht = true;
        File file = new File(pfadZuZielOrdner);
        if (file.exists()) {
            File[] files = file.listFiles();
            System.out.println("Es gibt " + files.length + " Dateien.");
            for (File f : files) {
                System.out.println(f.getName() + " DATEINAME " + dateiName);
            }
            for (File f : files) {
                if (f.getName().contains(dateiName)) {
                    System.out.println("Soll Loeschen " + f.exists());
                    alleGeloescht = f.delete();
                    System.out.println("Hat geloescht " + alleGeloescht + " NAME" +f.getName());
                }
            }
            if (!alleGeloescht) {
                System.out.println("Es konnten nicht alle Auslastungsdateien"
                        + "geloescht werden.");
            }
        }
    }

}
