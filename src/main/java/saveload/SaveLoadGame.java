package saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gamestates.Playing;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SaveLoadGame implements Serializable {
    Playing playing;
    Gson gson;

    public SaveLoadGame(Playing playing) {
        this.playing = playing;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save() {
        try (FileWriter writer = new FileWriter("src/main/java/res/save.json")) {
            GameData data = new GameData();
            data.xPosition = playing.getPlayer().getXPosition();
            data.yPosition = playing.getPlayer().getYPosition();

            data.lives = playing.getPlayer().getLives();
            data.diamonds = playing.getObjectController().getNumberOfDiamondsToTake();

            data.diamondsArray = playing.getObjectController().getDiamonds();
            data.heartsArray = playing.getObjectController().getHearts();

            data.pigsArray = playing.getEnemyController().getPigs();
            data.numberOfPigsAlive = playing.getEnemyController().getNumberOfPigsAlive();

            String json = gson.toJson(data);
            writer.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void load() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/java/res/save.json")));
            GameData data = gson.fromJson(json, GameData.class);


            // Set the player position
            playing.getPlayer().getHitBox().x = data.xPosition;
            playing.getPlayer().getHitBox().y = data.yPosition;

            playing.getPlayer().setLives(data.lives);
            playing.getObjectController().setNumberOfDiamondsToTake(data.diamonds);

            playing.getObjectController().setDiamonds(new ArrayList<>(data.diamondsArray));
            playing.getObjectController().setHearts(new ArrayList<>(data.heartsArray));

            playing.getEnemyController().setPigs(new ArrayList<>(data.pigsArray));
            playing.getEnemyController().setNumberOfPigsAlive(data.numberOfPigsAlive);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
