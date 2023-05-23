package utils;

public class Size {
    public final static int TILES_DEFAULT_SIZE = 32; // pixels of 1 tile
    public static float SCALE = 1f;
    public final static int TILES_IN_WIDTH = 28;
    public final static int TILES_IN_HEIGHT = 16;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public void setScale() {
        SCALE = 1.0f;
    }
}
