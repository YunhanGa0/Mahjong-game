package Game;

import javax.swing.*;
import java.awt.*;

public class ShinJFrame extends JFrame {

    private boolean isClassic = false;

    public ShinJFrame() {
        // 设置窗口标题
        setTitle("Choose the style of tile");
        // 设置窗口大小
        setSize(660, 660); // 根据图片尺寸调整窗口大小
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗口位置（相对于null，即屏幕中心）
        setLocationRelativeTo(null);

        // 加载背景图片
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\qwerty\\Pictures\\Saved Pictures\\微信图片_20240516192551.png"); // 确保路径正确
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null); // 使用绝对布局

        // 创建按钮
        JButton classicTileButton = new JButton("Classic Tile");
        JButton animatedTileButton = new JButton("Animated Tile");

        // 设置按钮位置和大小
        classicTileButton.setBounds(193, 250, 105, 105);
        animatedTileButton.setBounds(355, 250, 108, 105);

        // 设置按钮为透明
        classicTileButton.setOpaque(false);
        classicTileButton.setContentAreaFilled(false);
        classicTileButton.setBorderPainted(false);

        animatedTileButton.setOpaque(false);
        animatedTileButton.setContentAreaFilled(false);
        animatedTileButton.setBorderPainted(false);

        // 添加事件监听器
        classicTileButton.addActionListener(e -> {
            isClassic = true;
            classicTileButton.setBackground(Color.white);
            classicTileButton.setOpaque(true);
            classicTileButton.setContentAreaFilled(true);
            animatedTileButton.setBackground(null); // 重置另一个按钮的背景色
            animatedTileButton.setOpaque(false);
            animatedTileButton.setContentAreaFilled(false);
        });

        animatedTileButton.addActionListener(e -> {
            isClassic = false;
            animatedTileButton.setBackground(Color.GRAY);
            animatedTileButton.setOpaque(true);
            animatedTileButton.setContentAreaFilled(true);
            classicTileButton.setBackground(null); // 重置另一个按钮的背景色
            classicTileButton.setOpaque(false);
            classicTileButton.setContentAreaFilled(false);
        });

        // 将按钮添加到背景标签
        backgroundLabel.add(classicTileButton);
        backgroundLabel.add(animatedTileButton);

        // 添加背景标签到窗口
        setContentPane(backgroundLabel);

        // 确保所有内容都可见
        setVisible(true);
    }

    public boolean isClassic() {
        return isClassic;
    }
}
