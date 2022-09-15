package BiliBiliLiveHelper;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Shang Zemo on 2022/7/1
 */
public class BilibiliHelper {

    private Document document;
    private XPath xPath;
    private String explorerPath;
    private Map<String, String> liveStatus;
    private Map<String, String> idAndName;

    BilibiliHelper(String paraPath) {
        liveStatus = new HashMap<>();
        idAndName = new HashMap<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(paraPath);
            XPathFactory factory = XPathFactory.newInstance();
            xPath = factory.newXPath();
            explorerPath = xPath.evaluate("/para/explorer", document);

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    void searchAll() {
        try {
            int timeout = xPath.evaluateExpression("/para/timeout", document, Integer.class);
            int numbersOfId = xPath.evaluateExpression("count(/para/ids/id)", document, Integer.class);
            String id;
            String name;

            for (int i = 1; i <= numbersOfId; i++) {
                id = xPath.evaluate("/para/ids/id[" + i + "]", document);
                name = xPath.evaluate("/para/ids/id[" + i + "]/@name", document);
                searchStatus(timeout, id);
                idAndName.put(id, name);
            }
        } catch (XPathExpressionException | IOException e) {
            e.printStackTrace();
        }
    }

    BilibiliHelper(int timeoutForEach, String... ids) {
        liveStatus = new HashMap<>();
        idAndName = new HashMap<>();
        try {
            for (String id :
                    ids) {
                searchStatus(timeoutForEach, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void searchStatus(int timeout, String id) throws IOException {
        URL url = new URL("https://api.live.bilibili.com/xlive/web-room/v2/index/getRoomPlayInfo?room_id=" + id + "&protocol=0,1&format=0,1,2&codec=0,1&qn=0&platform=web&ptype=8&dolby=5&panorama=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestProperty("origin", "https://live.bilibili.com");
        connection.setRequestProperty("referer", "https://live.bilibili.com/" + id + "?broadcast_type=0&is_room_feed=0");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        connection.setConnectTimeout(timeout * 1000);
        connection.connect();

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        while (scanner.hasNextLine()) {
            String status = scanner.nextLine();
            if (status != null) {
                int index = status.indexOf("live_status\":") + "live_status\":".length();
                System.out.println(status);
                status = status.substring(index, index + 1);
                liveStatus.put(id, status);
                break;
            }
        }
    }

    void openLiveRoom(String id) {
        try {
            Runtime.getRuntime().exec(explorerPath + " https://live.bilibili.com/" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getLiveStatus() {
        return liveStatus;
    }

    public Map<String, String> getIdAndName() {
        return idAndName;
    }
}
