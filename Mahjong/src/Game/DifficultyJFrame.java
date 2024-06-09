package Game;

import javax.swing.*;

public class DifficultyJFrame extends JFrame {
    public static boolean isEasy, isMiddle, isHard;

    public DifficultyJFrame() {
        setTitle("Choose the difficulty");
        setSize(842, 842);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/MahjongPic/Difficulty.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null); // Use absolute layout

        // Create and configure the Easy button
        JButton easyButton = createButton(200);
        easyButton.addActionListener(e -> {
            isEasy = true;
            new GameJFrame().setVisible(true);
            dispose(); // Close the difficulty selection window
        });

        // Create and configure the Middle button
        JButton middleButton = createButton(355);
        middleButton.addActionListener(e -> {
            isMiddle = true;
            new GameJFrame().setVisible(true);
            dispose(); // Close the difficulty selection window
        });

        // Create and configure the Hard button
        JButton hardButton = createButton(510);
        hardButton.addActionListener(e -> {
            isHard = true;
            new GameJFrame().setVisible(true);
            dispose(); // Close the difficulty selection window
        });

        // Add buttons to the background label
        backgroundLabel.add(easyButton);
        backgroundLabel.add(middleButton);
        backgroundLabel.add(hardButton);

        // Set the background label as the content pane
        setContentPane(backgroundLabel);
        setVisible(true);
    }

    // Create a transparent button with specified bounds
    private JButton createButton(int x) {
        JButton button = new JButton();
        button.setBounds(x, 170, 120, 500);
        button.setOpaque(false); // Make button transparent
        button.setContentAreaFilled(false); // Make content area transparent
        button.setBorderPainted(false); // Do not paint the border
        return button;
    }
}
