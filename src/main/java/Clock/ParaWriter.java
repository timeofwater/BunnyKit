package Clock;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author Shang Zemo on 2022/10/10
 */
public class ParaWriter {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private XPathFactory factory;
    private XPath xPath;
    String file;
    ParaReader reader;

    ParaWriter(String file) {
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
        reader = new ParaReader(file);
    }

    void setDefault() {
        HashMap<String, String> ids = new HashMap<>();
        HashMap<String, String> pws = new HashMap<>();
        ids.put("慕容羽川", "896137");
        pws.put("Zsz", "");
        setPara("default-para.xml", new HashMap<>(), ids, pws);
    }

    void updatePara(HashMap<String, String> paras) {
        setPara("para.xml", paras, reader.getIds(), reader.getPws());
    }

    private void setPara(String filename,
                         HashMap<String, String> parasToChange,
                         HashMap<String, String> ids,
                         HashMap<String, String> pws) {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            OutputStream out = new FileOutputStream(filename);
            XMLStreamWriter writer = factory.createXMLStreamWriter(out, "UTF-8");

            writer.writeStartDocument();
            writer.writeStartElement("para");

            writer.writeComment("默认时钟窗口横纵坐标和宽高（相对于屏幕左上角）（像素）");

            writer.writeStartElement("x");
            writer.writeCharacters(parasToChange.getOrDefault("x", "900"));
            writer.writeEndElement();

            writer.writeStartElement("y");
            writer.writeCharacters(parasToChange.getOrDefault("y", "45"));
            writer.writeEndElement();

            writer.writeStartElement("width");
            writer.writeCharacters(parasToChange.getOrDefault("width", "220"));
            writer.writeEndElement();

            writer.writeStartElement("height");
            writer.writeCharacters(parasToChange.getOrDefault("height", "30"));
            writer.writeEndElement();

            writer.writeComment("时间模块横纵坐标和宽高（相对于窗口左上角）（像素）");

            writer.writeStartElement("timeX");
            writer.writeCharacters(parasToChange.getOrDefault("timeX", "0"));
            writer.writeEndElement();

            writer.writeStartElement("timeY");
            writer.writeCharacters(parasToChange.getOrDefault("timeY", "0"));
            writer.writeEndElement();

            writer.writeStartElement("timeWidth");
            writer.writeCharacters(parasToChange.getOrDefault("timeWidth", "195"));
            writer.writeEndElement();

            writer.writeStartElement("timeHeight");
            writer.writeCharacters(parasToChange.getOrDefault("timeHeight", "30"));
            writer.writeEndElement();

            writer.writeComment("天气模块横纵坐标和宽高（相对于窗口左上角）（像素）");

            writer.writeStartElement("weatherX");
            writer.writeCharacters(parasToChange.getOrDefault("weatherX", "195"));
            writer.writeEndElement();

            writer.writeStartElement("weatherY");
            writer.writeCharacters(parasToChange.getOrDefault("weatherY", "0"));
            writer.writeEndElement();

            writer.writeStartElement("weatherWidth");
            writer.writeCharacters(parasToChange.getOrDefault("weatherWidth", "25"));
            writer.writeEndElement();

            writer.writeStartElement("weatherHeight");
            writer.writeCharacters(parasToChange.getOrDefault("weatherHeight", "30"));
            writer.writeEndElement();

            writer.writeComment("字体字号");

            writer.writeStartElement("font");
            writer.writeCharacters(parasToChange.getOrDefault("font", "Monospaced"));
            writer.writeEndElement();

            writer.writeStartElement("fontSize");
            writer.writeCharacters(parasToChange.getOrDefault("fontSize", "20"));
            writer.writeEndElement();

            writer.writeComment("时间显示格式");

            writer.writeStartElement("timeFormatter");
            writer.writeCharacters(parasToChange.getOrDefault("timeFormatter", "MM,dd,星期 HH:mm:ss"));
            writer.writeEndElement();

            writer.writeComment("天气显示图标");

            writer.writeStartElement("weatherIcon");
            writer.writeCharacters(parasToChange.getOrDefault("weatherIcon", "\uD83D\uDC07"));
            writer.writeEndElement();

            writer.writeComment("鼠标触碰前后透明度（0-1）");

            writer.writeStartElement("normalOpacity");
            writer.writeCharacters(parasToChange.getOrDefault("normalOpacity", "0.7"));
            writer.writeEndElement();

            writer.writeStartElement("lightOpacity");
            writer.writeCharacters(parasToChange.getOrDefault("lightOpacity", "0.2"));
            writer.writeEndElement();

            writer.writeComment("查询超时时间（1-10）（秒）");

            writer.writeStartElement("timeout");
            writer.writeCharacters(parasToChange.getOrDefault("timeout", "3"));
            writer.writeEndElement();

            writer.writeComment("右键菜单显示内容");

            writer.writeStartElement("info");
            writer.writeCharacters(parasToChange.getOrDefault("info", "v1.5"));
            writer.writeEndElement();

            writer.writeComment("&￥ER%&@_*");

            writer.writeStartElement("explorer");
            writer.writeCharacters(parasToChange.getOrDefault("explorer",
                    "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"));
            writer.writeEndElement();

            writer.writeStartElement("ids");
            for (String id :
                    ids.keySet()) {
                writer.writeStartElement("id");
                writer.writeAttribute("name", id);
                writer.writeCharacters(ids.get(id));
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeComment("密码填充（不含大于号小于号）");

            writer.writeStartElement("pws");
            writer.writeAttribute("warning", "不能含有%lt;、%rt;");
            for (String pw :
                    pws.keySet()) {
                writer.writeStartElement("pw");
                writer.writeAttribute("name", pw);
                writer.writeCharacters(pws.get(pw));
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeEndDocument();
            writer.close();

        } catch (XMLStreamException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
