package Game;

import javax.swing.*;
import java.awt.*;
public class AboutJFrame extends JFrame {
    public AboutJFrame() {
        // 设置窗口标题
        setTitle("关于游戏");
        // 设置窗口大小
        setSize(350, 200);
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗口位置（相对于null，即屏幕中心）
        setLocationRelativeTo(null);

        // 创建文本区域以显示游戏相关信息
        JTextArea aboutText = new JTextArea(
                "这是一个麻将游戏。开发者：你的名字。\n这个游戏是为了提供娱乐和教育的目的而制作的。"
        );
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setEditable(false);
        aboutText.setBackground(Color.LIGHT_GRAY);
        aboutText.setFont(new Font("Serif", Font.PLAIN, 14));

        // 添加文本区域到窗口
        add(aboutText);
    }
}