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
        Color backgroundColor = Color.BLACK;
        Color bigTextColor = Color.YELLOW;
        Color smallTextColor = Color.WHITE;
        Font bigFont = new Font("Arial", Font.BOLD, 50);
        Font smallFont = new Font("Arial", Font.PLAIN, 20);
        String bigMessage = "Congratulations! You Win!";
        String smallMessage = "Press ESC to return to the menu";

        // Set background color
        g.setColor(backgroundColor);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Set big text color and font
        g.setColor(bigTextColor);
        g.setFont(bigFont);

        // Calculate big text position
        FontMetrics bigFontMetrics = g.getFontMetrics();
        int bigMessageWidth = bigFontMetrics.stringWidth(bigMessage);
        int bigX = (GAME_WIDTH - bigMessageWidth) / 2;
        int bigY = GAME_HEIGHT / 2;

        // Draw the big text
        g.drawString(bigMessage, bigX, bigY);

        // Set small text color and font
        g.setColor(smallTextColor);
        g.setFont(smallFont);

        // Calculate small text position
        FontMetrics smallFontMetrics = g.getFontMetrics();
        int smallMessageWidth = smallFontMetrics.stringWidth(smallMessage);
        int smallX = (GAME_WIDTH - smallMessageWidth) / 2;
        int smallY = bigY + bigFontMetrics.getHeight() + 20;

        // Draw the small text
        g.drawString(smallMessage, smallX, smallY);
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
