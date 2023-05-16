package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import utilz.LoadSave;
import utilz.LoggerManager;
import static utilz.Size.*;

public class Playing extends State implements StateInterface {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;

    private final BufferedImage background;
    private LoggerManager logger;
    private static int[][] levelData;


    public Playing(Game game, LoggerManager logger) {
        super(game);
        this.logger = logger;
        initializeClasses();
        background = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND);
    }

    private void initializeClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (78 * SCALE), (int) (58 * SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        objectManager = new ObjectManager(player);

    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
        enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
        objectManager.update();

        checkWin();
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        levelManager.draw(g);
        player.draw(g);
        enemyManager.draw(g);
        objectManager.draw(g);
    }

    public void checkWin () {
        if (enemyManager.numberOfPigsAlive == 0 && objectManager.numberOfDiamondsToTake == 0) {
            Gamestate.state = Gamestate.WIN;
        }
    }

    public void checkHit(Rectangle2D.Float jumpBox) {
        enemyManager.checkHit(jumpBox);
    }

    public void checkDiamondCollected(Rectangle2D.Float hitbox) {
        objectManager.checkDiamondCollected(hitbox);
    }

    public void checkHeartCollected (Rectangle2D.Float hitbox) {
        objectManager.checkHeartCollected(hitbox);
    }

    public void resetGame() {
        player.resetPlayer();
        enemyManager.resetEnemyManager();
        objectManager.resetObjects();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.setLeft(true);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_ESCAPE -> {
                Gamestate.state = Gamestate.MENU;
                logger.log("Switched to MENU state");
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
            case KeyEvent.VK_SPACE -> player.setJump(false);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
}