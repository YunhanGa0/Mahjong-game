package Game;

import javax.swing.*;
import java.awt.*;
public class AboutJFrame extends JFrame {
    public AboutJFrame() {
        // 设置窗口标题
        setTitle("Help");
        // 设置窗口大小
        setSize(900, 700);
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗口位置（相对于null，即屏幕中心）
        setLocationRelativeTo(null);

        // 创建文本区域以显示游戏相关信息
        JTextArea aboutText = new JTextArea(
                "1. Deck Composition\n" +
                "Beijing Mahjong typically uses a set of 144 tiles, which includes suits of bamboos, dots, characters, and honor tiles such as east, south, west, north, red, green, and white dragons. Some variations might not use flower tiles.\n" +
                "\n" +
                "2. Players\n" +
                "Beijing Mahjong is generally played by four players. Each player starts with 13 tiles, with the dealer drawing one extra tile.\n" +
                "\n" +
                "3. Winning\n" +
                "Basic Winning Hand: The basic winning hand requires forming 4 sets (either pungs or chows) + 1 pair of tiles.\n" +
                "\n" +
                "Missing One Suit: Players must lack one of the suits—characters, bamboos, or dots—in their hand, a unique rule in Beijing Mahjong.\n" +
                "Pure Suit: All tiles in the hand are of the same suit.\n" +
                "All Pungs: Consists of 4 pungs and 1 pair.\n" +
                "Seven Pairs: The hand consists of 7 identical pairs, no pungs or chows required.\n" +
                "Kong: Players can execute an exposed kong, concealed kong, or added kong. Typically, drawing an extra tile follows a kong.\n" +
                "\n" +
                "4. Gameplay\n" +
                "Discarding: Players take turns discarding unwanted tiles.\n" +
                "Chow, Pung, Kong: Players can make a chow (only from the previous player), pung, or kong based on the last discarded tile. Chows are less common in Beijing Mahjong.\n" +
                "Drawing Tiles: Beyond the initial deal, players draw tiles from the wall to continue the game.\n" +
                "Self-drawn and Discarded Win: Self-drawn refers to winning with a tile drawn by oneself, while discarded win refers to winning with a tile discarded by another player.\n" +
                "\n" +
                "5. Dealer Rules\n" +
                "Continuing Dealer: If the dealer wins or if all players exhaust tiles without winning (a draw), the dealer continues.\n" +
                "Passing the Deal: If the dealer does not win but another player does, the deal passes to the next player.\n" +
                "\n" +
                "6. Linked website\n" +
                "https://baike.baidu.com/item/%E5%8C%97%E4%BA%AC%E9%BA%BB%E5%B0%86/2093558"

        );
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setEditable(false);

        aboutText.setFont(new Font("Serif", Font.PLAIN, 14));

        // 添加文本区域到窗口
        add(aboutText);
    }
}