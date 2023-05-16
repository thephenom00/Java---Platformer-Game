package utilz;

import entities.Pig;
import objects.Diamond;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utilz.Size.*;

import static utilz.Constants.EnemyConstants.PIG;
import static utilz.Constants.ObjectConstants.*;

public class LoadSave {
    public static final String PLAYER_ATLAS = "king.png";
    public static final String LEVEL_ATLAS = "tilesetgrass.png";
    public static final String LEVEL_ONE_DATA = "map_with_diamonds.png";
    public static final String PIG_SPRITE = "pig.png";
    public static final String BACKGROUND = "background.png";
    public static final String DIAMOND_SPRITE = "diamond.png";

    public static final BufferedImage levelOne = GetSpriteAtlas(LEVEL_ONE_DATA);

    private static final Logger logger = Logger.getLogger(LoadSave.class.getName());

    // Loads Player Sprites
    public static BufferedImage GetSpriteAtlas(String png){

        InputStream is = null;
        BufferedImage img = null;
        try {
            is = new FileInputStream("res/" + png);
            img = ImageIO.read(is);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load sprite atlas: " + png, e);
        } finally {
            try{
                if (is!=null){
                    is.close();
                }

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to close input stream", e);
            }
        }
        return img;
    }

    public static ArrayList<Pig> GetPigs() {
        ArrayList<Pig> list = new ArrayList<>();

        // Iterates through LevelData and gets the position of each Pig
        for (int j = 0; j < levelOne.getHeight(); j++)
            for (int i = 0; i < levelOne.getWidth(); i++) {
                Color color = new Color(levelOne.getRGB(i, j));
                int greenValue = color.getGreen();
                if (greenValue == PIG)
                    // If there is any Pig on that position, it adds him to the array on his position
                    list.add(new Pig(i * TILES_SIZE, j * TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<Diamond> GetDiamonds() {
        ArrayList<Diamond> list = new ArrayList<>();

        // Iterates through LevelData and gets the position of each Diamond
        for (int j = 0; j < levelOne.getHeight(); j++)
            for (int i = 0; i < levelOne.getWidth(); i++) {
                Color color = new Color(levelOne.getRGB(i, j));
                int greenValue = color.getGreen();
                if (greenValue == DIAMOND)
                    list.add(new Diamond(i * TILES_SIZE, j * TILES_SIZE));
            }
        return list;
    }



    // Loads LevelData
    public static int[][] GetLevelData() {
        int[][] lvlData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];

        // Iterates through LevelData and gets the red value from each pixel
        for (int j = 0; j < levelOne.getHeight(); j++) // j = y, i = x
            for (int i = 0; i < levelOne.getWidth(); i++) {
                Color color = new Color(levelOne.getRGB(i,j));
                int redValue = color.getRed();
                if (redValue >= 48) {
                    redValue = 9;
                }

                lvlData[j][i] = redValue;
            }
            return lvlData;
        }

}


