
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import javax.swing.*;
import java.util.logging.*;

public class Fun2Link extends JFrame {

    private final Fun2LinkCanvas canvas;

    public Fun2Link() {
        super("Fun2Link");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(256, 256);
        setLocationRelativeTo(null);
        setVisible(true);
        createBufferStrategy(2);

        final BufferStrategy bufferStrategy = getBufferStrategy();

        canvas = new Fun2LinkCanvas(this);
        canvas.init();
        addKeyListener(canvas);
        while (isVisible()) {
            final Graphics g = new Graphics((Graphics2D) bufferStrategy.getDrawGraphics());
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

             
            
            double scaleX = getWidth() /222.0;   
            double scaleY = getHeight() / 222.0;  
            double scale = Math.min(scaleX, scaleY);  

            double offsetX = (getWidth() - 128 * scale) / 2;
            double offsetY = (getHeight() - 128 * scale) / 2;

            g.globalX = (int) offsetX;
            g.globalY = (int) offsetY;
            g.translate(0, 0);
            g.getGraphics2D().scale(scale, scale);

            canvas.paint(g);

            g.getGraphics2D().dispose();
            bufferStrategy.show();
        }

    }

    public void startApp() {
        canvas.init();
        setVisible(true);
        canvas.init();
    }

    public void destroyApp() {
        canvas.destroy();
        dispose();
    }

    public static void main(String[] args) { 
        LogManager logManager = LogManager.getLogManager();
        Logger yamahaLogger = Logger.getLogger("vavi.sound.smaf");
        if (yamahaLogger != null) {
            yamahaLogger.setLevel(Level.OFF);
        }

        Logger vaviLogger = Logger.getLogger("vavi");
        if (vaviLogger != null) {
            vaviLogger.setLevel(Level.OFF);
        }
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tH:%1$tM:%1$tS %4$-7s] %5$s %n");
        new Fun2Link().startApp();
    }
}
