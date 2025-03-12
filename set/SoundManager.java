package set;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundManager {

    public static void playSound(String fileName) {
        try {
            InputStream is = SoundManager.class.getResourceAsStream("/sound/" + fileName);
            if (is == null) {
                System.err.println("Sound file not found: /sound/" + fileName);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
