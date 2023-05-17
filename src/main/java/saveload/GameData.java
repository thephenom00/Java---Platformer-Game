package saveload;

import entities.Pig;
import objects.Diamond;
import objects.Heart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameData implements Serializable {
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
}
