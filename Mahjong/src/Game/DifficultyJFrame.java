package Game;

import javax.swing.*;
import java.awt.*;

public class DifficultyJFrame extends JFrame {

    public DifficultyJFrame() {
        // 设置窗口标题
        setTitle("Choose the difficulty");
        // 设置窗口大小
        setSize(660, 660); // 根据图片尺寸调整窗口大小
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗口位置（相对于null，即屏幕中心）
        setLocationRelativeTo(null);

        // 加载背景图片
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\qwerty\\Pictures\\Saved Pictures\\微信图片_20240516182504.png"); // 确保路径正确
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null); // 使用绝对布局

        // 创建并设置按钮
        JButton easyButton = new JButton();
        easyButton.setBounds(160, 130, 95, 390); // 根据实际情况设置按钮位置和大小
        easyButton.setOpaque(false); // 设置为透明
        easyButton.setContentAreaFilled(false); // 设置内容区为透明
        easyButton.setBorderPainted(false); // 隐藏边框
        easyButton.addActionListener(e -> new GameJFrame().setVisible(true));

        JButton middleButton = new JButton();
        middleButton.setBounds(278, 130, 95, 390); // 根据实际情况设置按钮位置和大小
        middleButton.setOpaque(false); // 设置为透明
        middleButton.setContentAreaFilled(false); // 设置内容区为透明
        middleButton.setBorderPainted(false); // 隐藏边框
        middleButton.addActionListener(e -> new GameJFrame().setVisible(true));

        JButton hardButton = new JButton();
        hardButton.setBounds(395, 130, 95, 390); // 根据实际情况设置按钮位置和大小
        hardButton.setOpaque(false); // 设置为透明
        hardButton.setContentAreaFilled(false); // 设置内容区为透明
        hardButton.setBorderPainted(false); // 隐藏边框
        hardButton.addActionListener(e -> new GameJFrame().setVisible(true));

        // 将按钮添加到背景标签
        backgroundLabel.add(easyButton);
        backgroundLabel.add(middleButton);
        backgroundLabel.add(hardButton);

        // 添加背景标签到窗口
        setContentPane(backgroundLabel);

        // 确保所有内容都可见
        setVisible(true);
    }
}
