package entities;

import gamestates.Playing;
import levels.LevelController;
import objects.Diamond;
import objects.Heart;
import objects.ObjectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.LoadSave;
import utils.Physics;
import utils.Size;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static utils.Size.SCALE;

public class PlayerTest {
    private Player player;
    @Mock
    private Playing playing;
    private Physics physics;
    private ObjectController objectController;
    private EnemyController enemyController;
    private Pig pig;

    // level 28x16
    private int[][] level = {
            {31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9},
            {9, 9, 9, 9, 9, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 31, 9, 9, 9, 31, 31, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 31, 31},
            {9, 31, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9},
            {9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1},
            {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 9, 9, 9, 9, 9, 9, 9, 9, 11, 11, 11, 11, 11}
    };

    public void movePlayer(float xCoordinate, float yCoordinate) {
        player.getHitBox().x = xCoordinate;
        player.getHitbox().y = yCoordinate;
    }

    /**
     * 10x updates players position
     */
    public void playerUpdate() {
        for (int i = 0; i < 10; i++) {
            player.updatePosition();
        }
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        player = new Player(33, 420, 100, 100, playing);
        pig = new Pig(200, 100);
        player.loadLvlData(level);

        when(playing.getObjectController()).thenReturn(objectController);
    }

    /**
     * Validates the initialization process of the Player class
     */
    @Test
    public void testPlayerInitialization() {
        assertEquals(33, player.getXPosition(), 0);
        assertEquals(420, player.getYPosition(), 0);

        assertFalse(player.isLeft());
        assertFalse(player.isRight());
        assertFalse(player.dead);
        assertFalse(player.isAttacking());
        assertFalse(player.isJump());
    }


    @Test
    public void testDiamondCollection() {
        Diamond diamond = new Diamond(50, 20);

        movePlayer(50, 20);

        assertTrue(diamond.playerCollectedDiamond(player.getHitbox()));
        assertTrue(diamond.isCollected);
    }

    @Test
    public void testHeartCollection() {
        Heart heart = new Heart(200, 328);

        movePlayer(200, 328);

        assertTrue(heart.playerCollectedHeart(player.getHitbox()));
        assertTrue(heart.isCollected);
    }

    @Test
    public void testPlayerMovementRight() {
        double expectedPosition = 15 + player.getXPosition();

        // Test for going right
        player.setRight(true);

        // Player is moved 10x 1.5 pixels to the right
        playerUpdate();

        assertEquals(expectedPosition, player.getXPosition());
    }

    @Test
    public void testPlayerMovementLeft() {
        double expectedPosition = player.getXPosition() - 15;

        // Test for going left
        player.setLeft(true);

        playerUpdate();

        assertEquals(expectedPosition, player.getXPosition());
    }

    @Test
    public void testPlayerJumping() {
        double initialYPosition = player.getYPosition();

        // Test for jumping
        player.setJump(true);

        playerUpdate();

        double finalYPosition = player.getYPosition();

        assertNotEquals(initialYPosition, finalYPosition);
        assertTrue(finalYPosition < initialYPosition);
    }


    @Test
    public void testPlayerFallToPit() {
        assertFalse(player.dead);

        // Moves player into a pit
        player.getHitbox().y = 500 * SCALE;
        player.checkOutOfBounds();

        assertTrue(player.dead);
    }

    /**
     * The TILE at 0 0 is solid
     */
    @Test
    public void testIsSolidTile() {
        movePlayer(0,0);
        player.setRight(true);
        playerUpdate();

        boolean canMoveHere = physics.CanMoveHere(player.getHitbox().x, player.getHitbox().y, player.width, player.height, level);
        assertFalse(canMoveHere);
    }

    @Test
    public void testEnemyInRange() {
        movePlayer(200, 100);

        boolean isInRange = pig.isPlayerInRange(player);
        assertTrue(isInRange);

        boolean isInAttackRange = pig.isPlayerInAttackRange(player);
        assertTrue(isInAttackRange);

        movePlayer(100, 40);
        isInRange = pig.isPlayerInAttackRange(player);
        assertFalse(isInRange);

        isInAttackRange = pig.isPlayerInAttackRange(player);
        assertFalse(isInAttackRange);

    }

    @Test
    public void testCanEnemySeePlayer() {
        movePlayer(200, 100);
        boolean canSee = pig.canSeePlayer(level, player);
        assertTrue(canSee);

        movePlayer(200, 150);
        canSee = pig.canSeePlayer(level, player);
        assertFalse(canSee);
    }
}

