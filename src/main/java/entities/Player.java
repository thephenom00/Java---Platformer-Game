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

/**
 * Handles player movement, animation, collision detection
 */
public class Player extends Entity implements Serializable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    // Animation
    private BufferedImage[][] animations;
    private int aniTick;
    private int aniIndex;
    private int aniSpeed = 25;

    // Player Actions
    private int playerAction = IDLE;
    private boolean running = false;
    private boolean attacking = false;
    private boolean getHit = false;
    public boolean dead = false;

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

    /**
     * @param x        x-coordinate of the player
     * @param y        y-coordinate of the player
     * @param width    width of the players hitbox
     * @param height   height of the players hitbox
     * @param playing  playing state
     */
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadPlayerSprites();
        createHitbox(x, y,16 * SCALE, 26 * SCALE);
        createJumpBox(x, y, 16 * SCALE, 1 * SCALE);
    }

    /**
     * Updates player position and animation, checks collisions with tiles
     */
    public void update() {
        updatePosition();
        updateAnimationTick();
        updateJumpBox();
        setAnimation();

        // Checking
        checkMethods();
    }


    /**
     * Draws player, coins and hearts on the canvas
     * @param g
     */
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

    public void createJumpBox(float x, float y, float width, float height) {
        jumpBox = new Rectangle2D.Float(x, y, width, height);
    }

    private void updateJumpBox() {
        jumpBox.x = hitbox.x;
        jumpBox.y = hitbox.y + hitbox.height;
    }

    public void drawJumpBox(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect((int) jumpBox.x, (int) jumpBox.y, (int) jumpBox.width, (int) jumpBox.height);
    }

    private void checkMethods(){
        checkJumpOnHead();
        checkDiamondCollected();
        checkHeartCollected();
        checkTouchingEnemy();
        checkOutOfBounds();
        checkIfPlayerInAir();
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

    /**
     * If player is in the air, he falls
     */
    private void checkIfPlayerInAir() {
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    /**
     * If player falls in a hole, dead variable is set to true
     */
    private void checkOutOfBounds() {
        if (hitbox.y >= 483 * SCALE) {
            dead = true;
        }
    }

    /**
     * If player touches the enemy, he cant move
     */
    private void checkTouchingEnemy() {
        if (playing.checkPlayerTouchesEnemy(getHitbox()) == true){
            if(left) {
                left = false;
            } else if (right) {
                right = false;
            }
        }

    }

    /**
     * Sets the animation according to the situation
     * Resets aniTick, after getting all displayed
     */
    public void setAnimation(){
        int firstAnimation = playerAction;

        if (running) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
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

        if (dead) {
            playerAction = DEAD;
            left = false;
            right = false;
            if (aniIndex == 3) {
                playing.changeState(Gamestate.GAMEOVER, true);
                if (playing.getGame().getLoggerState())
                    logger.log(Level.INFO, "GAME OVER");
            }
        }

        if (firstAnimation != playerAction) {
            resetAniTick();
        }

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Handles player position according to his movement in the air or not
     */
    public void updatePosition(){
        running = false;

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


        // Player is on the ground
        if (!inAir) {
            if (!left && !right || left && right) {
                return;  // No movement, so we exit the method
            }

            moveHorizontally();

            running = true;

        // Player jumped
        } else {
            // Player is in the air
            moveHorizontally();

            // Check if player can move up
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
            } else {
                // Player reached the ground
                inAir = false;
                airSpeed = 0;
            }
        }
    }

    private void moveHorizontally() {
        float xSpeed = 0;

        if (left) {
            xSpeed = -playerSpeed;
            mirrorX = width;
            mirrorWidth = -1;
            facingRight = false;
        }

        if (right) {
            xSpeed = playerSpeed;
            mirrorX = 0;
            mirrorWidth = 1;
            facingRight = true;
        }


        // Check if player can move left and right
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }
    }

    /**
     * Makes the player jump by changing players y speed.
     */
    private void jump() {

        // If we are in the air, we cannot jump
        if(inAir && !jumpOnHead) {
            return;
        }

        inAir = true;
        airSpeed = jumpSpeed;
    }

    /**
     * Handles all the animations and resets after finishing
     */
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            // If all the sprites were displayed - reset, also end the hit and attack animation
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
                getHit = false;
            }
        }
    }

    /**
     * Loads all sprites from the img
     */
    private void loadPlayerSprites() {
        animations = new BufferedImage[8][11];

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        // Iterates through every single sprite
        for (int j = 0; j < animations.length; j++) {
            for(int i = 0 ; i < animations[j].length; i++){
                animations[j][i] = img.getSubimage(i*78, j*58,78,58);
            }
        }

    }

    public void loadLvlData(int[][]lvlData){
        this.lvlData = lvlData;
    }

    public void resetMovement() {
        left = false;
        right = false;
        jump = false;
    }

    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Handles the knockback to a player
     * @param getHit boolean, if player gets hit or not
     */
    public void getHit(boolean getHit) {
        this.getHit = getHit;
        jump = false;

        if (playing.getGame().getLoggerState()) {
            logger.log(Level.INFO, "Player got hit");
        }

        // Knock-back
        if (!inAir) {
            if (enemyDirection == LEFT) {
                hitbox.x -= 25;
            }

            if (enemyDirection == RIGHT) {
                hitbox.x += 25;
            }

            if (enemyDirection == LEFT && hitbox.x - 25 < 0) {
                hitbox.x = 0;
            }

            if (enemyDirection == RIGHT && hitbox.x + 25 > GAME_WIDTH) {
                hitbox.x = GAME_WIDTH - hitbox.width;
            }
        }


    }

    /**
     * After winning the game, whole player has to be updated
     */
    public void resetPlayer() {
        resetMovement();
        resetAniTick();
        dead = false;
        running = false;
        attacking = false;
        getHit = false;
        playerAction = IDLE;
        lives = 3;
        inAir = false;
        airSpeed = 0;

        hitbox.x = x;
        hitbox.y = y;

        checkIfPlayerInAir();
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isLeft() {
        return left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isRight() {
        return right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public Rectangle2D.Float getHitBox() {
        return hitbox;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public void addLife() {
        lives++;
    }

    public void subtractLife() {
        lives--;
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

    public float getXPosition() {
        return hitbox.x;
    }

    public float getYPosition() {
        return hitbox.y;
    }

    public void enemyDirection(int direction) {
        this.enemyDirection = direction;
    }

    private void testing() {
        this.playerSpeed = 2f;
        this.jumpSpeed = -4.0f;
        this.lives = 400;
    }
}
