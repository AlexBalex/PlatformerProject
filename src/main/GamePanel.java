package main;

import Inputs.KeyboardInputs;
import Inputs.MouseInputs;

import javax.swing.*;

import java.awt.*;

import static java.awt.Color.blue;
import static main.Game.game_height;
import static main.Game.game_width;

public class GamePanel extends JPanel {
    private KeyboardInputs keyboardInputs;
    private MouseInputs mouseInputs;
    private Game game;



    public GamePanel(Game game) {

        this.game=game;
        keyboardInputs = new KeyboardInputs(this);
        mouseInputs = new MouseInputs(this);
        panelSize();
        addKeyListener(keyboardInputs);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

    }

    private void panelSize() {
        Dimension size = new Dimension(game_width,game_height);
        setPreferredSize(size);
    }

    public void updateGame(){

    }
    public void paint(Graphics graphics) {
             super.paint(graphics);
             graphics.setColor(blue);
             graphics.fillRect(0,0,game_width,game_height);
             game.render(graphics);
    }

    public Game getGame() {
        return game;
    }


}
