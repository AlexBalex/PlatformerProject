package utils;

import Objects.Spike;
import entities.Bad_guy;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.bad_guy;
import static utils.Constants.ObjectConstants.spike;

public class HelpMethods {
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!isSolid(x, y, lvlData))
            if (!isSolid(x + width, y + height, lvlData))
                if (!isSolid(x + width, y, lvlData))
                    if (!isSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.tiles_size;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.game_height)
            return true;
        float xIndex = x / Game.tiles_size;
        float yIndex = y / Game.tiles_size;

        return isTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean isTileSolid(int tileX, int tileY, int[][] lvlData) {
        int value = lvlData[tileY][tileX];

        switch (value) {
            case 5,6:
                return false;
            default:
                return true;
        }

    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.tiles_size);
        if (xSpeed > 0) {
            //if colliding with something to the right
            int tileXPos = currentTile * Game.tiles_size;
            int xOffset = (int) (Game.tiles_size - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            //if colliding with something to the left
            return currentTile * Game.tiles_size;
        }
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.tiles_size);
        if (airSpeed > 0) {
            //Falling
            int tileYPos = currentTile * Game.tiles_size;
            int yOffset = (int) (Game.tiles_size - hitbox.height);
            return tileYPos + yOffset - 1;
        } else
            //Jumping
            return currentTile * Game.tiles_size;
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        //check pixel bellow bottom left and bottom right
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean areAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + 1, y, lvlData))
                return false;
            if (!isTileSolid(xStart + 1, y + 1, lvlData))
                return false;
        }
        return true;
    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float hitbox1, Rectangle2D.Float hitbox2, int tileY) {
        int TileX1 = (int) (hitbox1.x / Game.tiles_size);
        int TileX2 = (int) (hitbox2.x / Game.tiles_size);
        if (TileX1 > TileX2)
            return areAllTilesWalkable(TileX2, TileX1, tileY, lvlData);
        else
            return areAllTilesWalkable(TileX1, TileX2, tileY, lvlData);

    }
    private static int getTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / Game.tiles_size);
        int yCord = (int) (yPos / Game.tiles_size);
        return lvlData[yCord][xCord];
    }
    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (getTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 6)
            if (getTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 6)
                return false;
        return true;
    }

    public static int[][] levelData(BufferedImage image) {
        int[][] lvlData = new int[image.getHeight()][image.getWidth()];
        //System.out.println(image.getHeight() + " " + image.getWidth() + "\n");
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if (value >= 7)
                    value = 0;
                //System.out.println(value + "\n");
                lvlData[j][i] = value;
            }
        return lvlData;

    }

    public static ArrayList<Bad_guy> BadGuys(BufferedImage image) {
        ArrayList<Bad_guy> list = new ArrayList<>();
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if (value == bad_guy)
                    list.add(new Bad_guy(i * Game.tiles_size, j * Game.tiles_size));

            }
        return list;
    }

    public static Point playerSpawn(BufferedImage image) {
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(i * Game.tiles_size, j * Game.tiles_size);
            }
        return new Point(1 * Game.tiles_size, 1 * Game.tiles_size);
    }

    public static ArrayList<Spike> getSpikes(BufferedImage image) {
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if (value == spike)
                    list.add(new Spike(i * Game.tiles_size, j * Game.tiles_size, spike));

            }
        return list;
    }
}
