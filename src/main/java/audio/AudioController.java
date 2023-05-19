package audio;
import gamestates.Gamestate;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioController {
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/java/audio/megalovania.wav"));

    public AudioController() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
