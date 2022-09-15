package Tetris;

import java.awt.*;

/**
 * @author Shang Zemo on 2022/8/17
 */
public class GameBoot {

    public static void main(String[] args) {
        TetrisGameClass tetris = new TetrisGameClass();
        tetris.startGame();
    }


    public static void otherBoot(PopupMenu mainPopupMenu) {

        MenuItem playMenu = new MenuItem("Tetris Game!");
        playMenu.addActionListener((l) -> new TetrisGameClass().startGame());
        mainPopupMenu.add(playMenu);
        mainPopupMenu.addSeparator();

    }
}
