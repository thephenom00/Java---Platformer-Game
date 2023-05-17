package gamestates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utilz.Size.*;

public class GameOver extends State implements StateInterface{
    private Game game;
    private Playing playing;
    public GameOver(Game game, Playing playing) {
        super(game);
        this.game = game;
        this.playing = playing;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String gameOverText = "GAME OVER!";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        int x = (GAME_WIDTH - textWidth) / 2;
        int y = GAME_HEIGHT / 2;
        g.drawString(gameOverText, x, y);


        String pressEscText = "Press ESC to get to MENU";
        int pressEscTextWidth = g.getFontMetrics().stringWidth(pressEscText);
        int x2 = (GAME_WIDTH - pressEscTextWidth) / 2;
        int y2 = y + 50; // Adjust the vertical position as needed
        g.drawString(pressEscText, x2, y2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
                Gamestate.state = Gamestate.MENU;
                playing.resetGame();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
