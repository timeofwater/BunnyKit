package Clock;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Shang Zemo on 2022/6/7
 */
public class ClockBoot {

    public static void main(String[] args) {
        ClockWindow body = new ClockWindow("para.xml");

        if (SystemTray.isSupported()) {
            try {
                // resources/images/icon.png -> build artifacts(at 1st time) | run (other time) -¡ý
                //    --(maven)--> target/classes/images/icon.png
                //    --(jdk)----> ?.jar/images/icon.png
                URL iconURL = ClockBoot.class.getResource("/images/icon.png");

                TrayIcon icon = new TrayIcon(ImageIO.read(iconURL));
                icon.setToolTip("Bunny Clock");

                PopupMenu popupMenu = new PopupMenu();
                MenuItem flashMenuItem = new MenuItem("flash");
                flashMenuItem.addActionListener((e) -> body.flash());
                popupMenu.add(flashMenuItem);
                MenuItem visibleMenuItem = new MenuItem("visible");
                visibleMenuItem.addActionListener(body.visibleActionListener);
                popupMenu.add(visibleMenuItem);
                MenuItem hideMenuItem = new MenuItem("hide");
                hideMenuItem.addActionListener(body.hideActionListener);
                popupMenu.add(hideMenuItem);
                MenuItem exitMenuItem = new MenuItem("exit");
                exitMenuItem.addActionListener(body.exitActionListener);
                popupMenu.add(exitMenuItem);

                icon.setPopupMenu(popupMenu);
                SystemTray.getSystemTray().add(icon);
            } catch (AWTException | IOException a) {
                a.printStackTrace();
            }
        }
    }

    public static void otherBoot(String paraPath,PopupMenu mainPopupMenu){
        ClockWindow body = new ClockWindow(paraPath);

        if (SystemTray.isSupported()) {
            MenuItem flashMenuItem = new MenuItem("flashClock");
            flashMenuItem.addActionListener((e) -> body.flash());
            mainPopupMenu.add(flashMenuItem);
            MenuItem visibleMenuItem = new MenuItem("visibleClock");
            visibleMenuItem.addActionListener(body.visibleActionListener);
            mainPopupMenu.add(visibleMenuItem);
            MenuItem hideMenuItem = new MenuItem("hideClock");
            hideMenuItem.addActionListener(body.hideActionListener);
            mainPopupMenu.add(hideMenuItem);
            mainPopupMenu.addSeparator();

        }
    }

}
