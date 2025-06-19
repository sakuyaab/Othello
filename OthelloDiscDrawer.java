package Othello;

import java.awt.*;

public class OthelloDiscDrawer {
    public static void drawDisc(Graphics g, int x, int y, Color color) {
        int size = OthelloBoard.CELL_SIZE;
        g.setColor(color);
        g.fillOval(x * size + 5, y * size + 5, size - 10, size - 10);
    }
}
