package Clock;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author Shang Zemo on 2022/6/7
 */
public class ClockWindow {

    JFrame frame;
    JRootPane rootPane;
    JLayeredPane layeredPane;
    JPanel contentPane;
    JPanel panel;
    RightClickFrame rightClickPopup;
    JPanel timeAndWeatherPane;
    JLabel timeLabel;
    JLabel weatherLabel;
    ParaReader para;
    DateTimeFormatter formatter;
    ActionListener exitActionListener;
    ActionListener hideActionListener;
    ActionListener visibleActionListener;
    MouseListener mouseListener;
    Runnable timerRunnable;
    static URL ringURL;
    Player audioPlayer;

    Thread timer;
    String[] datesList = {"日", "一", "二", "三", "四", "五", "六", "日"};
    String time = "";

    ClockWindow(String paraPath) {
        /*
         * panel
         * <p>
         * |-JRootPane rootPane                             <br>
         * .    |                                           <br>
         * .    |-JPanel glassPane                          <br>
         * .    |                                           <br>
         * .    |-JLayeredPane layeredPane                  <br>
         * .    .   |                                       <br>
         * .    .   |-JPanel contentPane                    <br>
         * .    .   |   |                                   <br>
         * .    .   |   |-JPanel panel <<< 平常使用的         <br>
         * .    .   |                                       <br>
         * .    .   |-JPanel menuPane <<< 自定义菜单的话放这    <br>
         * <p>
         */
        //ini
        frame = new JFrame();
        rootPane = frame.getRootPane();
        layeredPane = rootPane.getLayeredPane();
        contentPane = (JPanel) layeredPane.getComponent(0);
        panel = new JPanel();
        para = new ParaReader(paraPath);
        rightClickPopup = new RightClickFrame(para.file, para.getInfo());
        initializeInterfaces();
        ringURL = ClockWindow.class.getResource("/audios/ring_01.mp3");
        try {
            audioPlayer = new Player(ringURL.openStream());
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }


        //menu
        changeMenuPane();

        //content
        changePanel();
        timer = new Thread(timerRunnable);
        timer.start();

        //frame
        contentPane.add(panel);
        frame.addMouseListener(mouseListener);
        frame.setOpacity((float) para.getNormalOpacity());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setBounds(para.getX(), para.getY(), para.getWidth(), para.getHeight());
        frame.setVisible(true);
    }

    private void initializeInterfaces() {
        exitActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        hideActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        };

        visibleActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
            }
        };

        mouseListener = new MouseListener() {

            Point recordPoint;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    rightClickPopup.callOut(e.getLocationOnScreen());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                recordPoint = e.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //drag
                if (e.getLocationOnScreen().distanceSq(recordPoint.x, recordPoint.y) >= 25) {
                    int dx = e.getLocationOnScreen().x - recordPoint.x;
                    int dy = e.getLocationOnScreen().y - recordPoint.y;
                    dx += frame.getX();
                    dy += frame.getY();
                    frame.setLocation(dx, dy);
                    rightClickPopup.setClockWindowLocation(dx, dy);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                frame.setOpacity((float) para.getLightOpacity());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.setOpacity((float) para.getNormalOpacity());
            }
        };

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    while (true) {
                        TimeUnit.MILLISECONDS.sleep(100);
                        if (frame.isVisible()) {
                            changeTime();
                        }
                    }
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
        };
    }

    /**
     * <br>
     * rootPane.setWindowDecorationStyle(int):        <br>
     * <br>
     * .     NONE, 0,                      窄边框不能调节大小（无menuPane）   <br>
     * .     FRAME, 1,                     古老浅蓝风格                     <br>
     * .     WHEN_IN_FOCUSED_WINDOW, 2,    只有title和关闭按钮的古老浅蓝风格   <br>
     * .     INFORMATION_DIALOG, 3,        同上                            <br>
     * .     ERROR_DIALOG, 4,              只有title和关闭按钮的古老浅粉风格    <br>
     * .     COLOR_CHOOSER_DIALOG, 5,      只有title和关闭按钮的古老浅绿风格    <br>
     * .     FILE_CHOOSER_DIALOG, 6,       同上                            <br>
     * .     QUESTION_DIALOG, 7,           同上                            <br>
     * .     WARNING_DIALOG, 8,            只有title和关闭按钮的古老浅橙风格    <br>
     */
    void changeMenuPane() {
        frame.setUndecorated(true);
        rootPane.setWindowDecorationStyle(JRootPane.NONE);
        if (SystemTray.isSupported()) {
            frame.setType(JFrame.Type.UTILITY);
            frame.setAlwaysOnTop(true);
        }
    }

    void changePanel() {
        panel.setLayout(null);
        timeAndWeatherPane = new JPanel();
        timeAndWeatherPane.setBounds(para.getTimeX(), para.getTimeY(), para.getWidth(), para.getHeight());
        timeAndWeatherPane.setLayout(null);
        panel.add(timeAndWeatherPane);

        timeLabel = new JLabel(para.getTimeFormatter());
        timeLabel.setFont(new Font(para.getFont(), Font.PLAIN, para.getFontSize()));
        timeLabel.setBounds(para.getTimeX(), para.getTimeY(), para.getTimeWidth(), para.getTimeHeight());
        formatter = DateTimeFormatter.ofPattern(para.getTimeFormatter());
        timeAndWeatherPane.add(timeLabel);

        weatherLabel = new JLabel(para.getWeatherIcon());
        weatherLabel.setFont(new Font(para.getFont(), Font.PLAIN, para.getFontSize()));
        weatherLabel.setBounds(para.getWeatherX(), para.getWeatherY(), para.getWeatherWidth(), para.getWeatherHeight());
        weatherLabel.setToolTipText(BaiDuWeather.getInfo(para.getTimeout()));
        timeAndWeatherPane.add(weatherLabel);
    }

    void changeTime() {
        LocalDateTime now = LocalDateTime.now();
        time = now.format(formatter);
        int dateFlag = now.getDayOfWeek().getValue();
        time = time.replace("星期", datesList[dateFlag]);
        timeLabel.setText(time);
        timeLabel.setVisible(true);
        if (now.getMinute() == 0 && now.getSecond() == 0 && now.getNano() / 100000000 == 0) {
//        if (now.getSecond() == 0 && now.getNano() / 100000000 == 0) { // for debug
            ringATime();
        }
    }

    void flash() {
        changeTime();
        weatherLabel.setToolTipText(BaiDuWeather.getInfo(para.getTimeout()));
    }

    void ringATime() {
        new Thread(() -> {
            try {
                if (audioPlayer != null) {
                    System.out.println("ring on the hour");
                    audioPlayer.play();
                } else {
                    System.out.println("error!\n\taudioPlayer : null");
                }
                while (!audioPlayer.isComplete()) {
                    Thread.onSpinWait();
                }
                audioPlayer = new Player(ringURL.openStream());
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }, "audioPlayer").start();
    }
}
