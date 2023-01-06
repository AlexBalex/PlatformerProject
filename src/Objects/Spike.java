package Objects;

import main.Game;

public class Spike extends GameObject{
    public Spike(int x, int y, int objType) {
        super(x, y, objType);
        hitbox(32,16);
        xDrawOffset=0;
        yDrawOffset=(int)(Game.scale*16);
        hitbox.y+=yDrawOffset;
    }
}
