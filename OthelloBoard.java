package Othello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class OthelloBoard extends JPanel {
    public static final int CELL_SIZE = 60;
    public static final int BOARD_SIZE = 8;

    private final Color[][] board = new Color[BOARD_SIZE][BOARD_SIZE];
    private boolean isBlackTurn = true;
    private final JLabel turnLabel;

    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        { 0, -1},          { 0, 1},
        { 1, -1}, { 1, 0}, { 1, 1}
    };

    public OthelloBoard(JLabel label) {
        this.turnLabel = label;
        setPreferredSize(new Dimension(CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE));

        // 初期配置
        board[3][3] = Color.WHITE;
        board[4][4] = Color.WHITE;
        board[3][4] = Color.BLACK;
        board[4][3] = Color.BLACK;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;

                Color current = getCurrentColor();
                Color opponent = getOpponentColor();

                if (!canPlaceDisc(x, y, current)) return;

                placeDisc(x, y, current);

                // 勝敗判定：両者置けない場合
                if (!hasValidMove(opponent) && !hasValidMove(current)) {
                    repaint();
                    countDiscsAndShowResult();
                    return;
                }

                // 相手が置けるならターン交代、そうでなければパス
                if (hasValidMove(opponent)) {
                    isBlackTurn = !isBlackTurn;
                }

                // ターン表示更新
                turnLabel.setText(isBlackTurn ? "黒の番です" : "白の番です");

                repaint();
            }
        });
    }

    private Color getCurrentColor() {
        return isBlackTurn ? Color.BLACK : Color.WHITE;
    }

    private Color getOpponentColor() {
        return isBlackTurn ? Color.WHITE : Color.BLACK;
    }

    private boolean canPlaceDisc(int x, int y, Color color) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) return false;
        if (board[y][x] != null) return false;

        Color opponent = (color == Color.BLACK) ? Color.WHITE : Color.BLACK;

        for (int[] d : DIRECTIONS) {
            int dx = d[0], dy = d[1];
            int cx = x + dx, cy = y + dy;
            boolean hasOpponentBetween = false;

            while (cx >= 0 && cx < BOARD_SIZE && cy >= 0 && cy < BOARD_SIZE) {
                if (board[cy][cx] == opponent) {
                    hasOpponentBetween = true;
                } else if (board[cy][cx] == color) {
                    if (hasOpponentBetween) return true;
                    else break;
                } else {
                    break;
                }
                cx += dx;
                cy += dy;
            }
        }
        return false;
    }

    private void placeDisc(int x, int y, Color color) {
        board[y][x] = color;
        Color opponent = (color == Color.BLACK) ? Color.WHITE : Color.BLACK;

        for (int[] d : DIRECTIONS) {
            int dx = d[0], dy = d[1];
            int cx = x + dx, cy = y + dy;
            List<Point> toFlip = new ArrayList<>();

            while (cx >= 0 && cx < BOARD_SIZE && cy >= 0 && cy < BOARD_SIZE) {
                if (board[cy][cx] == opponent) {
                    toFlip.add(new Point(cx, cy));
                } else if (board[cy][cx] == color) {
                    for (Point p : toFlip) {
                        board[p.y][p.x] = color;
                    }
                    break;
                } else {
                    break;
                }
                cx += dx;
                cy += dy;
            }
        }
    }

    private boolean hasValidMove(Color color) {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (canPlaceDisc(x, y, color)) return true;
            }
        }
        return false;
    }

    private void countDiscsAndShowResult() {
        int black = 0, white = 0;
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (board[y][x] == Color.BLACK) black++;
                else if (board[y][x] == Color.WHITE) white++;
            }
        }

        String result;
        if (black > white) result = "黒の勝ち！ (" + black + " 対 " + white + ")";
        else if (white > black) result = "白の勝ち！ (" + white + " 対 " + black + ")";
        else result = "引き分け！ (" + black + " 対 " + white + ")";

        JOptionPane.showMessageDialog(this, result);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE);

        // グリッド
        g.setColor(Color.BLACK);
        for (int i = 0; i <= BOARD_SIZE; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, CELL_SIZE * BOARD_SIZE);
            g.drawLine(0, i * CELL_SIZE, CELL_SIZE * BOARD_SIZE, i * CELL_SIZE);
        }

        // 石描画
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (board[y][x] != null) {
                    OthelloDiscDrawer.drawDisc(g, x, y, board[y][x]);
                }
            }
        }
    }
}