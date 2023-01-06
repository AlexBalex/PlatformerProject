package gamestates;

import Objects.ObjectManager;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import main.GamePanel;
import userinterface.GameCompletedOverlay;
import userinterface.GameOverOverlay;
import userinterface.LevelCompletedOverlay;
import userinterface.PauseOverlay;
import utils.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class Playing extends State implements Statemethods{
    private GamePanel gamePanel;
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GameOverOverlay gameOverOverlay;
    private ObjectManager objectManager;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    private int xLevelOffset;
    private int leftBorder=(int)(0.3*Game.game_width);
    private int rightBorder=(int)(0.7*Game.game_width);
    private int maxLvlOffset;
    private BufferedImage imageBackground;
    private boolean gameOver;
    private boolean levelCompleted=false;
    private boolean playerDying=false;
    private boolean gameCompleted;
    private static Timer timer;
    private static JLabel counterLabel;

    Font font1 = new Font("Arial", Font.PLAIN, 30);
    int second, minute;
    String ddSecond, ddMinute;
    DecimalFormat dFormat = new DecimalFormat("00");
    public Playing(Game game) {
        super(game);

        imageBackground=LoadSave.spriteAtlas(LoadSave.image_background);
        classes();

        gamePanel = new GamePanel(game);
        counterLabel = new JLabel("");
        counterLabel.setBounds(0, 0, 200, 100);
        counterLabel.setFont(font1);
        gamePanel.add(counterLabel);
        counterLabel.setText("00:00");
        normalTimer();
        second =0;
        minute =0;
        timer.start();

        calculateLevelOffset();
        loadStartLevel();
    }
    public void normalTimer() {
        timer = new Timer(1000, e -> {

            second++;

            ddSecond = dFormat.format(second);
            ddMinute = dFormat.format(minute);
            counterLabel.setText(ddMinute + ":" + ddSecond);

            if(second==60) {
                second=0;
                minute++;

                ddSecond = dFormat.format(second);
                ddMinute = dFormat.format(minute);
                counterLabel.setText(ddMinute + ":" + ddSecond);
            }
        });
    }
    public void loadNextLevel(){
        levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
    }
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calculateLevelOffset() {
        maxLvlOffset=levelManager.getCurrentLevel().getMaxLvlOffset();
    }

    private void classes() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(200,580,(int) (54 * Game.scale),(int) (30.5f*Game.scale),this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay=new PauseOverlay(this);
        gameOverOverlay=new GameOverOverlay(this);
        levelCompletedOverlay=new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
    }
    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if(paused) {
            pauseOverlay.update();
        }else if(levelCompleted) {
            levelCompletedOverlay.update();
        }else if (gameCompleted){
                gameCompletedOverlay.update();
        }else if(gameOver){
            gameOverOverlay.update();
        }else if(playerDying){
            player.update();
        }else{
            levelManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(),player);
            checkCloseToBorder();
        }
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }

    public void resetAll() {
        gameOver=false;
        paused=false;
        levelCompleted=false;
        playerDying=false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }
    public void gameOver(boolean gameOver){
        this.gameOver=gameOver;
    }
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player);
    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }
    private void checkCloseToBorder() {
    int playerX=(int)player.getHitBox().x;
    int diff=playerX-xLevelOffset;

    if(diff>rightBorder)
        xLevelOffset+=diff-rightBorder;
    else if(diff < leftBorder)
        xLevelOffset+=diff-leftBorder;

    if(xLevelOffset>maxLvlOffset)
            xLevelOffset=maxLvlOffset;
    else if(xLevelOffset<0)
        xLevelOffset=0;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(imageBackground,0,0,Game.game_width,Game.game_height,null);
        levelManager.draw(graphics,xLevelOffset);
        player.render(graphics,xLevelOffset);
        enemyManager.draw(graphics,xLevelOffset);
        objectManager.draw(graphics,xLevelOffset);
        if(paused) {
            graphics.setColor(new Color(0,0,0,150));
            graphics.fillRect(0,0,Game.game_width,Game.game_height);
            pauseOverlay.draw(graphics);
        }else if(gameOver)
            gameOverOverlay.draw(graphics);
        else if(levelCompleted)
            levelCompletedOverlay.draw(graphics);
        else if (gameCompleted)
            gameCompletedOverlay.draw(graphics);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver)
            if(e.getButton() == MouseEvent.BUTTON1)
                player.attacking(true);
            else if(e.getButton()==MouseEvent.BUTTON3)
                player.chargeAttack();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (paused)
            pauseOverlay.mousePressed(e);
        else if (levelCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (paused)
            pauseOverlay.mouseReleased(e);
        else if (levelCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver)
        gameOverOverlay.mouseMoved(e);
		else if (paused)
            pauseOverlay.mouseMoved(e);
        else if (levelCompleted)
            levelCompletedOverlay.mouseMoved(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) {
            gameOverOverlay.escPressed(e);
            gameOverOverlay.enterPressed(e);
        }else if(paused) {
            pauseOverlay.keyPressed(e);
        }else if(gameCompleted){
            gameCompletedOverlay.keyPressed(e);
        } else
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    if(levelCompleted)
                        levelCompletedOverlay.escPressed(e);
                    break;
                case KeyEvent.VK_K:
                    player.attacking(true);
                    break;
                case KeyEvent.VK_L:
                    player.chargeAttack();
                    break;
                case KeyEvent.VK_ENTER:
                    if(levelCompleted)
                        levelCompletedOverlay.enterPressed(e);
                    break;
                default:
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !gameCompleted && !levelCompleted)
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;

            }
    }
    public void unpauseGame(){
        paused = false;
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver && !gameCompleted && !levelCompleted)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void windowFocusLost() {
        player.resetDirectionalBooleans();
    }
    public void setLevelCompleted(boolean levelCompleted){
        game.getAudioPlayer().levelCompleted();
        if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLevel();
            resetAll();
            return;
        }
        this.levelCompleted= levelCompleted;
    }
    public void setMaxLvlOffset(int maxLvlOffset) {
        this.maxLvlOffset = maxLvlOffset;
    }
    public ObjectManager getObjectManager() {
        return objectManager;
    }
    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

}

