package Inputs;

import gamestates.Gamestate;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel gamePanel)
    {
        this.gamePanel=gamePanel;
    }
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state){
        case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
        case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
        case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state){
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
        }
    }
}
