package Test;

import Algorithm.Other_Algorithm;
import Objects.MahjongCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class Other_AlgorithmGeneralTest {

    private ArrayList<MahjongCard> cards;
    private MahjongCard card1, card2, card3, comingCard;

    @Before
    public void setUp() {
        cards = new ArrayList<>();
        card1 = new MahjongCard("1_1");
        card2 = new MahjongCard("1_1");
        card3 = new MahjongCard("1_1");
        comingCard = new MahjongCard("1_1");
        // checkPeng and checkGang check array setting
        cards.add(card1);
        cards.add(card2);
    }

    @Test
    public void testCheckPeng() {
        assertTrue(Other_Algorithm.checkPeng(cards, comingCard));
        cards.add(card3);
        assertFalse(Other_Algorithm.checkPeng(cards, comingCard));
    }

    @Test
    public void testCheckGang() {
        cards.add(card3);
        assertTrue(Other_Algorithm.checkGang(cards, comingCard));
        cards.remove(card3);
        assertFalse(Other_Algorithm.checkGang(cards, comingCard));
    }

    @Test
    public void testOrder() {
        MahjongCard card4 = new MahjongCard("2_1");
        MahjongCard card5 = new MahjongCard("1_2");
        cards.add(card4);
        cards.add(card5);
        Other_Algorithm.order(cards);
        assertEquals("1_1", cards.get(0).getName());
        assertEquals("1_1", cards.get(1).getName());
        assertEquals("1_2", cards.get(2).getName());
        assertEquals("2_1", cards.get(3).getName());
    }
}
