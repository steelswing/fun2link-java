
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
class LinkCheck {
    Board board;
    Pipe current;
    Pipe next;
    Fun2LinkCanvas canvas;
    int linkCount;
    final int white;
    final int black;
    int blue = 3185146;
    private Image ok;
    private Image linkmore;
    private Image lastPointer;
    boolean lastPointerOn = true;
    Blink blink;
    byte lastPointerX;
    byte lastPointerY;
    byte lastPointerType;
    private Graphics m_bufferGraphics;
    private Image imageBuffer;

    public LinkCheck(Board board, Fun2LinkCanvas fun2LinkCanvas) {
        this.white = 0xFFFFFF;
        this.black = 0;
        this.board = board;
        this.canvas = fun2LinkCanvas;
        this.current = new Pipe(this.board);
        this.next = new Pipe(this.board);
        this.linkCount = 0;
        try {
            this.ok = ImageUtil.createImage("/icons/cursor19.png");
            this.linkmore = ImageUtil.createImage("/icons/cursor20.png");
            this.lastPointer = ImageUtil.createImage("/icons/lastPointer.png");
            this.imageBuffer = ImageUtil.createImage((int)128, (int)16);
        }
        catch (Exception exception) {
            System.out.println("Image Load Error");
        }
        this.blink = null;
        this.m_bufferGraphics = new Graphics((Graphics2D) this.imageBuffer.getGraphics());
    }

    public LinkCheck(Board board, Fun2LinkCanvas fun2LinkCanvas, int n) {
        this.white = 0xFFFFFF;
        this.black = 0;
        this.board = board;
        this.canvas = fun2LinkCanvas;
        this.current = new Pipe(this.board);
        this.next = new Pipe(this.board);
        this.linkCount = 0;
    }

    public void initLastPointer() {
        this.lastPointerX = (byte)(this.board.getMapStartX() + 1);
        this.lastPointerY = this.board.getMapStartY();
        this.lastPointerType = 0;
    }

    public void startLinkCheckAnimation() {
        if (this.blink != null) {
            return;
        }
        this.blink = new Blink();
        this.blink.start();
    }

    public void stopLinkCheckAnimation() {
        if (this.blink == null) {
            return;
        }
        this.blink.stop();
    }

    public void init() {
        this.stopLinkCheckAnimation();
        this.linkCount = 0;
        this.lastPointerOn = true;
    }

    public void startLinkCheck() {
        this.linkCount = 0;
        this.set(this.board.getMapStartX(), this.board.getMapStartY());
        for (int i = 0; i < 100; ++i) {
            if (this.checkConnect()) {
                if (this.next.type == 9) {
                    this.lastPointerOn = false;
                    this.startLinkCheckAnimation();
                    break;
                }
                if (this.next.type >= 1 && this.next.type <= 7) {
                    ++this.linkCount;
                }
            } else {
                this.lastPointerX = this.current.out_x;
                this.lastPointerY = this.current.out_y;
                this.board.exPointerX = this.lastPointerX;
                this.board.exPointerY = this.lastPointerY;
                if ((byte)(this.lastPointerX - 1) == this.current.pos_x) {
                    this.lastPointerType = 0;
                } else if ((byte)(this.lastPointerX + 1) == this.current.pos_x) {
                    this.lastPointerType = 1;
                } else if ((byte)(this.lastPointerY - 1) == this.current.pos_y) {
                    this.lastPointerType = (byte)2;
                } else if ((byte)(this.lastPointerY + 1) == this.current.pos_y) {
                    this.lastPointerType = (byte)3;
                }
                this.lastPointerOn = true;
                this.stopLinkCheckAnimation();
                break;
            }
            this.move();
        }
        this.canvas.repaintDest();
    }

    public void set(byte by, byte by2) {
        byte by3 = this.board.get(by, by2);
        this.current.set(by, by2, by3);
        byte by4 = this.current.out_x;
        byte by5 = this.current.out_y;
        by3 = this.board.get(by4, by5);
        this.next.set(by4, by5, by3, this.current.isVertical);
    }

    public void move() {
        boolean bl = this.next.isVertical;
        byte by = this.next.out_x;
        byte by2 = this.next.out_y;
        this.current.pos_x = this.next.pos_x;
        this.current.pos_y = this.next.pos_y;
        this.current.in_x = this.next.in_x;
        this.current.in_y = this.next.in_y;
        this.current.out_x = this.next.out_x;
        this.current.out_y = this.next.out_y;
        this.current.isVertical = this.next.isVertical;
        this.current.type = this.next.type;
        this.current.isReversed = this.next.isReversed;
        this.next.set(by, by2, this.board.get(by, by2), bl);
    }

    public boolean checkConnect() {
        if (this.next.pos_x < 0 || this.next.pos_y < 0) {
            return false;
        }
        if (this.next.pos_x > 8 || this.next.pos_y > 8) {
            return false;
        }
        if (this.next.type == 0) {
            return false;
        }
        if (this.next.in_x != this.current.pos_x || this.next.in_y != this.current.pos_y) {
            this.next.changeInOut();
        }
        return this.next.in_x == this.current.pos_x && this.next.in_y == this.current.pos_y;
    }

    public void paint(Graphics graphics) {
        graphics.setClip(70, 0, 58, 24);
        graphics.setColor(0);
        graphics.fillRect(70, 0, 58, 24);
        Font font = J2MEFont.getFont((int)0, (int)1, (int)8);
        graphics.setFont(font);
        graphics.setColor(0xFFFFFF);
        String string = this.linkCount + "/" + this.board.getMapDest();
        graphics.drawString(string, 127, 1, 24);
        graphics.drawString("Dest", 70, 1, 20);
        string = this.canvas.totalScore == 0 ? "0" : this.canvas.totalScore + "00";
        graphics.drawString(string, 127, 12, 24);
        graphics.drawString("Score", 70, 12, 20);
        if (this.lastPointerOn) {
            int n = this.lastPointerX * 16 - 8;
            int n2 = 8 + this.lastPointerY * 16;
            if (this.lastPointerY <= 7) {
                graphics.setClip(n, n2, 16, 16);
                graphics.drawImage(this.lastPointer, n - 32 * this.lastPointerType, n2, 4 | 0x10);
            }
        } else if (this.blink != null && this.blink.blinkOn) {
            int n = this.board.getMapEndX() * 16 - 8;
            int n3 = 8 + this.board.getMapEndY() * 16;
            if (this.linkCount < this.board.getMapDest()) {
                graphics.setClip(n - 3, n3, 22, 16);
                graphics.drawImage(this.linkmore, n - 3, n3, 4 | 0x10);
            } else {
                graphics.setClip(n, n3, 16, 16);
                graphics.drawImage(this.ok, n, n3, 4 | 0x10);
            }
        }
    }

    public class Blink
    extends JMEThread {
        boolean blinkOn = false;
        boolean isAlive = false;

        public final void run() {
            this.isAlive = true;
            while (this.isAlive) {
                this.blinkOn = !this.blinkOn;
                byte by = LinkCheck.this.board.getMapEndX();
                byte by2 = LinkCheck.this.board.getMapEndY();
                LinkCheck.this.canvas.repaintNear(by, by2);
                try {
                    Thread.sleep(1000L);
                }
                catch (Exception exception) {}
            }
        }

        public void stop() {
            this.isAlive = false;
            if (LinkCheck.this.blink != null) {
                LinkCheck.this.blink = null;
            }
        }
    }
}

