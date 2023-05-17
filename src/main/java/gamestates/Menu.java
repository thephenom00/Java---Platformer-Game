package gamestates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utilz.Size.GAME_WIDTH;

public class Menu extends State implements StateInterface {
    private Rectangle startButton;
    private Rectangle loadButton;
    private Rectangle saveButton;
    private Rectangle quitButton;

    public Menu(Game game) {
        super(game);
        int buttonWidth = 200;
        int buttonHeight = 80;
        int buttonSpacing = 20;
        int buttonX = GAME_WIDTH / 2 - buttonWidth / 2;
        int startY = 200;
        startButton = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);
        loadButton = new Rectangle(buttonX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        saveButton = new Rectangle(buttonX, startY + (buttonHeight + buttonSpacing) * 2, buttonWidth, buttonHeight);
        quitButton = new Rectangle(buttonX, startY + (buttonHeight + buttonSpacing) * 3, buttonWidth, buttonHeight);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw start button
        drawButton(g, startButton, "START GAME");

        // Draw load button
        drawButton(g, loadButton, "LOAD");

        // Draw save button
        drawButton(g, saveButton, "SAVE");

        // Draw quit button
        drawButton(g, quitButton, "QUIT");
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
        // RADas
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (startButton.contains(e.getX(), e.getY())) {
            Gamestate.state = Gamestate.PLAYING;
            System.out.println("Switched to PLAYING state");

        } else if (loadButton.contains(e.getX(), e.getY())) {
            game.getPlaying().saveLoadGame.load();
            System.out.println("Game Loaded");

        } else if (saveButton.contains(e.getX(), e.getY())) {
            game.getPlaying().saveLoadGame.save();
            System.out.println("Game Saved");

        } else if (quitButton.contains(e.getX(), e.getY())) {
            System.exit(0);
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                Gamestate.state = Gamestate.PLAYING;
                System.out.println("Switched to PLAYING state");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
