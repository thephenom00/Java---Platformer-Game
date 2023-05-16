package objects;

import entities.Player;

import static utilz.Constants.ObjectConstants.*;

public class Diamond extends Object{
    private static final int DIAMOND_HITBOX_WIDTH = 12;
    private static final int DIAMOND_HITBOX_HEIGHT = 10;
    public Diamond(float x, float y) {
        super(x, y, DIAMOND_WIDTH, DIAMOND_HEIGHT, DIAMOND);
        createHitbox(x, y, DIAMOND_HITBOX_WIDTH, DIAMOND_HITBOX_HEIGHT);
    }

    public void update(int[][] lvlData, Player player) {
        updateAnimationTick();
    }



}
