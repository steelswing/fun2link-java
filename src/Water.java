class Water extends LinkCheck implements Runnable {
    final int blue;
    int frame;
    boolean isStaying = true;
    boolean isClear = false;
    Thread waterThread = null;
    int crossCount;
    boolean isAlive;

    public Water(Board board, Fun2LinkCanvas fun2LinkCanvas) {
        super(board, fun2LinkCanvas, 1);
        this.blue = 3185146;
    }

    public void drawWater() {
        this.frame = 0;
        while (this.frame <= 16) {
            if (!this.isAlive) {
                return;
            }
            this.canvas.repaintPos(this.current.pos_x, this.current.pos_y);
            if (this.frame != 16) {
                try {
                    if (this.board.isFast) {
                        if (this.isStaying) {
                            Thread.sleep(300L);
                        } else {
                            Thread.sleep(50L);
                        }
                    } else if (this.isStaying) {
                        Thread.sleep(2000L);
                    } else {
                        Thread.sleep(500L);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            this.frame += 4;
        }
        byte by = this.board.getWaterMap(this.current.pos_x, this.current.pos_y);
        if (this.current.type == 7) {
            if (by == 0) {
                if (this.current.isVertical) {
                    this.board.putWaterMap(this.current.pos_x, this.current.pos_y, (byte)6);
                } else {
                    this.board.putWaterMap(this.current.pos_x, this.current.pos_y, (byte)5);
                }
            } else {
                ++this.crossCount;
                this.board.putWaterMap(this.current.pos_x, this.current.pos_y, this.current.type);
            }
        } else {
            this.board.putWaterMap(this.current.pos_x, this.current.pos_y, this.current.type);
        }
        this.canvas.serviceRepaints();
    }

    public void resetNext() {
        byte by = this.current.out_x;
        byte by2 = this.current.out_y;
        byte by3 = this.board.get(by, by2);
        this.next.set(by, by2, by3, this.current.isVertical);
    }

    public void start() {
        if (this.waterThread == null) {
            this.waterThread = new Thread(this);
            this.waterThread.start();
        }
    }

    public void set(byte by, byte by2) {
        byte by3 = this.board.get(by, by2);
        this.current.set(by, by2, by3);
        byte by4 = this.current.out_x;
        byte by5 = this.current.out_y;
        by3 = this.board.get(by4, by5);
        this.next.set(by4, by5, by3, this.current.isVertical);
    }

    public void run() {
        this.isAlive = true;
        this.linkCount = 0;
        this.crossCount = 0;
        this.set(this.board.getMapStartX(), this.board.getMapStartY());
        while (true) {
            this.drawWater();
            if (!this.isAlive) {
                return;
            }
            this.resetNext();
            if (this.checkConnect()) {
                if (this.current.type == 9) {
                    this.isClear = true;
                    break;
                }
                if (this.current.type >= 1 && this.current.type <= 7) {
                    ++this.linkCount;
                }
            } else {
                this.isClear = false;
                break;
            }
            this.move();
        }
        if (this.isClear && this.linkCount >= this.board.getMapDest()) {
            System.out.println("Clear!!!");
            this.canvas.gameClear();
        } else {
            System.out.println("Game over!!!");
            this.canvas.gameOver();
        }
    }

    public void stop() {
        this.isAlive = false;
        this.frame = 0;
        if (this.waterThread != null) {
            this.waterThread = null;
        }
    }

    public void paint(Graphics graphics) {
        int n = this.current.pos_x * 16 - 8;
        int n2 = 24 + this.current.pos_y * 16 - 16;
        int n3 = 4;
        if (this.frame > 0) {
            graphics.setClip(n, n2, 16, 16);
            graphics.setColor(3185146);
            switch (this.current.type) {
                case 1: {
                    if (!this.current.isReversed) {
                        if (this.frame <= 8) {
                            int n4 = n + (16 - this.frame);
                            int n5 = n2 + 6;
                            graphics.fillRect(n4, n5, this.frame, n3);
                        } else {
                            int n6 = n + 8;
                            int n7 = n2 + 6;
                            graphics.fillRect(n6, n7, 8, n3);
                            n6 = n + 6;
                            n7 = n2 + 8;
                            graphics.fillRect(n6, n7, n3, this.frame - 8);
                        }
                    } else if (this.frame <= 8) {
                        int n8 = n + 6;
                        int n9 = n2 + (16 - this.frame);
                        graphics.fillRect(n8, n9, n3, this.frame);
                    } else {
                        int n10 = n + 6;
                        int n11 = n2 + 8;
                        graphics.fillRect(n10, n11, n3, 8);
                        n10 = n + 8;
                        n11 = n2 + 6;
                        graphics.fillRect(n10, n11, this.frame - 8, n3);
                    }
                    this.isStaying = false;
                    break;
                }
                case 2: {
                    if (!this.current.isReversed) {
                        if (this.frame <= 8) {
                            int n12 = n;
                            int n13 = n2 + 6;
                            graphics.fillRect(n12, n13, this.frame, n3);
                        } else {
                            int n14 = n;
                            int n15 = n2 + 6;
                            graphics.fillRect(n14, n15, 8, n3);
                            n14 = n + 6;
                            n15 = n2 + 8;
                            graphics.fillRect(n14, n15, n3, this.frame - 8);
                        }
                    } else if (this.frame <= 8) {
                        int n16 = n + 6;
                        int n17 = n2 + (16 - this.frame);
                        graphics.fillRect(n16, n17, n3, this.frame);
                    } else {
                        int n18 = n + 6;
                        int n19 = n2 + 8;
                        graphics.fillRect(n18, n19, n3, 8);
                        n18 = n + (16 - this.frame);
                        n19 = n2 + 6;
                        graphics.fillRect(n18, n19, this.frame - 8, n3);
                    }
                    this.isStaying = false;
                    break;
                }
                case 3: {
                    if (!this.current.isReversed) {
                        if (this.frame <= 8) {
                            int n20 = n + (16 - this.frame);
                            int n21 = n2 + 6;
                            graphics.fillRect(n20, n21, this.frame, n3);
                        } else {
                            int n22 = n + 8;
                            int n23 = n2 + 6;
                            graphics.fillRect(n22, n23, 8, n3);
                            n22 = n + 6;
                            n23 = n2 + (16 - this.frame);
                            graphics.fillRect(n22, n23, n3, this.frame - 8);
                        }
                    } else if (this.frame <= 8) {
                        int n24 = n + 6;
                        int n25 = n2;
                        graphics.fillRect(n24, n25, n3, this.frame);
                    } else {
                        int n26 = n + 6;
                        int n27 = n2;
                        graphics.fillRect(n26, n27, n3, 8);
                        n26 = n + 8;
                        n27 = n2 + 6;
                        graphics.fillRect(n26, n27, this.frame - 8, n3);
                    }
                    this.isStaying = false;
                    break;
                }
                case 4: {
                    if (!this.current.isReversed) {
                        if (this.frame <= 8) {
                            int n28 = n;
                            int n29 = n2 + 6;
                            graphics.fillRect(n28, n29, this.frame, n3);
                        } else {
                            int n30 = n;
                            int n31 = n2 + 6;
                            graphics.fillRect(n30, n31, 8, n3);
                            n30 = n + 6;
                            n31 = n2 + (16 - this.frame);
                            graphics.fillRect(n30, n31, n3, this.frame - 8);
                        }
                    } else if (this.frame <= 8) {
                        int n32 = n + 6;
                        int n33 = n2;
                        graphics.fillRect(n32, n33, n3, this.frame);
                    } else {
                        int n34 = n + 6;
                        int n35 = n2;
                        graphics.fillRect(n34, n35, n3, 8);
                        n34 = n + (16 - this.frame);
                        n35 = n2 + 6;
                        graphics.fillRect(n34, n35, this.frame - 8, n3);
                    }
                    this.isStaying = false;
                    break;
                }
                case 5: {
                    if (!this.current.isReversed) {
                        int n36 = n;
                        int n37 = n2;
                        graphics.fillRect(n36, n37 + 6, this.frame, n3);
                    } else {
                        int n38 = n + (16 - this.frame);
                        int n39 = n2;
                        graphics.fillRect(n38, n39 + 6, this.frame, n3);
                    }
                    this.isStaying = false;
                    break;
                }
                case 6: {
                    if (!this.current.isReversed) {
                        int n40 = n;
                        int n41 = n2;
                        graphics.fillRect(n40 + 6, n41, n3, this.frame);
                    } else {
                        int n42 = n;
                        int n43 = n2 + (16 - this.frame);
                        graphics.fillRect(n42 + 6, n43, n3, this.frame);
                    }
                    this.isStaying = false;
                    break;
                }
                case 7: {
                    if (this.current.isVertical) {
                        if (!this.current.isReversed) {
                            int n44 = n;
                            int n45 = n2;
                            graphics.fillRect(n44 + 6, n45, n3, this.frame);
                        } else {
                            int n46 = n;
                            int n47 = n2 + (16 - this.frame);
                            graphics.fillRect(n46 + 6, n47, n3, this.frame);
                        }
                    } else if (!this.current.isReversed) {
                        int n48 = n;
                        int n49 = n2;
                        graphics.fillRect(n48, n49 + 6, this.frame, n3);
                    } else {
                        int n50 = n + (16 - this.frame);
                        int n51 = n2;
                        graphics.fillRect(n50, n51 + 6, this.frame, n3);
                    }
                    this.isStaying = false;
                    break;
                }
                case 8: {
                    if (this.frame <= 8) {
                        int n52 = n + 4 + (8 - this.frame) / n3;
                        int n53 = n2 + 4 + (8 - this.frame) / n3;
                        graphics.fillArc(n52, n53, this.frame, this.frame, 0, 360);
                    } else {
                        int n54 = n + 4;
                        int n55 = n2 + 4;
                        graphics.fillArc(n54, n55, 8, 8, 0, 360);
                        n54 = n + 8;
                        n55 = n2 + 6;
                        graphics.fillRect(n54, n55, this.frame - 8, n3);
                    }
                    this.isStaying = true;
                    break;
                }
                case 9: {
                    if (this.frame <= 8) {
                        int n56 = n;
                        int n57 = n2 + 6;
                        graphics.fillRect(n56, n57, this.frame, n3);
                    } else {
                        int n58 = n;
                        int n59 = n2 + 6;
                        graphics.fillRect(n58, n59, 8, n3);
                        n58 = n + 4 + (16 - this.frame) / n3;
                        n59 = n2 + 4 + (16 - this.frame) / n3;
                        graphics.fillArc(n58, n59, this.frame - 8, this.frame - 8, 0, 360);
                    }
                    this.isStaying = true;
                    break;
                }
                case 11: {
                    break;
                }
                case 12: {
                    break;
                }
                case 13: {
                    int n60;
                    int n61;
                    if (!this.current.isReversed) {
                        n61 = n;
                        n60 = n2;
                        graphics.fillRect(n61, n60 + 6, this.frame, n3);
                    } else {
                        n61 = n + (16 - this.frame);
                        n60 = n2;
                        graphics.fillRect(n61, n60 + 6, this.frame, n3);
                    }
                    if (this.frame >= 8) {
                        n61 = n + 4 + (16 - this.frame) / 2;
                        n60 = n2 + 4 + (16 - this.frame) / 2;
                        graphics.fillArc(n61, n60, this.frame - 8, this.frame - 8, 0, 360);
                    }
                    this.isStaying = true;
                    break;
                }
                case 14: {
                    int n62;
                    int n63;
                    if (!this.current.isReversed) {
                        n63 = n;
                        n62 = n2;
                        graphics.fillRect(n63 + 6, n62, n3, this.frame);
                    } else {
                        n63 = n;
                        n62 = n2 + (16 - this.frame);
                        graphics.fillRect(n63 + 6, n62, n3, this.frame);
                    }
                    if (this.frame >= 8) {
                        n63 = n + 4 + (16 - this.frame) / 2;
                        n62 = n2 + 4 + (16 - this.frame) / 2;
                        graphics.fillArc(n63, n62, this.frame - 8, this.frame - 8, 0, 360);
                    }
                    this.isStaying = true;
                    break;
                }
            }
        }
    }
}

