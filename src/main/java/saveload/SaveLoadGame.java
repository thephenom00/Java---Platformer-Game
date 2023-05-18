package saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gamestates.Playing;
import objects.Diamond;
import objects.Heart;

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
        try (FileWriter writer = new FileWriter("src/main/java/utils/save.json")) {
            GameData data = new GameData();
            data.xPosition = playing.getPlayer().getXPosition();
            data.yPosition = playing.getPlayer().getYPosition();

            data.lives = playing.getPlayer().getLives();
            data.diamonds = playing.getObjectManager().getNumberOfDiamondsToTake();

            data.diamondsArray = playing.getObjectManager().getDiamonds();
            data.heartsArray = playing.getObjectManager().getHearts();

            data.pigsArray = playing.getEnemyManager().getPigs();

            String json = gson.toJson(data);
            writer.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void load() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/java/utils/save.json")));
            GameData data = gson.fromJson(json, GameData.class);


            // Set the player position
            playing.getPlayer().getHitBox().x = data.xPosition;
            playing.getPlayer().getHitBox().y = data.yPosition;

            playing.getPlayer().setLives(data.lives);
            playing.getObjectManager().setNumberOfDiamondsToTake(data.diamonds);

            playing.getObjectManager().setDiamonds(new ArrayList<>(data.diamondsArray));
            playing.getObjectManager().setHearts(new ArrayList<>(data.heartsArray));

            playing.getEnemyManager().setPigs(new ArrayList<>(data.pigsArray));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
