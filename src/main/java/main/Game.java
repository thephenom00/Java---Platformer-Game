package main;


import java.awt.Graphics;

import gameStates.Gamestate;
import gameStates.Menu;
import gameStates.Playing;
import utilz.LoggerManager;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;
    private final int UPS = 200;

    private LoggerManager logger;
    private Playing playing;
    private Menu menu;


    public Game(LoggerManager logger) {
        this.logger = logger;
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }


    private void initClasses() {
        menu = new Menu(this, logger);
        playing = new Playing(this, logger);
    }

    // Starts the thread
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            default:
                break;

        }
    }


    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }


    // FPS takes care of rendering (player, enemies)
    // UPS takes care of movement
    @Override
    public void run() {

        // Divide second into 120 parts
        double timePerFrame = 1000000000.0 / FPS;
        double timePerUpdate = 1000000000.0 / UPS;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            long timeAfterOneLoop = currentTime - previousTime;

            // deltaF is incremented by time which passed after one loop
            deltaF += timeAfterOneLoop / timePerFrame;
            deltaU += timeAfterOneLoop / timePerUpdate;
            previousTime = currentTime;

            // has to be incremented till time = timePerFrame
            // if the time is equal (or more) to timePerFrame, we repaint
            if (deltaF >= 1) {
                gamePanel.repaint(); // Going to gamePanel and back here to render()
                frames++;
                deltaF--;
            }

            if (deltaU >= 1) {
                update(); // Updates players movement, enemy movement etc.
                updates++;
                deltaU--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                logger.log("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }

    }

    // If we leave the window, movement stops
    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

}