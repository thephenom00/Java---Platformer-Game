package main;

import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import audio.AudioController;
import gamestates.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Game implements Runnable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;
    private final int UPS = 200;

    private Playing playing;
    private Menu menu;
    private GameOver gameOver;
    private YouWin youwin;
    private AudioController audioController;

    private boolean loggerEnabled;

    /**
     * Takes care of the rendering and updating the whole game using threads
     * @param loggerEnabled boolean, controls if the log information should be displayed
     */
    public Game(boolean loggerEnabled) {
        this.loggerEnabled = loggerEnabled;
        initializeClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        playMusic();

        startGameLoop();
    }

    /**
     * music is played
     */
    private void playMusic() {
        try {
            audioController.playClip("src/main/java/audio/megalovania.wav");
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }


    private void initializeClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
        gameOver = new GameOver(this, playing); // RESTART
        youwin = new YouWin(this, playing); // RESTART
        audioController = new AudioController();
    }

    /**
     * starts the game loop thread.
     */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * takes care of movement of entities
     */
    public void update() {
        switch (Gamestate.state) {
            case PLAYING -> {
                playing.update();
            }
        }
    }

    /**
     * takes care of drawing entities and objects on the scene
     * @param g
     */
    public void draw(Graphics g) {
        switch (Gamestate.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case GAMEOVER -> gameOver.draw(g);
            case WIN -> youwin.draw(g);
            default -> {}
        }
    }

    /**
     * Frames take care of drawing the player, enemy
     * Updates take care of movement of player, enemy
     */
    @Override
    public void run() {
        double timePerDraw = 1000000000.0 / FPS;
        double timePerUpdate = 1000000000.0 / UPS;

        long previousTime = System.nanoTime();

        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            long timeAfterOneLoop = currentTime - previousTime;

            // deltaF is incremented by time which passed after one loop
            deltaF += timeAfterOneLoop / timePerDraw;
            deltaU += timeAfterOneLoop / timePerUpdate;
            previousTime = currentTime;

            // has to be incremented till time = timePerDraw
            // if the time is equal (or more) to timePerDraw, we repaint
            if (deltaF >= 1) {
                gamePanel.repaint(); // Going to gamePanel and back here to render()
                deltaF--;
            }

            if (deltaU >= 1) {
                update(); // Updates players movement, enemy movement etc.
                deltaU--;
            }

        }
    }

    /**
     * In case of player will switch to different window, the movement will reset
     */
    public void windowFocusLost() {
        if(loggerEnabled){
            logger.log(Level.INFO, "You left the game");
        }
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetMovement();
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public YouWin getYouWin() {
        return youwin;
    }
    public boolean getLoggerState() {
        return loggerEnabled;
    }

    public AudioController getAudioController() {
        return audioController;
    }

}