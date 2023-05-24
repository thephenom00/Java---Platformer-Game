package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import javax.swing.*;
import java.awt.*;

import static utils.Size.*;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;


    public GamePanel(Game game){
        mouseInputs = new MouseInputs(this);
        this.game = game;

        setBackground(Color.lightGray);
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    // Sets the size of the panel
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g);
    }

    public Game getGame() {
        return game;
    }
}

