package Game;

import javax.swing.*;

public class DifficultyJFrame extends JFrame {

    public DifficultyJFrame() {
        setTitle("Choose the difficulty");
        setSize(842, 842);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/MahjongPic/Difficulty.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null); // 使用绝对布局

        // 创建并设置按钮，设置为透明
        JButton easyButton = new JButton();
        easyButton.setBounds(200, 170, 120, 500);
        easyButton.setOpaque(false); // 设置为透明
        easyButton.setContentAreaFilled(false); // 内容区域填充为透明
        easyButton.setBorderPainted(false); // 不绘制边框
        easyButton.addActionListener(e -> new GameJFrame().setVisible(true));

        JButton middleButton = new JButton();
        middleButton.setBounds(355, 170, 120, 500);
        middleButton.setOpaque(false);
        middleButton.setContentAreaFilled(false);
        middleButton.setBorderPainted(false);
        middleButton.addActionListener(e -> new GameJFrame().setVisible(true));

        JButton hardButton = new JButton();
        hardButton.setBounds(510, 170, 120, 500);
        hardButton.setOpaque(false);
        hardButton.setContentAreaFilled(false);
        hardButton.setBorderPainted(false);
        hardButton.addActionListener(e -> new GameJFrame().setVisible(true));

        // 将按钮添加到背景标签
        backgroundLabel.add(easyButton);
        backgroundLabel.add(middleButton);
        backgroundLabel.add(hardButton);

        // 设置背景标签为内容面板
        setContentPane(backgroundLabel);
        setVisible(true);
    }
}
