package userinterface;


import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.buttons.*;

public class MenuButton {
    private int xPos,yPos,rowIndex,index;
    private int xOffsetCenter = button_width/2;
    private Gamestate state;
    private BufferedImage[] images;
    private boolean mouseOver,mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos,int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        bounds();
    }

    private void bounds() {
        bounds = new Rectangle(xPos-xOffsetCenter,yPos,button_width,button_height);
    }

    private void loadImages(){
        images=new BufferedImage[2];
        BufferedImage temp = LoadSave.spriteAtlas(LoadSave.menu_buttons);
        for(int i=0;i<images.length;i++)
           images[i] = temp.getSubimage(i*button_width_default,rowIndex*button_height_default,button_width_default,button_height_default);
    }
    public void draw(Graphics graphics){
        graphics.drawImage(images[index],xPos - xOffsetCenter,yPos,button_width,button_height,null);
    }
    public void update(){
        index = 0;
        if(mouseOver || mousePressed)
            index =1;
        //if(mousePressed)
            //index=2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
    public Rectangle getBounds(){
        return bounds;
    }
    public void applyGamestate(){
        Gamestate.state=state;
    }
    public void resetBooleans(){
        mouseOver=false;
        mousePressed=false;
    }
    public Gamestate getState(){
        return state;
    }
}
