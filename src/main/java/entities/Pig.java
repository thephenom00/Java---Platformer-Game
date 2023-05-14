package entities;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Size.*;

import static utilz.Constants.EnemyConstants.*;


public class Pig extends Enemy {
    private static final int PIG_HITBOX_WIDTH = 14;
    private static final int PIG_HITBOX_HEIGHT = 19;

    public Pig(float x, float y) {
        super(x, y, PIG_WIDTH, PIG_HEIGHT, PIG);
        createHitbox(x, y, (int)( PIG_HITBOX_WIDTH * SCALE), (int) (PIG_HITBOX_HEIGHT * SCALE));
    }


    public void update(int[][] lvlData, Player player) {
        updateMovement(lvlData, player);
        updateAnimationTick();
    }

    public void dead() {
        changeAction(DEAD);
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
                    if (canSeePlayer(lvlData, player)) {
                        facePlayer(player);
                        changeAction(RUNNING);
                    }
                    break;

                case RUNNING:
                    running(lvlData, player);

                    if (isInAttackRange(player)) {
                        changeAction(ATTACK);
                    }

                    if (!canSeePlayer(lvlData, player)) {
                        changeAction(IDLE);
                    }

                    break;
            }

        }
    }

}
