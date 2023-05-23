package gamestates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utils.Size.*;

/**
 * GameOver will be displayed when player will lose all his lives
 */
public class GameOver extends State {
    private Playing playing;
    public GameOver(Game game, Playing playing) {
        super(game);
        this.playing = playing;
    }


    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        String gameOverText = "GAME OVER!";
        g.setFont(new Font("Arial", Font.BOLD, 60));
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        int x = (GAME_WIDTH - textWidth) / 2;
        int y = GAME_HEIGHT / 2 - 50;
        g.setColor(Color.WHITE);
        g.drawString(gameOverText, x, y);

        String pressEscText = "Press ESC to go to MENU";
        int x2 = (GAME_WIDTH - textWidth) / 2;
        int y2 = y + 80;
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(pressEscText, x2, y2);

        String restartText = "Press R to restart the game";
        int x3 = (GAME_WIDTH - textWidth) / 2;
        int y3 = y2 + 50;
        g.drawString(restartText, x3, y3);
    }



    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                changeState(Gamestate.MENU, true);
                playing.resetGame();
                break;

            case KeyEvent.VK_R:
                playing.resetGame();
                changeState(Gamestate.PLAYING, true);
                break;
        }
    }
}
