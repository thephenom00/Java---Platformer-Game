package saveload;

import entities.Pig;
import objects.Diamond;
import objects.Heart;

import java.util.ArrayList;

public class GameData {
    // PLAYER DATA
    float xPosition;
    float yPosition;
    int diamonds;
    int lives;

    // OBJECT DATA
    ArrayList<Diamond> diamondsArray;
    ArrayList<Heart> heartsArray;

    // ENEMY DATA
    ArrayList<Pig> pigsArray;
    int numberOfPigsAlive;
}
