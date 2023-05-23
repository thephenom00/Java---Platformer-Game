package gamestates;

/**
 * Values in enum are never changed
 * State is automatically set to MENU
 */
public enum Gamestate {
    PLAYING,
    MENU,
    GAMEOVER,
    WIN;

    public static Gamestate state = MENU;
}
