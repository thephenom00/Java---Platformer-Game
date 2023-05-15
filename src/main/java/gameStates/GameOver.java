package gameStates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utilz.Size.*;

public class GameOver extends State implements StateInterface{
    private Game game;
    public GameOver(Game game) {
        super(game);
        this.game = game;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

//        g.setColor(Color.white);
//        g.drawString("Game Over", GAME_WIDTH / 2, 150);
//        g.drawString("Press esc to enter Main Menu!", GAME_WIDTH / 2, 300);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String gameOverText = "Game Over!";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        int x = (GAME_WIDTH - textWidth) / 2;
        int y = GAME_HEIGHT / 2;
        g.drawString(gameOverText, x, y);
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
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
