package Objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GameObject {
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected int xDrawOffset, yDrawOffset;
    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void hitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.scale), (int) (height * Game.scale));
    }
    public void drawHitbox(Graphics graphics, int xLvlOffset) {
        graphics.setColor(Color.PINK);
        graphics.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    public int getyDrawOffset() {
        return yDrawOffset;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
