package utilz;

import static utilz.Size.*;

public class Constants {

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class EnemyConstants {
        public static final int PIG = 0;
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK = 6;
        public static final int DEAD = 7;


        public static final int PIG_WIDTH_DEFAULT = 38;
        public static final int PIG_HEIGHT_DEFAULT = 28;

        public static final int PIG_WIDTH = (int)(PIG_WIDTH_DEFAULT * SCALE);
        public static final int PIG_HEIGHT = (int)(PIG_HEIGHT_DEFAULT * SCALE);

        public static final int PIG_XOFFSET = (int) (13 * SCALE);
        public static final int PIG_YOFFSET = (int) (8 * SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyAction) {
            switch(enemyType) {
                case PIG:
                    switch (enemyAction) {
                        case IDLE:
                            return 12;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 5;
                        case HIT:
                            return 2;
                        case FALLING:
                        case JUMP:
                        case GROUND:
                            return 1;
                        case DEAD:
                            return 4;
                    }
            }
            return 0;
        }
    }


    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK = 6;
        public static final int DEAD = 7;


        public static int GetSpriteAmount(int player_action) {
            switch(player_action) {
                case IDLE:
                    return 11;
                case RUNNING:
                    return 8;
                case HIT:
                    return 2;
                case JUMP:
                case FALLING:
                case GROUND:
                    return 1;
                case ATTACK:
                    return 3;
                case DEAD:
                    return 4;
                default:
                    return 0;
            }
        }
    }
}