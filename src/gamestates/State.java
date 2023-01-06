package gamestates;

import audio.AudioPlayer;
import main.Game;
import userinterface.MenuButton;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public boolean isIn(MouseEvent e, MenuButton menuButton) {
        return menuButton.getBounds().contains(e.getX(),e.getY());
    }

    public State(Game game){
        this.game=game;

    }
    public Game getGame(){
        return game;
    }
    public void setGamestate(Gamestate state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.menu1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }
        Gamestate.state=state;
    }
}
