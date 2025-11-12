
import java.awt.event.KeyEvent;

/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

/**
 * File: GameKeyMapping.java
 * Created on 2025 Nov 12, 15:41:03
 *
 * @author LWJGL2
 */
public class GameKeyMapping {

    // J2ME GameAction константы
    public static final int UP = 1;
    public static final int DOWN = 6;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_STAR = 42;
    public static final int KEY_POUND = 35;

    
    public static int getGameAction(int keyCode) {
        switch (keyCode) {
            // === ДВИЖЕНИЕ ===
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case '2': // 50
                return UP;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
            case '4': // 52
                return LEFT;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
            case '6': // 54
                return RIGHT;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
            case '8': // 56
                return DOWN;

            // === ОГОНЬ ===
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
            case '5': // 53
                return FIRE;

            case KeyEvent.VK_R:           // R → #
                return 10; // GAME_B

            case KeyEvent.VK_ESCAPE:      // ESC → Right Softkey
                return 11; // GAME_C

            // === Эмуляция * и # через numpad (если нужно) ===
            case KeyEvent.VK_MULTIPLY:    // * на numpad
                return 9;

            case KeyEvent.VK_DIVIDE:      // / часто используется как #
            case KeyEvent.VK_NUMBER_SIGN:
            case 35:
                return 10;
            case KeyEvent.VK_Q:   
                return KEY_STAR;
                
            case KeyEvent.VK_E:   
                return KEY_POUND;

            default:
                return 0; // Нет действия
        }
    }
}
