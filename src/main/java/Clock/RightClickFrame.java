package Clock;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Shang Zemo on 2022/6/30
 */
public class RightClickFrame extends JFrame {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private XPathFactory factory;
    private XPath xPath;
    private String filepath;
    private Point point;
    private Point clockWindowLocation;

    RightClickFrame(String filepath, String info) {
        super();
        this.filepath = filepath;
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(filepath);
            factory = XPathFactory.newInstance();
            xPath = factory.newXPath();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setType(JFrame.Type.UTILITY);
        this.setLocationRelativeTo(null);
        int[] size = initializeButtons(info);
        this.setSize(size[0], size[1]);
        this.getContentPane().setBackground(Color.white);
        this.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                disappear();
            }
        });
        clockWindowLocation = null;
    }

    /**
     * 1.显示info
     * 2.打开配置文件
     * 3.定时
     * 4.倒计时
     *
     * @return 按钮宽高
     */
    private int[] initializeButtons(String info) {
        JLabel infoLabel = new JLabel("  " + info);
        infoLabel.setEnabled(false);
        infoLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        infoLabel.setPreferredSize(new Dimension(60, 20));
        this.getContentPane().add(infoLabel);

        JLabel openParaFileButton = new JLabel("  para");
        openParaFileButton.setBackground(Color.white);
        openParaFileButton.setEnabled(false);
        openParaFileButton.setOpaque(true);
        openParaFileButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        openParaFileButton.setPreferredSize(new Dimension(60, 20));
        openParaFileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                try {
                    Desktop.getDesktop().open(new File(filepath));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                openParaFileButton.setBackground(Color.blue);
                openParaFileButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                openParaFileButton.setBackground(Color.white);
                openParaFileButton.setForeground(Color.black);
            }
        });
        this.getContentPane().add(openParaFileButton);

        JLabel moveLocationButton = new JLabel("  move");
        moveLocationButton.setBackground(Color.white);
        moveLocationButton.setEnabled(false);
        moveLocationButton.setOpaque(true);
        moveLocationButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        moveLocationButton.setPreferredSize(new Dimension(60, 20));
        moveLocationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (clockWindowLocation != null) {
                    ParaWriter writer = new ParaWriter(filepath);
                    HashMap<String, String> newParas = new HashMap<>();
                    newParas.put("x", "" + clockWindowLocation.x);
                    newParas.put("y", "" + clockWindowLocation.y);
                    writer.updatePara(newParas);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                moveLocationButton.setBackground(Color.blue);
                moveLocationButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                moveLocationButton.setBackground(Color.white);
                moveLocationButton.setForeground(Color.black);
            }
        });
        this.getContentPane().add(moveLocationButton);

        JLabel stopwatchButton = new JLabel("  秒表");
        stopwatchButton.setBackground(Color.white);
        stopwatchButton.setEnabled(false);
        stopwatchButton.setOpaque(true);
        stopwatchButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        stopwatchButton.setPreferredSize(new Dimension(60, 20));
        stopwatchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                new Thread(() -> new Stopwatch(point.x, point.y + 40), "stopWatchBooster").start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                stopwatchButton.setBackground(Color.blue);
                stopwatchButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                stopwatchButton.setBackground(Color.white);
                stopwatchButton.setForeground(Color.black);
            }
        });
        this.getContentPane().add(stopwatchButton);

        JLabel alarmButton = new JLabel("  闹钟");
        alarmButton.setBackground(Color.white);
        alarmButton.setEnabled(false);
        alarmButton.setOpaque(true);
        alarmButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        alarmButton.setPreferredSize(new Dimension(60, 20));
        alarmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                new Thread(() -> new Alarm(point.x, point.y + 40), "alarmBooster").start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                alarmButton.setBackground(Color.blue);
                alarmButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                alarmButton.setBackground(Color.white);
                alarmButton.setForeground(Color.black);
            }
        });
        this.getContentPane().add(alarmButton);

        return new int[]{60, 20 * 5};
    }

    void callOut(Point point) {
        this.point = point;
        this.setLocation(point);
        this.setVisible(true);
        this.setFocusable(true);
    }

    void disappear() {
        this.dispose();
    }

    void setClockWindowLocation(int x, int y) {
        if (clockWindowLocation == null) {
            clockWindowLocation = new Point(x, y);
        } else {
            clockWindowLocation.x = x;
            clockWindowLocation.y = y;
        }
    }
}
