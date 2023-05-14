package gameStates;

// We will not change the values
public enum Gamestate {
    PLAYING,
    MENU;

    // Always starts in MENU
    public static Gamestate state = MENU;
}
