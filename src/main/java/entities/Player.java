package entities;

import static utils.Constants.Directions.*;
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

    // Animation
    private BufferedImage[][] animations;
    private int aniTick;
    private int aniIndex;
    private int aniSpeed = 25;

    // Player Actions
    private int playerAction = IDLE;
    private boolean moving = false;
    private boolean attacking = false;
    private boolean getHit = false;
    private boolean dead = false;

    // Movement
    private boolean left;
    private boolean right;
    private boolean jump;
    private float playerSpeed = SCALE;

    // Level Data
    private int[][] lvlData;
    private Playing playing;

    // Hitbox Offset
    private float xDrawOffsetRight = 23 * SCALE;
    private float xDrawOffsetLeft = 38 * SCALE;
    private float yDrawOffset = 17 * SCALE;

    // Direction
    private Boolean facingRight = true;

    // Jump and Gravity
    private float airSpeed = 0f; // Speed of moving in the air
    private float gravity = 0.05f * SCALE; // Speed of falling down
    private float jumpSpeed = -2.6f * SCALE;
    private boolean inAir = false;

    // Jump on Head
    public boolean jumpOnHead = false;

    // Jump Box
    private Rectangle2D.Float jumpBox;

    // Mirror
    private int mirrorX = 0;
    private int mirrorWidth = 1;

    // GUI
    public int lives = 3;
    private int diamondsToCollect;

    // Enemy
    public int enemyDirection;

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

        drawLivesText(g);
        drawCoinsText(g);
    }

    public void subtractLife() {
        lives--;
    }

    public void addLife() {
        lives++;
    }

    private void drawCoinsText(Graphics g) {
        // Draws coins to collect
        Font coinsFont = new Font("Arial", Font.PLAIN, 20);
        g.setFont(coinsFont);
        g.setColor(Color.BLACK);
        g.drawString("Diamonds to Collect: " + diamondsToCollect, 10, 40);
    }

    private void drawLivesText(Graphics g) {
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
            float originalJumpSpeed = jumpSpeed;
            jumpSpeed = -1.5f * SCALE;
            jump();
            jumpSpeed = originalJumpSpeed;
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
            // If player moving downwards
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed); // We can move to the left and right

            } else {
                // Going down and hit the floor
                if (airSpeed > 0) {
                    inAir = false;
                    airSpeed = 0;
                } else {
                    airSpeed = 0;
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

    private void updateXPos(float xSpeed) {

        // If player is on the ground and tile is not solid, moves
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
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
        this.getHit = getHit;
        if (playing.getGame().getLoggerState()) {
            logger.log(Level.INFO, "Player got hit");
        }

        if (playerAction != DEAD)
            if (enemyDirection == LEFT)
                hitbox.x -= 25;
            else
                hitbox.x += 25;
    }

    public void setDeath(boolean dead) {
        this.dead = dead;
        if (playing.getGame().getLoggerState())
            logger.log(Level.INFO, "Player died");
    }

    public void resetPlayer() {
        resetMovement();
        resetAniTick();
        dead = false;
        moving = false;
        attacking = false;
        getHit = false;
        playerAction = IDLE;
        lives = 3;
        inAir = false;
        airSpeed = 0;

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

    public void enemyDirection(int direction) {
        this.enemyDirection = direction;
    }

}
