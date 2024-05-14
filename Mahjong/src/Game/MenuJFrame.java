package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuJFrame extends JFrame implements ActionListener {
    private JButton startGameButton;
    private JButton aboutGameButton;
    private JLabel titleLabel;

    public MenuJFrame() {
        setTitle("麻将游戏菜单");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        titleLabel = new JLabel("麻将游戏", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 0, 10, 0);

        add(titleLabel, constraints);

        startGameButton = new JButton("开始游戏");
        aboutGameButton = new JButton("关于游戏");
        startGameButton.addActionListener(this);
        aboutGameButton.addActionListener(this);
        startGameButton.setFont(new Font("Serif", Font.BOLD, 16));
        aboutGameButton.setFont(new Font("Serif", Font.BOLD, 16));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setBackground(Color.BLACK);
        aboutGameButton.setForeground(Color.WHITE);
        aboutGameButton.setBackground(Color.BLACK);

        add(startGameButton, constraints);
        add(aboutGameButton, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == startGameButton) {
            new DifficultyJFrame().setVisible(true); // 显示难度选择窗口
        }
        if (source == aboutGameButton) {
            new AboutJFrame().setVisible(true); // 显示关于游戏窗口
        }
    }

}
/*
package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuJFrame extends JFrame {
    private JButton startButton, optionsButton, helpButton, exitButton;
    private JLabel backgroundLabel;

    private boolean easyComputer = true;  // 默认开启
    private boolean hardComputer = false;
    private boolean superComputer = false;


    public MenuJFrame() {
        setTitle("Mahjong Game");
        setSize(842, 842); // 设置窗口大小与背景图片大小相同
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // 背景图片加载和设置
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\qwerty\\Pictures\\Saved Pictures\\微信图片_20240512132059.png");
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 842, 842);
        add(backgroundLabel);

        // 初始化并放置按钮
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

        Font buttonFont = new Font("SansSerif", Font.BOLD, 16); // 字体设置

        // 创建并添加按钮
        startButton = createButton("start", startX, startY, buttonWidth, buttonHeight, buttonFont);
        optionsButton = createButton("options", startX, startY + buttonHeight + buttonGap, buttonWidth, buttonHeight, buttonFont);
        helpButton = createButton("help", startX, startY + 2 * (buttonHeight + buttonGap), buttonWidth, buttonHeight, buttonFont);
        exitButton = createButton("exit", startX, startY + 3 * (buttonHeight + buttonGap), buttonWidth, buttonHeight, buttonFont);

        backgroundLabel.add(startButton);
        backgroundLabel.add(optionsButton);
        backgroundLabel.add(helpButton);
        backgroundLabel.add(exitButton);
    }
    private void showDialog(String content, boolean isImagePath) {
        JDialog dialog = new JDialog(this, "Help", true);
        if (isImagePath) {
            ImageIcon imageIcon = new ImageIcon(content);
            JLabel label = new JLabel(imageIcon);
            dialog.add(label);
        } else {
            JTextArea textArea = new JTextArea(content);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            dialog.add(scrollPane);
        }
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void showOptionsDialog() {
        JDialog dialog = new JDialog(this, "Select Difficulty", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(942, 942);
        dialog.setResizable(false);
        dialog.setLayout(null);

        // 设置新的背景图片
        ImageIcon newBackgroundImage = new ImageIcon("C:\\Users\\qwerty\\Pictures\\Saved Pictures\\微信图片_20240512170828.png");
        JLabel newBackgroundLabel = new JLabel(newBackgroundImage);
        newBackgroundLabel.setBounds(0, 0, 942, 942);
        dialog.add(newBackgroundLabel);

        // 创建复选框面板
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setOpaque(false); // 使面板透明
        panel.setBounds(370, 415, 300, 150); // 设置面板的位置和大小

        // 创建复选框
        JCheckBox easyCheckBox = new JCheckBox("Easy Computer", easyComputer);
        JCheckBox hardCheckBox = new JCheckBox("Hard Computer", hardComputer);
        JCheckBox superCheckBox = new JCheckBox("Super Computer", superComputer);

        // 设置复选框的字体大小和样式
        Font checkBoxFont = new Font("SansSerif", Font.BOLD, 22);
        easyCheckBox.setFont(checkBoxFont);
        hardCheckBox.setFont(checkBoxFont);
        superCheckBox.setFont(checkBoxFont);


        // 设置复选框透明
        easyCheckBox.setOpaque(false);
        hardCheckBox.setOpaque(false);
        superCheckBox.setOpaque(false);

        // 添加复选框的 ItemListener
        easyCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                easyComputer = true;
                hardComputer = false;
                superComputer = false;
                hardCheckBox.setSelected(false);
                superCheckBox.setSelected(false);
            }
        });

        hardCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                hardComputer = true;
                easyComputer = false;
                superComputer = false;
                easyCheckBox.setSelected(false);
                superCheckBox.setSelected(false);
            }
        });

        superCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                superComputer = true;
                easyComputer = false;
                hardComputer = false;
                easyCheckBox.setSelected(false);
                hardCheckBox.setSelected(false);
            }
        });

        // 将复选框添加到面板中
        panel.add(easyCheckBox);
        panel.add(hardCheckBox);
        panel.add(superCheckBox);

        // 将面板添加到背景标签中
        newBackgroundLabel.add(panel);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private JButton createButton(String command, int x, int y, int width, int height, Font font) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.DARK_GRAY); // 字体颜色
        button.setActionCommand(command); // 设置按钮的命令，用于识别
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (command) {
                    case "options":
                        showOptionsDialog(); // 显示难度选择对话框
                        break;
                    case "exit":
                        System.exit(0);
                        break;
                    case "help":
                        showDialog("1. 牌型组成\n" +
                         "北京麻将通常使用包含条、饼、万以及东南西北中发白等字牌的144张牌。有些玩法中可能不使用花牌。\n" +
                         "\n" +
                         "2. 游戏玩家\n" +
                         "北京麻将通常由4名玩家进行，每人初始发牌13张，庄家多抓一张牌。\n" +
                         "\n" +
                         "3. 胡牌和番种\n" +
                         "基本胡牌：基础的胡牌需要形成4个刻子（或顺子）+ 1对将。\n" +
                         "缺一门：玩家的手牌中必须缺少万、条、饼中的一门，这是北京麻将的一项特殊规定。\n" +
                         "清一色：全部手牌为同一花色的牌。\n" +
                         "对对胡：由4个刻子和1对将组成。\n" +
                         "七小对：手牌由7对相同的牌组成，不需要刻子或顺子。\n" +
                         "杠：玩家可以进行明杠、暗杠和加杠。杠牌后通常可以再抓一张牌。\n" +
                         "\n" +
                         "4. 打牌过程\n" +
                         "出牌：玩家轮流打出手中不需要的牌。\n" +
                         "吃、碰、杠：玩家可以根据前一家打出的牌进行吃（只能吃前一家）、碰或杠牌。吃牌较少见于北京麻将。\n" +
                         "抓牌：除了初始发牌外，每轮玩家从牌堆中抓牌继续游戏。\n" +
                         "自摸和放炮：自摸指玩家抓起的牌自己胡牌，放炮指玩家打出的牌被其他玩家胡。\n" +
                         "\n" +
                         "5. 计分规则\n" +
                         "北京麻将的计分可能根据不同的牌馆或地区有所不同，但通常情况下，胡牌的基本分会根据番种（翻倍规则）进行计算。各种杠牌也有对应的得分。\n" +
                         "\n" +
                         "6. 庄家规则\n" +
                         "连庄：如果庄家胡牌或者所有玩家荒牌（未胡牌），庄家继续连庄。\n" +
                         "轮庄：如果庄家未胡牌而其他玩家胡牌，下一局的庄家将轮换至下一位玩家。\n" +
                         "\n" +
                         "7. 荒牌\n" +
                         "当牌局中的牌抓完还没有人胡牌，该局称为荒牌，通常这局不计分或者根据具体规则有所不同。", false);
                        break;
                    default:
                        JOptionPane.showMessageDialog(MenuJFrame.this, command + " button clicked!");
                        break;
                }
            }
        });
        return button;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuJFrame::new);
    }
}


 */