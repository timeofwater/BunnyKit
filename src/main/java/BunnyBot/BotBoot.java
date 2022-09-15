package BunnyBot;

import Clock.ClockBoot;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shang Zemo on 2022/6/24
 */
public class BotBoot {

    public static void main(String[] args) {
        if (SystemTray.isSupported()) {
            try {
                URL iconURL = ClockBoot.class.getResource("/images/icon2.png");

                TrayIcon icon = new TrayIcon(ImageIO.read(iconURL));
                icon.setToolTip("Bunny Bot");

                PopupMenu popupMenu = new PopupMenu();
                Menu playMenu = new Menu("play");
                flash(playMenu, "para.xml");
                popupMenu.add(playMenu);
                MenuItem flashMenuItem = new MenuItem("flash");
                flashMenuItem.addActionListener((e) -> flash(playMenu, "para.xml"));
                popupMenu.add(flashMenuItem);
                popupMenu.addSeparator();
                MenuItem exitMenuItem = new MenuItem("exit");
                exitMenuItem.addActionListener((e) -> System.exit(0));
                popupMenu.add(exitMenuItem);

                icon.setPopupMenu(popupMenu);

                SystemTray.getSystemTray().add(icon);
            } catch (AWTException | IOException a) {
                a.printStackTrace();
            }
        }
    }

    public static void otherBoot(String paraPath, PopupMenu mainPopupMenu) {
        Menu botMenu = new Menu("bunny bot");
        flash(botMenu, paraPath);
        mainPopupMenu.add(botMenu);
        MenuItem flashMenuItem = new MenuItem("flashList");
        flashMenuItem.addActionListener((e) -> flash(botMenu, paraPath));
        mainPopupMenu.add(flashMenuItem);
        mainPopupMenu.addSeparator();
    }

    static void flash(Menu playMenu, String paraPath) {
//        ---for older scripts---
//        try {
//            playMenu.removeAll();
//            ArrayList<Path> files = new ArrayList<>();
//            Files.list(Path.of("history")).forEach(files::add);
//            for (Path path :
//                    files) {
//                String name = path.toString().substring(path.toString().lastIndexOf('\\') + 1, path.toString().lastIndexOf('.'));
//                MenuItem loopItem = new MenuItem(name);
//                loopItem.addActionListener((event) -> flowHandler.play(name));
//                playMenu.add(loopItem);
//            }
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }

        try {
            Document document;
            XPath xPath;
            Map<String, String> pwAndName = new HashMap<>();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(paraPath);
            XPathFactory factory = XPathFactory.newInstance();
            xPath = factory.newXPath();

            int numbersOfPws = xPath.evaluateExpression("count(/para/pws/pw)", document, Integer.class);
            String pw;
            String name;

            for (int i = 1; i <= numbersOfPws; i++) {
                name = xPath.evaluate("/para/pws/pw[" + i + "]/@name", document);
                pw = xPath.evaluate("/para/pws/pw[" + i + "]", document);
                pwAndName.put(name, pw);
                MenuItem loopItem = new MenuItem(name);
                loopItem.addActionListener((event) -> PWHelper.play(pwAndName.get(loopItem.getLabel())));
                playMenu.add(loopItem);
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
