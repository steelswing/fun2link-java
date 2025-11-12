
import java.awt.Image;

public class Cursor {
    public byte currentX;
    public byte currentY;
    private Image cursorImage;
    private Image boomImage;
    private byte currentPipe;
    private byte frame;
    private Fun2LinkCanvas canvas;
    Boom boom;
    boolean isBoom = false;

    public Cursor(Fun2LinkCanvas fun2LinkCanvas) {
        this.canvas = fun2LinkCanvas;
        this.frame = 0;
        this.isBoom = false;
        this.currentX = 0;
        this.currentY = 0;
        this.currentPipe = 0;
        try {
            this.boomImage = ImageUtil.createImage("/icons/s_boom.png");
            this.cursorImage = ImageUtil.createImage("/icons/s_cursor.png");
        }
        catch (Exception exception) {
            System.out.println("Image Load Error");
        }
    }

    public void setPipe(byte by) {
        if (by > 0 && by <= 7) {
            this.currentPipe = by;
        }
    }

    public void setX(byte by) {
        if (by > 0 && by <= 7 && !this.isBoom) {
            this.currentX = by;
        }
    }

    public void setY(byte by) {
        if (by > 0 && by <= 6 && !this.isBoom) {
            this.currentY = by;
        }
    }

    public void setLocation(byte by, byte by2) {
        if (!this.isBoom) {
            if (by > 0 && by <= 7) {
                this.currentX = by;
            }
            if (by2 > 0 && by2 <= 6) {
                this.currentY = by2;
            }
        }
    }

    public byte getX() {
        return this.currentX;
    }

    public byte getY() {
        return this.currentY;
    }

    public void drawBoom(Graphics graphics, byte by, int n, int n2) {
        graphics.setClip(n, n2, 16, 16);
        graphics.drawImage(this.boomImage, n - 16 * by, n2, 4 | 0x10);
    }

    public void paint(Graphics graphics) {
        if (!this.isBoom) {
            int n = this.currentX * 16 - 8;
            int n2 = 8 + this.currentY * 16;
            graphics.setClip(n, n2, 16, 16);
            graphics.drawImage(this.cursorImage, n - 16 * this.currentPipe, n2, 4 | 0x10);
        } else if (this.frame >= 0 && this.frame < 8) {
            this.drawBoom(graphics, this.frame, this.currentX * 16 - 8, 8 + this.currentY * 16);
        }
    }

    public void boom() {
        this.boom = new Boom();
        this.boom.start();
    }

    static /* synthetic */ byte access$108(Cursor cursor) {
        byte by = cursor.frame;
        cursor.frame = (byte)(by + 1);
        return by;
    }

    private class Boom extends JMEThread {
        boolean isAlive;

        private Boom() {
        }

        public final void run() {
            Cursor.this.isBoom = true;
            byte by = Cursor.this.getX();
            byte by2 = Cursor.this.getY();
            Cursor.this.frame = (byte)0;
            while (Cursor.this.frame < 8) {
                Cursor.access$108(Cursor.this);
                Cursor.this.canvas.repaintPos(by, by2);
                try {
                    Thread.sleep(60L);
                }
                catch (Exception exception) {}
            }
            Cursor.this.isBoom = false;
            Cursor.this.canvas.serviceRepaints();
        }

        public void stop() {
            this.isAlive = false;
            if (Cursor.this.boom != null) {
                Cursor.this.boom = null;
            }
        }
    }
}

