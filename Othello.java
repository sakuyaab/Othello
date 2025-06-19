package Othello;
import javax.swing.*;
import java.awt.*; 

public class Othello {
    public static void main(String[] args) {
        JFrame frame = new JFrame("オセロ盤面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // ターン表示ラベル
        JLabel turnLabel = new JLabel("黒の番です", SwingConstants.CENTER);
        turnLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); 
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 盤面とラベルをまとめる
        OthelloBoard board = new OthelloBoard(turnLabel);

        // レイアウト
        frame.setLayout(new BorderLayout());
        frame.add(board, BorderLayout.CENTER);
        frame.add(turnLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
