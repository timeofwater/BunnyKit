package BitmapPiecer;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

/**
 * @author Shang Zemo on 2022/6/30
 */
public class ColorHandler {

    static final int DEFAULT_SET = 0;
    static final int DEFAULT_ZH_SET = 1;

    private String loadPath;
    private String savePath;
    private int height = 0;
    private int width = 0;
    private LinkedList<Color> colorList; // ↘
    private LinkedList<V3> colorBlockSet; // data base
    private LinkedList<String> blockList; // out to file

    ColorHandler() throws IOException {
        FileDialog loadDialog = new FileDialog(new JFrame(), "选择需要分析的图片", FileDialog.LOAD);
        loadDialog.setVisible(true);
        colorList = new LinkedList<>();
        colorBlockSet = new LinkedList<>();
        blockList = new LinkedList<>();

        if (loadDialog.getFile() == null) {
            return;
        } else {
            this.loadPath = loadDialog.getDirectory() + loadDialog.getFile();

            FileDialog saveDialog = new FileDialog(new JFrame(), "保存于", FileDialog.SAVE);
            saveDialog.setVisible(true);
            this.savePath = saveDialog.getDirectory() + saveDialog.getFile();

            BufferedImage image = ImageIO.read(new File(loadPath));
            height = image.getHeight();
            width = image.getWidth();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    colorList.add(new Color(image.getRGB(j, i)));
                }
            }
            setColorBlockSet(DEFAULT_SET);
        }
    }

    void setColorBlockSet(int set) {
        try {
            switch (set) {
                case DEFAULT_SET:
                case DEFAULT_ZH_SET:
                    URL wools;
                    if (set == DEFAULT_SET) {
                        wools = PiecerBoot.class.getResource("/ColorBlocks/wools.xml");
                    } else {
                        wools = PiecerBoot.class.getResource("/ColorBlocks/wools_zh.xml");
                    }
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(new File(wools.toURI().getPath().substring(1)));
                    XPathFactory factory = XPathFactory.newInstance();
                    XPath xPath = factory.newXPath();

                    try {
                        String loopUnit;
                        int r;
                        int g;
                        int b;
                        int number = xPath.evaluateExpression("count(/wools/color)", document, Integer.class);
                        for (int i = 1; i <= number; i++) {
                            loopUnit = xPath.evaluate("/wools/color[" + i + "]", document).
                                    toLowerCase(Locale.ROOT);
                            if (loopUnit.startsWith("#")) {
                                r = Integer.parseInt(loopUnit.substring(1, 3), 16);
                                g = Integer.parseInt(loopUnit.substring(3, 5), 16);
                                b = Integer.parseInt(loopUnit.substring(5, 7), 16);
                                V3 tempV3 = new V3(r, g, b);
                                tempV3.name = xPath.evaluate("/wools/color[" + i + "]/@id", document).
                                        toLowerCase(Locale.ROOT);
                                colorBlockSet.add(tempV3);
                            }
                        }
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    // TODO 扩展使用的方块集
            }
        } catch (ParserConfigurationException | SAXException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    void save() throws IOException {
        for (Iterator<Color> targetIterator = colorList.iterator(); targetIterator.hasNext(); ) {
            Color loop = targetIterator.next();
            V3 thisPoint = new V3(loop.getRed(), loop.getGreen(), loop.getBlue());
            V3 minBase = colorBlockSet.get(0);
            float minDistance = 440; // 256*√3
            for (Iterator<V3> baseIterator = colorBlockSet.iterator(); baseIterator.hasNext(); ) {
                V3 thisBase = baseIterator.next();
                if (thisBase.distance(thisPoint) < minDistance) {
                    minDistance = thisBase.distance(thisPoint);
                    minBase = thisBase;
                }
            }
            blockList.add(minBase.name);
        }
        System.out.println("colorList: " + colorList);
        System.out.println("colorBlockSet: " + colorBlockSet);
        System.out.println("blockList: " + blockList);

        if (savePath == null) {
            return;
        } else {
            if (!Files.exists(Path.of(savePath))) {
                Files.createFile(Path.of(savePath));
            }
        }
        if (Files.isWritable(Path.of(savePath))) {
            if (!savePath.endsWith(".png")) {
                savePath = savePath + ".png";
            }
            PainterFrame frame = new PainterFrame(width, height, colorBlockSet, blockList, savePath);

            Desktop.getDesktop().open(new File(savePath));
        }
    }
}
