package Test;

import Algorithm.Hu_Algorithm;
import Objects.MahjongCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HuAlgorithmTest {

    private ArrayList<MahjongCard> handCards;

    @Before
    public void setUp() {
        handCards = new ArrayList<>();
    }

    @Test
    public void testWithoutWildCard() {
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("1-2"));
        handCards.add(new MahjongCard("1-5"));
        handCards.add(new MahjongCard("1-6"));
        handCards.add(new MahjongCard("1-7"));
        handCards.add(new MahjongCard("1-1"));

        MahjongCard wild = new MahjongCard("2-1");
        MahjongCard newc = new MahjongCard("0-1");
        assertFalse("Should not be able to Hu without wild card.", Hu_Algorithm.checkHu(handCards, newc, wild));
    }

    @Test
    public void testWithOneWildCard() {
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("1-2"));
        handCards.add(new MahjongCard("1-5"));
        handCards.add(new MahjongCard("1-6"));
        handCards.add(new MahjongCard("1-7"));
        handCards.add(new MahjongCard("4-0"));

        MahjongCard wildCard = new MahjongCard("4-0");
        MahjongCard newc = new MahjongCard("0-1");
        assertTrue("Should be able to Hu with one wild card.", Hu_Algorithm.checkHu(handCards, newc, wildCard));
    }

    @Test
    public void testWithMultipleWildCards() {
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-4"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("0-5"));
        handCards.add(new MahjongCard("1-2"));
        handCards.add(new MahjongCard("1-5"));
        handCards.add(new MahjongCard("1-6"));
        handCards.add(new MahjongCard("1-7"));
        handCards.add(new MahjongCard("4-0"));
        handCards.add(new MahjongCard("4-0"));

        MahjongCard wildCard = new MahjongCard("4-0");
        MahjongCard newc = new MahjongCard("0-1");
        assertTrue("Should be able to Hu with multiple wild cards.", Hu_Algorithm.checkHu(handCards, newc, wildCard));
    }

    @Test
    public void testVariousSuitsCombination() {
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-1"));
        handCards.add(new MahjongCard("0-2"));
        handCards.add(new MahjongCard("0-3"));
        handCards.add(new MahjongCard("1-2"));
        handCards.add(new MahjongCard("1-3"));
        handCards.add(new MahjongCard("1-4"));
        handCards.add(new MahjongCard("2-5"));
        handCards.add(new MahjongCard("2-6"));
        handCards.add(new MahjongCard("2-7"));
        handCards.add(new MahjongCard("3-0"));
        handCards.add(new MahjongCard("3-0"));
        handCards.add(new MahjongCard("3-0"));

        MahjongCard wildCard = new MahjongCard("4-0");
        MahjongCard newc = new MahjongCard("4-0");
        assertTrue("Should be able to Hu with various suits combination.", Hu_Algorithm.checkHu(handCards, newc, wildCard));
    }

    @Test
    public void testError() {
        handCards.add(new MahjongCard("1-3"));
        handCards.add(new MahjongCard("1-4"));
        handCards.add(new MahjongCard("1-4"));
        handCards.add(new MahjongCard("1-6"));
        handCards.add(new MahjongCard("1-7"));
        handCards.add(new MahjongCard("1-8"));
        handCards.add(new MahjongCard("2-1"));
        handCards.add(new MahjongCard("2-2"));
        handCards.add(new MahjongCard("2-3"));
        handCards.add(new MahjongCard("2-3"));
        handCards.add(new MahjongCard("2-4"));
        handCards.add(new MahjongCard("2-4"));
        handCards.add(new MahjongCard("2-7"));
        handCards.add(new MahjongCard("2-9"));

        MahjongCard wildCard = new MahjongCard("1-3");
        assertFalse("Should not be able to Hu with various suits combination.", Hu_Algorithm.checkHu(handCards, wildCard));
    }
}
