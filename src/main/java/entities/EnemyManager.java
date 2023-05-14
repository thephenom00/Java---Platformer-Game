package entities;

import gameStates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] pigArray;
    private ArrayList<Pig> pigs = new ArrayList<>();

    public EnemyManager(Playing playing) {
        loadEnemyImgs();
        getPigsFromPng();
    }

    // Updates all pigs in our arraylist
    public void update(int[][] lvlData, Player player) {
        for (Pig onePig : pigs)
            onePig.update(lvlData, player);
    }

    public void checkHit(Rectangle2D.Float jumpBox) {
        for (Pig onePig: pigs) {
            if (jumpBox.intersects(onePig.getHitbox())) {
                onePig.dead();
                return;
            }
        }

    }

    // Gets all the pigs from the Level array and stores to list
    public void getPigsFromPng() {
        pigs = LoadSave.GetPigs();
    }

    public void draw(Graphics g) {
        drawPigs(g);
//        drawHitbox(g);
    }

    // Saves all the Pigs from list into an array
    private void loadEnemyImgs() {
        pigArray = new BufferedImage[8][12];
        BufferedImage pigSprite = LoadSave.GetSpriteAtlas(LoadSave.PIG_SPRITE);
        for (int j = 0; j < pigArray.length; j++)
            for (int i = 0; i < pigArray[j].length; i++)
                pigArray[j][i] = pigSprite.getSubimage(i * PIG_WIDTH_DEFAULT, j * PIG_HEIGHT_DEFAULT, PIG_WIDTH_DEFAULT, PIG_HEIGHT_DEFAULT);
    }

    private void drawPigs(Graphics g) {
        for (Pig onePig : pigs)
            g.drawImage(pigArray[onePig.getEnemyAction()][onePig.getAniIndex()], (int) onePig.getHitbox().x - PIG_XOFFSET, (int) onePig.getHitbox().y - PIG_YOFFSET, PIG_WIDTH, PIG_HEIGHT, null);
    }

    private void drawHitbox(Graphics g) {
        for (Pig onePig : pigs)
            onePig.drawHitbox(g);
    }

    public ArrayList<Pig> getPigs() {
        return pigs;
    }
}
