package userinterface;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.ui.pauseButtons.*;

public class SoundButton extends PauseButton{
    private BufferedImage[][] soundImages;
    private boolean mouseOver,mousePressed;
    private boolean muted;
    private int rowIndex,columnIndex;
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImages();
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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    private void loadSoundImages() {
        BufferedImage temp= LoadSave.spriteAtlas(LoadSave.sound_buttons);
        soundImages=new BufferedImage[2][2];
        for(int j=0;j<soundImages.length;j++)
            for(int i=0;i<soundImages[j].length;i++)
                soundImages[j][i]=temp.getSubimage(i*sound_size_default,j*sound_size_default,sound_size_default,sound_size_default);
    }
    public void update(){
    if(muted)
        rowIndex=1;
    else
        rowIndex=0;
    columnIndex=0;
    if(mouseOver || mousePressed)
        columnIndex=1;
    }
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }
    public void draw(Graphics graphics){
        graphics.drawImage(soundImages[rowIndex][columnIndex],x,y,width,height,null);
    }
}
