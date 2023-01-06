package gamestates;

import main.Game;
import userinterface.AudioOptions;
import userinterface.PauseButton;
import userinterface.UsefulButtons;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constants.ui.usefulButtons.*;

public class GameOptions extends State implements Statemethods{

    private AudioOptions audioOptions;
    private BufferedImage backgroundImage,optionsBackgroundImage;
    private int backgroundX,backgroundY,backgroundWidth,backgroundHeight;
    private UsefulButtons menuButton;

    public GameOptions(Game game) {
        super(game);
        loadImages();
        loadButton();
        audioOptions=game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int) (387 * Game.scale);
        int menuY = (int) (325 * Game.scale);
        menuButton=new UsefulButtons(menuX,menuY,useful_size,useful_size,2);
    }

    private void loadImages() {
        backgroundImage= LoadSave.spriteAtlas(LoadSave.menu_background_image);
        optionsBackgroundImage=LoadSave.spriteAtlas(LoadSave.options_menu);

        backgroundWidth = (int) (optionsBackgroundImage.getWidth() * Game.scale);
        backgroundHeight = (int) (optionsBackgroundImage.getHeight() * Game.scale);
        backgroundX = Game.game_width / 2 - backgroundWidth / 2;
        backgroundY = (int) (25 * Game.scale);
    }

    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(backgroundImage,0,0,Game.game_width,Game.game_height,null);
        graphics.drawImage(optionsBackgroundImage,backgroundX,backgroundY,backgroundWidth,backgroundHeight,null);

        menuButton.draw(graphics);
        audioOptions.draw(graphics);

    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e,menuButton)){
            menuButton.setMousePressed(true);
        }else
            audioOptions.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e,menuButton)){
            if(menuButton.isMousePressed())
                Gamestate.state=Gamestate.MENU;
        }else
            audioOptions.mouseReleased(e);

        menuButton.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);

        if(isIn(e,menuButton))
            menuButton.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state=Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    private boolean isIn(MouseEvent e, PauseButton p){
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
