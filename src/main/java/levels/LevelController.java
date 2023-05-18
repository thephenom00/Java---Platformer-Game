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
    private Level levelOne;

    public LevelController(Game game) {
        this.game = game;
        importLevelSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    // Iterates through the img with all tiles and splits it into each tile
    private void importLevelSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[40];
        for (int j = 0; j<4; j++) { // j = y
            for (int i = 0; i < 10; i++) { // i = x
                int index = j*10 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }

    // Draws the whole map, using the getSpriteIndex method
    public void draw(Graphics g) {
        for(int j=0; j < TILES_IN_HEIGHT; j++)
            for(int i = 0; i< TILES_IN_WIDTH; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], TILES_SIZE * i,TILES_SIZE * j, TILES_SIZE, TILES_SIZE,null);
            }
    }

    public void update() {

    }

    public Level getCurrentLevel(){
        return levelOne;
    }
}