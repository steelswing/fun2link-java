
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * File: ImageUtil.java
 * Created on 2025 Nov 12, 15:20:42
 *
 * @author LWJGL2
 */
public class ImageUtil {

    public static BufferedImage createImage(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }

        // J2ME обычно ищет ресурсы в JAR, поэтому используем getResourceAsStream
        InputStream in = ImageUtil.class.getResourceAsStream(path);
        if (in == null) {
            // если не найден как ресурс — пробуем как обычный файл
            in = ImageUtil.class.getResourceAsStream("/" + path);
            if (in == null) {
                throw new IOException("Resource not found: " + path);
            }
        }

        try {
            return ImageIO.read(in);
        } finally {
            in.close();
        }
    }


    /**
     * Создаёт пустой BufferedImage с указанной шириной и высотой.
     * Аналог J2ME Image.createImage(width, height)
     */
    public static BufferedImage createImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Инициализация прозрачным фоном (как в J2ME)
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, width, height);

        g.dispose();
        return img;
    }

    public static int alignX(int imageWidth, int x, int anchor) {
        int alignedX = x;
        if ((anchor & Graphics.RIGHT) != 0) {
            alignedX -= imageWidth;
        } else if ((anchor & Graphics.HCENTER) != 0) {
            alignedX -= imageWidth >> 1;
        }
        return alignedX;
    }

    public static int alignY(int imageHeight, int y, int anchor) {
        int alignedY = y;
        if ((anchor & Graphics.BOTTOM) != 0) {
            alignedY -= imageHeight;
        } else if ((anchor & Graphics.VCENTER) != 0) {
            alignedY -= imageHeight >> 1;
        }
        return alignedY;
    }

    public static BufferedImage rotateImage(BufferedImage originalImage, int rotateTimes) {
        boolean flipDimensions = originalImage.getWidth() != originalImage.getHeight() && rotateTimes % 2 != 0;
        int width = flipDimensions ? originalImage.getHeight() : originalImage.getWidth();
        int height = flipDimensions ? originalImage.getWidth() : originalImage.getHeight();
        double theta = (Math.PI / 2) * rotateTimes;

        BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = rotatedImage.createGraphics();

        if (flipDimensions) {
            int translate = (width - height) >> 1;
            graphics2D.translate(translate, translate);
        }
        graphics2D.rotate(theta, width / 2.0, height / 2.0);
        graphics2D.drawRenderedImage(originalImage, null);

        return rotatedImage;
    }

    public static BufferedImage mirrorImageVertical(BufferedImage originalImage) {
        BufferedImage mirroredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = mirroredImage.createGraphics();

        graphics2D.translate(0, originalImage.getHeight());
        graphics2D.drawImage(originalImage, 0, 0, originalImage.getWidth(), -originalImage.getHeight(), null);
        return mirroredImage;
    }

    public static BufferedImage mirrorImageHorizontal(BufferedImage originalImage) {
        BufferedImage mirroredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = mirroredImage.createGraphics();

        graphics2D.translate(originalImage.getWidth(), 0);
        graphics2D.drawImage(originalImage, 0, 0, -originalImage.getWidth(), originalImage.getHeight(), null);
        return mirroredImage;
    }

    public static BufferedImage createCompatibleImage(int width, int height) {
        GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return graphicsConfiguration.createCompatibleImage(width, height);
    }
}
