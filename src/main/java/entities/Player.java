package entities;

import static utils.Physics.CanMoveHere;
import static utils.Physics.*;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Constants.PlayerConstants.*;
import static utils.Size.*;

public class Player extends Entity implements Serializable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false, getHit = false, dead = false;
    private boolean left, right, jump;
    private float playerSpeed = 1.0f * SCALE;
    private int[][] lvlData;
    private Playing playing;

    // Offset for hitbox
    private float xDrawOffsetRight = 23 * SCALE;
    private float xDrawOffsetLeft = 38 * SCALE;
    private float yDrawOffset = 17 * SCALE;

    private Boolean facingRight = true;

    // Jump and gravitation
    private float airSpeed = 0f; // Speed we are travelling in the air
    private float gravity = 0.05f * SCALE; // How fast player fall down
    private float jumpSpeed = -2.5f * SCALE;
    private float fallSpeedAfterCollision = 0.05f * SCALE;
    private boolean inAir = false;

    public boolean jumpOnHead = false;
    private boolean hitLogged = false;

    //Jump box
    private Rectangle2D.Float jumpBox;

    // Mirror the player
    private int mirrorX = 0;
    private int mirrorWidth = 1;

    // GUI
    public int lives = 3;
    private int diamondsToCollect;

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
        updateJumpBox();

        setAnimation();

        // Checking
        checkMethods();

    }

    private void checkMethods(){
        checkJumpOnHead();
        checkDiamondCollected();
        checkHeartCollected();
        checkTouchingEnemy();
        checkOutOfBounds();
        checkIfPlayerInAir();
    }

    // If he fells out to the hole = dead
    private void checkOutOfBounds() {
        if (hitbox.y >= 483 * SCALE) {
            dead = true;
        }
    }

    // If player touches the enemy he cant move
    private void checkTouchingEnemy() {
        if (playing.checkPlayerTouchesEnemy() == true){
            if(left) {
                left = false;
            } else if (right) {
                right = false;
            }
        }

    }

    public void draw(Graphics g) {
        // Draw the player
        if (facingRight) {
            g.drawImage(animations[playerAction][aniIndex],
                    (int) (hitbox.x - xDrawOffsetRight) + mirrorX,
                    (int) (hitbox.y - yDrawOffset),
                    width * mirrorWidth,
                    height,
                    null);
        } else {
            g.drawImage(animations[playerAction][aniIndex],
                    (int) (hitbox.x - xDrawOffsetLeft) + mirrorX,
                    (int) (hitbox.y - yDrawOffset),
                    width * mirrorWidth,
                    height,
                    null);
        }


        // Draw hitboxes
//        drawHitbox(g);
//        drawJumpBox(g);

        drawLives(g);
        drawCoins(g);
    }

    public void subtractLife() {
        lives--;
    }

    public void addLife() {
        lives++;
    }

    private void drawCoins(Graphics g) {
        // Draws coins to collect
        Font coinsFont = new Font("Arial", Font.PLAIN, 20);
        g.setFont(coinsFont);
        g.setColor(Color.BLACK);
        g.drawString("Diamonds to Collect: " + diamondsToCollect, 10, 40);
    }

    private void drawLives(Graphics g) {
        // Draws lives text
        Font font = new Font("Arial", Font.PLAIN,  20);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("Lives: " + lives, 10, 20);
    }


    private void updateJumpBox() {
        jumpBox.x = hitbox.x;
        jumpBox.y = hitbox.y + hitbox.height;
    }

    public void createJumpBox(float x, float y, float width, float height) {
        jumpBox = new Rectangle2D.Float(x, y, width, height);
    }

    public void drawJumpBox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((int) jumpBox.x, (int) jumpBox.y, (int) jumpBox.width, (int) jumpBox.height);
    }

    private void checkJumpOnHead() {
        playing.checkJumpOnHead(jumpBox);
    }

    private void checkDiamondCollected() {
        playing.checkDiamondCollected(hitbox);
    }

    public void checkHeartCollected() {
        playing.checkHeartCollected(hitbox);
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

        if (getHit) {
            playerAction = HIT;
        }

        if (dead){
            playerAction = DEAD;
            left = false;
            right = false;
            if (aniIndex == 3) {
                Gamestate.state = Gamestate.GAMEOVER;
                if (playing.getGame().getLoggerState())
                    logger.log(Level.INFO, "GAME OVER");
            }
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

        if (jumpOnHead) {
            jumpSpeed = -1.5f * SCALE;
            jump();
            jumpSpeed = -2.5f * SCALE;
            jumpOnHead = false;
        }

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
            facingRight = false;
        }

        if (right) {
            xSpeed += playerSpeed;
            mirrorX = 0;
            mirrorWidth = 1;
            facingRight = true;
        }

        if (inAir) {
            // Checks the movement up and down
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
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
            // If we are not on the floor, we are in air
            checkIfPlayerInAir();
            updateXPos(xSpeed); // We can move the player in x direction
            moving = true;
        }


    }

    private void checkIfPlayerInAir() {
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }


    private void jump() {

        // If we are in the air, we cannot jump
        if(inAir && !jumpOnHead) {
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

            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);

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
                getHit = false;
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

    public void resetMovement() {
        left = false;
        right = false;
        jump = false;
    }

    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }

    public void getHit(boolean getHit) {
        if (playing.getGame().getLoggerState()) {
            logger.log(Level.INFO, "Player got hit");
        }
        this.getHit = getHit;
    }

    public void setDeath(boolean dead) {
        this.dead = dead;
        if (playing.getGame().getLoggerState())
            logger.log(Level.INFO, "Player died");
    }

    public void resetPlayer() {
        resetMovement();
        resetInAir();
        resetAniTick();
        dead = false;
        moving = false;
        attacking = false;
        getHit = false;
        playerAction = IDLE;
        lives = 3;


        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
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

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public Rectangle2D.Float getHitBox() {
        return hitbox;
    }


    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public float getXPosition() {
        return hitbox.x;
    }

    public float getYPosition() {
        return hitbox.y;
    }

    public void setDiamondsToCollect(int diamondsToCollect) {
        this.diamondsToCollect = diamondsToCollect;
    }

    public int getDiamondsToCollect() {
        return diamondsToCollect;
    }

    public Playing getPlaying() {
        return playing;
    }

}
