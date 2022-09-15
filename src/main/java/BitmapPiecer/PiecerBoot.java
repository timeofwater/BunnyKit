package BitmapPiecer;

import java.awt.*;
import java.io.IOException;

/**
 * @author Shang Zemo on 2022/6/30
 */
public class PiecerBoot {

    public static void main(String[] args) {
        try {
            ColorHandler colorHandler = new ColorHandler();
            colorHandler.setColorBlockSet(ColorHandler.DEFAULT_ZH_SET);
            colorHandler.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void otherBoot(PopupMenu mainPopupMenu) {
        if (SystemTray.isSupported()) {
            MenuItem flashMenuItem = new MenuItem("picPiecer");
            flashMenuItem.addActionListener((l) -> {
                try {
                    ColorHandler colorHandler = new ColorHandler();
                    colorHandler.setColorBlockSet(ColorHandler.DEFAULT_ZH_SET);
                    colorHandler.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            mainPopupMenu.add(flashMenuItem);
            mainPopupMenu.addSeparator();
        }

    }

}
