package Objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.spike_height;
import static utils.Constants.ObjectConstants.spike_width;

public class ObjectManager {
    private Playing playing;
    private BufferedImage spikeImage;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();
    }
    public void checkSpikesTouched(Player player){
        for(Spike s: spikes)
            if(s.getHitbox().intersects(player.getHitBox()))
                player.kill();
    }
    public void draw(Graphics graphics,int xLevelOffset)
    {
        drawTraps(graphics,xLevelOffset);
    }

    private void drawTraps(Graphics graphics, int xLevelOffset) {
        for(Spike s: spikes)
            graphics.drawImage(spikeImage, (int) (s.getHitbox().x - xLevelOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), spike_width, spike_height, null);
    }

    private void loadImages() {
        spikeImage = LoadSave.spriteAtlas(LoadSave.spikes);
    }
    public void loadObjects(Level newLevel){
        spikes=newLevel.getSpikes();
    }

    public void update() {
    }

    public void resetAllObjects() {
    }
}
