package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager{


    private Playing playing;
    private BufferedImage[][] bad_guy_array;
    private ArrayList<Bad_guy> bad_guys = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing=playing;
        loadEnemyImages();
    }

    public void loadEnemies(Level level) {
        bad_guys=level.getBad_guys();
    }

    public void update(int[][] lvlData,Player player){
        boolean isAnyActive=false;
        for(Bad_guy bg : bad_guys)
            if(bg.isActive()) {
                bg.update(lvlData, player);
                isAnyActive=true;
            }
        if(!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }
    public void draw(Graphics graphics,int xLevelOffset){
        drawBadGuys(graphics,xLevelOffset);
    }

    private void drawBadGuys(Graphics graphics,int xLevelOffset) {
        for(Bad_guy bg : bad_guys)
            if(bg.isActive()) {
                    graphics.drawImage(bad_guy_array[bg.getState()][bg.getAIndex()],(int)(bg.getHitBox().x - xLevelOffset-bad_guy_drawoffset_x),(int)(bg.getHitBox().y-bad_guy_drawoffset_y),bad_guy_width,bad_guy_height,null);
                    //bg.drawHitBox(graphics,xLevelOffset);
                    //bg.drawAttackBox(graphics,xLevelOffset);
                }
    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Bad_guy bg: bad_guys)
            if(bg.isActive())
                if(bg.getCurrentHp() > 0)
                    if(attackBox.intersects(bg.getHitBox())){
                        bg.hurt(10);
                        return;
                    }
    }
    private void loadEnemyImages() {
        bad_guy_array= new BufferedImage[5][2];
        BufferedImage temp = LoadSave.spriteAtlas(LoadSave.bad_guy_atlas);
        for(int j=0;j<bad_guy_array.length;j++)
            for(int i=0;i<bad_guy_array[j].length;i++)
                bad_guy_array[j][i]=temp.getSubimage(i * bad_guy_width_default,j*bad_guy_height_default,bad_guy_width_default,bad_guy_height_default);

    }
    public void resetAllEnemies(){
        for(Bad_guy bg: bad_guys)
            bg.resetEnemy();
    }
}
