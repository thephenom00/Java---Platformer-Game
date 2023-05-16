package objects;

import entities.Pig;
import entities.Player;
import gameStates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.PIG_HEIGHT;
import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] diamondArray;
    private static ArrayList<Diamond> diamonds = new ArrayList<>();
    public static int numberOfDiamondsToTake;

    public ObjectManager (Playing playing) {
        this.playing = playing;
        loadDiamondImg();
        getDiamondsFromPng();
    }

    public void draw(Graphics g) {
        drawDiamonds(g);
    }


    private void getDiamondsFromPng() {
        diamonds = LoadSave.GetDiamonds();
        numberOfDiamondsToTake = diamonds.size();
    }

    private void loadDiamondImg() {
        diamondArray = new BufferedImage[1][10];
        BufferedImage diamondSprite = LoadSave.GetSpriteAtlas(LoadSave.DIAMOND_SPRITE);
        for (int i = 0; i < diamondArray[0].length; i++)
            diamondArray[0][i] = diamondSprite.getSubimage(i * DIAMOND_WIDTH_DEFAULT, 0, DIAMOND_WIDTH_DEFAULT, DIAMOND_HEIGHT_DEFAULT);
    }


    public void update(int[][] lvlData, Player player) {
        for (Diamond oneDiamond : diamonds)
            oneDiamond.update(lvlData, player);
    }

    public void drawDiamonds(Graphics g) {
        for (Diamond oneDiamond : diamonds) {
            g.drawImage(diamondArray[0][oneDiamond.getObjectIndex()],
                    (int) oneDiamond.x,
                    (int) oneDiamond.y,
                    DIAMOND_WIDTH,
                    DIAMOND_HEIGHT,
                    null);
        }
        }
    }
