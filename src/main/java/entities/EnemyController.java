package entities;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Constants.EnemyConstants.*;

/**
 * Class manages drawing, behaviour and interactions with player
 */
public class EnemyController implements Serializable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());
    private final Playing playing;
    private transient BufferedImage[][] pigArray;
    private static ArrayList<Pig> pigs = new ArrayList<>();
    public static int numberOfPigsAlive;


    public EnemyController(Playing playing) {
        this.playing = playing;
        loadPigSprites();
        getPigsFromPng();
    }

    /**
     * Updates the state of all the pigs in the enemy array
     * @param lvlData the game world
     * @param player  the player object
     */
    public void update(int[][] lvlData, Player player) {
        for (Pig onePig : pigs)
            if (onePig.alive) {
                onePig.update(lvlData, player);
            }
    }


    /**
     * Checks if any pig touches the players hitbox
     * @param playerHitbox the players hitbox
     * @return true if any pig touches the player, false otherwise
     */
    public boolean touchPlayer(Rectangle2D.Float playerHitbox) {
        for (Pig onePig : pigs)
            if (onePig.alive && onePig.hitbox.intersects(playerHitbox) && onePig.enemyAction != DEAD) {
                return true;

            }
        return false;
    }

    /**
     * Checks if any pig is hit on the head by the players jump
     *
     * @param jumpBox the hitbox of the players jump
     */
    public void checkJumpOnHead(Rectangle2D.Float jumpBox) {
        for (Pig onePig: pigs) {
            if (onePig.alive) {
                if (jumpBox.intersects(onePig.topHitbox) && onePig.enemyAction != DEAD) {
                    onePig.changeAction(DEAD);
                    numberOfPigsAlive--;

                    if (playing.getGame().getLoggerState()) {
                        logger.log(Level.INFO, numberOfPigsAlive + " pigs remaining");
                    }

                    playing.getPlayer().setAttack(true);
                    playing.getPlayer().jumpOnHead = true;

                }
            }
        }

    }

    /**
     * Gets all the pigs from the Level array and stores into a list
     */
    public void getPigsFromPng() {
        pigs = LoadSave.GetPigs();
        numberOfPigsAlive = pigs.size();

    }

    public void draw(Graphics g) {
        drawPigs(g);
//        drawHitbox(g);
//        drawAttackBox(g);
//        drawTopHitbox(g);
    }

    private void drawTopHitbox(Graphics g) {
        for (Pig onePig : pigs)
            if (onePig.alive)
                onePig.drawTopHitbox(g);
    }

    // Saves all the Pigs from list into an array
    private void loadPigSprites() {
        pigArray = new BufferedImage[8][12];
        BufferedImage pigSprite = LoadSave.GetSpriteAtlas(LoadSave.PIG_SPRITE);
        for (int j = 0; j < pigArray.length; j++)
            for (int i = 0; i < pigArray[j].length; i++)
                pigArray[j][i] = pigSprite.getSubimage(i * PIG_WIDTH_DEFAULT, j * PIG_HEIGHT_DEFAULT, PIG_WIDTH_DEFAULT, PIG_HEIGHT_DEFAULT);
    }

    private void drawPigs(Graphics g) {
        for (Pig onePig : pigs)
            if (onePig.alive) {
                g.drawImage(pigArray[onePig.getEnemyAction()][onePig.getAniIndex()],
                        (int) onePig.getHitbox().x - PIG_XOFFSET + onePig.checkMirrorX(),
                        (int) onePig.getHitbox().y - PIG_YOFFSET,
                        PIG_WIDTH * onePig.checkMirrorWidth(),
                        PIG_HEIGHT, null);
            }

    }

    private void drawHitbox(Graphics g) {
        for (Pig onePig : pigs)
            onePig.drawHitbox(g);
    }

    private void drawAttackBox(Graphics g) {
        for (Pig onePig : pigs)
            if (onePig.alive) {
                onePig.drawAttackBox(g);
            }

    }

    /**
     * In case of winning, reset all the enemies
     */
    public void resetEnemyController() {
        for (Pig onePig : pigs){
            onePig.resetEnemy();
        }
        numberOfPigsAlive = pigs.size();
    }

    public ArrayList<Pig> getPigs() {
        return pigs;
    }

    public void setPigs(ArrayList<Pig> pigs) {
        this.pigs = pigs;
    }

    public int getNumberOfPigsAlive() {
        return numberOfPigsAlive;
    }

    public void setNumberOfPigsAlive(int numberOfPigsAlive) {
        this.numberOfPigsAlive = numberOfPigsAlive;
    }




}
