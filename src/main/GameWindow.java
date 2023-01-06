package main;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame{

    public GameWindow(GamePanel gamePanel)
    {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            add(gamePanel);
            setResizable(false);
            pack();
            setVisible(true);
            setTitle("Gustav");
            Image icon=Toolkit.getDefaultToolkit().getImage("resources/Gustav.png");
            setIconImage(icon);
            setLocationRelativeTo(null);
            addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {

                }

                @Override
                public void windowLostFocus(WindowEvent e) {
                    gamePanel.getGame().windowFocusLost();
                }
            });
    }
}
