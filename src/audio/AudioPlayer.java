package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
        public static int menu1 = 0;
        public static int level1 = 1;
        public static int level2 = 2;

        public static int die = 0;
        public static int jump = 1;
        public static int gameover = 2;
        public static int levelcompleted = 3;
        public static int attack1 = 4;

        private Clip[] songs, effects;
        private int currentSongId;
        private float volume = 1f;
        private boolean songMute, effectMute;

        public AudioPlayer(){
                loadSongs();
                loadEffects();
                playSong(menu1);
        }
        private void loadSongs(){
                String[] names={"menu","level1","level2"};
                songs=new Clip[names.length];
                for(int i=0;i<songs.length;i++)
                        songs[i]=getClip(names[i]);
        }
        private void loadEffects(){
                String[] effectNames={"die", "jump", "gameover", "lvlcompleted", "attack"};
                effects=new Clip[effectNames.length];
                for(int i=0;i<effects.length;i++)
                        effects[i]=getClip(effectNames[i]);
                updateEffectsVolume();
        }
        private Clip getClip(String name){
                URL url = getClass().getResource("/audio/"+name+".wav");
                AudioInputStream audio;
                 try{
                         audio= AudioSystem.getAudioInputStream(url);
                         Clip clip = AudioSystem.getClip();
                         clip.open(audio);

                         return clip;

                 }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
                         e.printStackTrace();
                 }

                return null;
        }
        public void toggleSongMute(){
                this.songMute=!songMute;
                for(Clip clip: songs){
                        BooleanControl booleanControl=(BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
                        booleanControl.setValue(songMute);
                }
        }
        public void toggleEffectMute(){
                this.effectMute=!effectMute;
                for(Clip clip: effects){
                        BooleanControl booleanControl=(BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
                        booleanControl.setValue(effectMute);
                }
                if(!effectMute)
                        playEffect(jump);
        }

        public void setVolume(float volume){
                this.volume=volume;
                updateSongVolume();
                updateEffectsVolume();
        }
        public void stopSong(){
                if(songs[currentSongId].isActive())
                        songs[currentSongId].stop();
        }
        public void setLevelSong(int levelIndex){
                if(levelIndex%2==0)
                        playSong(level1);
                else
                        playSong(level2);
        }
        public void levelCompleted(){
                stopSong();
                playEffect(levelcompleted);
        }
        public void playEffect(int effect) {
                if (effects[effect].getMicrosecondPosition() > 0)
                        effects[effect].setMicrosecondPosition(0);
                effects[effect].start();
        }
        public void playSong(int song){
                stopSong();
                currentSongId=song;
                updateSongVolume();
                songs[currentSongId].setMicrosecondPosition(0);
                songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
        }

        private void updateSongVolume(){
                FloatControl gainControl = (FloatControl) (songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN));
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range*volume)+gainControl.getMinimum();
                gainControl.setValue(gain);
        }
        private void updateEffectsVolume(){
                for(Clip clip: effects) {
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float range = gainControl.getMaximum() - gainControl.getMinimum();
                        float gain = (range * volume) + gainControl.getMinimum();
                        gainControl.setValue(gain);
                }
        }
}
