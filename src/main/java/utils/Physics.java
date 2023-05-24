package utils;

import static utils.Size.*;

import java.awt.geom.Rectangle2D;

public class Physics {
    /**
     * checks if the entity can move there
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of entity
     * @param height height of entity
     * @param lvlData array representing game world
     * @return true if the movement is possible, false otherwise
     */
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // Calls isSolid method with each corner as a parameter
        return !isSolid(x, y, lvlData) &&
                !isSolid(x + width, y + height, lvlData) &&
                !isSolid(x + width, y, lvlData) &&
                !isSolid(x, y + height, lvlData);
    }

    /**
     * Checks if a tile at the specified index is solid.
     *
     * @param x  The x-index of the tile.
     * @param y  The y-index of the tile.
     * @param lvlData The level data representing the game level.
     * @return true if the tile is solid, false otherwise.
     */
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

        return isSolidHelper((int) xIndex, (int) yIndex, lvlData);
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

    /**
     * If the pixel under the players hitbox is solid, he is on the floor
     * @param hitbox
     * @param lvlData
     * @return
     */
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

    /**
     * Checks if the entity goes to the edge
     *
     * @param hitbox   The hitbox to check for a floor.
     * @param xSpeed   The horizontal speed.
     * @param lvlData  The level data containing tile information.
     * @return True if there is a solid floor, otherwise false.
     */
    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) {
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }

    }


/**
 * Checks if there is an obstacle between the enemy and the player by checking every tile between them
 *
 * @param lvlData         The level data containing tile information.
 * @param EnemyHitbox     The hitbox of the enemy.
 * @param PlayerHitbox    The hitbox of the player.
 * @param yPosition       The y-position of the obstacle.
 * @return True if there is an obstacle between the enemy and player, otherwise false.
 */
    public static boolean isThereObstacle(int[][] lvlData, Rectangle2D.Float EnemyHitbox, Rectangle2D.Float PlayerHitbox, int yPosition) {
        int enemyXPosition = (int) (EnemyHitbox.x / TILES_SIZE);
        int playerXPosition = (int) (PlayerHitbox.x / TILES_SIZE);

        // Player ... Enemy
        if (enemyXPosition > playerXPosition) {
            for (int i = 0; i < enemyXPosition - playerXPosition; i++) {
                if (isSolidHelper(playerXPosition + i, yPosition, lvlData)) {
                    return true;
                }
            }

            // Enemy ... Player
        } else {
            for (int i = 0; i < playerXPosition - enemyXPosition ; i++) {
                if (isSolidHelper(enemyXPosition + i, yPosition, lvlData)) {
                    return true;
                }
            }
        }
        return false;
    }
}
