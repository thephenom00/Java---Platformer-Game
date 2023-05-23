package gamestates;

import main.Game;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.Size.GAME_WIDTH;
import static utils.Size.SCALE;

public class Menu extends State {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private Rectangle startButton;
    private Rectangle loadButton;
    private Rectangle saveButton;
    private Rectangle quitButton;

    private Image logoImage;
    private boolean gameSaved = false;
    public Menu(Game game) {
        super(game);
        initClasses();
    }

    public void initClasses() {
        int buttonWidth = (int) (133 * SCALE);
        int buttonHeight = (int) (53 * SCALE);
        int buttonSpacing = (int) (13 * SCALE);
        int buttonX = GAME_WIDTH / 2 - buttonWidth / 2;
        int startY = (int) (213 * SCALE);
        int logoSize = (int) (128 * SCALE);

        startButton = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);
        loadButton = new Rectangle(buttonX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        saveButton = new Rectangle(buttonX, startY + (buttonHeight + buttonSpacing) * 2, buttonWidth, buttonHeight);
        quitButton = new Rectangle(buttonX, startY + (buttonHeight + buttonSpacing) * 3, buttonWidth, buttonHeight);
        logoImage = Toolkit.getDefaultToolkit().getImage("src/main/java/res/logo.png");
        logoImage = logoImage.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);
    }


    public void draw(Graphics g) {
        int logoX = GAME_WIDTH / 2 - (logoImage.getWidth(null) / 2);
        int logoY = 100;
        g.drawImage(logoImage, logoX, logoY, null);

        drawButton(g, startButton, "PLAY");

        drawButton(g, loadButton, "LOAD");

        drawButton(g, saveButton, "SAVE");

        drawButton(g, quitButton, "QUIT");

        if (gameSaved) {
            drawGameSavedText(g);
        }
    }

    private void drawGameSavedText(Graphics g) {
        Font savedFont = new Font("Arial", Font.BOLD, 32);
        g.setFont(savedFont);
        g.setColor(Color.BLACK);
        String savedText = "GAME SAVED";
        int savedTextX = GAME_WIDTH / 2 - g.getFontMetrics(savedFont).stringWidth(savedText) / 2;
        int savedTextY = 75;
        g.drawString(savedText, savedTextX, savedTextY);
    }

    private void drawButton(Graphics g, Rectangle button, String text) {
        Font buttonFont = new Font("Arial", Font.BOLD, 32);
        g.setFont(buttonFont);
        g.setColor(new Color(47, 79, 61));
        g.fillRoundRect(button.x, button.y, button.width, button.height, 10, 10);
        g.setColor(Color.white);
        g.drawRoundRect(button.x, button.y, button.width, button.height, 10, 10);
        FontMetrics fm = g.getFontMetrics(buttonFont);
        int buttonTextX = button.x + (button.width - fm.stringWidth(text)) / 2;
        int buttonTextY = button.y + (button.height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, buttonTextX, buttonTextY);
    }

    public void mouseClicked(MouseEvent e) {
        if (startButton.contains(e.getX(), e.getY())) {
            changeState(Gamestate.PLAYING, false);
            gameSaved = false;
            if (game.getLoggerState())
                logger.log(Level.INFO,"Switched to PLAYING state");

        } else if (loadButton.contains(e.getX(), e.getY())) {
            game.getPlaying().saveLoadGame.load();
            changeState(Gamestate.PLAYING, false);
            gameSaved = false;
            if (game.getLoggerState())
                logger.log(Level.INFO,"GAME LOADED");

        } else if (saveButton.contains(e.getX(), e.getY())) {
            game.getPlaying().saveLoadGame.save();
            gameSaved = true;
            if (game.getLoggerState())
                logger.log(Level.INFO,"GAME SAVED");

        } else if (quitButton.contains(e.getX(), e.getY())) {
            System.exit(0);
            gameSaved = false;
            if (game.getLoggerState())
                logger.log(Level.INFO,"QUIT GAME");
        }
    }


    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                changeState(Gamestate.PLAYING, false);
                if (game.getLoggerState())
                    logger.log(Level.INFO,"Switched to PLAYING state");
                gameSaved = false;
        }
    }

}
