package entities;
import gameStates.Gamestate;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Size.*;

import static utilz.Constants.EnemyConstants.*;


public class Pig extends Enemy {
    private static final int PIG_HITBOX_WIDTH = 14;
    private static final int PIG_HITBOX_HEIGHT = 19;
    private Rectangle2D.Float attackBox;

    public Pig(float x, float y) {
        super(x, y, PIG_WIDTH, PIG_HEIGHT, PIG);
        createHitbox(x, y, (int)(PIG_HITBOX_WIDTH * SCALE), (int) (PIG_HITBOX_HEIGHT * SCALE));
        createAttackBox(x, y, (int)(10 * SCALE), (int) (PIG_HITBOX_HEIGHT * SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateMovement(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void createAttackBox(float x, float y, float width, float height) {
        attackBox = new Rectangle2D.Float(x, y, width, height);
    }

    protected void drawAttackBox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
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
//                    if (canSeePlayer(lvlData, player)) {
//                        facePlayer(player);
//                        changeAction(RUNNING);
//                    }
                    changeAction(RUNNING);
                    break;

                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        facePlayer(player);
                   }

                    if (isPlayerInAttackRange(player)) {
                        changeAction(ATTACK);
                    }

                    running(lvlData, player);

//                    if (!canSeePlayer(lvlData, player)) {
//                        changeAction(IDLE);
//                    }
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

