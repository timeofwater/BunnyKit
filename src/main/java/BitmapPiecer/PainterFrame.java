package BitmapPiecer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Shang Zemo on 2022/7/14
 */
public class PainterFrame extends JFrame {

    private int width; // window width
    private int height; // window height
    private int aNumber; // -
    private int bNumber; // |
    private int size; // item size
    private LinkedList<V3> databaseList;
    private LinkedList<Image> databaseImages;
    private LinkedList<String> blockList;

    private boolean isRunning;
    private BufferedImage image;
    private int process;

    PainterFrame(int width, int height, LinkedList<V3> databaseList, LinkedList<String> blockList, String savePath) {
        this.width = Math.max(width, 100);
        this.height = Math.max(height, 25);
        this.width = Math.min(this.width, 300);
        this.height = Math.min(this.height, 75);
        this.aNumber = width;
        this.bNumber = height;
        this.size = fitSize();
        this.databaseList = new LinkedList<>(databaseList);
        this.databaseImages = new LinkedList<>();
        this.blockList = new LinkedList<>(blockList);

        this.setTitle("Piecer running...");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isRunning = false;
            }
        });
        this.setSize(this.width, this.height);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.white);

        initDBI();
        isRunning = true;
        if (aNumber * bNumber < 5000) {
            paintImage();
        } else {
            this.setVisible(true);
            try {
                paintBigImage();
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                this.dispose();
            }finally {
                try {
                    ImageIO.write(image, "png", new File(savePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            ImageIO.write(image, "png", new File(savePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int fitSize() {
        int number = Math.max(aNumber, bNumber);
        int window = Math.min(width, height - 20);
        return (int) Math.ceil((float) window / number);
    }

    void initDBI() {
        databaseImages = new LinkedList<>(); // TODO add images
    }

    private void paintImage() {
        image = new BufferedImage(aNumber * size, bNumber * size, BufferedImage.TYPE_INT_RGB);
        Graphics2D gImage = (Graphics2D) image.getGraphics();

        for (int i = 0; i < aNumber; i++) {
            for (int j = 0; j < bNumber; j++) {
                if (isRunning) {
                    for (V3 v3 : databaseList) {
                        if (v3.name.equals(blockList.get(i * aNumber + j))) {
                            gImage.setColor(new Color(v3.x, v3.y, v3.z));
                            gImage.fillRect(i * size, j * size, size, size);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @throws IndexOutOfBoundsException jvm mem poor -> IndexOutOfBoundsException
     */
    private void paintBigImage() throws IndexOutOfBoundsException{
        image = new BufferedImage(aNumber * size, bNumber * size, BufferedImage.TYPE_INT_RGB);
        Graphics2D gImage = (Graphics2D) image.getGraphics();

        this.setVisible(true);

        for (int i = 0; i < aNumber; i++) {
            if (isRunning) {
                for (int j = 0; j < bNumber; j++) {
                    for (V3 v3 : databaseList) {
                        if (v3.name.equals(blockList.get(i * aNumber + j))) {
                            gImage.setColor(new Color(v3.x, v3.y, v3.z));
                            gImage.fillRect(i * size, j * size, size, size);
                        }
                    }
                }
                process = i;
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.fillRect(0, 0, process * width / aNumber, height);
        g2d.drawString("" + process + "/" + aNumber, width - 75, height - 10);
    }
}
