package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Shang Zemo on 2022/8/17
 */
public class TetrisGameClass extends JFrame {
    private static final int WIDTH_LINES = 10;
    private static final int HEIGHT_LINES = 20;
    private static final int DELAY_MILLISECONDS = 500;
    private final int[][][] BLOCKS;

    private int[] gameGrids;
    private int[] fallingGrids;
    private int[] thisBlock;
    private int[] nextBlock;
    private boolean isRunning;
    private boolean otherCrash;

    BufferedImage image;
    int score;

    public TetrisGameClass() {
        initGame();
        BLOCKS = initBlocks();
    }

    void initGame() {
        gameGrids = new int[HEIGHT_LINES + 2];
        for (int i = 0; i < HEIGHT_LINES + 1; i++) {
            gameGrids[i] = 2049;
        }
        gameGrids[HEIGHT_LINES + 1] = 2046;
        fallingGrids = new int[HEIGHT_LINES + 2];
        thisBlock = new int[2];
        nextBlock = new int[2];
        isRunning = false;
        otherCrash = false;

        this.setTitle("Tetris");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRunning = false;
            }
        });
        this.setSize((WIDTH_LINES + 2) * 20, (HEIGHT_LINES + 2) * 20 + 30);
        this.setLocationRelativeTo(null);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
//                super.keyTyped(e);
                if ((e.getKeyChar() + "").matches("[wasd]")) {
                    switch (e.getKeyChar()) {
                        case 'w':
                            if (!upEvent()) {
                                otherCrash = false;
                            } else {
                                appear();
                            }
                            break;
                        case 's':
                            if (!downEvent()) {
                                otherCrash = false;
                            } else {
                                appear();
                            }
                            break;
                        case 'a':
                            if (!leftEvent()) {
                                otherCrash = false;
                            } else {
                                appear();
                            }
                            break;
                        case 'd':
                            if (!rightEvent()) {
                                otherCrash = false;
                            } else {
                                appear();
                            }
                            break;
                    }
                }
            }
        });

        score = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        image = new BufferedImage((WIDTH_LINES + 2) * 20, (HEIGHT_LINES + 2) * 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2D = (Graphics2D) image.getGraphics();

        //background color
        g2D.setColor(Color.darkGray);
        g2D.fillRect(0, 0, (WIDTH_LINES + 2) * 20, (HEIGHT_LINES + 2) * 20);

        //score color
        g2D.setColor(Color.lightGray);
        g2D.setFont(new Font("monospace", Font.BOLD, 16));
        g2D.drawString(String.format("%1$9d", score), (WIDTH_LINES - 2) * 20, 20);

        //block color
        g2D.setColor(Color.pink);
        for (int i = 0; i < HEIGHT_LINES + 2; i++) {
            for (int j = WIDTH_LINES + 1; j >= 0; j--) {
                if (((1 << j) & (gameGrids[i] | fallingGrids[i])) == (1 << j)) {
                    g2D.fillRect((WIDTH_LINES + 1 - j) * 20, i * 20, 20, 20);
                }
            }
        }

        //next block color
        g2D.setColor(Color.magenta);
        for (int i = 0; i < BLOCKS[nextBlock[0]][nextBlock[1]].length; i++) {
            for (int j = WIDTH_LINES + 1; j >= 0; j--) {
                if (((1 << j) & BLOCKS[nextBlock[0]][nextBlock[1]][i]) == (1 << j)) {
                    g2D.fillRect((WIDTH_LINES + 1 - j) * 5 + 10, i * 5 + 30, 5, 5);
                }
            }
        }

        g.drawImage(image, 0, 30, (WIDTH_LINES + 2) * 20, (HEIGHT_LINES + 2) * 20, this);
    }

    boolean upEvent() {
        int i = 0;
        for (; i < fallingGrids.length; i++) {
            if (fallingGrids[i] != 0) {
                break;
            }
        }
        if (i + BLOCKS[thisBlock[0]][(thisBlock[1] + 1) % BLOCKS[thisBlock[0]].length].length >= HEIGHT_LINES + 1) {
            return false;
        }
        thisBlock[1] = (thisBlock[1] + 1) % BLOCKS[thisBlock[0]].length;
        int j = 0;
        for (; j < BLOCKS[thisBlock[0]][thisBlock[1]].length; j++) {
            if ((BLOCKS[thisBlock[0]][thisBlock[1]][j] ^ gameGrids[i + j]) !=
                    (BLOCKS[thisBlock[0]][thisBlock[1]][j] | gameGrids[i + j])) {
                return false;
            }
        }
        for (j = 0; j < BLOCKS[thisBlock[0]][thisBlock[1]].length; j++) {
            fallingGrids[i + j] = BLOCKS[thisBlock[0]][thisBlock[1]][j];
        }

        for (; i + j < fallingGrids.length; j++) {
            fallingGrids[i + j] = 0;
        }

        return true;
    }

    boolean downEvent() {
        for (int i = 1; i < HEIGHT_LINES + 2; i++) {
            if ((fallingGrids[i - 1] & gameGrids[i]) != 0) {
                return false;
            }
        }
        for (int i = fallingGrids.length - 1; i > 0; i--) {
            fallingGrids[i] = fallingGrids[i - 1];
        }
        fallingGrids[0] = 0;
        return true;
    }

    boolean leftEvent() {
        for (int i = 0; i < HEIGHT_LINES + 2; i++) {
            if (((fallingGrids[i] << 1) & gameGrids[i]) != 0) {
                return false;
            }
        }
        for (int i = fallingGrids.length - 1; i >= 0; i--) {
            fallingGrids[i] = (fallingGrids[i] << 1);
        }

        return true;
    }

    boolean rightEvent() {
        for (int i = 0; i < HEIGHT_LINES + 2; i++) {
            if (((fallingGrids[i] >>> 1) & gameGrids[i]) != 0) {
                return false;
            }
        }
        for (int i = fallingGrids.length - 1; i >= 0; i--) {
            fallingGrids[i] = (fallingGrids[i] >>> 1);
        }

        return true;
    }

    /**
     * <pre>
     * 0.¡ö¡ö¡ö¡ö  0.0 ¡ö¡ö¡ö¡ö 0.1 ¡ö
     *                      ¡ö
     *                      ¡ö
     *                      ¡ö
     *
     * 1.  ¡ö   1.0   ¡ö  1.1 ¡ö    1.2 ¡ö¡ö¡ö 1.3 ¡ö¡ö
     *   ¡ö¡ö¡ö       ¡ö¡ö¡ö      ¡ö        ¡ö        ¡ö
     *                      ¡ö¡ö                ¡ö
     *
     * 2. ¡ö    2.0  ¡ö   2.1 ¡ö    2.2 ¡ö¡ö¡ö 2.3  ¡ö
     *   ¡ö¡ö¡ö       ¡ö¡ö¡ö      ¡ö¡ö        ¡ö      ¡ö¡ö
     *                      ¡ö                 ¡ö
     *
     * 3. ¡ö¡ö   3.0  ¡ö¡ö  3.1 ¡ö
     *   ¡ö¡ö        ¡ö¡ö       ¡ö¡ö
     *                       ¡ö
     *
     * 4.¡ö¡ö    4.0 ¡ö¡ö   4.1  ¡ö
     *    ¡ö¡ö        ¡ö¡ö      ¡ö¡ö
     *                      ¡ö
     *
     * 5.¡ö¡ö    5.0 ¡ö¡ö
     *   ¡ö¡ö        ¡ö¡ö
     * </pre>
     *
     * @return all the blocks
     */
    int[][][] initBlocks() {
        int[][][] rs = new int[6][][];

        rs[0] = new int[2][];
        rs[0][0] = new int[1];
        rs[0][0][0] = 0b1111 << 4;

        rs[0][1] = new int[4];
        rs[0][1][0] = 0b0100 << 4;
        rs[0][1][1] = 0b0100 << 4;
        rs[0][1][2] = 0b0100 << 4;
        rs[0][1][3] = 0b0100 << 4;


        rs[1] = new int[4][];
        rs[1][0] = new int[2];
        rs[1][0][0] = 0b0010 << 4;
        rs[1][0][1] = 0b1110 << 4;

        rs[1][1] = new int[3];
        rs[1][1][0] = 0b0100 << 4;
        rs[1][1][1] = 0b0100 << 4;
        rs[1][1][2] = 0b0110 << 4;

        rs[1][2] = new int[2];
        rs[1][2][0] = 0b1110 << 4;
        rs[1][2][1] = 0b1000 << 4;

        rs[1][3] = new int[3];
        rs[1][3][0] = 0b0110 << 4;
        rs[1][3][1] = 0b0010 << 4;
        rs[1][3][2] = 0b0010 << 4;


        rs[2] = new int[4][];
        rs[2][0] = new int[2];
        rs[2][0][0] = 0b0100 << 4;
        rs[2][0][1] = 0b1110 << 4;

        rs[2][1] = new int[3];
        rs[2][1][0] = 0b0100 << 4;
        rs[2][1][1] = 0b0110 << 4;
        rs[2][1][2] = 0b0100 << 4;

        rs[2][2] = new int[2];
        rs[2][2][0] = 0b1110 << 4;
        rs[2][2][1] = 0b0100 << 4;

        rs[2][3] = new int[3];
        rs[2][3][0] = 0b0100 << 4;
        rs[2][3][1] = 0b1100 << 4;
        rs[2][3][2] = 0b0100 << 4;


        rs[3] = new int[2][];
        rs[3][0] = new int[2];
        rs[3][0][0] = 0b0110 << 4;
        rs[3][0][1] = 0b1100 << 4;

        rs[3][1] = new int[3];
        rs[3][1][0] = 0b0100 << 4;
        rs[3][1][1] = 0b0110 << 4;
        rs[3][1][2] = 0b0010 << 4;


        rs[4] = new int[2][];
        rs[4][0] = new int[2];
        rs[4][0][0] = 0b1100 << 4;
        rs[4][0][1] = 0b0110 << 4;

        rs[4][1] = new int[3];
        rs[4][1][0] = 0b0010 << 4;
        rs[4][1][1] = 0b0110 << 4;
        rs[4][1][2] = 0b0100 << 4;


        rs[5] = new int[1][];
        rs[5][0] = new int[2];
        rs[5][0][0] = 0b0110 << 4;
        rs[5][0][1] = 0b0110 << 4;

        return rs;
    }

    void startGame() {
        isRunning = true;
        new Thread(() -> {
            try {
                nextBlock[0] = new Random().nextInt(6);
                nextBlock[1] = new Random().nextInt(BLOCKS[nextBlock[0]].length);
                gene();
                while (isRunning && (!otherCrash && fall() || gene())) {
                    appear();
//            printGrids(gameGrids);
//            printGrids(fallingGrids);
                    TimeUnit.MILLISECONDS.sleep(DELAY_MILLISECONDS);
                }
                printGrids();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
    }

    boolean gene() {
        thisBlock[0] = nextBlock[0];
        thisBlock[1] = nextBlock[1];
        nextBlock[0] = new Random().nextInt(6);
        nextBlock[1] = new Random().nextInt(BLOCKS[nextBlock[0]].length);
        for (int i = 0; i < BLOCKS[thisBlock[0]][thisBlock[1]].length; i++) {
            int temp = BLOCKS[thisBlock[0]][thisBlock[1]][i];
            if ((gameGrids[i] ^ temp) == gameGrids[i] + temp) {
                fallingGrids[i] = temp;
            } else {
                return false;
            }
        }

        return true;
    }

    boolean fall() {
        for (int i = 1; i < HEIGHT_LINES + 2; i++) {
            if ((fallingGrids[i - 1] & gameGrids[i]) != 0) {
                solid();
                return false;
            }
        }
        for (int i = fallingGrids.length - 1; i > 0; i--) {
            fallingGrids[i] = fallingGrids[i - 1];
        }
        fallingGrids[0] = 0;
        return true;
    }

    void solid() {
        int disappearLines = 0;
        for (int i = gameGrids.length - 1; i >= 0; i--) {
            gameGrids[i] ^= fallingGrids[i];
            gameGrids[i + disappearLines] = gameGrids[i];
            if (gameGrids[i] == 4095) {
                gameGrids[i] = 0;
                disappearLines += 1;
                score += 100;
            }
        }
        fallingGrids = new int[HEIGHT_LINES + 2];
    }

    void appear() {
        repaint();
        setVisible(true);
    }

    public void printGrids() {
        printGrids(this.gameGrids);
    }

    private void printGrids(int[] grids) {
        for (int i = 0; i < HEIGHT_LINES + 2; i++) {
            for (int j = WIDTH_LINES + 1; j >= 0; j--) {
                System.out.print((grids[i] & (1 << j)) == (1 << j) ? " *" : "  ");
            }
            System.out.println();
        }
    }
}
