package entities;

import gamestates.Playing;
import levels.LevelController;
import main.Game;
import objects.Diamond;
import objects.Heart;
import objects.ObjectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.LoadSave;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.ObjectConstants.DIAMOND;
import static utils.Size.SCALE;


public class PlayerTest {
    private Player player;
    private Playing playing;
    private Game game;
    private LoadSave loadSave;
    private LevelController levelController;
    private ObjectController objectController;

    public void movePlayer(float xCoordinate, float yCoordinate) {
        player.getHitBox().x = xCoordinate;
        player.getHitbox().y = yCoordinate;
    }

    public void playerUpdate() {
        for (int i = 0; i < 10; i++) {
            player.updatePosition();
        }
    }


    @BeforeEach
    public void setUp() {
        game = new Game(false);
        playing = new Playing(game);
        player = new Player(33 * SCALE,420 * SCALE,100,100, playing);
        loadSave = new LoadSave();
        levelController = new LevelController(game);
        objectController = new ObjectController(player);

        player.loadLvlData(levelController.getCurrentLevel().getLevelData());

    }

    @Test
    public void testPlayerInitialization() {
        assertEquals(0, player.getXPosition(), 0);
        assertEquals(0, player.getYPosition(), 0);
        assertEquals(3, player.getLives(), 0);
        assertEquals(0, player.getDiamondsToCollect());
        assertFalse(player.isLeft());
        assertFalse(player.isRight());
    }

    @Test
    public void testPlayerMovement() {
        // Test for going right
        player.setRight(true);
        playerUpdate();
        player.setRight(false);
        assertEquals(42, player.getHitBox().x);

        // Test for jump
        float currentYPosition = player.getYPosition();
        player.setJump(true);
        playerUpdate();
        assertTrue(player.getYPosition() < currentYPosition);
        player.setJump(false);
    }

    @Test
    public void testDiamondCollection() {
        Diamond diamond = new Diamond( 50,20);

        movePlayer(50, 20);

        assertTrue(diamond.playerCollectedDiamond(player.getHitbox()));
        assertTrue(diamond.isCollected);
    }

    @Test
    public void testHeartCollection() {
        Heart heart = new Heart( 200,328);

        movePlayer(200, 328);

        assertTrue(heart.playerCollectedHeart(player.getHitbox()));
        assertTrue(heart.isCollected);
    }

    @Test
    public void testPlayerResetAfterDeath() {
        movePlayer(200, 200);
        player.setLives(0);

        player.resetPlayer();

        assertEquals(33 * SCALE, player.getXPosition(), 0);
        assertEquals(420 * SCALE, player.getYPosition(), 0);
        assertEquals(3, player.getLives());
        assertFalse(player.isLeft());
        assertFalse(player.isRight());
    }


}
