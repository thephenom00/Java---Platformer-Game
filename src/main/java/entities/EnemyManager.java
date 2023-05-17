package entities;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] pigArray;
    private static ArrayList<Pig> pigs = new ArrayList<>();
    public static int numberOfPigsAlive;


    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadPigSprites();
        getPigsFromPng();
    }

    // Updates all pigs in our arraylist
    public void update(int[][] lvlData, Player player) {
        for (Pig onePig : pigs)
            if (onePig.alive) {
                onePig.update(lvlData, player);
            }
    }

    public boolean touchPlayer(Rectangle2D.Float playerHitbox) {
        for (Pig onePig : pigs)
            if (onePig.alive && onePig.hitbox.intersects(playerHitbox) && onePig.enemyAction != DEAD) {
                return true;

            }
        return false;
    }

    public void checkJumpOnHead(Rectangle2D.Float jumpBox) {
        for (Pig onePig: pigs) {
            if (onePig.alive) {
                if (jumpBox.intersects(onePig.topHitbox) && onePig.enemyAction != DEAD) {
                    onePig.changeAction(DEAD);
                    playing.getPlayer().setAttack(true);
                    playing.getPlayer().jumpOnHead = true;
                    return;
                }

            }
        }

    }

    // Gets all the pigs from the Level array and stores to list
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

    public void resetEnemyManager() {
        for (Pig onePig : pigs){
            onePig.resetEnemy();
        }
        numberOfPigsAlive = pigs.size();

    }

    public static ArrayList<Pig> getPigs() {
        return pigs;
    }


    public static void subtractPigFromArray() {
        numberOfPigsAlive--;
    }




}
