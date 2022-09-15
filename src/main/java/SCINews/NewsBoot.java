package SCINews;

import java.awt.*;

/**
 * @author Shang Zemo on 2022/7/2
 */
public class NewsBoot {

    public static void main(String[] args) {
        NewsHandler handler = new NewsHandler("newsPages");
        handler.setVisible(true);
    }

    public static void otherBoot(PopupMenu mainPopupMenu) {
        MenuItem displayMenuItem = new MenuItem("some news");
        displayMenuItem.addActionListener((l) -> new NewsHandler("newsPages",true));
        mainPopupMenu.add(displayMenuItem);
        mainPopupMenu.addSeparator();
    }
}
