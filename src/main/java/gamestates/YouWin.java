package gamestates;

import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import static utils.Size.*;

public class YouWin extends State{
    private Playing playing;
    public YouWin(Game game, Playing playing) {
        super(game);
        this.playing = playing;
    }



    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        Color backgroundColor = Color.BLACK;
        Color bigTextColor = Color.YELLOW;
        Color smallTextColor = Color.WHITE;
        Font bigFont = new Font("Arial", Font.BOLD, 50);
        Font smallFont = new Font("Arial", Font.PLAIN, 20);
        String bigMessage = "Congratulations! You Win!";
        String smallMessage = "Press ESC to return to the menu";

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

        g.drawString(smallMessage, smallX, smallY);
    }



    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            changeState(Gamestate.MENU, true);
            playing.resetGame();
        }
    }

}
