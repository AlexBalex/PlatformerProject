package userinterface;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.usefulButtons.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UsefulButtons menu, next;
    private BufferedImage image;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        Img();
        Buttons();
    }

    private void Buttons() {
        int menuX = (int) (330 * Game.scale);
        int nextX = (int) (445 * Game.scale);
        int y = (int) (195 * Game.scale);
        next = new UsefulButtons(nextX, y, useful_size, useful_size, 0);
        menu = new UsefulButtons(menuX, y, useful_size, useful_size, 2);
    }

    private void Img() {
        image = LoadSave.spriteAtlas(LoadSave.complete_image);
        backgroundWidth = (int) (image.getWidth() * Game.scale);
        backgroundHeight = (int) (image.getHeight() * Game.scale);
        backgroundX = Game.game_width / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.scale);
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        next.draw(graphics);
        menu.draw(graphics);
    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean isIn(UsefulButtons b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
            }

        } else if (isIn(next, e)) {
            if (next.isMousePressed()) {
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
        }
        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }

    public void enterPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            playing.loadNextLevel();
            playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
        }
    }

    public void escPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            playing.setGamestate(Gamestate.MENU);
        }
    }
}
