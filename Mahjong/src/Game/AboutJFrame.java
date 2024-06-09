package Game;

import javax.swing.*;
import java.awt.*;

public class AboutJFrame extends JFrame {
    public AboutJFrame() {
        setTitle("Help");
        setSize(842, 842);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/MahjongPic/Help.jpg"));

        // Create a panel with a custom paintComponent to draw the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Create a JLabel for the text with HTML content for better formatting
        JLabel textLabel = new JLabel();
        textLabel.setText("<html><body style='padding: 20px; font-size: 12px;'>" +
                "<div style='width:750px;'>" +
                "1. Deck Composition<br>" +
                "Beijing Mahjong typically uses a set of 144 tiles, which includes suits of bamboos, dots, characters, and honor tiles such as east, south, west, north, red, green, and white dragons. Some variations might not use flower tiles.<br><br>" +
                "2. Players<br>" +
                "Beijing Mahjong is generally played by four players. Each player starts with 13 tiles, with the dealer drawing one extra tile.<br><br>" +
                "3. Winning<br>" +
                "Basic Winning Hand: The basic winning hand requires forming 4 sets (either peng or chows) + 1 pair of tiles.<br>" +
                "Missing One Suit: Players must lack one of the suits—characters, bamboos, or dots—in their hand, a unique rule in Beijing Mahjong.<br>" +
                "Pure Suit: All tiles in the hand are of the same suit.<br>" +
                "All Peng: Consists of 4 peng and 1 pair.<br>" +
                "Seven Pairs: The hand consists of 7 identical pairs, no peng or chows required.<br>" +
                "Kong: Players can execute an exposed kong, concealed kong, or added kong. Typically, drawing an extra tile follows a kong.<br><br>" +
                "4. Gameplay<br>" +
                "Discarding: Players take turns discarding unwanted tiles.<br>" +
                "Chow, Peng, Kong: Players can make a chow (only from the previous player), peng, or kong based on the last discarded tile. Chows are less common in Beijing Mahjong.<br>" +
                "Drawing Tiles: Beyond the initial deal, players draw tiles from the wall to continue the game.<br>" +
                "Self-drawn and Discarded Win: Self-drawn refers to winning with a tile drawn by oneself, while discarded win refers to winning with a tile discarded by another player.<br><br>" +
                "5. Dealer Rules<br>" +
                "Continuing Dealer: If the dealer wins or if all players exhaust tiles without winning (a draw), the dealer continues.<br>" +
                "Passing the Deal: If the dealer does not win but another player does, the deal passes to the next player.<br><br>" +
                "6. Linked website<br>" +
                "https://baike.baidu.com/item/%E5%8C%97%E4%BA%AC%E9%BA%BB%E5%B0%86/2093558" +
                "</div></body></html>");
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        textLabel.setForeground(Color.white);

        // Make the JLabel transparent to show the background
        textLabel.setOpaque(false);

        // Add the JLabel to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(textLabel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the JScrollPane to the JPanel
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Set the JPanel as the content pane of the JFrame
        setContentPane(backgroundPanel);
        setVisible(true);
    }
}
