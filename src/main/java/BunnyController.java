import BiliBiliLiveHelper.BiliBoot;
import BitmapPiecer.PiecerBoot;
import BunnyBot.BotBoot;
import Clock.ClockBoot;
import SCINews.NewsBoot;
import Tetris.GameBoot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Shang Zemo on 2022/6/30
 */
public class BunnyController {

    public static void main(String[] args) {
        if (SystemTray.isSupported()) {
            try {
                URL iconURL = BunnyController.class.getResource("/images/icon2.png");

                TrayIcon icon = new TrayIcon(ImageIO.read(iconURL));
                icon.setToolTip("Bunny Kit");

                PopupMenu popupMenu = new PopupMenu();

                // add para here
                String paraPath = "para.xml";
                // add boot here
                BiliBoot.otherBoot(paraPath, popupMenu);
                PiecerBoot.otherBoot(popupMenu);
                BotBoot.otherBoot(paraPath, popupMenu);
                ClockBoot.otherBoot(paraPath, popupMenu);
                NewsBoot.otherBoot(popupMenu);
                GameBoot.otherBoot(popupMenu);

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
}
