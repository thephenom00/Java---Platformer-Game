package utils;

import entities.Pig;
import objects.Diamond;
import objects.Heart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.Size.*;

import static utils.Constants.EnemyConstants.PIG;
import static utils.Constants.ObjectConstants.*;

public class LoadSave {
    public static final String PLAYER_SPRITES = "king.png";
    public static final String PIG_SPRITE = "pig.png";
    public static final String DIAMOND_SPRITE = "diamond.png";
    public static final String HEART_SPRITE = "heart.png";

    public static final String BACKGROUND = "background.png";
    public static final String LEVEL_SPRITES = "tiles.png";
    public static final String LEVEL_DATA = "level_data.png";

    public static final BufferedImage levelOne = GetSpriteAtlas(LEVEL_DATA);

    private static final Logger logger = Logger.getLogger(LoadSave.class.getName());

    /**
     * Loads a sprite atlas image from PNG
     *
     * @param png the file path of the PNG sprite atlas
     * @return the loaded BufferedImage representing the sprite atlas
     */
    public static BufferedImage GetSpriteAtlas(String png){

        InputStream is = null;
        BufferedImage img = null;
        try {
            is = new FileInputStream("src/main/java/res/" + png);
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

    /**
     * Gets the positions of all Pigs from the level data
     *
     * @return an ArrayList of Pig objects representing the positions of all Pigs in the level
     */
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

    /**
     * gets the positions of all diamonds from the level data.
     *
     * @return list of diamonds with each x and y position
     */

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

    /**
     * gets the positions of all hearts from the level data.
     *
     * @return list of hearts with each x and y position
     */
    public static ArrayList<Heart> GetHearts() {
        ArrayList<Heart> list = new ArrayList<>();

        // Iterates through LevelData and gets the position of each Diamond
        for (int j = 0; j < levelOne.getHeight(); j++)
            for (int i = 0; i < levelOne.getWidth(); i++) {
                Color color = new Color(levelOne.getRGB(i, j));
                int greenValue = color.getGreen();
                if (greenValue == HEART)
                    list.add(new Heart(i * TILES_SIZE, j * TILES_SIZE));
            }
        return list;
    }



    /**
     * gets red value of each pixel and stores it into an array
     *
     * @return 2D array of integers representing the level map
     */
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


