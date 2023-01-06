package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.EnemyConstants.*;

public class Bad_guy extends  Enemy{

    private int attackBoxOffsetX;
    public Bad_guy(float x, float y) {
        super(x, y, bad_guy_width, bad_guy_height, bad_guy);
        HitBox(20,26);
        AttackBox();
    }

    private void AttackBox() {
        attackBox=new Rectangle2D.Float(x,y,(int)(48*Game.scale),(int)(26*Game.scale));
        attackBoxOffsetX=(int)(Game.scale*14);
    }

    public void update(int[][] lvlData,Player player){
        updateBehaviour(lvlData,player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x=hitbox.x-attackBoxOffsetX;
        attackBox.y=hitbox.y;
    }

    private void updateBehaviour(int[][] lvlData,Player player){
        if(firstUpdate)
            firstUpdateCheck(lvlData);
        if(inAir)
           updateInAir(lvlData);
        else {
            switch (state){
                case idle:
                    state=running;
                    break;
                case running:
                    if(canSeePlayer(lvlData,player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(attack);
                    }
                    move(lvlData);
                    break;
                case attack:
                    if(aIndex==0)
                        attackChecked=false;
                    if(aIndex==1 && !attackChecked)
                        checkPlayerHit(attackBox,player);
                    break;
                case hit:
                    break;
            }
        }
    }
}
