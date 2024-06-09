package Game;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class ShinJFrame extends JFrame {

    private static final Preferences prefs = Preferences.userRoot().node(ShinJFrame.class.getName());
    private static final String PREF_KEY = "isClassic";
    private final JButton classicTileButton;
    private final JButton animatedTileButton;

    public ShinJFrame() {
        setTitle("Choose the style of tile");
        setSize(842, 842);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load and set background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/MahjongPic/Shin.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null);

        // Create custom transparent buttons
        classicTileButton = new CustomButton();
        animatedTileButton = new CustomButton();

        classicTileButton.setBounds(249, 325, 130, 130);
        animatedTileButton.setBounds(468, 325, 130, 130);

        // Set initial button state based on preferences
        boolean isClassic = prefs.getBoolean(PREF_KEY, false);
        if (isClassic) {
            classicTileButton.setSelected(true);
            animatedTileButton.setSelected(false);
        } else {
            animatedTileButton.setSelected(true);
            classicTileButton.setSelected(false);
        }

        // Add action listeners to buttons
        classicTileButton.addActionListener(e -> {
            prefs.putBoolean(PREF_KEY, true);
            classicTileButton.setSelected(true);
            animatedTileButton.setSelected(false);
            repaint(); // Repaint to update button appearance
        });

        animatedTileButton.addActionListener(e -> {
            prefs.putBoolean(PREF_KEY, false);
            animatedTileButton.setSelected(true);
            classicTileButton.setSelected(false);
            repaint(); // Repaint to update button appearance
        });

        // Add buttons to background label
        backgroundLabel.add(classicTileButton);
        backgroundLabel.add(animatedTileButton);

        setContentPane(backgroundLabel);
        setVisible(true);
    }

    // Return the boolean type to Card class
    public static boolean getIsClassic() {
        return prefs.getBoolean(PREF_KEY, false);
    }

    // Custom button class to draw transparent background and a circle when selected
    private static class CustomButton extends JButton {
        private boolean isSelected = false;

        public CustomButton() {
            super();
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                int diameter = getWidth() / 4;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
                g2.drawOval(x, y, diameter, diameter); // Draw black circle when button is selected
            }
        }
    }
}
