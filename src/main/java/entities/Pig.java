package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import static utils.Constants.Directions.*;
import static utils.Size.*;

import static utils.Constants.EnemyConstants.*;


public class Pig extends Enemy {
    private static final Logger logger = Logger.getLogger(Game.class.getName());
    private static final int PIG_HITBOX_WIDTH = 14;
    private static final int PIG_HITBOX_HEIGHT = 19;
    protected Rectangle2D.Float attackBox;
    protected Rectangle2D.Float topHitbox;

    public Pig (float x, float y) {
        super(x, y, PIG_WIDTH, PIG_HEIGHT, PIG);
        createHitbox(x, y, (int)(PIG_HITBOX_WIDTH * SCALE), (int) (PIG_HITBOX_HEIGHT * SCALE));
        createAttackBox(x, y, (int)(10 * SCALE), (int) (PIG_HITBOX_HEIGHT * SCALE));
        createTopHitbox(x, y, (int)(PIG_HITBOX_WIDTH * SCALE) - 10,  3 * SCALE);
    }

    public void update(int[][] lvlData, Player player) {
        updateMovement(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
        updateTopHitbox();
    }

    private void createAttackBox(float x, float y, float width, float height) {
        attackBox = new Rectangle2D.Float(x, y, width, height);
    }

    private void createTopHitbox(float x, float y, float width, float height) {
        topHitbox = new Rectangle2D.Float(x + (hitbox.width - width) / 2, y, width, height);
    }

    protected void drawAttackBox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void drawTopHitbox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((int) topHitbox.x, (int) topHitbox.y, (int) topHitbox.width, (int) topHitbox.height);
    }

    protected void updateTopHitbox() {
        topHitbox.x = hitbox.x + (hitbox.width - topHitbox.width) / 2;
        topHitbox.y = hitbox.y;
    }

    protected void updateAttackBox() {
        if (runDirection == RIGHT) {
            attackBox.x = hitbox.x + hitbox.width;
        } else if (runDirection == LEFT) {
            attackBox.x = hitbox.x - attackBox.width;
        }
        attackBox.y = hitbox.y;
    }

    public void updateMovement(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }

        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyAction) {

                case IDLE:
                    changeAction(RUNNING);
                    break;

                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        facePlayer(player);
                    }

                    if (isPlayerInAttackRange(player)) {
                        changeAction(ATTACK);
                    }

                    running(lvlData);
                    break;

                case ATTACK:
                    if (aniIndex == 3 && isPlayerInAttackRange(player)) {
                        playerGetHit(player);
                        break;
                    }


            }

        }
    }

    protected boolean isPlayerInAttackRange(Player player) {
        return attackBox.intersects(player.getHitBox());
    }

    protected void playerGetHit(Player player) {
        player.getHit(true);
        player.subtractLife();
        if (player.getLives() == 0) {
            player.setDeath(true);
        }
        changeAction(IDLE);
    }

    protected int checkMirrorWidth() {
        if (runDirection == RIGHT) {
            return -1;
        } else {
            return 1;
        }

    }

    protected int checkMirrorX() {
        if (runDirection == RIGHT) {
            return width;
        } else {
            return 0;
        }
    }


}

