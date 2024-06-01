package Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuJFrame extends JFrame implements ActionListener {
    private final JLabel backgroundLabel;

    public MenuJFrame() {
        setTitle("Mahjong Game");
        setSize(842, 842); // 设置窗口大小与背景图片大小相同
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // 背景图片加载和设置
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/MahjongPic/Menu.jpg"));

        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 842, 842);
        add(backgroundLabel);

        // 初始化并放置按钮区域
        initializeButtons();

        // 确保所有内容都可见
        setVisible(true);
    }

    private void initializeButtons() {
        int buttonWidth = 234;  // 按钮宽度
        int buttonHeight = 48;  // 按钮高度
        int startX = 306;       // 第一个按钮的起始X位置
        int startY = 356;       // 第一个按钮的起始Y位置
        int buttonGap = 45;     // 按钮间的间距

        // 创建并添加透明按钮覆盖在背景图片的相应区域
        createButton("start", startX, startY, buttonWidth, buttonHeight);
        createButton("options", startX, startY + buttonHeight + buttonGap, buttonWidth, buttonHeight);
        createButton("help", startX, startY + 2 * (buttonHeight + buttonGap), buttonWidth, buttonHeight);
        createButton("exit", startX, startY + 3 * (buttonHeight + buttonGap), buttonWidth, buttonHeight);
    }

    private void createButton(String command, int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setActionCommand(command); // 设置按钮的命令，用于识别
        button.addActionListener(this); // 注册 ActionListener
        backgroundLabel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "start" -> new DifficultyJFrame().setVisible(true); // 显示游戏窗口
            case "options" -> new ShinJFrame().setVisible(true); // 显示难度选择窗口
            case "help" -> new AboutJFrame().setVisible(true); // 显示关于游戏窗口
            case "exit" -> System.exit(0);
        }
    }
}
