package userinterface;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.usefulButtons.useful_size;


public class GameOverOverlay {
    private Playing playing;
    private BufferedImage image;
    private int imageX,imageY,imageWidth,imageHeight;
    private UsefulButtons menu,replay;
    public GameOverOverlay(Playing playing){
        this.playing=playing;
        createImage();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.scale);
        int replayX = (int) (440 * Game.scale);
        int y = (int) (220 * Game.scale);
        replay = new UsefulButtons(replayX, y, useful_size, useful_size, 1);
        menu = new UsefulButtons(menuX, y, useful_size, useful_size, 2);
    }

    private void createImage() {
        image= LoadSave.spriteAtlas(LoadSave.death_screen);
        imageWidth= (int) (image.getWidth()*Game.scale);
        imageHeight= (int) (image.getHeight()*Game.scale);
        imageX=Game.game_width/2-imageWidth/2;
        imageY= (int) (100*Game.scale);
    }

    public void draw(Graphics graphics){
        graphics.setColor(new Color(0,0,0,200));
        graphics.fillRect(0,0, Game.game_width,Game.game_height);

        graphics.drawImage(image,imageX,imageY,imageWidth,imageHeight,null);

        menu.draw(graphics);
        replay.draw(graphics);
    }

    public void update(){
        menu.update();
        replay.update();
    }

    private boolean isIn(UsefulButtons b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        replay.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(replay, e))
            replay.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
            }

        } else if (isIn(replay, e)) {
            if (replay.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
        }
        menu.resetBools();
        replay.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(replay, e))
            replay.setMousePressed(true);
    }

    public void escPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            playing.resetAll();
            playing.setGamestate(Gamestate.MENU);
        }
    }

    public void enterPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            playing.resetAll();
            playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
        }
    }
}
