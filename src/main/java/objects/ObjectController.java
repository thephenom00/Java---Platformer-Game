package objects;

import entities.Player;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Constants.ObjectConstants.*;

/**
 * class manages the diamonds and hearts in the game
 */
public class ObjectController {
    private static final Logger logger = Logger.getLogger(Game.class.getName());
    private Player player;

    //Diamonds
    private BufferedImage[][] diamondSprites;
    private ArrayList<Diamond> diamonds = new ArrayList<>();
    public int numberOfDiamondsToTake;

    // Hearts
    private BufferedImage[][] heartSprites;
    private ArrayList<Heart> hearts = new ArrayList<>();


    public ObjectController(Player player) {
        this.player = player;

        //Hearts
        loadHeartsImg();
        getHeartArray();

        // Diamonds
        loadDiamondImg();
        getDiamondArray();
    }

    public void draw(Graphics g) {
        drawDiamonds(g);
        drawHearts(g);
    }

    public void update() {
        for (Heart oneHeart : hearts) {
            if(oneHeart.isCollected == false)
                oneHeart.update();
        }

        for (Diamond oneDiamond : diamonds)
            if(oneDiamond.isCollected == false)
                oneDiamond.update();
    }

    /**
     * retrieves the hearts from png
     */
    private void getHeartArray() {
        hearts = LoadSave.GetHearts();
    }

    /**
     * retrieves diamonds from png
     */
    private void getDiamondArray() {
        diamonds = LoadSave.GetDiamonds();
        numberOfDiamondsToTake = diamonds.size();
        player.setDiamondsToCollect(numberOfDiamondsToTake);
    }

    /**
     * loads the heart images from the sprite atlas and saves to heartSprites
     */
    private void loadHeartsImg() {
        heartSprites = new BufferedImage[1][8];
        BufferedImage heartSprite = LoadSave.GetSpriteAtlas(LoadSave.HEART_SPRITE);
        for (int i = 0; i < heartSprites[0].length; i++)
            heartSprites[0][i] = heartSprite.getSubimage(i * HEART_WIDTH_DEFAULT, 0, HEART_WIDTH_DEFAULT, HEART_HEIGHT_DEFAULT);
    }

    /**
     * loads the diamond images from the sprite atlas and saves to diamondSprites
     */
    private void loadDiamondImg() {
        diamondSprites = new BufferedImage[1][10];
        BufferedImage diamondSprite = LoadSave.GetSpriteAtlas(LoadSave.DIAMOND_SPRITE);
        for (int i = 0; i < diamondSprites[0].length; i++)
            diamondSprites[0][i] = diamondSprite.getSubimage(i * DIAMOND_WIDTH_DEFAULT, 0, DIAMOND_WIDTH_DEFAULT, DIAMOND_HEIGHT_DEFAULT);
    }

    private void drawHearts(Graphics g) {
        for (Heart oneHeart : hearts) {
            if(oneHeart.isCollected == false) {
                g.drawImage(heartSprites[0][oneHeart.getObjectIndex()],
                        (int) oneHeart.hitbox.x - Heart.HEART_XOFFSET,
                        (int) oneHeart.hitbox.y - Heart.HEART_YOFFSET + 10,
                        HEART_WIDTH,
                        HEART_HEIGHT,
                        null);
            }

        }
    }

    public void drawDiamonds(Graphics g) {
        for (Diamond oneDiamond : diamonds) {
            if (!oneDiamond.isCollected ) {
                g.drawImage(diamondSprites[0][oneDiamond.getObjectIndex()],
                        (int) oneDiamond.hitbox.x - Diamond.DIAMOND_XOFFSET,
                        (int) oneDiamond.hitbox.y - Diamond.DIAMOND_YOFFSET + 10,
                        DIAMOND_WIDTH,
                        DIAMOND_HEIGHT,
                        null);
            }

        }
    }

    private void drawHeartHitbox(Graphics g) {
        for (Heart oneHeart : hearts) {
            if(!oneHeart.isCollected)
                oneHeart.drawHitbox(g);
        }
    }


    private void drawDiamondHitbox(Graphics g) {
        for (Diamond oneDiamond: diamonds) {
            if(!oneDiamond.isCollected)
                oneDiamond.drawHitbox(g);
        }
    }

    /**
     * Method checks if the player collected heart
     * @param playerHitbox
     */
    public void checkHeartCollected(Rectangle2D playerHitbox) {
        for (Heart oneHeart: hearts) {
            if (!oneHeart.isCollected)
                if (playerHitbox.intersects(oneHeart.hitbox)) {
                    if (player.getPlaying().getGame().getLoggerState())
                        logger.log(Level.INFO, "Heart collected ");
                    oneHeart.isCollected = true;
                    player.addLife();
                }
        }
    }

    /**
     * Method checks if the player collected diamond
     * @param playerHitbox
     */
    public void checkDiamondCollected(Rectangle2D.Float playerHitbox) {
        for (Diamond oneDiamond : diamonds) {
            if (!oneDiamond.isCollected)
                if (playerHitbox.intersects(oneDiamond.hitbox)){
                    oneDiamond.isCollected = true;
                    numberOfDiamondsToTake--;
                    player.setDiamondsToCollect(numberOfDiamondsToTake);
                    if (player.getPlaying().getGame().getLoggerState())
                        logger.log(Level.INFO, numberOfDiamondsToTake + " diamonds remaining");
                }

        }
    }

    /**
     * In case of player winning the game, all objects has to be reset
     */
    public void resetObjects() {
        for (Diamond oneDiamond : diamonds) {
            oneDiamond.isCollected = false;
        }
        numberOfDiamondsToTake = diamonds.size();

        for (Heart oneHeart : hearts) {
            oneHeart.isCollected = false;
        }

    }

    public ArrayList getDiamonds() {
        return diamonds;
    }

    public ArrayList getHearts() {
        return hearts;
    }

    public void setDiamonds(ArrayList<Diamond> diamonds) {
        this.diamonds = diamonds;
    }

    public void setHearts(ArrayList<Heart> hearts) {
        this.hearts = hearts;
    }

    public void setNumberOfDiamondsToTake(int diamondsToTake) {
        this.numberOfDiamondsToTake = diamondsToTake;
        player.setDiamondsToCollect(numberOfDiamondsToTake);
    }


    public int getNumberOfDiamondsToTake() {
        return numberOfDiamondsToTake;
    }

}

