package entities;

import gameStates.Gamestate;

import java.awt.*;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Physics.*;
import static utilz.Constants.Directions.*;
import static utilz.Size.*;


public abstract class Enemy extends Entity{
    protected int aniIndex, enemyAction, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
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
    protected float attackRange = TILES_SIZE;
    protected float seePlayerRange = TILES_SIZE * 5;

    // Is Alive
    protected boolean alive = true;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        createHitbox(x, y, width, height);
    }



    protected void facePlayer(Player player) {

        // Enemy ... Player
        if (player.hitbox.x > hitbox.x) {
            runDirection = RIGHT;

        } else {
            runDirection = LEFT;
        }
    }

    // Checks if player is in the range of enemy
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        playerYLevel = (int) (player.getHitbox().y / TILES_SIZE);

        if (playerYLevel == yLevel) { // Enemy and Player are in the same Y position
            if (isPlayerInRange(player)) {
                if (!isThereObstacle(lvlData, hitbox, player.hitbox, yLevel)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {

        int EnemyPlayerDistance = (int) Math.abs(hitbox.x - player.hitbox.x);
        if (EnemyPlayerDistance <= seePlayerRange) {
            return true;
        }
        return false;

    }


    protected void changeAction(int enemyAction) {
        this.enemyAction = enemyAction;
        aniIndex = 0;
        aniTick = 0;
    }

    protected void running(int[][] lvlData, Player player) {
        float xSpeed = 0;
        if (runDirection == LEFT) {
            xSpeed = -enemySpeed;
        } else {
            xSpeed = enemySpeed;
        }

        //Checks if CanMoveHere and if there is an edge
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (isFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        // If all the IFs above will fail, we change direction
        changeDirection();

    }

    protected void firstUpdateCheck(int[][] lvlData) {
        // If is not on floor = in air
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            yLevel = (int) (hitbox.y / TILES_SIZE); // Saves the Y position of enemy
        }
    }

    // Method for update the Animation Tick
    protected void updateAnimationTick() {
        aniTick ++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            // If all the sprites were displayed, reset
            if (aniIndex >= GetSpriteAmount(enemyType, enemyAction)) {
                aniIndex = 0;
                if (enemyAction == ATTACK) {
                    enemyAction = IDLE;
                }

                if (enemyAction == DEAD) {
                    alive = false;
                    EnemyManager.subtractPigFromArray();
                }
            }
        }
    }

    protected void changeDirection() {
        if (runDirection == LEFT) {
            runDirection = RIGHT;
        } else {
            runDirection = LEFT;
        }
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        changeAction(IDLE);
        alive = true;
        fallSpeed = 0;
        mirrorWidth = 1;
        mirrorX = 0;
    }

    // We draw it in enemyHandler
    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyAction() {
        return enemyAction;
    }

    public int getYPosition() {
        return yLevel;
    }

    public boolean isAlive() {
        return alive;
    }
}


