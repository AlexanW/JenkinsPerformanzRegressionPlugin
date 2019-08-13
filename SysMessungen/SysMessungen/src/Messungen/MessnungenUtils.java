package Messungen;

import java.io.File;

public class MessnungenUtils {
    
    public static void loescheAuslastugen(String pfadZuZielOrdner, String dateiName) {
        boolean alleGeloescht = true;
        File file = new File(pfadZuZielOrdner);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().contains(dateiName)) {
                    alleGeloescht = f.delete();
                }
            }
            if (!alleGeloescht) {
                System.out.println("Es konnten nicht alle Auslastungsdateien"
                        + "geloescht werden.");
            }
        }
    }

}
