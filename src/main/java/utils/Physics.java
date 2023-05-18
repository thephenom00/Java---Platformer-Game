package utils;

import static utils.Size.*;

import java.awt.geom.Rectangle2D;

public class Physics {
    // Checks if the movement is possible
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // Calls isSolid method with each corner as a parameter
        if(!isSolid(x,y,lvlData)) {
            if(!isSolid(x+width, y+height, lvlData)) {
                if (!isSolid(x+width,y, lvlData)) {
                    if (!isSolid(x, y+height, lvlData)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        // If the coordinates are out of the game
        if (x < 0 || x >= GAME_WIDTH) {
            return true;
        }

        if (y < 0 || y >= GAME_HEIGHT) {
            return true;
        }


        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        return isSolidHelper((int)xIndex, (int)yIndex, lvlData);
    }

    public static boolean isSolidHelper (int xIndex, int yIndex, int[][] lvlData) {
        // Gets the value which is equals to each tile
        int value = lvlData[yIndex][xIndex];

        // If the value is bigger than the number of all tiles
        // or its equal to any of our tile except 9 (the empty tile)
        if (value >= 40 || value <= 0 || value != 9) {
            return true;
        }
        return false;
    }


    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        // In which tile we are located
        int currentTile = (int) (hitbox.x / TILES_SIZE);

        // Colides with tile on the right
        if(xSpeed > 0) {
            // Value in pixels
            int tileXPos = currentTile * TILES_SIZE;

            // Space between our position and the wall
            int xOffset = (int)(TILES_SIZE - hitbox.width);

            return tileXPos + xOffset - 1; // -1 because the hitbox is in tile

            // Colides with tile on the left
        } else {
            // Does not compute offset, only returns the tile we are in
            return currentTile * TILES_SIZE;
        }
    }


    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        // In which tile we are located
        int currentTile = (int) (hitbox.y / TILES_SIZE);

        // Falling
        if (airSpeed > 0) {
            // Computes offset in y
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int)(TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;

            // Jumping
        } else {
            return currentTile * TILES_SIZE;
        }
    }
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // Checks if the pixels under the hitbox are solid - if not = not on the floor
        if(!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            if(!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        // On the floor
        return true;
    }


    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) {
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }

    }


    // Checks every tile between player and enemy
    public static boolean isThereObstacle(int[][] lvlData, Rectangle2D.Float EnemyHitbox, Rectangle2D.Float PlayerHitbox, int yPosition) {
        int enemyXPosition = (int) (EnemyHitbox.x / TILES_SIZE);
        int playerXPosition = (int) (PlayerHitbox.x / TILES_SIZE);

        // Player ... Enemy
        if (enemyXPosition > playerXPosition) {
            for (int i = 0; i < enemyXPosition - playerXPosition; i++) {
                if (isSolidHelper(playerXPosition + i, yPosition, lvlData) == true) {
                    return true;
                }
            }

            // Enemy ... Player
        } else {
            for (int i = 0; i < playerXPosition - enemyXPosition ; i++) {
                if (isSolidHelper(enemyXPosition + i, yPosition, lvlData) == true) {
                    return true;
                }
            }
        }
        return false;
    }
}
