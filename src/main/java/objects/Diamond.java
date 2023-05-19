package objects;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ObjectConstants.*;
import static utils.Size.*;

public class Diamond extends Object{
    private static final int DIAMOND_HITBOX_WIDTH = 12;
    private static final int DIAMOND_HITBOX_HEIGHT = 10;
    public static final int DIAMOND_XOFFSET = 7;
    public static final int DIAMOND_YOFFSET = 3;


    public Diamond(float x, float y) {
        super(x, y, DIAMOND_WIDTH, DIAMOND_HEIGHT, DIAMOND);
        createHitbox(x, y, DIAMOND_HITBOX_WIDTH * SCALE, DIAMOND_HITBOX_HEIGHT * SCALE);
    }

    public void update() {
        updateAnimationTick();
    }

    public float getDiamondXPosition() {
        return getHitbox().x;
    }

    public float getDiamondYPosition() {
        return getHitbox().y;
    }

    public boolean playerCollectedDiamond(Rectangle2D.Float playerHitbox) {
        if (playerHitbox.intersects(getHitbox())) {
            isCollected = true;
            return true;
        }
        return false;
    }




}
