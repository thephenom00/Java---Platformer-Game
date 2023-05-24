package levels;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Size.*;



public class LevelController {
    // Saves each tile to the array
    private BufferedImage[] levelSprite;

    private Game game;
    private int[][] level;

    public LevelController(Game game) {
        this.game = game;
        importLevelSprites();
        level = LoadSave.GetLevelData();
    }

    /**
     * Imports tile sprites and stores into Buffered img
     */
    private void importLevelSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_SPRITES);
        levelSprite = new BufferedImage[40];
        for (int j = 0; j < 4; j++) { // j = y
            for (int i = 0; i < 10; i++) { // i = x
                int index = j*10 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }

    /**
     * Draws the level map on the screen according to level data red value and position of sprite
     *
     * @param g
     */
    public void draw(Graphics g) {
        for(int j = 0; j < TILES_IN_HEIGHT; j++)
            for(int i = 0; i < TILES_IN_WIDTH; i++) {
                int index = level[j][i];
                g.drawImage(levelSprite[index], TILES_SIZE * i,TILES_SIZE * j, TILES_SIZE, TILES_SIZE,null);
            }
    }


    public int[][] getLevel(){
        return level;
    }
}