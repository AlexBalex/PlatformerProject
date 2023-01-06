package userinterface;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.pauseButtons.*;
import static utils.Constants.ui.usefulButtons.*;
import static utils.Constants.ui.volumeButtons.*;
public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImage;
    private int backgroundX,backgroundY,backgroundWidth,backgroundHeight;
    private AudioOptions audioOptions;
    private UsefulButtons menuButton,replayButton,unpauseButton;

    public PauseOverlay(Playing playing){
        this.playing=playing;
        loadBackground();
        audioOptions=playing.getGame().getAudioOptions();
        createUsefulButtons();

    }


    private void createUsefulButtons() {
        int menuX=(int)(313*Game.scale);
        int replayX=(int)(387*Game.scale);
        int unpauseX=(int)(462*Game.scale);
        int buttonY=(int)(325*Game.scale);

        unpauseButton=new UsefulButtons(unpauseX,buttonY,useful_size,useful_size,0);
        replayButton=new UsefulButtons(replayX,buttonY,useful_size,useful_size,1);
        menuButton=new UsefulButtons(menuX,buttonY,useful_size,useful_size,2);
    }




    private void loadBackground() {
        backgroundImage= LoadSave.spriteAtlas(LoadSave.pause_menu);
        backgroundWidth=(int)(backgroundImage.getWidth()* Game.scale);
        backgroundHeight=(int)(backgroundImage.getHeight()* Game.scale);
        backgroundX=Game.game_width/2 - backgroundWidth/2;
        backgroundY=(int)(25*Game.scale);
    }

    public void update(){
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        audioOptions.update();
    }

    public void draw(Graphics graphics){
        //background
        graphics.drawImage(backgroundImage,backgroundX,backgroundY,backgroundWidth,backgroundHeight,null);
        //useful buttons
        menuButton.draw(graphics);
        replayButton.draw(graphics);
        unpauseButton.draw(graphics);
        audioOptions.draw(graphics);
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e,menuButton)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
                playing.unpauseGame();
            }
        }
        else if(isIn(e,replayButton)) {
            if (replayButton.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }

        }
        else if(isIn(e,unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        }
        else
            audioOptions.mouseReleased(e);


        menuButton.resetBools();
        replayButton.resetBools();
        unpauseButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        if(isIn(e,menuButton))
            menuButton.setMouseOver(true);
        else if(isIn(e,replayButton))
            replayButton.setMouseOver(true);
        else if(isIn(e,unpauseButton))
            unpauseButton.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            playing.resetAll();
            playing.unpauseGame();
            playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
        }
    }

    private boolean isIn(MouseEvent e,PauseButton p){
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
