class Pipe {
    public byte pos_x;
    public byte pos_y;
    public byte in_x;
    public byte in_y;
    public byte out_x;
    public byte out_y;
    public boolean isVertical;
    public byte type;
    Board board;
    public boolean isReversed;

    public Pipe(Board board) {
        this.board = board;
    }

    public void set(byte by, byte by2, byte by3) {
        this.type = by3;
        this.pos_x = by;
        this.pos_y = by2;
        this.isReversed = false;
        switch (this.type) {
            case 1: {
                this.in_x = (byte)(by + 1);
                this.in_y = by2;
                this.out_x = by;
                this.out_y = (byte)(by2 + 1);
                this.isVertical = true;
                break;
            }
            case 2: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = by;
                this.out_y = (byte)(by2 + 1);
                this.isVertical = true;
                break;
            }
            case 3: {
                this.in_x = (byte)(by + 1);
                this.in_y = by2;
                this.out_x = by;
                this.out_y = (byte)(by2 - 1);
                this.isVertical = true;
                break;
            }
            case 4: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = by;
                this.out_y = (byte)(by2 - 1);
                this.isVertical = true;
                break;
            }
            case 5: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = (byte)(by + 1);
                this.out_y = by2;
                this.isVertical = false;
                break;
            }
            case 6: {
                this.in_x = by;
                this.in_y = (byte)(by2 - 1);
                this.out_x = by;
                this.out_y = (byte)(by2 + 1);
                this.isVertical = true;
                break;
            }
            case 8: {
                this.in_x = (byte)(by + 1);
                this.in_y = by2;
                this.out_x = (byte)(by + 1);
                this.out_y = by2;
                this.isVertical = false;
                break;
            }
            case 9: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = (byte)(by - 1);
                this.out_y = by2;
                this.isVertical = false;
                break;
            }
            case 11: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = this.board.getMapOutX();
                this.out_y = this.board.getMapOutY();
                break;
            }
            case 12: {
                this.in_x = this.board.getMapInX();
                this.in_y = this.board.getMapInY();
                this.out_x = (byte)(by + 1);
                this.out_y = by2;
                break;
            }
            case 13: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = (byte)(by + 1);
                this.out_y = by2;
                this.isVertical = false;
                break;
            }
            case 14: {
                this.in_x = by;
                this.in_y = (byte)(by2 - 1);
                this.out_x = by;
                this.out_y = (byte)(by2 + 1);
                this.isVertical = true;
                break;
            }
            case 15: {
                this.in_x = by;
                this.in_y = (byte)(by2 + 1);
                this.out_x = by;
                this.out_y = (byte)8;
                break;
            }
            case 16: {
                this.in_x = by;
                this.in_y = (byte)(by2 - 1);
                this.out_x = by;
                this.out_y = 0;
                break;
            }
            case 17: {
                this.in_x = (byte)(by + 1);
                this.in_y = by2;
                this.out_x = (byte)8;
                this.out_y = by2;
                break;
            }
            case 18: {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = 0;
                this.out_y = by2;
                break;
            }
        }
    }

    public void set(byte by, byte by2, byte by3, boolean bl) {
        this.set(by, by2, by3);
        if (by3 == 7) {
            if (bl) {
                this.in_x = by;
                this.in_y = (byte)(by2 - 1);
                this.out_x = by;
                this.out_y = (byte)(by2 + 1);
                this.isVertical = bl;
            } else {
                this.in_x = (byte)(by - 1);
                this.in_y = by2;
                this.out_x = (byte)(by + 1);
                this.out_y = by2;
                this.isVertical = bl;
            }
        }
    }

    public void changeInOut() {
        byte by = this.in_x;
        this.in_x = this.out_x;
        this.out_x = by;
        by = this.in_y;
        this.in_y = this.out_y;
        this.out_y = by;
        this.isReversed = !this.isReversed;
        switch (this.type) {
            case 1: 
            case 2: 
            case 3: 
            case 4: {
                this.isVertical = !this.isVertical;
                break;
            }
        }
    }
}

