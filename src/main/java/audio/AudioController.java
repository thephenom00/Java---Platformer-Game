package audio;

import gamestates.Gamestate;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioController {
    private Clip clip;
    private boolean isClipPlaying;

    /**
     * Gets audio from specific file path
     * @param path
     * @return
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public Clip getClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File(path);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }

    /**
     * Plays the clip using method above
     * If the clip is already playing, it stops it
     * Plays the clip on the loop
     * @param path
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public void playClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        getClip(path);
        stopClip();
        clip = getClip(path);
        clip.setFramePosition(0);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        isClipPlaying = true;
    }

    /**
     * Stops and resets the clip if any is playing
     */
    private void stopClip() {
        if (isClipPlaying && clip != null) {
            clip.stop();
            clip.close();
            isClipPlaying = false;
        }
    }
}


