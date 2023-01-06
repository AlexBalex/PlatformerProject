package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.EnemyConstants.*;

import static utils.Constants.aSpeed;
import static utils.Constants.directions.*;
import static utils.Constants.*;
import static utils.HelpMethods.*;

public abstract class Enemy extends Entity{
    protected int enemyType;
    protected int enemyAniSpeed=aSpeed+45;
    protected boolean firstUpdate=true;
    protected float walkSpeed=0.35f * Game.scale;
    protected int walkDir=left1;
    protected int tileY;
    protected float attackDistance=Game.tiles_size;
    protected boolean active=true;
    protected boolean attackChecked;
    public Enemy(float x, float y, int width, int height,int enemyType) {
        super(x, y, width, height);
        this.enemyType=enemyType;
        maxHp=getMaxHp(enemyType);
        currentHp=maxHp;
        walkSpeed=0.35f*Game.scale;
    }
    protected void  firstUpdateCheck(int[][] lvlData){
        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate=false;
    }
    protected void updateInAir(int[][] lvlData){
        if(canMoveHere(hitbox.x,hitbox.y + airSpeed,hitbox.width,hitbox.height,lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += gravity;
        }else{
            inAir=false;
            hitbox.y=getEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
            tileY=(int)(hitbox.y/Game.tiles_size);
        }
    }
    protected void move(int[][] lvlData){
        float xSpeed=0;
        if(walkDir == left1)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if(canMoveHere(hitbox.x + xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData))
            if(isFloor(hitbox,xSpeed,lvlData)){
                hitbox.x+=xSpeed;
                return;
            }
        changeWalkDir();
    }
    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x>hitbox.x)
            walkDir=right1;
        else
            walkDir=left1;
    }
    protected boolean canSeePlayer(int [][]lvlData,Player player){
        int playerTileY= (int) (player.getHitBox().y/Game.tiles_size);
        if(playerTileY==tileY)
            if(isPlayerInRange(player)){
                if(isSightClear(lvlData,hitbox,player.hitbox,tileY))
                    return true;
            }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue= (int) Math.abs(player.hitbox.x-hitbox.x);
        return absValue <= attackDistance*5;
    }
    protected boolean isPlayerCloseForAttack(Player player){
        int absValue= (int) Math.abs(player.hitbox.x-hitbox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState){
        this.state=enemyState;
        aTick=0;
        aIndex=0;
    }
    public void hurt(int amount){
        currentHp-=amount;
        if(currentHp<=0)
            newState(dead);
        else{
            newState(hit);
            if (walkDir == left1)
                pushBackDirection = right1;
            else
                pushBackDirection = left1;
            pushBackOffsetDir = up;
            pushDrawOffset = 0;
        }
    }
    protected void checkPlayerHit(Rectangle2D.Float attackBox,Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHp(-getEnemyDamage(enemyType), this);

        attackChecked=true;
    }
    protected void updateAnimationTick(){
        aTick++;
        if(aTick>=enemyAniSpeed) {
            aTick = 0;
            aIndex++;
            if (aIndex >= getSpriteAmount(enemyType,state)){
                aIndex=0;
                switch (state){
                    case attack:
                    case hit:
                        state=idle;
                        break;
                    case dead:
                        active=false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void changeWalkDir() {
        if(walkDir==left1)
            walkDir=right1;
        else
            walkDir=left1;
    }
    public void resetEnemy(){
        hitbox.x=x;
        hitbox.y=y;
        firstUpdate=true;
        currentHp=maxHp;
        newState(idle);
        active=true;
        airSpeed=0;
    }

    public boolean isActive() {
        return active;
    }
}

