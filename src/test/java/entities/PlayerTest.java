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
    @Mock
    private Physics physics;
    private LoadSave loadSave;
    @Mock
    private ObjectController objectController;
    @Mock
    private LevelController levelController;
    private Size size;
    private int[][] level = {{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9}, {9, 9, 9, 9, 9, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 31, 9, 9, 9, 31, 31, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 31, 31}, {9, 31, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9}, {9, 9, 9, 9, 9, 9, 31, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 31, 31, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1}, {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 9, 9, 9, 9, 9, 9, 9, 9, 11, 11, 11, 11, 11}};

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
        MockitoAnnotations.openMocks(this);

        player = new Player(33, 420, 100, 100, playing);
        player.loadLvlData(level);
        loadSave = new LoadSave();
        size = new Size();

        size.setScale(1f); // Call the setScale() method to set SCALE to 1.0f

        when(playing.getObjectController()).thenReturn(objectController);

    }

    @Test
    public void testPlayerInitialization() {
        assertEquals(33, player.getXPosition(), 0);
        assertEquals(420, player.getYPosition(), 0);
        assertFalse(player.isLeft());
        assertFalse(player.isRight());
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
        double expectedPosition = 10 * SCALE + player.getXPosition();
        // Test for going left
        player.setRight(true);
        playerUpdate();
        player.setLeft(false);
        assertEquals(expectedPosition, player.getXPosition());
    }

    @Test
    public void testPlayerMovementLeft() {
        double expectedPosition = player.getXPosition() - 10 * SCALE;
        // Test for going left
        player.setLeft(true);
        playerUpdate();
        player.setRight(false);
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
        player.getHitbox().y = 500 * SCALE;
        player.checkOutOfBounds();
        assertTrue(player.dead);
    }
}
