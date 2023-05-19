package gamestates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utils.Size.*;

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
            case KeyEvent.VK_R:
                Gamestate.state = Gamestate.PLAYING;
                playing.resetGame();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
