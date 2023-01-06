package userinterface;

import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utils.Constants.ui.pauseButtons.sound_size;
import static utils.Constants.ui.volumeButtons.slider_width;
import static utils.Constants.ui.volumeButtons.volume_height;

public class AudioOptions {
    private VolumeButton volumeButton;
    private SoundButton musicButton,sfxButton;
    private Game game;
    public AudioOptions(Game game){
        this.game=game;
        createSoundButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int volumeX=(int)(309* Game.scale);
        int volumeY=(int)(278*Game.scale);
        volumeButton=new VolumeButton(volumeX,volumeY,slider_width,volume_height);
    }
    private void createSoundButtons() {
        int soundX=(int)(450 * Game.scale);
        int musicY=(int)(140 * Game.scale);
        int sfxY = (int) (186 * Game.scale);
        musicButton=new SoundButton(soundX,musicY,sound_size,sound_size);
        sfxButton=new SoundButton(soundX,sfxY,sound_size,sound_size);
    }
    public void update(){
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }
    public void draw(Graphics graphics){
        //sound buttons
        musicButton.draw(graphics);
        sfxButton.draw(graphics);
        //volume slider
        volumeButton.draw(graphics);
    }
    public void mouseDragged(MouseEvent e){
        if(volumeButton.isMousePressed()){
            float valueBefore=volumeButton.getFloatValue();
            volumeButton.changeX(e.getX());
            float valueAfter=volumeButton.getFloatValue();
            if(valueBefore!=valueAfter)
                game.getAudioPlayer().setVolume(valueAfter);
        }
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e,musicButton))
            musicButton.setMousePressed(true);
        else if(isIn(e,sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e,musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        } else if(isIn(e,sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }

        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);
        if(isIn(e,musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e,sfxButton))
            sfxButton.setMouseOver(true);
        else if(isIn(e,volumeButton))
            volumeButton.setMouseOver(true);


    }
    private boolean isIn(MouseEvent e,PauseButton p){
        return p.getBounds().contains(e.getX(), e.getY());
    }
}

