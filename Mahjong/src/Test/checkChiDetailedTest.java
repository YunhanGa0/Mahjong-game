package Test;

import Algorithm.Other_Algorithm;
import Objects.MahjongCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class checkChiDetailedTest {

    private ArrayList<MahjongCard> cards, cards2;
    private MahjongCard comingCard1, comingCard2, comingCard3, card1, card2, card3, card4;

    @Before
    public void setUp() {
        cards = new ArrayList<>();
        cards2 = new ArrayList<>();
        // 假设牌的名称格式为 "花色_值"
        card1 = new MahjongCard("1_2");
        card2 = new MahjongCard("1_3");
        card3 = new MahjongCard("1_4");
        card4 = new MahjongCard("1_6");
        comingCard1 = new MahjongCard("1_1");
        comingCard2 = new MahjongCard("1_5");
        comingCard3 = new MahjongCard("1_7");

        //card1.setIfEat(true);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);

        cards2.add(card1);
        cards2.add(card3);
    }

    @Test
    public void testCheckChi() {
        // 测试可以组成顺子的情况
        // comingCard is the first card of Chi
        assertTrue(Other_Algorithm.checkEat(cards, comingCard1)); // ('1', 2, 3)
        // comingCard is the second card of Chi
        assertTrue(Other_Algorithm.checkEat(cards, comingCard2)); // (3, 4, '5')
        // comingCard is the third card of Chi
        assertTrue(Other_Algorithm.checkEat(cards2, card2)); // (2, '3', 4)

        // 添加无法组成顺子的牌
        cards.add(card4);
        assertFalse(Other_Algorithm.checkEat(cards, comingCard3)); // 无法组成顺子 (6, 7, 8)
    }
}
