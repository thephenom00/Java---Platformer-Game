package audio;

import gamestates.Gamestate;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioController {
    private Clip clip;
    private boolean isClipPlaying;

    public Clip getClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File(path);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }

    public void playClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        getClip(path);
        stopClip();
        clip = getClip(path);
        clip.setFramePosition(0);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        isClipPlaying = true;
    }

    private void stopClip() {
        if (isClipPlaying && clip != null) {
            clip.stop();
            clip.close();
            isClipPlaying = false;
        }
    }
}


