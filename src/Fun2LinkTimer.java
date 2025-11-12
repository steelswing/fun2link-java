import java.util.TimerTask;

class Fun2LinkTimer
extends TimerTask {
    Fun2LinkCanvas canvas;
    Board board;
    int limit;

    Fun2LinkTimer(Fun2LinkCanvas fun2LinkCanvas, Board board) {
        this.canvas = fun2LinkCanvas;
        this.board = board;
    }

    public void run() {
        try {
            if (this.board.isPause) {
                return;
            }
            if (this.board.timerCount >= 0 && this.board.timerCount <= 10) {
                this.canvas.repaint(0, 0, 128, 160);
            }
            ++this.board.timerCount;
            this.canvas.drawTimer();
            this.canvas.repaintTimer();
            this.limit = this.board.isFast ? 28 : 28;
            if (this.board.timerCount >= this.limit) {
                this.canvas.startWater();
                this.cancel();
                this.canvas.repaint();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

