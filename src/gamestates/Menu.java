package gamestates;

import main.Game;
import userinterface.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons=new MenuButton[3];
    private BufferedImage backgroundImage,backgroundImagePicture;
    private int menuX,menuY,menuWidth,menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        backgroundImagePicture=LoadSave.spriteAtlas(LoadSave.menu_background_image);
    }

    private void loadBackground() {
        backgroundImage= LoadSave.spriteAtlas(LoadSave.menu_background);
        menuWidth=(int)(backgroundImage.getWidth()*Game.scale);
        menuHeight=(int)(backgroundImage.getHeight()*Game.scale);
        menuX=Game.game_width/2 - menuWidth/2;
        menuY=(int)(45*Game.scale);
    }

    private void loadButtons() {
        buttons[0]=new MenuButton(Game.game_width/2,(int) (150*Game.scale),0,Gamestate.PLAYING);
        buttons[1]=new MenuButton(Game.game_width/2,(int) (220*Game.scale),1,Gamestate.OPTIONS);
        buttons[2]=new MenuButton(Game.game_width/2,(int) (290*Game.scale),2,Gamestate.QUIT);
    }

    @Override
    public void update() {
        for(MenuButton menuButton : buttons)
            menuButton.update();
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImagePicture,0,0,Game.game_width,Game.game_height,null);
        graphics.drawImage(backgroundImage,menuX,menuY,menuWidth,menuHeight,null);
        for(MenuButton menuButton : buttons)
            menuButton.draw(graphics);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mouseButton : buttons){
            if(isIn(e,mouseButton))
                mouseButton.setMousePressed(true);
        }
    }

    private void resetButtons() {
        for(MenuButton menuButton : buttons){
            menuButton.resetBooleans();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton menuButton : buttons) {
            if(isIn(e,menuButton)){
                if(menuButton.isMousePressed())
                    menuButton.applyGamestate();
                if(menuButton.getState()==Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mouseButton : buttons)
            mouseButton.setMouseOver(false);
        for(MenuButton mouseButton : buttons)
            if(isIn(e,mouseButton)){
                mouseButton.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_ENTER)
            Gamestate.state=Gamestate.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
