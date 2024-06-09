package Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuJFrame extends JFrame implements ActionListener {
    private final JLabel backgroundLabel;

    public MenuJFrame() {
        setTitle("Mahjong Game");
        setSize(842, 842); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Load and set background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/MahjongPic/Menu.jpg"));
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 842, 842);
        add(backgroundLabel);

        initializeButtons(); // Initialize buttons

        setVisible(true);
    }

    // Initialize and place buttons
    private void initializeButtons() {
        int buttonWidth = 234;
        int buttonHeight = 48;
        int startX = 306;
        int startY = 356;
        int buttonGap = 45;

        // Create and add buttons
        createButton("start", startX, startY, buttonWidth, buttonHeight);
        createButton("options", startX, startY + buttonHeight + buttonGap, buttonWidth, buttonHeight);
        createButton("help", startX, startY + 2 * (buttonHeight + buttonGap), buttonWidth, buttonHeight);
        createButton("exit", startX, startY + 3 * (buttonHeight + buttonGap), buttonWidth, buttonHeight);
    }

    // Create a button and add it to the background label
    private void createButton(String command, int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setActionCommand(command); // Set command to identify the button
        button.addActionListener(this);
        backgroundLabel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Handle button actions
        switch (command) {
            case "start" -> new DifficultyJFrame().setVisible(true);
            case "options" -> new ShinJFrame().setVisible(true);
            case "help" -> new AboutJFrame().setVisible(true);
            case "exit" -> System.exit(0);
        }
    }
}
