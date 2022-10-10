package Clock;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Shang Zemo on 2022/6/10
 */
public class ParaReader {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private XPathFactory factory;
    private XPath xPath;
    String file;

    //default
    private final int x = 0;
    private final int y = 0;
    private final int timeX = 0;
    private final int timeY = 0;
    private final int width = 300;
    private final int height = 50;
    private final String font = "Monospaced";
    private final int fontSize = 20;
    private final String timeFormatter = "MM,dd HH:mm:ss";
    private final String weatherIcon = "\uD83D\uDC07";
    private final float normalOpacity = 1.0f;
    private final float lightOpacity = 0.1f;
    private final double timeout = 1;
    private final String info = "Shang Zemo";
    private final String explorer = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";

    ParaReader(String file) {
        this.file = file;
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(file);
            factory = XPathFactory.newInstance();
            xPath = factory.newXPath();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    int getX() {
        int result = x;
        try {
            result = xPath.evaluateExpression("/para/x", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getY() {
        int result = y;
        try {
            result = xPath.evaluateExpression("/para/y", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getWidth() {
        int result = width;
        try {
            result = xPath.evaluateExpression("/para/width", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getHeight() {
        int result = height;
        try {
            result = xPath.evaluateExpression("/para/height", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getTimeX() {
        int result = timeX;
        try {
            result = xPath.evaluateExpression("/para/timeX", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getTimeY() {
        int result = timeY;
        try {
            result = xPath.evaluateExpression("/para/timeY", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getTimeWidth() {
        int result = width;
        try {
            result = xPath.evaluateExpression("/para/timeWidth", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getTimeHeight() {
        int result = height;
        try {
            result = xPath.evaluateExpression("/para/timeHeight", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getWeatherX() {
        int result = timeX;
        try {
            result = xPath.evaluateExpression("/para/weatherX", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getWeatherY() {
        int result = timeY;
        try {
            result = xPath.evaluateExpression("/para/weatherY", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getWeatherWidth() {
        int result = width;
        try {
            result = xPath.evaluateExpression("/para/weatherWidth", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getWeatherHeight() {
        int result = height;
        try {
            result = xPath.evaluateExpression("/para/weatherHeight", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    String getFont() {
        String result = font;
        try {
            result = xPath.evaluate("/para/font", document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    int getFontSize() {
        int result = fontSize;
        try {
            result = xPath.evaluateExpression("/para/fontSize", document, Integer.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    String getTimeFormatter() {
        String rs = timeFormatter;
        try {
            rs = xPath.evaluate("/para/timeFormatter", document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return rs;
    }

    String getWeatherIcon() {
        String result = weatherIcon;
        try {
            result = xPath.evaluate("/para/weatherIcon", document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    double getNormalOpacity() {
        double result = normalOpacity;
        try {
            result = xPath.evaluateExpression("/para/normalOpacity", document, Double.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    double getLightOpacity() {
        double result = lightOpacity;
        try {
            result = xPath.evaluateExpression("/para/lightOpacity", document, Double.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    double getTimeout() {
        double result = timeout;
        try {
            result = xPath.evaluateExpression("/para/timeout", document, Double.class);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    String getInfo() {
        String result = info;
        try {
            result = xPath.evaluate("/para/info", document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    String getExplorer() {
        String result = info;
        try {
            result = xPath.evaluate("/para/explorer", document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    HashMap<String, String> getIds() {
        HashMap<String, String> result = new HashMap<>();
        try {
            int num = xPath.evaluateExpression("count(/para/ids/id)", document, Integer.class);
            for (int i = 1; i <= num; i++) {
                String key = xPath.evaluate("/para/ids/id[" + i + "]/@name", document);
                String value = xPath.evaluate("/para/ids/id[" + i + "]", document);
                result.put(key, value);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    HashMap<String, String> getPws() {
        HashMap<String, String> result = new HashMap<>();
        try {
            int num = xPath.evaluateExpression("count(/para/pws/pw)", document, Integer.class);
            for (int i = 1; i <= num; i++) {
                String key = xPath.evaluate("/para/pws/pw[" + i + "]/@name", document);
                String value = xPath.evaluate("/para/pws/pw[" + i + "]", document);
                result.put(key, value);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
