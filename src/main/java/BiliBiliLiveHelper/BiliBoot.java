package BiliBiliLiveHelper;

import java.awt.*;

/**
 * @author Shang Zemo on 2022/7/1
 */
public class BiliBoot {

    public static void main(String[] args) {
        new BilibiliHelper(5, "23103128");
    }

    public static void otherBoot(String paraPath, PopupMenu mainPopupMenu) {
        BilibiliHelper helper = new BilibiliHelper(paraPath);

        Menu playMenu = new Menu("bilibili lives");
        mainPopupMenu.add(playMenu);
        MenuItem flashMenuItem = new MenuItem("flashLives");
        flashMenuItem.addActionListener((e) -> flash(playMenu, helper));
        flash(playMenu, helper);
        mainPopupMenu.add(flashMenuItem);
        mainPopupMenu.addSeparator();

    }

    static void flash(Menu playMenu, BilibiliHelper bilibiliHelper) {
        new Thread(() -> {
            playMenu.removeAll();
            bilibiliHelper.searchAll();
            System.out.println(bilibiliHelper.getLiveStatus());
            bilibiliHelper.getLiveStatus().forEach((id, status) -> {
                if (status.equals("1")) {
                    String name = bilibiliHelper.getIdAndName().get(id);
                    MenuItem loopItem = new MenuItem(name);
                    loopItem.addActionListener((event) -> bilibiliHelper.openLiveRoom(id));
                    playMenu.add(loopItem);
                }
            });
        }).start();
    }
}
