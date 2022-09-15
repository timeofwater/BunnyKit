package SCINews;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Shang Zemo on 2022/7/2
 */
public class NewsHandler extends JFrame {

    private String pageDir;
    private LinkedList<String> scienceNews;
    private LinkedList<String> techSinaNews;
    private LinkedList<String> tech163News;

    NewsHandler(String pageDir, boolean isVisible) {
        this(pageDir);
        this.setVisible(isVisible);
    }

    NewsHandler(String pageDir) {
        super();
        this.pageDir = pageDir;
        scienceNews = new LinkedList<>();
        techSinaNews = new LinkedList<>();
        tech163News = new LinkedList<>();
        try {
            if (!Files.isDirectory(Path.of(pageDir))) {
                Files.createDirectory(Path.of(pageDir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            science(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            techSina(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tech163(5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM,dd新闻资讯")));

        StringBuilder toDisplay = new StringBuilder();
        toDisplay.append("---science:---\n");
        scienceNews.forEach(s -> toDisplay.append(s).append("\n"));
        toDisplay.append("---sina:---\n");
        techSinaNews.forEach(s -> toDisplay.append(s).append("\n"));
        toDisplay.append("---163:---\n");
        tech163News.forEach(s -> toDisplay.append(s).append("\n"));
        toDisplay.append("\n---end---");
        JTextArea text = new JTextArea(toDisplay.toString());
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setPreferredSize(new Dimension(575, toDisplay.toString().split("\n").length * 18));
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setSize(new Dimension(575, 500));
        this.add(scrollPane);
    }

    void science(float timeout) throws IOException {
        Path filePath = Path.of(pageDir + "/science.html");
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            Files.write(filePath, "<info>\n".getBytes(StandardCharsets.UTF_8));
        } else {
            Files.write(filePath, "<info>\n".getBytes(StandardCharsets.UTF_8));
        }

        URL url = new URL("https://www.science.org/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        connection.setRequestProperty("cookie", "MAID=ocOZcpEuibFlENHcxBGyPQ==; s_ecid=MCMID%7C77881081327369332121789608813447011534; tid=wveFHOPObqaIwOu3+T4Hf9ct/CjonBcA0sNJNfY1h6c1YfGMdDXVzIYNXLIguQLsHXVY9w==; __atuvc=1%7C13; cookiePolicy=iaccept; adBlockEnabled=blocked; _gid=GA1.2.576869717.1656699827; AMCVS_242B6472541199F70A4C98A6%40AdobeOrg=1; AMCV_242B6472541199F70A4C98A6%40AdobeOrg=-2121179033%7CMCIDTS%7C19175%7CMCMID%7C77881081327369332121789608813447011534%7CMCAAMLH-1657304627%7C11%7CMCAAMB-1657304627%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1656707027s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C5.3.0; s_cc=true; _gcl_au=1.1.1031151334.1656699846; JSESSIONID=ba86b804-76ac-490a-ad9c-3922c63e16e3; SERVER=WZ6myaEXBLFMWb1IDQJnuA==; MACHINE_LAST_SEEN=2022-07-01T12%3A26%3A24.959-07%3A00; __cf_bm=T_5U3mA2pkOWHNYLcbZVduD4u0Tc_VG5chfnglaew9Q-1656703584-0-ARqGmYUh/JpYY2gtARos92QjZPFhsdmwkSZ0zLP66TkOk7Nv4BB1ImgvbcCu1miWbXJJWgXvB/EvmJZwK4//xZE=; _ga=GA1.1.1668626367.1636807898; s_plt=3.71; s_pltp=www.science.org; s_sq=aaas.sciencemag%3D%2526pid%253Dwww.science.org%2526pidt%253D1%2526oid%253Dhttps%25253A%25252F%25252Fwww.science.org%25252F%252523header-side-menu%2526ot%253DA; _ga_KQG7WRFJWG=GS1.1.1656703583.2.1.1656703654.0; s_tp=7503; s_ppv=www.science.org%2C10%2C10%2C760");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        connection.setConnectTimeout((int) (timeout * 1000));
        connection.connect();

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        boolean outStreamFlag = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine() + "\n";
            if (line.equals("</article>" + "\n")) {
                outStreamFlag = false;
            }
            if (outStreamFlag) {
                Files.write(filePath, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
            if (line.equals("<article class=\"card-do card-do--latest-news\">" + "\n")) {
                outStreamFlag = true;
            }
        }
        Files.write(filePath, "</info>".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath.toFile());
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();

            int number = xPath.evaluateExpression("count(//span)", document, Integer.class);
            String time;
            String content;
            for (int i = 1; i <= number; i++) {
                time = xPath.evaluate("/info/div[" + i + "]/div[1]/time", document);
                content = xPath.evaluate("/info/div[" + i + "]/div[2]/div/a/span", document);
                scienceNews.add(time + " " + content);
            }

        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }

        System.out.println(scienceNews);
    }

    void techSina(float timeout) throws IOException {
        // 新浪科技
        Path filePath = Path.of(pageDir + "/sina.html");
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            Files.write(filePath, "<info>\n".getBytes(StandardCharsets.UTF_8));
        } else {
            Files.write(filePath, "<info>\n".getBytes(StandardCharsets.UTF_8));
        }

        URL url = new URL("https://tech.sina.com.cn/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        connection.setConnectTimeout((int) (timeout * 1000));
        connection.connect();

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        boolean outStreamFlag = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine() + "\n";
            if (line.startsWith("<!-- waptechtop browser begin 勿删 -->" + "\n")) {
                outStreamFlag = true;
            }
            if (line.equals("<!-- waptechtop browser end 勿删 -->" + "\n")) {
                outStreamFlag = false;
            }
            if (outStreamFlag) {
                Files.write(filePath, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        }
        Files.write(filePath, "</info>".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath.toFile());
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();

            int number = xPath.evaluateExpression("count(//li)", document, Integer.class);
            String content;
            for (int i = 1; i <= number; i++) {
                int numberOfAnchor = xPath.evaluateExpression("count(/info/ul/li[" + i + "]/a)", document, Integer.class);
                for (int j = 1; j <= numberOfAnchor; j++) {
                    content = xPath.evaluate("/info/ul/li[" + i + "]/a[" + j + "]", document);
                    techSinaNews.add(content);
                }
            }

        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }

        System.out.println(techSinaNews);
    }

    void tech163(float timeout) throws IOException {
        Path filePath = Path.of(pageDir + "/tech163.html");
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            Files.write(filePath, "<info>\n".getBytes(StandardCharsets.UTF_8));
        } else {
            Files.write(filePath, "<info>\n".getBytes(StandardCharsets.UTF_8));
        }

        URL url = new URL("https://tech.163.com/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        connection.setConnectTimeout((int) (timeout * 1000));
        connection.connect();

        Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        boolean outStreamFlag = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine() + "\n";
            if (line.startsWith("\t\t\t<!-- 科技信息流静态化 -->")) {
                outStreamFlag = true;
            }
            if (line.startsWith("\t\t\t<!-- 今日热点 -->")) {
                outStreamFlag = false;
            }
            if (outStreamFlag) {
                Files.write(filePath, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        }
        Files.write(filePath, "</info>".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath.toFile());
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();

            int number = xPath.evaluateExpression("count(//a)", document, Integer.class);
            String content;
            for (int i = 1; i <= number; i++) {
                content = xPath.evaluate("/info/div/a[" + i + "]", document);
                tech163News.add(content);
            }

        } catch (ParserConfigurationException | SAXException |
                XPathExpressionException e) {
            e.printStackTrace();
        }

        System.out.println(tech163News);
    }
}
