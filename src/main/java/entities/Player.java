package entities;

import static utilz.Physics.CanMoveHere;
import static utilz.Physics.*;

import gameStates.Playing;
import utilz.LoadSave;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Size.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean up, left, right, down, jump;
    private float playerSpeed = 1.0f * SCALE;
    private int[][] lvlData;
    private Playing playing;

    // Offset for hitbox
    private float xDrawOffset = 23 * SCALE;
    private float yDrawOffset = 17 * SCALE;

    // Jump and gravitation
    private float airSpeed = 0f; // Speed we are travelling in the air
    private float gravity = 0.04f * SCALE; // How fast player fall down
    private float jumpSpeed = -3.0f * SCALE;
    private float fallSpeedAfterCollision = 0.05f * SCALE;
    private boolean inAir = false;

    //Jump box
    private Rectangle2D.Float jumpBox;

    // Mirror the player
    private int mirrorX = 0;
    private int mirrorWidth = 1;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        createHitbox(x, y,16 * SCALE, 26 * SCALE);
        createJumpBox(x, y, 16 * SCALE, 1 * SCALE);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
        updateJumpBox();

        if (inAir) {
            checkHit();
        }

    }

    public void render(Graphics g) {
        // Subtract offset from hitbox
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) + mirrorX, (int) (hitbox.y - yDrawOffset), width * mirrorWidth, height, null);
//        drawHitbox(g);
        drawJumpBox(g);
    }


    private void updateJumpBox() {
        if (right) {
            jumpBox.x = hitbox.x;
            jumpBox.y = hitbox.y + hitbox.height;
        }
        if (left) {
            jumpBox.x = hitbox.x + hitbox.width;
            jumpBox.y = hitbox.y + hitbox.height;
        }


    }

    public void createJumpBox(float x, float y, float width, float height) {
        jumpBox = new Rectangle2D.Float(x, y, width, height);
    }

    public void drawJumpBox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((int) jumpBox.x, (int) jumpBox.y, (int) jumpBox.width, (int) jumpBox.height);
    }

    private void checkHit() {
        playing.checkHit(jumpBox);
    }


    // Sets an animation
    public void setAnimation(){
        int startAni = playerAction;

        if (!moving) {
            playerAction = IDLE;
        } else {
            playerAction = RUNNING;
        }

        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }

        if (attacking) {
            playerAction = ATTACK;
        }


        if (startAni != playerAction) {
            resetAniTick();
        }

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    public void updatePosition(){
        moving = false;

        if (jump) {
            jump();
        }

        // If the player does not do any movement, go out of the function
        if (!inAir) {
            if (!left && !right || (left && right)) {
                return;
            }
        }

        float xSpeed = 0;

        // If right or left is clicked, speed is added
        if (left) {
            xSpeed -= playerSpeed;
            mirrorX = width;
            mirrorWidth = -1;
        }

        if (right) {
            xSpeed += playerSpeed;
            mirrorX = 0;
            mirrorWidth = 1;
        }


        if (inAir) {
            // Checks the movement up and down
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                // Adds the airSpeed
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed); // We can move to the left and right

                // If we cant move up, we are either on the floor or we touched the ceiling
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                // Going down and hit the floor
                if (airSpeed > 0) {
                    resetInAir();
                // Hits the ceiling
                } else {
                    airSpeed = fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }
            }

        } else {
            // If we are not in air, we fall down
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
            updateXPos(xSpeed); // We can move the player in x direction
            moving = true;
        }


    }


    private void jump() {

        // If we are in the air, we cannot jump
        if(inAir) {
            return;
        }

        inAir = true;
        airSpeed = jumpSpeed;
    }


    private void updateXPos(float xSpeed) {

        // If player is on the ground and tile is not solid, moves
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            // If player collides with wall

//            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);

        }
    }

    // Method for update the Animation Tick
    private void updateAnimationTick() {
        aniTick ++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            // If all the sprites were displayed - reset
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[8][11];

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        // Iterates through every sprite
        for (int j = 0; j < animations.length; j++) {
            for(int i = 0 ; i < animations[j].length; i++){
                animations[j][i] = img.getSubimage(i*78, j*58,78,58);
            }
        }

    }

    public void loadLvlData(int[][]lvlData){
        this.lvlData = lvlData;

        // If the player spawns in the air, he falls
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }


    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }


}
