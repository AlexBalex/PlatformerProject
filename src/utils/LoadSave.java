package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Bad_guy;
import static utils.Constants.EnemyConstants.bad_guy;
import main.Game;

public class LoadSave {
    public static final String player_atlas = "players_sprite2.png";
    public static final String level_atlas = "block_sprites_final.png";
    public static final String menu_buttons = "buttons1.png";
    public static final String menu_background = "menu_background.png";
    public static final String pause_menu = "pause_menu.png";
    public static final String sound_buttons = "sound_button.png";
    public static final String useful_buttons = "useful_buttons.png";
    public static final String volume_buttons = "volume_buttons.png";
    public static final String menu_background_image =  "menu_background_pic.jpg";
    public static final String image_background =  "image_background.png";
    public static final String bad_guy_atlas =  "bad_guy_sprite.png";
    //public static final String health_pic = "health.png";
    public static final String status_image="healthBar.png";
    public static final String complete_image="completed_image.png";
    public static final String spikes="spikes.png";
    public static final String death_screen="death_screen.png";
    public static final String options_menu="options_background.png";
    public static final String game_completed="game_completed.png";
    public static final String water="water.png";

    public static BufferedImage spriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            image = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static BufferedImage[] allLevels(){
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try{
            file = new File(url.toURI());
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File[] files=file.listFiles();
        File[] filesSorted=new File[files.length];

        for(int i=0;i<filesSorted.length;i++)
            for(int j=0;j<files.length;j++){
                if(files[j].getName().equals((i+1)+".png"))
                    filesSorted[i]=files[j];
            }
        BufferedImage[] images=new BufferedImage[filesSorted.length];
        for(int i=0;i<images.length;i++)
            try {
                images[i] = ImageIO.read(filesSorted[i]);
            }catch (IOException e){
                e.printStackTrace();
            }
        return images;
    }

}
