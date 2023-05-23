package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.EnemyController;
import entities.Player;
import levels.LevelController;
import main.Game;
import objects.ObjectController;
import saveload.SaveLoadGame;
import utils.LoadSave;


import static utils.Size.*;

/**
 * The game itself, handles gameplay, object collection or interactions with enemies
 */
public class Playing extends State implements StateInterface {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private Player player;
    private LevelController levelController;
    private EnemyController enemyController;
    private ObjectController objectController;

    SaveLoadGame saveLoadGame = new SaveLoadGame(this);

    private final BufferedImage background;

    public Playing(Game game){
        super(game);
        initializeClasses();
        background = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND);
    }

    /**
     * Initializes all classes used in this class
     */
    private void initializeClasses() {
        levelController = new LevelController(game);
        enemyController = new EnemyController(this);
        player = new Player(33 * SCALE, 420 * SCALE, (int) (78 * SCALE), (int) (58 * SCALE), this);
        player.loadLvlData(levelController.getCurrentLevel().getLevelData());
        objectController = new ObjectController(player);
    }

    @Override
    public void update() {
        levelController.update();
        player.update();
        enemyController.update(levelController.getCurrentLevel().getLevelData(), player);
        objectController.update();

        checkWin();
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        levelController.draw(g);
        player.draw(g);
        enemyController.draw(g);
        objectController.draw(g);
    }

    /**
     * @param hitbox the hitbox of a player
     * @return returns True if the players hitbox touches eneme, false otherwise
     */
    public boolean checkPlayerTouchesEnemy(Rectangle2D.Float hitbox) {
        if (enemyController.touchPlayer(hitbox))
            return true;
        return false;
    }


    /**
     * Checks if the player collected all diamonds and all pigs are dead, then player won the game
     */
    public void checkWin () {
        if (enemyController.numberOfPigsAlive == 0 && objectController.numberOfDiamondsToTake == 0) {
            if (game.getLoggerState()) {
                logger.log(Level.INFO, "Player Wins");
                changeState(Gamestate.WIN, true);
            }
        }
    }

    /**
     * Checks if player touches the head of enemy
     * @param jumpBox represents the area under the players feet
     */
    public void checkJumpOnHead(Rectangle2D.Float jumpBox) {
        enemyController.checkJumpOnHead(jumpBox);
    }


    /**
     * Checks if the players hitbox touches the hitbox of a diamond
     * @param hitbox the player hitbox
     */
    public void checkDiamondCollected(Rectangle2D.Float hitbox) {
        objectController.checkDiamondCollected(hitbox);
    }

    /**
     * Checks if the players hitbox touches the hitbox of a heart
     * @param hitbox the player hitbox
     */
    public void checkHeartCollected (Rectangle2D.Float hitbox) {
        objectController.checkHeartCollected(hitbox);
    }


    /**
     * In case of winning and reseting the game, all classes has to be reset
     */
    public void resetGame() {
        player.resetPlayer();
        enemyController.resetEnemyController();
        objectController.resetObjects();
        if(game.getLoggerState())
            logger.log(Level.INFO, "Game Reset");
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
            case KeyEvent.VK_P -> changeState(Gamestate.WIN, true);
            case KeyEvent.VK_ESCAPE -> {
                changeState(Gamestate.MENU, false);
                player.resetMovement();
                if(game.getLoggerState())
                    logger.log(Level.INFO, "Switched to MENU state");
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

    public Player getPlayer() {
        return player;
    }

    public ObjectController getObjectManager() {
        return objectController;
    }

    public EnemyController getEnemyManager() {
        return enemyController;
    }

}