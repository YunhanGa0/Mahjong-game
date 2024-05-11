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