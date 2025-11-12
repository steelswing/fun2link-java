import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Board {

    protected Color black = Color.BLACK;
    protected Color white = Color.white;
    protected Color blue = new Color(3185146);
    private byte mapDest;
    private byte mapStartX;
    private byte mapStartY;
    private byte mapEndX;
    private byte mapEndY;
    private byte curStartX = 0;
    private byte curStartY = 0;
    private byte mapInX;
    private byte mapInY;
    private byte mapOutX;
    private byte mapOutY;
    private byte[][] map;
    private byte[][] waterMap;
    public byte[] next;
    private byte[][] mapBackup;
    private byte[][] waterMapBackup;
    public byte[] nextBackup;
    boolean isFast;
    boolean isPause;
    int timerCount;
    int timerCountBackup;
    long limitTime;
    int exPointerX;
    int exPointerY;
    private Image pipeImage;
    private Image imageUpboard;
    private Image imageFast;
    private Image imageEnd;
    private Image lastPointer;
    private Random rand = new Random();
    private Graphics m_bufferGraphics;

    public Board() {
        this.map = new byte[9][9];
        this.waterMap = new byte[9][9];
        this.next = new byte[4];
        this.mapBackup = new byte[9][9];
        this.waterMapBackup = new byte[9][9];
        this.nextBackup = new byte[4];
        try {
            this.pipeImage = ImageUtil.createImage("/icons/pipes.png");
            this.imageUpboard = ImageUtil.createImage("/icons/upboard.png");
        } catch (Exception exception) {
            System.out.println("Image Load Error");
        }
        try {
            this.imageFast = ImageUtil.createImage("/icons/fast.png");
            this.imageEnd = ImageUtil.createImage("/icons/end.png");
            this.lastPointer = ImageUtil.createImage("/icons/lastPointer.png");
        } catch (Exception exception) {
            System.out.println("Image Load Error");
        }
        this.exPointerY = 0;
        this.exPointerX = 0;
    }

    public void init() {
        this.isPause = false;
        this.isFast = false;
        for (int i = 0; i < 4; ++i) {
            this.next[i] = (byte) (Math.abs(this.rand.nextInt()) % 7 + 1);
        }
    }

    public void saveMap() {
        int n;
        for (n = 0; n < 4; ++n) {
            this.nextBackup[n] = this.next[n];
        }
        for (n = 0; n < 9; ++n) {
            for (int i = 0; i < 9; ++i) {
                this.waterMapBackup[n][i] = this.waterMap[n][i];
                this.mapBackup[n][i] = this.map[n][i];
            }
        }
        this.timerCountBackup = this.timerCount;
    }

    public void loadMap() {
        int n;
        for (n = 0; n < 4; ++n) {
            this.next[n] = this.nextBackup[n];
        }
        for (n = 0; n < 9; ++n) {
            for (int i = 0; i < 9; ++i) {
                this.waterMap[n][i] = this.waterMapBackup[n][i];
                this.map[n][i] = this.mapBackup[n][i];
            }
        }
        this.timerCount = this.timerCountBackup;
    }

    public void reset() {
        int n;
        int n2;
        for (n2 = 0; n2 < 9; ++n2) {
            for (n = 0; n < 9; ++n) {
                this.waterMap[n2][n] = 0;
            }
        }
        for (n2 = 0; n2 < 9; ++n2) {
            for (n = 0; n < 9; ++n) {
                this.map[n2][n] = 0;
            }
        }
        this.init();
    }

    public byte get(byte by, byte by2) {
        if (by >= 0 && by <= 8 && by2 >= 0 && by2 <= 8) {
            return this.map[by2][by];
        }
        return 0;
    }

    public byte get(int n, int n2) {
        if (n >= 0 && n <= 8 && n2 >= 0 && n2 <= 8) {
            return this.map[n2][n];
        }
        return 0;
    }

    public byte getMapDest() {
        return this.mapDest;
    }

    public void set(byte by, byte by2, byte by3) {
        this.map[by2][by] = by3;
    }

    public void set(int n, int n2, int n3) {
        this.map[n2][n] = (byte) n3;
    }

    public void putPipe(byte by, byte by2) {
        this.map[by2][by] = this.next[3];
        this.next[3] = this.next[2];
        this.next[2] = this.next[1];
        this.next[1] = this.next[0];
        this.next[0] = (byte) (Math.abs(this.rand.nextInt()) % 7 + 1);
    }

    public byte getCurrentPipe() {
        return this.next[3];
    }

    public byte getCurStartX() {
        return this.curStartX;
    }

    public byte getCurStartY() {
        return this.curStartY;
    }

    public byte getMapStartX() {
        return this.mapStartX;
    }

    public byte getMapStartY() {
        return this.mapStartY;
    }

    public byte getMapEndX() {
        return this.mapEndX;
    }

    public byte getMapEndY() {
        return this.mapEndY;
    }

    public byte getMapInX() {
        return this.mapInX;
    }

    public byte getMapInY() {
        return this.mapInY;
    }

    public byte getMapOutX() {
        return this.mapOutX;
    }

    public byte getMapOutY() {
        return this.mapOutY;
    }

    public void putWaterMap(byte by, byte by2, byte by3) {
        this.waterMap[by2][by] = by3;
    }

    public byte getWaterMap(byte by, byte by2) {
        return this.waterMap[by2][by];
    }

    public void drawPipe(Graphics graphics, byte by, int n, int n2) {
        graphics.setClip(n, n2, 16, 16);
        graphics.drawImage(this.pipeImage, n - 16 * by, n2, 4 | 0x10);
    }

    public void paint(Graphics graphics) {
        byte by;
        int n;
        graphics.setClip(0, 120, 128, 8);
        graphics.setColor(this.black);
        graphics.fillRect(0, 120, 128, 8);
        graphics.setColor(this.white);
        graphics.drawRect(35, 121, 57, 6);
        graphics.setColor(this.blue);
        graphics.fillRect(37, 122, (this.timerCount - 10) * 3, 4);
        graphics.setClip(0, 120, 34, 8);
        if (this.isFast) {
            graphics.drawImage(this.imageFast, 1, 128, 0x20 | 4);
        } else {
            graphics.drawImage(this.imageFast, -26, 128, 0x20 | 4);
        }
        graphics.setClip(94, 120, 34, 8);
        graphics.drawImage(this.imageEnd, 128, 128, 0x20 | 8);
        if (this.exPointerY == 8) {
            n = this.exPointerX * 16 - 8;
            graphics.setClip(n, 112, 16, 16);
            graphics.drawImage(this.lastPointer, n - 64, 0, 4 | 0x10);
        }
        for (n = 1; n < 7; ++n) {
            for (int i = 0; i < 9; ++i) {
                by = this.map[n][i];
                this.drawPipe(graphics, by, i * 16 - 8, 8 + n * 16);
                by = this.waterMap[n][i];
                this.drawWater(graphics, i, n, by);
            }
        }
        graphics.setClip(0, 0, 128, 24);
        graphics.setColor(this.black);
        graphics.fillRect(0, 0, 128, 24);
        graphics.setClip(0, 2, 69, 20);
        graphics.drawImage(this.imageUpboard, 0, 2, 4 | 0x10);
        for (n = 0; n < 4; ++n) {
            by = this.next[n];
            this.drawPipe(graphics, by, n * 16 + 2, 4);
        }
    }

    public void drawWater(Graphics graphics, int n, int n2, byte by) {
        int n3 = n * 16 - 8;
        int n4 = 24 + n2 * 16 - 16;
        graphics.setColor(this.blue);
        switch (by) {
            case 0: {
                break;
            }
            case 1: {
                int n5 = n3 + 8;
                int n6 = n4;
                graphics.fillRect(n5, n6 + 6, 8, 4);
                n5 = n3 + 6;
                n6 = n4 + 8;
                graphics.fillRect(n5, n6, 4, 8);
                break;
            }
            case 2: {
                int n7 = n3;
                int n8 = n4;
                graphics.fillRect(n7, n8 + 6, 8, 4);
                n7 = n3 + 6;
                n8 = n4 + 8;
                graphics.fillRect(n7, n8, 4, 8);
                break;
            }
            case 3: {
                int n9 = n3 + 8;
                int n10 = n4;
                graphics.fillRect(n9, n10 + 6, 8, 4);
                n9 = n3 + 6;
                n10 = n4;
                graphics.fillRect(n9, n10, 4, 8);
                break;
            }
            case 4: {
                int n11 = n3;
                int n12 = n4;
                graphics.fillRect(n11, n12 + 6, 8, 4);
                n11 = n3 + 6;
                n12 = n4;
                graphics.fillRect(n11, n12, 4, 8);
                break;
            }
            case 5: {
                int n13 = n3;
                int n14 = n4 + 6;
                graphics.fillRect(n13, n14, 16, 4);
                break;
            }
            case 6: {
                int n15 = n3 + 6;
                int n16 = n4;
                graphics.fillRect(n15, n16, 4, 16);
                break;
            }
            case 7: {
                int n17 = n3;
                int n18 = n4 + 6;
                graphics.fillRect(n17, n18, 16, 4);
                n17 = n3 + 6;
                n18 = n4;
                graphics.fillRect(n17, n18, 4, 16);
                break;
            }
            case 8: {
                int n19 = n3 + 4;
                int n20 = n4 + 4;
                graphics.fillArc(n19, n20, 8, 8, 0, 360);
                n19 = n3 + 8;
                n20 = n4;
                graphics.fillRect(n19, n20 + 6, 8, 4);
                break;
            }
            case 13: {
                int n21 = n3 + 2;
                int n22 = n4 + 2;
                graphics.fillArc(n21, n22, 10, 10, 0, 360);
                n21 = n3;
                n22 = n4;
                graphics.fillRect(n21, n22 + 6, 16, 4);
                break;
            }
            case 14: {
                int n23 = n3 + 2;
                int n24 = n4 + 2;
                graphics.fillArc(n23, n24, 10, 10, 0, 360);
                n23 = n3;
                n24 = n4;
                graphics.fillRect(n23 + 6, n24, 4, 16);
                break;
            }
        }
    }

    public void read(InputStream inputStream, byte by) {
        boolean bl = false;
        byte by2 = 0;
        int n = 0;
        byte by3 = 0;
        String string = new String();
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.waterMap[i][j] = 0;
                this.map[i][j] = 0;
            }
        }
        try {
            int n2;
            byte by4;
            string = "";
            while ((by4 = (byte) inputStream.read()) != -1 && by4 != 10) {
                if (by4 == 13) {
                    continue;
                }
                string = string + (char) by4;
            }
            this.mapDest = (byte) Integer.parseInt(string);
            string = "";
            while ((by4 = (byte) inputStream.read()) != -1 && by4 != 10) {
                if (by4 == 13) {
                    continue;
                }
                string = string + (char) by4;
            }
            this.limitTime = Integer.parseInt(string);
            block23:
            while ((n2 = inputStream.read()) != -1) {
                switch (n2) {
                    case 10: {
                        if (by2 > by3) {
                            by3 = by2;
                        }
                        n = (byte) (n + 1);
                        by2 = 0;
                        continue block23;
                    }
                    case 83: {
                        this.mapStartX = by2;
                        this.mapStartY = (byte) n;
                        byte by5 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by5] = 8;
                        continue block23;
                    }
                    case 69: {
                        this.mapEndX = by2;
                        this.mapEndY = (byte) n;
                        byte by6 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by6] = 9;
                        continue block23;
                    }
                    case 35: {
                        byte by7 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by7] = 0;
                        continue block23;
                    }
                    case 43: {
                        this.curStartX = by2;
                        this.curStartY = (byte) n;
                        by2 = (byte) (by2 + 1);
                        continue block23;
                    }
                    case 64: {
                        byte by8 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by8] = 10;
                        continue block23;
                    }
                    case 105: {
                        this.mapInX = by2;
                        this.mapInY = (byte) n;
                        byte by9 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by9] = 11;
                        continue block23;
                    }
                    case 111: {
                        this.mapOutX = by2;
                        this.mapOutY = (byte) n;
                        byte by10 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by10] = 12;
                        continue block23;
                    }
                    case 45: {
                        byte by11 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by11] = 13;
                        continue block23;
                    }
                    case 124: {
                        byte by12 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by12] = 14;
                        continue block23;
                    }
                    case 126: {
                        byte by13 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by13] = 0;
                        continue block23;
                    }
                    case 117: {
                        byte by14 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by14] = 15;
                        continue block23;
                    }
                    case 100: {
                        byte by15 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by15] = 16;
                        continue block23;
                    }
                    case 108: {
                        byte by16 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by16] = 17;
                        continue block23;
                    }
                    case 114: {
                        byte by17 = by2;
                        by2 = (byte) (by2 + 1);
                        this.map[n][by17] = 18;
                        continue block23;
                    }
                }
                byte by18 = by2;
                by2 = (byte) (by2 + 1);
                this.map[n][by18] = 0;
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}
