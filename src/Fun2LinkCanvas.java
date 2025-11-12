
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

class Fun2LinkCanvas implements KeyListener {

    private Fun2Link fun2link;
    private Cursor cursor;
    public Board board;
    private LinkCheck linkCheck;
    public Water water;
    private Timer timer;
    private TimerTask timertask;
    private Score rmsScore;
    private Stage rmsStage;
    private Cross rmsCross;
    private Image imageMain;
    private Image imageMenuOn1;
    private Image imageMenuOn2;
    private Image imageMenuOn3;
    private Image imageNumber;
    private Image imageBack;
    private Image imageMsgTitle;
    private Image img_spos;
    private Image img_epos;
    private Image img_gameover;
    private AudioClip explode;
    private AudioClip gunshot;
    private AudioClip titlesound;
    private AudioClip select;
    private AudioClip selectok;
    private AudioClip clearshot;
    private AudioClip stagestart;
    private byte selectMode;
    private String message1;
    private String message2;
    private String message3;
    private boolean soundOn = true;
    private boolean preSoundOn;
    private Ticker ticker;
    private Demo demo;
    private byte cnt = 0;


    // === КОНСТАНТЫ РЕЖИМОВ ИГРЫ ===
    private static final byte MODE_NULL = 0;
    private static final byte MODE_START = 1;
    private static final byte MODE_PLAY = 2;
    private static final byte MODE_CLEAR1 = 3;
    private static final byte MODE_CLEAR2 = 4;
    private static final byte MODE_GAMEOVER = 5;
    private static final byte MODE_HOWTO = 6;
    private static final byte MODE_EXIT = 7;
    private static final byte MODE_SCORE = 8;
    private static final byte MODE_OPENING = 9;
    private static final byte MODE_ENDING = 10;
    private static final byte MODE_HIGHSCORE = 11;
    private static final byte MODE_SAVE = 12;
    private static final byte MODE_OPTION = 13;

    // === КОНСТАНТЫ КЛАВИШ (по GameCanvas) ===
    private static final int KEY_UP = 1;  // UP
    private static final int KEY_LEFT = 2;  // LEFT
    private static final int KEY_RIGHT = 5;  // RIGHT
    private static final int KEY_DOWN = 6;  // DOWN
    private static final int KEY_FIRE = 8;  // FIRE / 5

    // === КОНСТАНТЫ НОМЕРОВ КЛАВИШ (Nokia-style) ===
    private static final int KEY_NUM_2 = 50;  // '2' — вверх
    private static final int KEY_NUM_4 = 52;  // '4' — влево
    private static final int KEY_NUM_6 = 54;  // '6' — вправо
    private static final int KEY_NUM_8 = 56;  // '8' — вниз
    private static final int KEY_NUM_5 = 53;  // '5' — огонь
    private static final int KEY_STAR = 42;  // '*' — ускорение
    private static final int KEY_HASH = 35;  // '#' — пауза/меню
    private static final int KEY_SOFT2 = -8;  // Правая софт-клавиша (выход)

    // === КОНСТАНТЫ ПОЛЕЙ И ЯЧЕЕК ===
    private static final byte CELL_EMPTY = 0;
    private static final byte CELL_PIPE_MIN = 1;
    private static final byte CELL_PIPE_MAX = 7;

    // === КОНСТАНТЫ МЕНЮ ===
    private static final byte MENU_NEW_GAME = 0;
    private static final byte MENU_CONTINUE = 1;
    private static final byte MENU_HIGHSCORE = 2;
    private static final byte MENU_HOWTO = 3;
    private static final byte MENU_SOUND = 4;

    // === КОНСТАНТЫ РАЗМЕРОВ ЭКРАНА (для J2ME) ===
    private static final int SCREEN_WIDTH = 128;
    private static final int SCREEN_HEIGHT = 160;


    final byte NULL;
    final byte START;
    final byte PLAY = (byte) 2;
    final byte CLEAR1 = (byte) 3;
    final byte CLEAR2 = (byte) 4;
    final byte GAMEOVER = (byte) 5;
    final byte HOWTO = (byte) 6;
    final byte EXIT = (byte) 7;
    final byte SCORE = (byte) 8;
    final byte OPENNING = (byte) 9;
    final byte ENDING = (byte) 10;
    final byte HIGHSCORE = (byte) 11;
    final byte SAVE = (byte) 12;
    final byte OPTION = (byte) 13;
    byte axisX = 0;
    byte axisY = (byte) 24;
    byte step = (byte) 16;
    int ticker_x = 20;
    int ticker_y = 75;
    int ticker_width = 0;
    int ticker_height = 16;
    int white = 0xFFFFFF;
    int black = 0;
    int blue = 3185146;
    public byte mode;
    public byte stage;
    public int totalScore;
    public int totalScoreBackup;
    public int hap;

    public Fun2LinkCanvas(Fun2Link fun2Link) {
        this.NULL = 0;
        this.START = 1;
        this.fun2link = fun2Link;
        this.board = new Board();
        this.cursor = new Cursor(this);
        this.rmsScore = new Score();
        this.rmsStage = new Stage();
        this.rmsCross = new Cross();
        this.linkCheck = new LinkCheck(this.board, this);
        this.water = new Water(this.board, this);
        try {
            this.imageMain = ImageUtil.createImage("/icons/main.png");
            this.imageMenuOn1 = ImageUtil.createImage("/icons/menu_on1.png");
            this.imageMenuOn2 = ImageUtil.createImage("/icons/menu_on2.png");
            this.imageMenuOn3 = ImageUtil.createImage("/icons/menu_on3.png");
            this.imageNumber = ImageUtil.createImage("/icons/number.png");
            this.img_spos = ImageUtil.createImage("/icons/start_pos.png");
            this.img_epos = ImageUtil.createImage("/icons/end_pos.png");
            this.img_gameover = ImageUtil.createImage("/icons/gameover.png");
            this.imageMsgTitle = ImageUtil.createImage("/icons/MsgTitle.png");
            this.imageBack = ImageUtil.createImage("/icons/back.png");
            this.explode = new AudioClip(1, "/sound/explode.mmf");
            this.gunshot = new AudioClip(1, "/sound/gunshot.mmf");
            this.titlesound = new AudioClip(1, "/sound/titlesound.mmf");
            this.select = new AudioClip(1, "/sound/select.mmf");
            this.selectok = new AudioClip(1, "/sound/selectok.mmf");
            this.clearshot = new AudioClip(1, "/sound/clearshot.mmf");
            this.stagestart = new AudioClip(1, "/sound/stagestart.mmf");
        } catch (Exception exception) {
            System.out.println("Image Load Error");
        }
        if (this.soundOn) {
            this.titlesound.stop();
            this.titlesound.play(1, 1);
        }
    }

    public void init() {
        this.board.isPause = false;
        this.board.isFast = false;
        this.selectMode = 0;
        this.mode = 1;
        this.repaint(0, 0, 128, 160);
    }

    public void reset() {
        if (!this.board.isPause) {
            this.stopTimer();
            this.water.stop();
            this.board.reset();
        }
    }

    public void restart() {
        this.board.init();
        this.cursor.setPipe(this.board.getCurrentPipe());
        this.linkCheck.init();
        this.startTimer();
        this.board.timerCount = 0;
        this.totalScore = 0;
        this.repaint(0, 0, 128, 160);
    }

    public void gotoMenu() {
        this.reset();
        if (this.soundOn) {
            this.titlesound.stop();
            this.titlesound.play(1, 1);
        }
        this.mode = 1;
        this.repaint(0, 0, 128, 160);
    }

    public void destroy() {
        this.stopTimer();
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void startWater() {
        this.water.start();
    }

    public void gameOver() {
        if (this.mode == 2) {
            this.mode = (byte) 5;
            this.ticker = new Ticker();
            this.ticker.start();
            this.repaint(0, 0, 128, 160);
        }
    }

    public void gameClear() {
        boolean bl = false;
        if (this.soundOn) {
            this.clearshot.stop();
            this.clearshot.play(1, 1);
        }
        this.hap = this.totalScore + 3;
        this.hap += this.water.crossCount * 10;
        this.rmsStage.addHighStage(this.stage);
        if (this.rmsCross.isHighCross(this.water.crossCount)) {
            this.rmsCross.addHighCross(this.water.crossCount);
        }
        if (this.rmsScore.isHighScore(this.hap)) {
            this.rmsScore.addHighScore(this.hap);
            this.mode = (byte) 4;
        } else {
            this.mode = (byte) 3;
        }
        this.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyRepeated(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyReleased(e.getKeyCode());
    }

    public void keyReleased(int n) {
    }

    public void keyRepeated(int n) {
        this.keyProcess(n);
    }

    public void keyPressed(int n) {
        this.keyProcess(n);
    }

    /**
     * Основной метод обработки нажатия клавиши.
     *
     * @param keyCode код клавиши (как int)
     */
    public void keyProcess(int keyCode) {
        int gameAction = GameKeyMapping.getGameAction(keyCode);
        System.out.println("Action: " + gameAction + ", KeyCode: " + keyCode);

        switch (mode) {

            // ============================================================
            // РЕЖИМ ИГРЫ (MODE_PLAY = 2)
            // ============================================================
            case MODE_PLAY: {
                // Нельзя двигаться, если таймер кончился или курсор в режиме "взрыва"
                if (board.timerCount <= 10 || cursor.isBoom) {
                    break;
                }

                // --- Движение курсора ---
                if (gameAction == KEY_LEFT || keyCode == KEY_NUM_4) {
                    cursor.setX((byte) (cursor.currentX - 1));
                    repaintNear(cursor.currentX, cursor.currentY);
                    break;
                }
                if (gameAction == KEY_RIGHT || keyCode == KEY_NUM_6) {
                    cursor.setX((byte) (cursor.currentX + 1));
                    repaintNear(cursor.currentX, cursor.currentY);
                    break;
                }
                if (gameAction == KEY_UP || keyCode == KEY_NUM_2) {
                    cursor.setY((byte) (cursor.currentY - 1));
                    repaintNear(cursor.currentX, cursor.currentY);
                    break;
                }
                if (gameAction == KEY_DOWN || keyCode == KEY_NUM_8) {
                    cursor.setY((byte) (cursor.currentY + 1));
                    repaintNear(cursor.currentX, cursor.currentY);
                    break;
                }

                // --- Действие: размещение трубы (FIRE / 5) ---
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5) {
                    handlePipePlacement();
                    break;
                }

                // --- Ускорение (кнопка *) ---
                if (gameAction == KEY_STAR) {
                    fast();
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    break;
                }

                // --- Пауза / выход в меню (# или правая софт-клавиша) ---
                if ((gameAction == KEY_HASH || gameAction == KEY_SOFT2) && !water.isAlive) {
                    board.isPause = true;
                    gotoMenu();
                }
                break;
            }

            // ============================================================
            // ГЛАВНОЕ МЕНЮ (MODE_START = 1)
            // ============================================================
            case MODE_START: {
                if (gameAction == KEY_UP || keyCode == KEY_NUM_2) {
                    selectMode = (byte) ((selectMode > 0) ? selectMode - 1 : MENU_SOUND);
                    playSound(select);
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    break;
                }
                if (gameAction == KEY_DOWN || keyCode == KEY_NUM_8) {
                    selectMode = (byte) ((selectMode < MENU_SOUND) ? selectMode + 1 : MENU_NEW_GAME);
                    playSound(select);
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    break;
                }
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5) {
                    playSound(selectok);
                    handleMenuSelection();
                }
                break;
            }

            // ============================================================
            // НОВАЯ ИГРА (MODE_OPENING = 9)
            // ============================================================
            case MODE_OPENING: {
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5) {
                    newGame();
                    restart();
                    mode = MODE_PLAY;
                    playSound(stagestart);
                }
                break;
            }

            // ============================================================
            // УСПЕШНОЕ ПРОХОЖДЕНИЕ УРОВНЯ (MODE_CLEAR1/2 = 3,4)
            // ============================================================
            case MODE_CLEAR1:
            case MODE_CLEAR2: {
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5) {
                    if (stage == 20) {
                        mode = MODE_ENDING;
                        rmsStage.initHighStage();
                        repaint();
                    } else {
                        reset();
                        nextStage();
                        restart();
                        playSound(stagestart);
                    }
                }
                break;
            }

            // ============================================================
            // КОНЕЦ ИГРЫ — ПЕРЕХОД К ТАБЛИЦЕ РЕКОРДОВ
            // ============================================================
            case MODE_ENDING: {
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5) {
                    mode = MODE_HIGHSCORE;
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
                break;
            }

            // ============================================================
            // ТАБЛИЦА РЕКОРДОВ — ВОЗВРАТ В МЕНЮ
            // ============================================================
            case MODE_HIGHSCORE: {
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5 || keyCode == KEY_SOFT2) {
                    gotoMenu();
                }
                break;
            }

            // ============================================================
            // ГЕЙМОВЕР — ВОЗВРАТ В МЕНЮ
            // ============================================================
            case MODE_GAMEOVER: {
                mode = MODE_HIGHSCORE;
                repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                break;
            }

            // ============================================================
            // ИНСТРУКЦИЯ — ВЫХОД В МЕНЮ
            // ============================================================
            case MODE_HOWTO: {
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5 ||
                        keyCode == KEY_HASH || keyCode == KEY_SOFT2) {
                    howtoStop();
                    gotoMenu();
                }
                break;
            }

            // ============================================================
            // НАСТРОЙКИ ЗВУКА (MODE_OPTION = 13)
            // ============================================================
            case MODE_OPTION: {
                if (gameAction == KEY_FIRE || keyCode == KEY_NUM_5) {
                    gotoMenu();
                    break;
                }
                if (keyCode == KEY_SOFT2) {
                    soundOn = preSoundOn;
                    gotoMenu();
                    break;
                }
                if (gameAction == KEY_UP || keyCode == KEY_NUM_2) {
                    soundOn = true;
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    break;
                }
                if (gameAction == KEY_DOWN || keyCode == KEY_NUM_8) {
                    soundOn = false;
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    break;
                }
                break;
            }
        }
    }

    // ============================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ============================================================

    /**
     * Размещение трубы на поле.
     * Учитывает: есть ли вода, можно ли взорвать трубу, пустая ли клетка.
     */
    private void handlePipePlacement() {
        if (cursor.isBoom) {
            return;
        }

        byte cellValue = board.get(cursor.currentX, cursor.currentY);

        if (water.waterThread == null) {
            // Режим строительства до начала потока воды
            if (cellValue >= CELL_PIPE_MIN && cellValue <= CELL_PIPE_MAX) {
                board.putPipe(cursor.currentX, cursor.currentY);
                cursor.boom();
                cursor.setPipe(board.getCurrentPipe());
                playSound(explode);
            } else if (cellValue == CELL_EMPTY) {
                totalScore++;
                board.putPipe(cursor.currentX, cursor.currentY);
                cursor.setPipe(board.getCurrentPipe());
                playSound(gunshot);
            }
        } else {
            // Во время потока воды — нельзя размещать на залитых клетках
            byte waterMap = board.getWaterMap(cursor.currentX, cursor.currentY);
            if (waterMap != 0) {
                return;
            }

            if (cellValue >= CELL_PIPE_MIN && cellValue <= CELL_PIPE_MAX) {
                cursor.boom();
                playSound(explode);
                board.putPipe(cursor.currentX, cursor.currentY);
                cursor.setPipe(board.getCurrentPipe());
            } else if (cellValue == CELL_EMPTY) {
                board.putPipe(cursor.currentX, cursor.currentY);
                cursor.setPipe(board.getCurrentPipe());
                playSound(gunshot);
            }
        }

        repaintPos(cursor.currentX, cursor.currentY);
        repaintUp();
        linkCheck.startLinkCheck();
    }

    /**
     * Обработка выбора пункта меню.
     */
    private void handleMenuSelection() {
        switch (selectMode) {
            case MENU_NEW_GAME:
                mode = MODE_OPENING;
                repaint();
                break;
            case MENU_CONTINUE:
                if (board.isPause) {
                    board.isPause = false;
                    mode = MODE_PLAY;
                    repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                } else {
                    loadGame();
                    restart();
                }
                break;
            case MENU_HIGHSCORE:
                mode = MODE_HIGHSCORE;
                repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                break;
            case MENU_HOWTO:
                howtoStart();
                break;
            case MENU_SOUND:
                mode = MODE_OPTION;
                preSoundOn = soundOn;
                repaint(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                break;
        }
    }

    /**
     * Воспроизведение звука, если включён.
     */
    private void playSound(AudioClip sound) {
        if(sound==explode){
            return;
        }
        if (soundOn && sound != null) {
            sound.stop();
            sound.play(1, 1);
        }
    }

    public void keyProces2s(int n) {
        int n2 = GameKeyMapping.getGameAction(n);
        System.out.println(n2 + " " + n);
        switch (this.mode) {
            case 2: {
                if (this.board.timerCount <= 10 || this.cursor.isBoom) {
                    break;
                }
                if (n2 == 2 || n == 52) {
                    this.cursor.setX((byte) (this.cursor.currentX - 1));
                    this.repaintNear(this.cursor.currentX, this.cursor.currentY);
                    break;
                }
                if (n2 == 5 || n == 54) {
                    this.cursor.setX((byte) (this.cursor.currentX + 1));
                    this.repaintNear(this.cursor.currentX, this.cursor.currentY);
                    break;
                }
                if (n2 == 1 || n == 50) {
                    this.cursor.setY((byte) (this.cursor.currentY - 1));
                    this.repaintNear(this.cursor.currentX, this.cursor.currentY);
                    break;
                }
                if (n2 == 6 || n == 56) {
                    this.cursor.setY((byte) (this.cursor.currentY + 1));
                    this.repaintNear(this.cursor.currentX, this.cursor.currentY);
                    break;
                }
                if (n2 == 8 || n == 53) {
                    if (this.cursor.isBoom) {
                        break;
                    }
                    if (this.water.waterThread == null) {
                        byte by = this.board.get(this.cursor.currentX, this.cursor.currentY);
                        if (by > 0 && by < 8) {
                            this.board.putPipe(this.cursor.currentX, this.cursor.currentY);
                            this.cursor.boom();
                            this.cursor.setPipe(this.board.getCurrentPipe());
                            if (this.soundOn) {
                                this.explode.stop();
                                this.explode.play(1, 1);
                            }
                        } else if (by == 0) {
                            ++this.totalScore;
                            this.board.putPipe(this.cursor.currentX, this.cursor.currentY);
                            this.cursor.setPipe(this.board.getCurrentPipe());
                            if (this.soundOn) {
                                this.gunshot.stop();
                                this.gunshot.play(1, 1);
                            }
                        }
                        this.repaintPos(this.cursor.currentX, this.cursor.currentY);
                        this.repaintUp();
                        this.linkCheck.startLinkCheck();
                        break;
                    }
                    byte by = this.board.get(this.cursor.currentX, this.cursor.currentY);
                    byte by2 = this.board.getWaterMap(this.cursor.currentX, this.cursor.currentY);
                    if (by2 != 0) {
                        break;
                    }
                    if (by > 0 && by < 8) {
                        this.cursor.boom();
                        if (this.soundOn) {
                            this.explode.stop();
                            this.explode.play(1, 1);
                        }
                        this.board.putPipe(this.cursor.currentX, this.cursor.currentY);
                        this.cursor.setPipe(this.board.getCurrentPipe());
                    } else if (by == 0) {
                        this.board.putPipe(this.cursor.currentX, this.cursor.currentY);
                        this.cursor.setPipe(this.board.getCurrentPipe());
                        if (this.soundOn) {
                            this.gunshot.stop();
                            this.gunshot.play(1, 1);
                        }
                    }
                    this.repaintPos(this.cursor.currentX, this.cursor.currentY);
                    this.repaintUp();
                    this.linkCheck.startLinkCheck();
                    break;
                }
                if (n == 42) {
                    this.fast();
                    this.repaint(0, 0, 128, 160);
                    break;
                }
                if (n != 35 && n != -8 || this.water.isAlive) {
                    break;
                }
                this.board.isPause = true;
                this.gotoMenu();
                break;
            }
            case 1: {
                if (n2 == 1 || n == 50) {
                    if (this.selectMode > 0) {
                        this.selectMode = (byte) (this.selectMode - 1);
                        if (this.soundOn) {
                            this.select.stop();
                            this.select.play(1, 1);
                        }
                        this.repaint(0, 0, 128, 160);
                        break;
                    }
                    this.selectMode = (byte) 4;
                    if (this.soundOn) {
                        this.select.stop();
                        this.select.play(1, 1);
                    }
                    this.repaint(0, 0, 128, 160);
                    break;
                }
                if (n2 == 6 || n == 56) {
                    if (this.selectMode < 4) {
                        this.selectMode = (byte) (this.selectMode + 1);
                        if (this.soundOn) {
                            this.select.stop();
                            this.select.play(1, 1);
                        }
                        this.repaint(0, 0, 128, 160);
                        break;
                    }
                    this.selectMode = 0;
                    if (this.soundOn) {
                        this.select.stop();
                        this.select.play(1, 1);
                    }
                    this.repaint(0, 0, 128, 160);
                    break;
                }
                if (n2 != 8 && n != 53) {
                    break;
                }
                if (this.soundOn) {
                    this.selectok.stop();
                    this.selectok.play(1, 1);
                }
                switch (this.selectMode) {
                    case 0: {
                        this.mode = (byte) 9;
                        this.repaint();
                        break;
                    }
                    case 1: {
                        if (this.board.isPause) {
                            this.board.isPause = false;
                            this.mode = (byte) 2;
                            this.repaint(0, 0, 128, 160);
                            break;
                        }
                        this.loadGame();
                        this.restart();
                        break;
                    }
                    case 2: {
                        this.mode = (byte) 11;
                        this.repaint(0, 0, 128, 160);
                        break;
                    }
                    case 3: {
                        this.howtoStart();
                        break;
                    }
                    case 4: {
                        this.mode = (byte) 13;
                        this.preSoundOn = this.soundOn;
                        this.repaint(0, 0, 128, 160);
                    }
                }
                break;
            }
            case 9: {
                if (n2 != 8 && n != 53) {
                    break;
                }
                this.newGame();
                this.restart();
                this.mode = (byte) 2;
                if (!this.soundOn) {
                    break;
                }
                this.stagestart.stop();
                this.stagestart.play(1, 1);
                break;
            }
            case 3:
            case 4: {
                if (n2 != 8 && n != 53) {
                    break;
                }
                if (this.stage == 20) {
                    this.mode = (byte) 10;
                    this.rmsStage.initHighStage();
                    this.repaint();
                    break;
                }
                this.reset();
                this.nextStage();
                this.restart();
                if (!this.soundOn) {
                    break;
                }
                this.stagestart.stop();
                this.stagestart.play(1, 1);
                break;
            }
            case 10: {
                if (n2 != 8 && n != 53) {
                    break;
                }
                this.mode = (byte) 11;
                this.repaint(0, 0, 128, 160);
                break;
            }
            case 11: {
                if (n2 != 8 && n != 53 && n != -8) {
                    break;
                }
                this.gotoMenu();
                break;
            }
            case 5: {
                this.mode = (byte) 11;
                this.repaint(0, 0, 128, 160);
                break;
            }
            case 6: {
                if (n2 != 8 && n != 53 && n != 35 && n != -8) {
                    break;
                }
                this.howtoStop();
                this.gotoMenu();
                break;
            }
            case 13: {
                if (n2 == 8 || n == 53) {
                    this.gotoMenu();
                    break;
                }
                if (n == -8) {
                    this.soundOn = this.preSoundOn;
                    this.gotoMenu();
                    break;
                }
                if (n2 == 1 || n == 50) {
                    this.soundOn = true;
                    this.repaint(0, 0, 128, 160);
                    break;
                }
                if (n2 != 6 && n != 56) {
                    break;
                }
                this.soundOn = false;
                this.repaint(0, 0, 128, 160);
            }
        }
    }

    public void drawPos(Graphics graphics, byte by, int n, int n2) {
        if (by == 0) {
            graphics.setClip(n, n2, 32, 23);
            graphics.drawImage(this.img_spos, n, n2, 4 | 0x10);
        } else {
            graphics.setClip(n += 4, n2, 22, 23);
            graphics.drawImage(this.img_epos, n, n2, 4 | 0x10);
        }
    }


    private void putInt(byte[] byArray, int n) {
        byArray[0] = (byte) (n >> 24 & 0xFF);
        byArray[1] = (byte) (n >> 16 & 0xFF);
        byArray[2] = (byte) (n >> 8 & 0xFF);
        byArray[3] = (byte) (n >> 0 & 0xFF);
    }


    public void drawNumber(Graphics var1, int var2, int var3, int var4) {
        int[] var5 = new int[]{var2 / 10, 0};
        var2 -= 10 * var5[0];
        var5[1] = var2 / 1;
        boolean var6 = false;
        boolean var7 = false;

        for (int var8 = 0; var8 < 2; ++var8) {
            if (var6) {
                var1.setClip(var3, var4, 10, 16);
                var1.drawImage(this.imageNumber, var3 - 10 * var5[var8], var4, 4 | 16);
                var3 += 10;
            }

            if (var5[var8] != 0 && !var6) {
                var6 = true;
                var1.setClip(var3, var4, 10, 16);
                var1.drawImage(this.imageNumber, var3 - 10 * var5[var8], var4, 4 | 16);
                var3 += 10;
            }
        }
    }

    protected void repaint() {
    }

    protected void repaint(int i, int i0, int i1, int i2) {
    }

    protected synchronized void paint(Graphics graphics) {
        switch (this.mode) {
            case 1: {
                graphics.setClip(0, 0, 128, 128);
                graphics.setColor(this.white);
                graphics.fillRect(0, 0, 128, 160);
                graphics.drawImage(this.imageMain, 0, 0, 4 | 0x10);
                if (this.selectMode >= 0 && this.selectMode <= 2) {
                    graphics.setClip(25, 81 + 9 * this.selectMode, 82, 9);
                    graphics.drawImage(this.imageMenuOn1, 25, 81, 4 | 0x10);
                    break;
                }
                if (this.selectMode == 3) {
                    graphics.setClip(0, 0, 128, 128);
                    graphics.drawImage(this.imageMenuOn2, 25, 107, 4 | 0x10);
                    break;
                }
                graphics.setClip(0, 0, 128, 128);
                graphics.drawImage(this.imageMenuOn3, 25, 117, 4 | 0x10);
                break;
            }
            case 2: {
                this.board.paint(graphics);
                this.water.paint(graphics);
                graphics.setClip(0, 0, 128, 128);
                graphics.setColor(this.white);
                graphics.fillRect(0, 151, 128, 9);
                if (this.board.timerCount > 10) {
                    this.linkCheck.paint(graphics);
                    this.cursor.paint(graphics);
                    break;
                }
                if (this.board.timerCount >= 0 && this.board.timerCount < 4) {
                    graphics.setClip(35, 60, 45, 16);
                    graphics.drawImage(this.imageMsgTitle, 35, 60, 4 | 0x10);
                    graphics.setClip(0, 60, 128, 16);
                    this.drawNumber(graphics, this.stage, 85, 60);
                    break;
                }
                if (this.board.timerCount == 4 || this.board.timerCount == 7) {
                    this.drawPos(graphics, (byte) 1, this.axisX + (this.board.getMapEndX() - 1) * this.step, this.axisY + (this.board.getMapEndY() - 2) * this.step);
                    this.drawPos(graphics, (byte) 0, this.axisX + (this.board.getMapStartX() - 1) * this.step, this.axisY + (this.board.getMapStartY() - 2) * this.step);
                    break;
                }
                if (this.board.timerCount == 6 || this.board.timerCount != 10) {
                    break;
                }
                this.setTimer();
                this.linkCheck.initLastPointer();
                break;
            }
            case 5: {
                graphics.setClip(this.ticker_x, this.ticker_y, this.ticker_width, this.ticker_height);
                graphics.drawImage(this.img_gameover, this.ticker_x, this.ticker_y, 4 | 0x10);
                this.linkCheck.stopLinkCheckAnimation();
                this.cnt = (byte) (this.cnt + 1);
                if (this.cnt != 9) {
                    break;
                }
                this.mode = (byte) 11;
                this.repaint(0, 0, 128, 160);
                this.cnt = 0;
                break;
            }
            case 6: {
                this.board.paint(graphics);
                this.linkCheck.paint(graphics);
                this.cursor.paint(graphics);
                graphics.setClip(0, 0, 128, 128);
                graphics.setColor(this.white);
                graphics.fillRoundRect(6, 72, 114, 44, 10, 10);
                graphics.setColor(this.black);
                graphics.fillRoundRect(8, 74, 110, 40, 10, 10);
                Font font = J2MEFont.getFont((int) 0, (int) 1, (int) 8);
                graphics.setFont(font);
                graphics.setColor(this.white);
                graphics.drawString(this.message1, 13, 87, 68);
                graphics.drawString(this.message2, 13, 97, 68);
                graphics.drawString(this.message3, 13, 107, 68);
                break;
            }
            case 3:
            case 4: {
                graphics.setClip(0, 0, 128, 128);
                graphics.setColor(this.white);
                graphics.fillRoundRect(8, 32, 112, 84, 10, 10);
                graphics.setColor(this.black);
                graphics.fillRoundRect(10, 34, 108, 80, 10, 10);
                if (this.mode == 3) {
                    graphics.setClip(14, 26, 51, 16);
                    graphics.drawImage(this.imageMsgTitle, -31, 26, 0x10 | 4);
                } else {
                    graphics.setClip(14, 26, 69, 16);
                    graphics.drawImage(this.imageMsgTitle, -82, 26, 0x10 | 4);
                }
                graphics.setClip(0, 0, 128, 128);
                Font font = J2MEFont.getFont((int) 0, (int) 1, (int) 8);
                graphics.setFont(font);
                graphics.setColor(this.white);
                graphics.drawString("Total Link", 14, 54, 68);
                String string = this.linkCheck.linkCount + "";
                graphics.drawString(string, 113, 54, 72);
                graphics.drawString("Cross Pipe", 14, 69, 68);
                string = this.water.crossCount + "";
                graphics.drawString(string, 113, 69, 72);
                graphics.drawString("Clear Bonus", 14, 84, 68);
                string = "300";
                graphics.drawString(string, 113, 84, 72);
                graphics.fillRect(14, 92, 100, 2);
                graphics.drawString("Total Score", 14, 107, 68);
                string = this.hap + "00";
                graphics.drawString(string, 113, 107, 72);
                break;
            }
            case 9: {
                graphics.setClip(0, 0, 128, 128);
                graphics.drawImage(this.imageBack, 0, 0, 4 | 0x10);
                Font font = J2MEFont.getFont((int) 0, (int) 1, (int) 8);
                graphics.setFont(font);
                graphics.setColor(this.white);
                graphics.drawString("MISSION", 14, 20, 68);
                graphics.drawString("An oil field has", 14, 35, 68);
                graphics.drawString("been discoverd.", 14, 45, 68);
                graphics.drawString("But oil is not", 14, 55, 68);
                graphics.drawString("being transferred", 14, 65, 68);
                graphics.drawString("due to no pipe-", 14, 75, 68);
                graphics.drawString("line.", 14, 85, 68);
                graphics.drawString("Your mission is", 14, 95, 68);
                graphics.drawString("connecting oil", 14, 105, 68);
                graphics.drawString("pipeline.", 14, 115, 68);
                break;
            }
            case 10: {
                graphics.setClip(0, 0, 128, 128);
                graphics.drawImage(this.imageBack, 0, 0, 4 | 0x10);
                Font font = J2MEFont.getFont((int) 0, (int) 1, (int) 8);
                graphics.setFont(font);
                graphics.setColor(this.white);
                graphics.drawString("MISSION CLEAR", 14, 20, 68);
                graphics.drawString("Congratulations.", 14, 35, 68);
                graphics.drawString("Due to successful", 14, 50, 68);
                graphics.drawString("oil pipe connect", 14, 60, 68);
                graphics.drawString("-ion.", 14, 70, 68);
                graphics.drawString("Mankind is safe", 14, 85, 68);
                graphics.drawString("with oil at least", 14, 95, 68);
                graphics.drawString("20 years.", 14, 105, 68);
                break;
            }
            case 11: {
                graphics.setClip(0, 0, 128, 128);
                graphics.drawImage(this.imageBack, 0, 0, 4 | 0x10);
                Font font = J2MEFont.getFont((int) 0, (int) 1, (int) 0);
                graphics.setFont(font);
                graphics.setColor(this.white);
                graphics.drawString("High Score", 25, 20, 68);
                graphics.drawString("1", 25, 35, 68);
                String string = this.rmsScore.values[0] + "00";
                graphics.drawString(string, 40, 35, 68);
                graphics.drawString("2", 25, 47, 68);
                string = this.rmsScore.values[1] + "00";
                graphics.drawString(string, 40, 47, 68);
                graphics.drawString("3", 25, 59, 68);
                string = this.rmsScore.values[2] + "00";
                graphics.drawString(string, 40, 59, 68);
                graphics.setColor(this.white);
                graphics.drawString("High Cross", 25, 75, 68);
                graphics.drawString("1", 25, 90, 68);
                string = this.rmsCross.values[0] + "";
                graphics.drawString(string, 40, 90, 68);
                graphics.drawString("2", 25, 102, 68);
                string = this.rmsCross.values[1] + "";
                graphics.drawString(string, 40, 102, 68);
                graphics.drawString("3", 25, 114, 68);
                string = this.rmsCross.values[2] + "";
                graphics.drawString(string, 40, 114, 68);
                break;
            }
            case 13: {
                graphics.setClip(0, 0, 128, 128);
                graphics.setColor(this.white);
                graphics.fillRoundRect(17, 60, 94, 36, 10, 10);
                graphics.setColor(this.black);
                graphics.fillRoundRect(19, 62, 90, 32, 10, 10);
                Font font = J2MEFont.getFont((int) 0, (int) 1, (int) 8);
                graphics.setFont(font);
                graphics.setColor(this.white);
                graphics.drawString("Sound On", 35, 76, 68);
                graphics.drawString("Sound Off", 35, 86, 68);
                if (this.soundOn) {
                    graphics.drawString(">", 25, 76, 68);
                    break;
                }
                graphics.drawString(">", 25, 86, 68);
                break;
            }
        }
    }

    public void repaintNear(byte by, byte by2) {
        int n = this.axisX + (by - 1) * this.step - 8;
        int n2 = this.axisY + (by2 - 1) * this.step - 16;
        int n3 = n + this.step * 3;
        int n4 = n2 + this.step * 3;
        if (n < 0) {
            n = 0;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        this.repaint(n, n2, n3, n4);
    }

    public void repaintPos(byte by, byte by2) {
        int n = this.axisX + by * this.step - 8;
        int n2 = this.axisY + by2 * this.step - 16;
        int n3 = n + this.step;
        int n4 = n2 + this.step;
        if (n < 0) {
            n = 0;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        this.repaint(n, n2, n3, n4);
    }

    public void repaintTimer() {
        this.repaint(0, 0, 128, 160);
    }

    public void repaintUp() {
        int n = 0;
        int n2 = 0;
        int n3 = n + 100;
        int n4 = n2 + 16;
        this.repaint(n, n2, n3, n4);
    }

    public void repaintDest() {
        this.repaint(96, 0, 38, 10);
    }

    public void startTimer() {
        if (this.timer == null) {
            this.timer = new Timer();
        }
        if (this.timertask != null) {
            this.timertask.cancel();
            this.timertask = null;
        }
        this.timertask = new Fun2LinkTimer(this, this.board);
        this.timer.schedule(this.timertask, 1000L, 800L);
    }

    public void stopTimer() {
        if (this.timertask != null) {
            this.timertask.cancel();
        }
    }

    public void setTimer() {
        this.board.isFast = false;
        this.timertask.cancel();
        this.timertask = new Fun2LinkTimer(this, this.board);
        this.timer.schedule(this.timertask, 0L, 10000L);
    }

    public void fast() {
        this.board.isFast = true;
        this.timertask.cancel();
        this.timertask = new Fun2LinkTimer(this, this.board);
        this.timer.schedule(this.timertask, 10L, 200L);
    }

    public void drawTimer() {
    }

    public void newGame() {
        this.stage = 1;
        this.startStage();
    }

    public void loadGame() {
        this.stage = this.rmsStage.getStage();
        if (this.stage <= 0) {
            this.stage = 1;
        }
        this.startStage();
    }

    public void startStage() {
        this.readScreen(this.stage);
        if (this.stage == 1) {
            this.mode = (byte) 9;
        } else {
            this.mode = (byte) 2;
            if (this.soundOn) {
                this.stagestart.stop();
                this.stagestart.play(1, 1);
            }
        }
        this.repaint(0, 0, 128, 160);
    }

    public void nextStage() {
        this.stage = (byte) (this.stage + 1);
        this.startStage();
    }

    public void howtoStart() {
        this.board.saveMap();
        this.totalScoreBackup = this.totalScore;
        this.totalScore = 0;
        this.board.timerCount = 0;
        this.readScreen((byte) 0);
        this.repaint(0, 0, 128, 160);
        this.mode = (byte) 6;
        this.board.next[0] = 1;
        this.board.next[1] = 3;
        this.board.next[2] = 4;
        this.board.next[3] = 7;
        this.board.set(3, 1, 5);
        this.board.set(4, 1, 2);
        this.board.set(4, 2, 3);
        this.board.set(5, 2, 5);
        this.demo = new Demo();
        this.demo.start();
        this.linkCheck.startLinkCheckAnimation();
        this.repaint(0, 0, 128, 160);
    }

    public void howtoStop() {
        this.demo.stop();
        this.readScreen(this.stage);
        this.board.loadMap();
        this.totalScore = this.totalScoreBackup;
        this.linkCheck.startLinkCheck();
        this.repaint(0, 0, 128, 160);
    }

    private boolean readScreen(byte by) {
        if (by < 0) {
            System.out.println("Can't open Stage " + by);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = ((Object) ((Object) this)).getClass().getResourceAsStream("/data/stage." + by);
                if (inputStream == null) {
                    System.out.println("Could not find the game board for stage " + by);
                    return false;
                }
                this.board.read(inputStream, by);
                inputStream.close();
            } catch (IOException iOException) {
                return false;
            }
        }
        this.cursor.setLocation(this.board.getCurStartX(), this.board.getCurStartY());
        return true;
    }

    public void serviceRepaints() {
    }

    public class Demo extends JMEThread {

        boolean isAlive;
        byte bx;
        byte by;

        private Demo() {
        }

        public void run() {
            this.isAlive = true;
            if (!this.isAlive) {
                return;
            }
            Fun2LinkCanvas.this.linkCheck.startLinkCheck();
            Fun2LinkCanvas.this.cursor.setPipe(Fun2LinkCanvas.this.board.getCurrentPipe());
            Fun2LinkCanvas.this.message1 = "Connect start and";
            Fun2LinkCanvas.this.message2 = "end";
            Fun2LinkCanvas.this.message3 = "";
            Fun2LinkCanvas.this.repaint(0, 0, 128, 160);
            try {
                Thread.sleep(5000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (!this.isAlive) {
                return;
            }
            Fun2LinkCanvas.this.message1 = "Must use more";
            Fun2LinkCanvas.this.message2 = "links than the";
            Fun2LinkCanvas.this.message3 = "number below dest";
            Fun2LinkCanvas.this.repaint(0, 0, 128, 160);
            try {
                Thread.sleep(7000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (!this.isAlive) {
                return;
            }
            Fun2LinkCanvas.this.message1 = "Link more pipes.";
            Fun2LinkCanvas.this.message2 = "";
            Fun2LinkCanvas.this.message3 = "";
            Fun2LinkCanvas.this.repaint(0, 0, 128, 160);
            Fun2LinkCanvas.this.cursor.setPipe(Fun2LinkCanvas.this.board.getCurrentPipe());
            Fun2LinkCanvas.this.repaintPos(this.bx, this.by);
            try {
                Thread.sleep(5000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (this.isAlive) {
                this.bx = (byte) 4;
                this.by = (byte) 2;
                Fun2LinkCanvas.this.cursor.setLocation(this.bx, this.by);
                if (Fun2LinkCanvas.this.soundOn) {
                    Fun2LinkCanvas.this.explode.stop();
                    Fun2LinkCanvas.this.explode.play(1, 1);
                }
            } else {
                return;
            }
            Fun2LinkCanvas.this.cursor.boom();
            Fun2LinkCanvas.this.board.putPipe(this.bx, this.by);
            Fun2LinkCanvas.this.cursor.setPipe(Fun2LinkCanvas.this.board.getCurrentPipe());
            Fun2LinkCanvas.this.repaintPos(this.bx, this.by);
            Fun2LinkCanvas.this.repaintUp();
            Fun2LinkCanvas.this.linkCheck.startLinkCheck();
            try {
                Thread.sleep(2000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (this.isAlive) {
                this.bx = (byte) 4;
                this.by = (byte) 3;
                Fun2LinkCanvas.this.cursor.setLocation(this.bx, this.by);
                Fun2LinkCanvas.this.repaintNear(this.bx, this.by);
                try {
                    Thread.sleep(1000L);
                } catch (Exception exception) {
                    // empty catch block
                }
                if (Fun2LinkCanvas.this.soundOn) {
                    Fun2LinkCanvas.this.gunshot.stop();
                    Fun2LinkCanvas.this.gunshot.play(1, 1);
                }
            } else {
                return;
            }
            Fun2LinkCanvas.this.board.putPipe(this.bx, this.by);
            Fun2LinkCanvas.this.cursor.setPipe(Fun2LinkCanvas.this.board.getCurrentPipe());
            Fun2LinkCanvas.this.repaintPos(this.bx, this.by);
            Fun2LinkCanvas.this.linkCheck.startLinkCheck();
            Fun2LinkCanvas.this.repaintUp();
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (!this.isAlive) {
                return;
            }
            this.bx = (byte) 3;
            this.by = (byte) 3;
            Fun2LinkCanvas.this.cursor.setLocation(this.bx, this.by);
            Fun2LinkCanvas.this.repaintNear(this.bx, this.by);
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (this.isAlive) {
                if (Fun2LinkCanvas.this.soundOn) {
                    Fun2LinkCanvas.this.gunshot.stop();
                    Fun2LinkCanvas.this.gunshot.play(1, 1);
                }
            } else {
                return;
            }
            Fun2LinkCanvas.this.board.putPipe(this.bx, this.by);
            Fun2LinkCanvas.this.cursor.setPipe(Fun2LinkCanvas.this.board.getCurrentPipe());
            Fun2LinkCanvas.this.repaintPos(this.bx, this.by);
            Fun2LinkCanvas.this.repaintUp();
            Fun2LinkCanvas.this.linkCheck.startLinkCheck();
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (!this.isAlive) {
                return;
            }
            this.bx = (byte) 3;
            this.by = (byte) 2;
            Fun2LinkCanvas.this.cursor.setLocation(this.bx, this.by);
            Fun2LinkCanvas.this.repaintNear(this.bx, this.by);
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (this.isAlive) {
                if (Fun2LinkCanvas.this.soundOn) {
                    Fun2LinkCanvas.this.gunshot.stop();
                    Fun2LinkCanvas.this.gunshot.play(1, 1);
                }
            } else {
                return;
            }
            Fun2LinkCanvas.this.board.putPipe(this.bx, this.by);
            Fun2LinkCanvas.this.cursor.setPipe(Fun2LinkCanvas.this.board.getCurrentPipe());
            Fun2LinkCanvas.this.repaintPos(this.bx, this.by);
            Fun2LinkCanvas.this.repaintUp();
            Fun2LinkCanvas.this.linkCheck.startLinkCheck();
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (!this.isAlive) {
                return;
            }
            Fun2LinkCanvas.this.message1 = "Once connection";
            Fun2LinkCanvas.this.message2 = "is completed";
            Fun2LinkCanvas.this.message3 = "push button *";
            Fun2LinkCanvas.this.repaint(0, 0, 128, 160);
            try {
                Thread.sleep(5000L);
            } catch (Exception exception) {
                // empty catch block
            }
            if (!this.isAlive) {
                return;
            }
            Fun2LinkCanvas.this.message1 = "Key 2468 : Move";
            Fun2LinkCanvas.this.message2 = "Key 5 : Put pipe";
            Fun2LinkCanvas.this.message3 = "Key * : Fast mode";
            Fun2LinkCanvas.this.repaint(0, 0, 128, 160);
            try {
                Thread.sleep(7000L);
            } catch (Exception exception) {
                // empty catch block
            }
            Fun2LinkCanvas.this.mode = 1;
            Fun2LinkCanvas.this.howtoStop();
            Fun2LinkCanvas.this.gotoMenu();
        }

        public void stop() {
            this.isAlive = false;
            if (Fun2LinkCanvas.this.demo != null) {
                Fun2LinkCanvas.this.demo = null;
            }
        }
    }

    private class Ticker extends JMEThread {

        boolean isAlive;

        private Ticker() {
        }

        public final void run() {
            this.isAlive = true;
            for (int i = 0; i < 8 && this.isAlive; ++i) {
                switch (i) {
                    case 0: {
                        Fun2LinkCanvas.this.ticker_width = 13;
                        break;
                    }
                    case 1: {
                        Fun2LinkCanvas.this.ticker_width = 22;
                        break;
                    }
                    case 2: {
                        Fun2LinkCanvas.this.ticker_width = 36;
                        break;
                    }
                    case 3: {
                        Fun2LinkCanvas.this.ticker_width = 46;
                        break;
                    }
                    case 4: {
                        Fun2LinkCanvas.this.ticker_width = 62;
                        break;
                    }
                    case 5: {
                        Fun2LinkCanvas.this.ticker_width = 71;
                        break;
                    }
                    case 6: {
                        Fun2LinkCanvas.this.ticker_width = 80;
                        break;
                    }
                    case 7: {
                        Fun2LinkCanvas.this.ticker_width = 88;
                    }
                }
                try {
                    Thread.sleep(500L);
                } catch (Exception exception) {
                    // empty catch block
                }
                Fun2LinkCanvas.this.repaint(Fun2LinkCanvas.this.ticker_x, Fun2LinkCanvas.this.ticker_y, Fun2LinkCanvas.this.ticker_x + Fun2LinkCanvas.this.ticker_width, Fun2LinkCanvas.this.ticker_y + Fun2LinkCanvas.this.ticker_height);
            }
        }

        public void stop() {
            this.isAlive = false;
            if (Fun2LinkCanvas.this.ticker != null) {
                Fun2LinkCanvas.this.ticker = null;
            }
        }
    }
}
