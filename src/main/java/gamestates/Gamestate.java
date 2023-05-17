package gamestates;

// We will not change the values
public enum Gamestate {
    PLAYING,
    MENU,
    GAMEOVER,
    WIN;
    // Always starts in MENU
    public static Gamestate state = MENU;
}
