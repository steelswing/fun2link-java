
import java.awt.Font;

/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

/**
 * File: J2MEFont.java
 * Created on 2025 Nov 12, 15:26:58
 *
 * @author LWJGL2
 */
public class J2MEFont {

    // --- Константы как в javax.microedition.lcdui.Font ---
    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;

    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4; // (AWT не поддерживает напрямую)

    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_LARGE = 16;

    /**
     * Эмуляция Font.getFont(face, style, size)
     * Возвращает java.awt.Font с аналогичными параметрами.
     */
    public static Font getFont(int face, int style, int size) {
        String fontName = "Dialog"; // системный по умолчанию

        switch (face) {
            case FACE_MONOSPACE:
                fontName = "Monospaced";
                break;
            case FACE_PROPORTIONAL:
                fontName = "SansSerif";
                break;
            case FACE_SYSTEM:
            default:
                fontName = "Dialog";
                break;
        }

        int awtStyle = Font.PLAIN;
        if ((style & STYLE_BOLD) != 0) {
            awtStyle |= Font.BOLD;
        }
        if ((style & STYLE_ITALIC) != 0) {
            awtStyle |= Font.ITALIC;
        }

        int fontSize = 14; // базовый (MEDIUM)
        if (size == SIZE_SMALL) {
            fontSize = 10;
        } else if (size == SIZE_LARGE) {
            fontSize = 18;
        }

        return new Font(fontName, awtStyle, fontSize);
    }
}
