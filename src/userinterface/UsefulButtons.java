package userinterface;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.usefulButtons.*;

public class UsefulButtons extends PauseButton{
    private BufferedImage[] images;
    private int rowIndex,index;
    private boolean mouseOver,mousePressed;
    public UsefulButtons(int x, int y, int width, int height,int rowIndex) {
        super(x, y, width, height);
        this.rowIndex=rowIndex;
        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.spriteAtlas(LoadSave.useful_buttons);
        images = new BufferedImage[3];
        for(int i=0;i<images.length;i++)
            images[i]=temp.getSubimage(i*useful_size_default,rowIndex*useful_size_default,useful_size_default,useful_size_default);
    }

    public void update(){
        index =0;
        if(mouseOver || mousePressed)
            index =1;

    }
    public void draw(Graphics graphics){
        graphics.drawImage(images[index],x,y,useful_size,useful_size,null);
    }
    public void resetBools(){
        mouseOver=false;
        mousePressed=false;
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

}
