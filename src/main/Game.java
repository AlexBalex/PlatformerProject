package main;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import userinterface.AudioOptions;

import java.awt.*;

public class Game implements Runnable{

   private GameWindow gameWindow;
   private GamePanel gamePanel;
   private Thread gameLoop;
   private final int fps=120;
   private final int ups=200;
   private Playing playing;
   private Menu menu;
   private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    public final static int tiles_default_size = 32;
    public final static float scale = 2f;
    public final static int tiles_width = 26;
    public final static int tiles_height = 14;
    public final static int tiles_size = (int) (tiles_default_size * scale);
    public final static int game_width = tiles_size * tiles_width;
    public final static int game_height = tiles_size * tiles_height;


    public Game() {

        classes();
        gamePanel = new GamePanel(this);
        gameWindow= new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gameLoop();

    }

    private void classes() {
        audioOptions=new AudioOptions(this);
        audioPlayer=new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions=new GameOptions(this);
    }


    public void gameLoop()
    {
        gameLoop=new Thread(this);
        gameLoop.start();
    }
    public void update(){

        switch (Gamestate.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
                System.exit(0);
                break;
            default:
                break;
        }
    }
    public void render(Graphics graphics) {
        switch (Gamestate.state){
            case MENU:
                menu.draw(graphics);
                break;
            case PLAYING:
                playing.draw(graphics);
                break;
            case OPTIONS:
                gameOptions.draw(graphics);
                break;
            default:
                break;
        }

    }
    public void run() {
        double frameTime = 1000000000.0 / fps;
        double updateTime = 1000000000.0 / ups;
        long previousTime = System.nanoTime();
        int updates=0;
        int frames=0;
        long LastCheck = System.currentTimeMillis();
        double deltaU=0; //deltaUpdates
        double deltaF=0; // deltaFrames
        while (true) {
            long currentTime =  System.nanoTime();
            deltaU += (currentTime - previousTime) / updateTime;
            deltaF += (currentTime - previousTime) / frameTime;
            previousTime = currentTime;
            if(deltaU>=1) {
                update();
                updates++;
                deltaU--;
            }
            if(deltaF>=1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            if(System.currentTimeMillis()-LastCheck >=1000)
            {
                LastCheck = System.currentTimeMillis();
                //System.out.println("Fps: " + frames + " | ups: " + updates);
                frames=0;
                updates=0;
            }
        }
    }

    public void windowFocusLost() {
        if(Gamestate.state==Gamestate.PLAYING)
            playing.getPlayer().resetDirectionalBooleans();
    }
    public Menu getMenu(){
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }
    public GameOptions getGameOptions(){
        return gameOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
