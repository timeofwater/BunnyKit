package Clock;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Shang Zemo on 2022/9/15
 */
public class Alarm extends JFrame {

    private JPanel panel;
    private JLabel label;
    private JTextField hour;
    private JTextField minute;
    private JTextField second;
    private LocalDateTime targetTime;
    private LocalDateTime now;
    private Duration duration;
    private boolean isAnyTextFiledChanged;
    private TextFiledFocusAdapter hourListener;
    private TextFiledFocusAdapter minuteListener;
    private TextFiledFocusAdapter secondListener;
    private Player audioPlayer;

    Alarm(int x, int y) {
        setTitle("BunnyÄÖÖÓ");
        setBounds(x, y, 170, 100);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        panel = new JPanel();
        add(panel);

        label = new JLabel("23:59:59 to ddl.");
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
        panel.add(label);

        hour = new JTextField("0", 4);
        hour.setToolTipText("hour");
        panel.add(hour);

        minute = new JTextField("0", 4);
        minute.setToolTipText("minute");
        panel.add(minute);

        second = new JTextField("0", 4);
        second.setToolTipText("second");
        panel.add(second);

        hourListener = new TextFiledFocusAdapter(hour);
        hour.addFocusListener(hourListener);
        minuteListener = new TextFiledFocusAdapter(minute);
        minute.addFocusListener(minuteListener);
        secondListener = new TextFiledFocusAdapter(second);
        second.addFocusListener(secondListener);

        isAnyTextFiledChanged = false;
        new Thread(() -> {
            while (true) {
                now = LocalDateTime.now();
                isAnyTextFiledChanged = hourListener.getIsChanged() || minuteListener.getIsChanged() ||
                        secondListener.getIsChanged();
                if (isAnyTextFiledChanged) {
                    targetTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                            Integer.parseInt(hour.getText()),
                            Integer.parseInt(minute.getText()),
                            Integer.parseInt(second.getText()));
                    hourListener.setChanged(false);
                    minuteListener.setChanged(false);
                    secondListener.setChanged(false);
                }
                if (targetTime == null || targetTime.isBefore(now)) {
                    duration = Duration.ofSeconds(42314L);
                } else {
                    duration = Duration.between(now, targetTime);
                }
                int h = duration.toHoursPart();
                int m = duration.toMinutesPart();
                int s = duration.toSecondsPart();
                if (h + m + s == 0) {
                    ringATime();
                }
                String str = "";
                str += (h < 10) ? "0" + h : h;
                str += ":";
                str += (m < 10) ? "0" + m : m;
                str += ":";
                str += (s < 10) ? "0" + s : s;
                label.setText(str + " to ddl.");

                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }, "alarmTimer").start();

        try {
            audioPlayer = new Player(ClockWindow.ringURL.openStream());
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }

    void ringATime() {
        new Thread(() -> {
            try {
                if (audioPlayer != null) {
                    System.out.println("alarm on the hour");
                    audioPlayer.play();
                } else {
                    System.out.println("error!\n\taudioPlayer : null");
                }
                while (!audioPlayer.isComplete()) {
                    Thread.onSpinWait();
                }
                audioPlayer = new Player(ClockWindow.ringURL.openStream());
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }, "alarmPlayer").start();
    }
}

class TextFiledFocusAdapter extends FocusAdapter {
    JTextField textField;
    private boolean isChanged;

    TextFiledFocusAdapter(JTextField textField) {
        this.textField = textField;
        isChanged = false;
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);
        String str = textField.getText();
        str = str.replaceAll("[^0-9]", "");
        if (str.equals("")) {
            textField.setText("0");
        }
        isChanged = true;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public boolean getIsChanged() {
        return isChanged;
    }
}