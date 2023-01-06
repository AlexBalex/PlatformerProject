package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import static utils.LoadSave.water;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private BufferedImage[] water;
    private ArrayList<Level> levels;
    private int levelIndex=0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        createWater();
        levels=new ArrayList<>();
        buildAllLevels();
    }

    private void createWater() {
        water=new BufferedImage[1];
        water[0]=LoadSave.spriteAtlas(LoadSave.water);
    }

    public void loadNextLevel(){
        Level newLevel=levels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getMaxLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }
    private void buildAllLevels() {
        BufferedImage[] allLevels=LoadSave.allLevels();
        for(BufferedImage image:allLevels)
            levels.add(new Level(image));
    }

    private void importOutsideSprites() {
        BufferedImage image = LoadSave.spriteAtlas(LoadSave.level_atlas);
        levelSprite = new BufferedImage[8];
        for (int j = 0; j < 1; j++)
            for (int i = 0; i < 6; i++) {
                int index = j * 5 + i;
                levelSprite[index] = image.getSubimage(i * 5, j * 5, 5, 5);
            }
    }

    public void draw(Graphics graphics,int xLevelOffset) {
        for (int j = 0; j < Game.tiles_height; j++)
            for (int i = 0; i < levels.get(levelIndex).getLevelData()[0].length; i++) {
                int index =  levels.get(levelIndex).spriteIndex(i, j);
                if(index==6)
                    graphics.drawImage(water[0],Game.tiles_size * i-xLevelOffset, Game.tiles_size * j, Game.tiles_size, Game.tiles_size, null);
                else
                    graphics.drawImage(levelSprite[index], Game.tiles_size * i-xLevelOffset, Game.tiles_size * j, Game.tiles_size, Game.tiles_size, null);
            }
    }

    public void update() {
    }

    public Level getCurrentLevel(){
        return  levels.get(levelIndex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }
    public int getLevelIndex(){
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}
