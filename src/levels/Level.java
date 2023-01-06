package levels;

import Objects.Spike;
import entities.Bad_guy;
import main.Game;
import utils.HelpMethods;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.*;

public class Level {

    private BufferedImage image;
    private int[][] lvlData;
    private ArrayList<Bad_guy> bad_guys;
    private ArrayList<Spike> spikes;
    private int lvlTotalTilesWidth;
    private int maxTilesOffset;
    private int maxLvlOffset;
    private Point playerSpawn;
    public Level(BufferedImage image) {
        this.image=image;
        createLevelData();
        createEnemies();
        createSpikes();
        calculateLevelOffsets();
        calculatePlayerSpawn();

    }

    private void createSpikes() {
        spikes = HelpMethods.getSpikes(image);
    }

    private void calculatePlayerSpawn() {
        playerSpawn=playerSpawn(image);
    }

    private void calculateLevelOffsets() {
        lvlTotalTilesWidth=image.getWidth();
        maxTilesOffset=lvlTotalTilesWidth-Game.tiles_width;
        maxLvlOffset=Game.tiles_size*maxTilesOffset;
    }

    private void createEnemies() {
        bad_guys=BadGuys(image);
    }

    private void createLevelData() {
       lvlData=levelData(image);
    }

    public int spriteIndex(int x, int y) {
        return lvlData[y][x];
    }
    public int[][] getLevelData(){
        return lvlData;
    }

    public int getMaxLvlOffset() {
        return maxLvlOffset;
    }

    public ArrayList<Bad_guy> getBad_guys() {
        return bad_guys;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
    public ArrayList<Spike> getSpikes(){
        return spikes;
    }
}

