package Clock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Shang Zemo on 2022/9/15
 */
public class Stopwatch extends JFrame {

    private JPanel panel;
    private JLabel label;
    private volatile boolean isTiming;
    private Duration duration;
    private Runnable timerRunnable;
    private Thread timer;

    Stopwatch(int x, int y) {
        setTitle("Bunny秒表");
        setBounds(x, y, 200, 80);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        panel = new JPanel();
        add(panel);
        label = new JLabel("23:59:59 999ms");
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
        panel.add(label);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (isTiming) {
                    // for atomic operation on boolean 'isTiming'
                    isTiming = false;
                } else {
                    isTiming = true;
                }
                System.out.println(isTiming ? "秒表计时中" : "秒表计时结束");
                if (isTiming) {
                    timer = new Thread(timerRunnable, "StopwatchTimer");
                    timer.start();
                }
            }
        });
        isTiming = false;
        duration = Duration.ofSeconds(0L);
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                LocalDateTime start = LocalDateTime.now();
                if (isTiming) {
                    label.setText("timing...");
                }
                while (isTiming) {
                    Thread.onSpinWait();
                }
                LocalDateTime end = LocalDateTime.now();
                duration = duration.plus(Duration.between(start, end));
                int h = duration.toHoursPart();
                int m = duration.toMinutesPart();
                int s = duration.toSecondsPart();
                String str = "";
                str += (h < 10) ? "0" + h : h;
                str += ":";
                str += (m < 10) ? "0" + m : m;
                str += ":";
                str += (s < 10) ? "0" + s : s;
                label.setText(str + " " + duration.toMillisPart() + "ms");
            }
        };
    }
}
