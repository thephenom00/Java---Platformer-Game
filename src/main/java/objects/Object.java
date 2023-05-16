package objects;

import entities.EnemyManager;
import gameStates.Gamestate;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ObjectConstants.*;

public abstract class Object {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    // Animations
    protected int aniTick, aniSpeed = 25;
    protected int aniIndex, objectType;
    public boolean isCollected = false;

    // Passes variables from Player
    public Object (float x, float y, int width, int height, int objectType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        aniTick ++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            // If all the sprites were displayed, reset
            if (aniIndex >= GetSpriteAmount(objectType)) {
                aniIndex = 0;
            }
        }
    }

    protected void drawHitbox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect((int) hitbox.x, (int) hitbox.y + 10, (int) hitbox.width, (int) hitbox.height);
    }

    // Creates hitbox
    protected void createHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y + 10, width, height);
    }


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getObjectIndex() {
        return aniIndex;
    }

}
