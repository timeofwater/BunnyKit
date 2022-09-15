package BunnyBot;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Shang Zemo on 2022/6/24
 */
public class FlowHandler {

    private final Robot robot;
    private final JFrame frame;
    private final JComponent drawComponent;
    private final MouseListener frameMouseListener;
    private final KeyListener frameKeyListener;

    private static ArrayList<StandardOperation> opList;
    private static ArrayList<Rectangle2D> rectangle2DArrayList;
    private static Instant gapStart;
    private static Instant gapEnd;
    private static Point startPoint;
    private static Instant dragStart;
    private static Instant dragEnd;
    private static Point endPoint;


    FlowHandler() throws AWTException {
        this.robot = new Robot();
        this.frame = new JFrame();
        this.drawComponent = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2D = (Graphics2D) g;
                for (Rectangle2D rectangle :
                        rectangle2DArrayList) {
                    g2D.draw(rectangle);
                }
            }
        };
        this.frame.getContentPane().add(this.drawComponent);
        this.frameMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                // 按中键退出
                if (e.getButton() == MouseEvent.BUTTON2) {
                    try {
                        // out to file
                        XMLOutputFactory factory = XMLOutputFactory.newInstance();
                        if (!Files.exists(Path.of("history"))) {
                            Files.createDirectory(Path.of("history"));
                        }
                        String defaultOutName = "out";
                        int index = 1;
                        while (Files.exists(Path.of("history/" + defaultOutName + index + ".xml"))) {
                            index += 1;
                        }
                        OutputStream out = new FileOutputStream("history/" + defaultOutName + index + ".xml");
                        XMLStreamWriter writer = factory.createXMLStreamWriter(out);
                        writer.writeStartDocument();
                        writer.writeStartElement("record");
                        for (StandardOperation op : opList) {
                            writer.writeStartElement("step");

                            writer.writeStartElement("gapTime");
                            writer.writeCharacters("" + op.getGapTime());
                            writer.writeEndElement();

                            writer.writeStartElement("startPoint");
                            writer.writeStartElement("x");
                            writer.writeCharacters("" + op.getStart().x);
                            writer.writeEndElement();
                            writer.writeStartElement("y");
                            writer.writeCharacters("" + op.getStart().y);
                            writer.writeEndElement();
                            writer.writeEndElement();

                            writer.writeStartElement("dragTime");
                            writer.writeCharacters("" + op.getDragTime());
                            writer.writeEndElement();

                            writer.writeStartElement("endPoint");
                            writer.writeStartElement("x");
                            writer.writeCharacters("" + op.getEnd().x);
                            writer.writeEndElement();
                            writer.writeStartElement("y");
                            writer.writeCharacters("" + op.getEnd().y);
                            writer.writeEndElement();
                            writer.writeEndElement();

                            writer.writeStartElement("button");
                            writer.writeCharacters("" + op.getButton());
                            writer.writeEndElement();

                            writer.writeEndElement();
                        }
                        writer.writeEndDocument();
                        writer.close();

                    } catch (XMLStreamException | IOException exception) {
                        exception.printStackTrace();
                    }
                    opList = new ArrayList<>();
                    frame.setVisible(false);
                }

                gapEnd = Instant.now();
                startPoint = e.getLocationOnScreen();
                rectangle2DArrayList.add(new Rectangle2D.Double(-50, 0, 50, 50));
                drawComponent.repaint();
                dragStart = Instant.now();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                dragEnd = Instant.now();
                endPoint = e.getLocationOnScreen();
                int button = e.getButton() == MouseEvent.BUTTON1 ? MouseEvent.BUTTON1_DOWN_MASK : MouseEvent.BUTTON3_DOWN_MASK;
                StandardOperation newOp = new StandardOperation(Duration.between(gapStart, gapEnd).toMillis(),
                        startPoint, Duration.between(dragStart, dragEnd).toMillis(), endPoint, button);
                opList.add(newOp);
                rectangle2DArrayList = new ArrayList<>();
                frame.setVisible(false);
                doOperationWithoutGapTime(newOp);
                gapStart = Instant.now();
                frame.setVisible(true);
            }
        };
        this.frameKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                gapEnd = Instant.now();
                startPoint = new Point(114, 514);
                dragStart = Instant.now();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                dragEnd = Instant.now();
                endPoint = new Point(22, 22);
                StandardOperation newOp = new StandardOperation(Duration.between(gapStart, gapEnd).toMillis(),
                        startPoint, Duration.between(dragStart, dragEnd).toMillis(), endPoint, e.getKeyCode());

                System.out.println("------\nExtendedKeyCode: " + e.getExtendedKeyCode() +
                        "\nKeyLocation: " + e.getKeyLocation() + "\nKeyCode: " + e.getKeyCode() +
                        "\nKeyChar: " + (int) e.getKeyChar() + "\n------");
                opList.add(newOp);
                rectangle2DArrayList = new ArrayList<>();
                frame.setVisible(false);
                doOperationWithoutGapTime(newOp);
                gapStart = Instant.now();
                frame.setVisible(true);
            }
        };
        this.frame.getContentPane().addMouseListener(this.frameMouseListener);
        this.frame.addKeyListener(this.frameKeyListener);

        opList = new ArrayList<>();
        rectangle2DArrayList = new ArrayList<>();

        this.frame.getContentPane().add(new JLabel("按快捷键时只需依次按下对应的键即可,否则将产生乱码"));
        this.frame.setUndecorated(true);
        this.frame.setOpacity(0.2f);
        this.frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    boolean doOperation(StandardOperation op) {
        try {
            TimeUnit.MILLISECONDS.sleep(op.getGapTime());
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            return false;
        }

        return doOperationWithoutGapTime(op);
    }

    boolean doOperationWithoutGapTime(StandardOperation op) {
        Point start = op.getStart();
        robot.mouseMove(start.x, start.y);
        switch (op.getButton()) {
            case MouseEvent.BUTTON1_DOWN_MASK:
                robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                break;
            case MouseEvent.BUTTON3_DOWN_MASK:
                robot.mousePress(MouseEvent.BUTTON3_DOWN_MASK);// it's ok
                break;
            case KeyEvent.VK_SHIFT:
                robot.keyPress(KeyEvent.VK_SHIFT);
                break;
            case KeyEvent.VK_CONTROL:
                robot.keyPress(KeyEvent.VK_CONTROL);
                break;
            case KeyEvent.VK_ALT:
                robot.keyPress(KeyEvent.VK_ALT);
                break;
            default:
                if (op.getButton() > 0 && op.getButton() < 255) {
                    robot.keyPress(op.getButton());
                    robot.keyRelease(op.getButton());
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyRelease(KeyEvent.VK_ALT);
                }
        }

        try {
            TimeUnit.MILLISECONDS.sleep(op.getDragTime());
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            return false;
        }

        Point end = op.getEnd();
        robot.mouseMove(end.x, end.y);
        robot.mouseRelease(op.getButton() == MouseEvent.BUTTON1_DOWN_MASK ? MouseEvent.BUTTON1_DOWN_MASK : MouseEvent.BUTTON3_DOWN_MASK);

        return true;
    }

    void startRecord() {
        this.frame.setVisible(true);
        gapStart = Instant.now();
    }

    /**
     * @param file file name without '.xml'
     * @return if the file exist
     */
    boolean play(String file) {
        if (!Files.exists(Path.of("history/" + file + ".xml"))) {
            return false;
        }

        ArrayList<BigInteger[]> fileAllStep = new ArrayList<>();

        // loadOpFile
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("history/" + file + ".xml");
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();

            int n = xPath.evaluateExpression("count(/record/step)", document, Integer.class);
            for (int i = 1; i <= n; i++) {
                BigInteger[] oneStep = new BigInteger[7];
                oneStep[0] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/gapTime", document));
                oneStep[1] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/startPoint/x", document));
                oneStep[2] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/startPoint/y", document));
                oneStep[3] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/dragTime", document));
                oneStep[4] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/endPoint/x", document));
                oneStep[5] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/endPoint/y", document));
                oneStep[6] = new BigInteger(xPath.evaluate("/record/step[" + i + "]/button", document));
                fileAllStep.add(oneStep);
            }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

        fileAllStep.forEach((s) -> doOperation(new StandardOperation(s[0].longValue(), s[1].intValue(), s[2].intValue(),
                s[3].longValue(), s[4].intValue(), s[5].intValue(), s[6].intValue())));

        return true;
    }


}
