package entities;

import main.Game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import static utils.Constants.directions.*;
import static utils.HelpMethods.canMoveHere;

public abstract class  Entity {
    protected float x,y;
    protected int width,height;
    protected Rectangle2D.Float hitbox;
    protected int aTick,aIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHp;
    protected int currentHp;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed=1.0f* Game.scale;

    protected int pushBackDirection;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = up;
     public Entity(float x,float y,int width,int height) {
         this.x=x;
         this.y=y;
         this.width=width;
         this.height=height;
     }
     protected void drawHitBox(Graphics graphics,int xLvlOffset) {
         graphics.setColor(Color.PINK);
         graphics.drawRect((int) hitbox.x-xLvlOffset,(int) hitbox.y,(int)hitbox.width,(int)hitbox.height);
     }
    protected void drawAttackBox(Graphics graphics, int xLevelOffset) {
        graphics.setColor(Color.red);
        graphics.drawRect((int)attackBox.x - xLevelOffset,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }
    protected void pushBack(int pushBackDirection, int[][] lvlData, float speedMultiplier) {
        float xSpeed = 0;
        if (pushBackDirection == left1)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (canMoveHere(hitbox.x + xSpeed * speedMultiplier, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMultiplier;
    }
     protected void HitBox(int width,int height) {
         hitbox= new Rectangle2D.Float( x, y, (int)(width*Game.scale),(int)(height*Game.scale));
     }
     public Rectangle2D.Float getHitBox(){
         return hitbox;
     }

     public int getState(){
         return state;
     }
    public int getAIndex() {
        return aIndex;
    }
    public int getCurrentHp() {
        return currentHp;
    }
    protected void newState(int state) {
        this.state = state;
        aTick = 0;
        aIndex = 0;
    }
}
