package gamestates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import static utilz.Size.*;

public class YouWin extends State implements StateInterface{
    private Playing playing;
    public YouWin(Game game, Playing playing) {
        super(game);
        this.playing = playing;
    }

    @Override
    public void update() {

    }


    @Override
    public void draw(Graphics g) {
        // Clear the screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Draw the winning message
        Color winningColor = Color.GREEN; // Set your desired winning color here
        g.setColor(winningColor);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String message = "Congratulations! You Win!";
        int messageWidth = g.getFontMetrics().stringWidth(message);
        int x = (GAME_WIDTH - messageWidth) / 2;
        int y = GAME_HEIGHT / 2;
        g.drawString(message, x, y);
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Gamestate.state = Gamestate.MENU;
            playing.resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
