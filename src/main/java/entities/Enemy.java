package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.Physics.*;
import static utils.Constants.Directions.*;
import static utils.Size.*;


public abstract class Enemy extends Entity{
    protected int aniIndex, enemyAction;
    protected int aniTick, aniSpeed = 25;
    protected boolean startForTheFirstTime = true;
    protected float fallSpeed;
    protected float gravity = 0.04f * SCALE;
    protected float enemySpeed = 0.3f * SCALE;
    protected int runDirection = LEFT;

    // Mirror the player
    protected int mirrorX = 0;
    protected int mirrorWidth = 1;

    // Range
    protected int yLevel;
    protected int playerYLevel;
    protected float seePlayerRange = TILES_SIZE * 5;

    // Is Alive
    protected boolean alive = true;

    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height);
        createHitbox(x, y, width, height);
    }


    /**
     * If player is on the left or right, enemy will turn to him
     * @param player Player object
     */
    protected void facePlayer(Player player) {

        // Enemy ... Player
        if (player.hitbox.x > hitbox.x) {
            runDirection = RIGHT;

        } else {
            runDirection = LEFT;
        }
    }

    /**
     * Checks if the enemy can run towards player
     * @param lvlData game world
     * @param player  player object
     * @return true if the player is in range and visible to the enemy, false otherwise
     */
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        playerYLevel = (int) (player.getHitbox().y / TILES_SIZE);
        yLevel = (int) (hitbox.y / TILES_SIZE);
        if (playerYLevel == yLevel) { // Enemy and Player are in the same Y position
            if (isPlayerInRange(player)) {
                if (!isThereObstacle(lvlData, hitbox, player.hitbox, yLevel)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player in the range of the enemy
     * @param player the player object
     * @return true if the player is in the range of the enemy, false otherwise
     */
    protected boolean isPlayerInRange(Player player) {
        int EnemyPlayerDistance = (int) Math.abs(hitbox.x - player.hitbox.x);
        if (EnemyPlayerDistance <= seePlayerRange) {
            return true;
        }
        return false;

    }

    /**
     * If action is changed, resets the animation tick
     * @param enemyAction new action of enemy
     */
    protected void changeAction(int enemyAction) {
        this.enemyAction = enemyAction;
        aniIndex = 0;
        aniTick = 0;
    }

    /**
     * Handles logic of running of an enemy
     * @param lvlData game world
     */
    protected void running(int[][] lvlData) {
        float xSpeed;
        if (runDirection == LEFT) {
            xSpeed = -enemySpeed;
        } else {
            xSpeed = enemySpeed;
        }

        //Checks if CanMoveHere and if there is an edge
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (isFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        // If all the IFs above will fail, we change direction
        if (runDirection == LEFT) {
            runDirection = RIGHT;
        } else {
            runDirection = LEFT;
        }
    }

    /**
     * If the game starts for the first time, enemies will fall on the floor
     * @param lvlData
     */
    protected void fallIfGameIsStarted(int[][] lvlData) {
        if (startForTheFirstTime) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += fallSpeed;
                    fallSpeed += gravity;
                }
            } else {
                yLevel = (int) (hitbox.y / TILES_SIZE); // Saves the Y position of enemy
                startForTheFirstTime = false;
            }
        }
    }

    /**
     * Updates the aniTick and handles reseting the animations
     */
    protected void updateAnimationTick() {
        aniTick ++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            // If all the sprites were displayed, reset
            if (aniIndex >= GetSpriteAmount(enemyAction)) {
                aniIndex = 0;
                if (enemyAction == ATTACK) {
                    enemyAction = IDLE;
                }

                if (enemyAction == DEAD) {
                    alive = false;
                }
            }
        }
    }

    /**
     * If player win the game, the whole enemy class has to be reset
     */
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        startForTheFirstTime = true;
        changeAction(IDLE);
        alive = true;
        fallSpeed = 0;
        mirrorWidth = 1;
        mirrorX = 0;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyAction() {
        return enemyAction;
    }

}


