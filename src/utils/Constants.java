package utils;

import main.Game;

public class Constants {

    public static final float gravity=0.08f* Game.scale;
    public static final int aSpeed = 25;

    public static class ObjectConstants{
        public static final int spike=0;

        public static final int spike_width_default=32;
        public static final int spike_height_default=32;
        public static final int spike_width= (int) (Game.scale*spike_width_default);
        public static final int spike_height= (int) (Game.scale*spike_height_default);
    }
    public static class EnemyConstants {
        public static final int bad_guy = 0;

        public static final int idle = 0;
        public static final int running = 1;
        public static final int attack = 2;
        public static final int hit = 3;
        public static final int dead = 4;

        public static final int bad_guy_width_default = 26;
        public static final int bad_guy_height_default = 16;

        public static final int bad_guy_width = (int) (bad_guy_width_default * Game.scale * 2);
        public static final int bad_guy_height = (int) (bad_guy_height_default * Game.scale * 2);

        public static final int bad_guy_drawoffset_x = (int) (16 * Game.scale);
        public static final int bad_guy_drawoffset_y = (int) (5.5f * Game.scale);

        public static int getSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case bad_guy:
                    switch (enemy_state) {
                        case idle:
                        case running:
                        case hit:
                        case dead:
                        default:
                            return 1;
                        case attack:
                            return 2;
                    }

            }
            return 0;

        }

        public static int getMaxHp(int enemy_type) {
            switch (enemy_type) {
                case bad_guy:
                    return 1;
                default:
                    return 0;
            }
        }

        public static int getEnemyDamage(int enemy_type) {
            switch (enemy_type) {
                case bad_guy:
                    return 10;
                default:
                    return 0;
            }
        }
    }
    public static class ui{
        public static class buttons{
            public static final int button_width_default = 140;
            public static final int button_height_default = 56;
            public static final int button_width =(int) (button_width_default * Game.scale);
            public static final int button_height =(int) (button_height_default * Game.scale);
        }
        public static class pauseButtons{
            public static final int sound_size_default = 42;
            public static final int sound_size =(int)(sound_size_default*Game.scale);
        }
        public static class usefulButtons{
            public static final int useful_size_default = 56;
            public static final int useful_size = (int)(useful_size_default*Game.scale);
        }
        public static class volumeButtons{
            public static final int volume_width_default = 28;
            public static final int volume_height_default = 44;
            public static final int slider_width_default = 215;
            public static final int volume_width = (int)(volume_width_default*Game.scale);
            public static final int volume_height =(int)(volume_height_default*Game.scale);
            public static final int slider_width = (int)(slider_width_default*Game.scale);
        }
    }
    public static  class  directions{
        public static final int left1 = 0;
        public static final int up = 1;
        public static final int right1 = 2;
        public static final int down = 3;
    }
    public static class playerConstants{
        public static final int idle = 0;
        public static final int walk = 1;
        public static final int jumpp = 2;
        public static final int attack = 3;
        public static final int hit = 4;
        public static final int dead = 5;

        public static int getSpriteAmount(int player_action) {

            switch (player_action) {
                case walk:
                    return 2;
                case attack:
                    return 3;
                case idle:
                case jumpp:
                case hit:
                case dead:
                default:
                    return 1;
            }
        }
    }
}
