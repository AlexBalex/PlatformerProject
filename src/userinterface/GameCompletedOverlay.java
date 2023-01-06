package userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

public class GameCompletedOverlay {
    private Playing playing;
    private BufferedImage img;
    private MenuButton quit;
    private int imgX, imgY, imgW, imgH;

    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        createImage();
        createButtons();
    }

    private void createButtons() {
        quit = new MenuButton(Game.game_width / 2, (int) (270 * Game.scale), 2, Gamestate.MENU);
    }

    private void createImage() {
        img = LoadSave.spriteAtlas(LoadSave.game_completed);
        imgW = (int) (img.getWidth() * Game.scale);
        imgH = (int) (img.getHeight() * Game.scale);
        imgX = Game.game_width / 2 - imgW / 2;
        imgY = (int) (100 * Game.scale);

    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.game_width, Game.game_height);
        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        quit.draw(g);
    }

    public void update() {
        quit.update();
    }

    private boolean isIn(MenuButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        quit.setMouseOver(false);

        if (isIn(quit, e))
            quit.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(quit, e)) {
            if (quit.isMousePressed()) {
                playing.resetAll();
                playing.resetGameCompleted();
                playing.setGamestate(Gamestate.MENU);

            }
        }

        quit.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(quit, e))
            quit.setMousePressed(true);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            playing.resetGameCompleted();
            playing.setGamestate(Gamestate.MENU);
        }
    }
}
