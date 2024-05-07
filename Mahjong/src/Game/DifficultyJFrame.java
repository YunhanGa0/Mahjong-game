package Game;

import javax.swing.*;
import java.awt.*;

public class DifficultyJFrame extends JFrame {
    public DifficultyJFrame() {
        // 设置窗口标题
        setTitle("选择游戏难度");
        // 设置窗口大小
        setSize(300, 200);
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗口位置（相对于null，即屏幕中心）
        setLocationRelativeTo(null);

        // 创建面板用于放置按钮
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));  // 使用网格布局

        // 创建按钮并添加到面板
        JButton easyButton = new JButton("Easy");
        JButton middleButton = new JButton("Middle");
        JButton hardButton = new JButton("Hard");

        // 添加事件监听器
        easyButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "你选择了 Easy 难度"));
        middleButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "你选择了 Middle 难度"));
        hardButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "你选择了 Hard 难度"));

        panel.add(easyButton);
        panel.add(middleButton);
        panel.add(hardButton);

        // 添加面板到窗口
        add(panel);
    }

}