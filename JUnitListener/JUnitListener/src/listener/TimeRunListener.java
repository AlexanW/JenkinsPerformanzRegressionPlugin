package listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class TimeRunListener extends RunListener {

    private FileOutputStream stream;
    
    private String tempText;
    
    public TimeRunListener(String pfad) {
        try {
            stream = new FileOutputStream(new File(pfad), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
     * Schreibt am Start eines Tests die Variable mit Name;StartZeit;EndZeit
     */
    @Override
    public void testStarted(Description description) throws Exception {
        tempText = description.getClassName() + ";" + System.currentTimeMillis();
    } 
    /*
     *Ergaenzt die Datei um die Endzeit und schreibt diesen Teil an das Ende der Mail.
     */
    @Override
     public void testFinished(Description description) throws Exception {
        tempText += ";" + System.currentTimeMillis();
        stream.write(tempText.getBytes("UTF-8"));
    }
}
