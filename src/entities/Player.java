package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static audio.AudioPlayer.attack1;
import static utils.Constants.*;
import static utils.Constants.directions.*;
import static utils.Constants.playerConstants.*;
import static  utils.HelpMethods.canMoveHere;
import static utils.HelpMethods.*;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

public class Player extends Entity{

    private BufferedImage[][] animations;
    private boolean moving = false,attacking = false;
    private boolean left,right,jump;
    private int[][] lvlData;
    private float xDrawOffset = 5 * Game.scale;
    private float yDrawOffset = 3.5f * Game.scale;

    //for Jumping and gravity
    private float jumpSpeed = -3.5f*Game.scale;
    private float fallSpeedAfterCollision = 0.5f*Game.scale;

    //hpImage
    private BufferedImage statusImage;

    private int statusBarWidth = (int) (470 * Game.scale);
    private int statusBarHeight = (int) (120 *Game.scale);
    private int statusBarX = (int) (20 * Game.scale);
    private int statusBarY = (int) (30 * Game.scale);

    private int hpBarWidth = (int) (125* Game.scale);
    private int hpBarHeight = (int) (6 * Game.scale);
    private int hpBarXStart = (int) (4 * Game.scale);
    private int hpBarYStart = (int) (2 * Game.scale);

    private int hpWidth=hpBarWidth;

    private int chargeBarWidth = (int) (125* Game.scale);
    private int chargeBarHeight = (int) (3 * Game.scale);
    private int chargeBarXStart = (int) (4 * Game.scale);
    private int chargeBarYStart = (int) (8 * Game.scale);
    private int chargeWidth=chargeBarWidth;
    protected int chargeMaxValue=40;
    protected int chargeValue =chargeMaxValue;

    private int flipX=0;
    private int flipW=1;

    private boolean attackChecked;
    private Playing playing;
    private boolean chargeAttackActive;
    private int chargeAttackTick;
    private int chargeGrowSpeed= 100;
    private int chargeGrowTick;
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing=playing;
        this.state=idle;
        this.maxHp=20;
        this.currentHp=maxHp;
        this.walkSpeed=1.0f*Game.scale;
        loadAnimations();
        HitBox(24,(int)(26.5f));
        AttackBox();
    }
    public void setSpawn(Point spawn){
        this.x=spawn.x;
        this.y=spawn.y;
        hitbox.x=x;
        hitbox.y=y;
    }

    private void AttackBox() {
        attackBox=new Rectangle2D.Float(x,y,(int)(25*Game.scale),(int)(20*Game.scale));
        resetAttackBox();
    }

    public void update() {
        updateHpBar();
        updateChargeBar();
        if (currentHp <= 0) {
            if (state != dead) {
                state = dead;
                aTick = 0;
                aIndex = 0;
                jump=false;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.die);
            } else if (aIndex == getSpriteAmount(dead) - 1 && aTick >= aSpeed - 1) {
                playing.gameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.gameover);
            } else
                updateAnimationTick();
            return;
        }
        updateAttackBox();

        if (state == hit) {
            if (aIndex <= getSpriteAmount(state))
                pushBack(pushBackDirection, lvlData, 1.25f);
        } else
            updatePosition();

        if (moving){
            checkSpikesTouched();
            checkInsideWater();
        if (chargeAttackActive) {
            chargeAttackTick++;
            if(chargeAttackTick>=35){
                chargeAttackTick=0;
                chargeAttackActive=false;
            }
        }
    }
        if(attacking || chargeAttackActive)
            checkAttack();
        updateAnimationTick();
        animation();
    }

    private void checkInsideWater() {
        if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
            currentHp = 0;
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkAttack() {
        if(attackChecked || aIndex!=1)
            return;
        attackChecked=true;

        if(chargeAttackActive)
            attackChecked=false;

        playing.checkEnemyHit(attackBox);
        playing.getGame().getAudioPlayer().playEffect(attack1);
    }

    private void updateAttackBox() {
        if(right && left){
            if(flipW==1){
                attackBox.x=hitbox.x+hitbox.width+(int)(Game.scale*2);
            }else{
                attackBox.x=hitbox.x-hitbox.width-(int)(Game.scale*2);
            }
        }else if(right || (chargeAttackActive && flipW==1)){
            attackBox.x=hitbox.x+hitbox.width+(int)(Game.scale*2);
        }else if(left|| (chargeAttackActive && flipW==-1)) {
            attackBox.x=hitbox.x-hitbox.width-(int)(Game.scale*2);
        }
        attackBox.y=hitbox.y + (Game.scale*10);
    }

    private void updateHpBar() {
        hpWidth=(int)(((float)currentHp/maxHp)*hpBarWidth);
    }
    private void updateChargeBar(){
        chargeWidth=(int)((chargeValue/(float)chargeMaxValue) * chargeBarWidth);

        chargeGrowTick++;
        if(chargeGrowTick>=chargeGrowSpeed) {
            chargeGrowTick = 0;
            changeCharge(1);
        }
    }

    private void changeCharge(int value) {
        chargeValue+=value;
        if(chargeValue>=chargeMaxValue)
            chargeValue=chargeMaxValue;
        else if(chargeValue<=0)
            chargeValue=0;
    }

    public void render(Graphics graphics,int xLevelOffset){
        graphics.drawImage(animations[state][aIndex],(int)(hitbox.x-xDrawOffset)-xLevelOffset+flipX,(int)(hitbox.y-yDrawOffset + (int) (pushDrawOffset)),width*flipW,height,null);
        //drawHitBox(graphics,xLevelOffset);
        //drawAttackBox(graphics,xLevelOffset);
        drawUI(graphics);
    }


    private void drawUI(Graphics graphics) {
        //Health bar
        graphics.setColor(Color.red);
        graphics.fillRect(hpBarXStart + statusBarX,hpBarYStart + statusBarY,hpWidth,hpBarHeight);
        //Charge bar
        graphics.setColor(Color.green);
        graphics.fillRect(chargeBarXStart+statusBarX,chargeBarYStart+statusBarY,chargeWidth,chargeBarHeight);
        //Background ui
        graphics.drawImage(statusImage,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);

    }

    private void updateAnimationTick() {
        aTick++;
        if(aTick>= aSpeed){
            aTick=0;
            aIndex++;
            if(aIndex>= getSpriteAmount(state)) {
                aIndex = 0;
                attacking = false;
                attackChecked=false;
                if (state == hit) {
                    newState(idle);
                    airSpeed = 0f;
                    if (!isFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }
    private void animation() {
        int startAnimation = state;
        if (state == hit)
            return;
        if(moving)
            state=walk;
        else
            state=idle;
        if(inAir)
            state=jumpp;

        if(chargeAttackActive){
            state=attack;
            aIndex=1;
            aTick=0;
        }
        if(attacking) {
            state = attack;
            if(startAnimation!=attack){
                aIndex=1;
                aTick=0;
                return;
            }
        }

        if(startAnimation != state)
            resetAnimationTick();
    }

    private void resetAnimationTick() {
        aTick=0;
        aIndex=0;
    }

    private void updatePosition() {
        moving = false;
        if(jump)
            jump();
        if(!inAir)
            if(!chargeAttackActive)
                if((!left && !right) || (right & left))
                  return;
        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX=width-40;
            flipW=-1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX=0;
            flipW=1;
        }
        if(chargeAttackActive){
            if((!left && !right) || (left  && right)) {
                    if (flipW == -1)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;
            }
            xSpeed*=3;
        }
        if(!inAir)
            if(!isEntityOnFloor(hitbox,lvlData))
                inAir=true;
        if(inAir && !chargeAttackActive){
            if(canMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData)){
                hitbox.y+=airSpeed;
                airSpeed+=gravity;
                updateXPos(xSpeed);
            }else{
                hitbox.y=getEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if(airSpeed>0)
                    resetInAir();
                else
                    airSpeed=fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }else
            updateXPos(xSpeed);
        moving=true;
    }

    private void jump() {
        if(inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.jump);
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void resetInAir() {
        inAir=false;
        airSpeed=0;
    }

    private void updateXPos(float xSpeed) {
        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }else{
            hitbox.x=getEntityXPosNextToWall(hitbox,xSpeed);
            if(chargeAttackActive){
                chargeAttackActive=false;
                chargeAttackTick=0;
            }
        }
    }
    public void changeHp(int value){
        if (value < 0) {
            if (state == hit)
                return;
            else
                newState(hit);
        }

        currentHp += value;
        currentHp = Math.max(Math.min(currentHp, maxHp), 0);
    }
    public void changeHp(int value,Enemy enemy){
        if (state == hit)
            return;
        changeHp(value);
        pushBackOffsetDir = up;
        pushDrawOffset = 0;

        if (enemy.getHitBox().x < hitbox.x)
            pushBackDirection = right1;
        else
            pushBackDirection = left1;
    }
    public void kill() {
        currentHp=0;
    }
    private void loadAnimations() {
        BufferedImage image = LoadSave.spriteAtlas(LoadSave.player_atlas);

            animations= new BufferedImage[6][3];
            for(int j=0;j<animations.length;j++)
                for(int i=0;i<animations[j].length;i++)
                    animations[j][i]=image.getSubimage(i*11,j*9,11,9);

            statusImage=LoadSave.spriteAtlas(LoadSave.status_image);
    }
    public void loadLevelData(int[][] lvlData){
        this.lvlData=lvlData;
        if(!isEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }
    public void resetDirectionalBooleans() {
        left = false;
        right = false;
    }

    public void attacking(boolean attacking){
        this.attacking=attacking;

    }
    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirectionalBooleans();
        inAir=false;
        attacking=false;
        moving=false;
        jump=false;
        airSpeed=0f;
        state=idle;
        currentHp=maxHp;
        chargeValue=chargeMaxValue;
        hitbox.x=x;
        hitbox.y=y;
        resetAttackBox();
        if(!isEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }

    private void resetAttackBox(){
        if(flipW==1){
            attackBox.x=hitbox.x+hitbox.width+(int)(Game.scale*2);
        }else{
            attackBox.x=hitbox.x-hitbox.width-(int)(Game.scale*2);
        }
    }

    public void chargeAttack() {
        if(chargeAttackActive)
            return;
        if(chargeValue>=10){
            chargeAttackActive=true;
            changeCharge(-10);
        }

    }
}
