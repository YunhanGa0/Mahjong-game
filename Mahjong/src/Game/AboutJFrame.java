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
                "1. 牌型组成\n" +
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
                "当牌局中的牌抓完还没有人胡牌，该局称为荒牌，通常这局不计分或者根据具体规则有所不同。"
        );
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setEditable(false);

        aboutText.setFont(new Font("Serif", Font.PLAIN, 14));

        // 添加文本区域到窗口
        add(aboutText);
    }
}