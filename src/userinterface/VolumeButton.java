package userinterface;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.volumeButtons.*;


public class VolumeButton extends PauseButton{
    private BufferedImage[] images;
    private BufferedImage slider;
    private int index=0;
    private boolean mouseOver,mousePressed;
    private int buttonX,minX,maxX;
    private float floatValue=0f;
    public VolumeButton(int x, int y, int width, int height) {
        super(x+width/2, y, volume_width, height);
        bounds.x -=volume_width/2;
        buttonX=x+width/2;
        this.x=x;
        this.width=width;
        minX=x + volume_width/2;
        maxX=x+width - volume_width/2;
        loadImages();
    }
    private void loadImages() {
        BufferedImage temp = LoadSave.spriteAtlas(LoadSave.volume_buttons);
        images=new BufferedImage[2];
        for(int i=0;i<images.length;i++)
            images[i]=temp.getSubimage(i*volume_width_default,0,volume_width_default,volume_height_default);
        slider = temp.getSubimage(2*volume_width_default,0,slider_width_default,volume_height_default);
    }

    public void update(){
        index=0;
        if(mouseOver || mousePressed)
            index=1;

    }
    public void draw(Graphics graphics){
        graphics.drawImage(slider,x,y,width,height,null);
        graphics.drawImage(images[index],buttonX-volume_width/2,y,volume_width,height,null);
    }
    public void changeX(int x){
        if(x < minX)
            buttonX=minX;
        else if(x > maxX)
            buttonX=maxX;
        else
            buttonX=x;
        updateFloatValue();
        bounds.x=buttonX - volume_width/2;
    }

    private void updateFloatValue() {
        float range = maxX-minX;
        float value = buttonX-minX;
        floatValue = value/range;
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
    public float getFloatValue(){
        return floatValue;
    }
}
