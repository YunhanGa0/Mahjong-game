package Algorithm;

import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.*;

// Auxiliary methods needed for Mahjong game operations
public class Other_Algorithm {

    public static String chiSituation;

    // Check if the current card can be Peng
    public static boolean checkPeng(ArrayList<MahjongCard> cards, MahjongCard comingCard){
        // Record duplicate cards
        ArrayList<String> repeatCards = new ArrayList<>();
        // Record the count of each card
        HashMap<String, Integer> cardCounts = new HashMap<>();
        // Iterate through the hand to check the occurrence of each card
        for (MahjongCard card : cards) {
            if (!card.getIfPeng() && !card.getIfEat()) { // Cards that have been Peng, Eat, or Gang cannot be Peng again
                cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
            }
        }
        // Find cards that appear twice
        for (String name : cardCounts.keySet()) {
            if (cardCounts.get(name) == 2) {
                repeatCards.add(name);
            }
        }
        // Check if the comingCard matches any card in repeatCards
        for (String name : repeatCards) {
            if (name.equals(comingCard.getName())) {
                return true;
            }
        }
        return false;
    }

    // Check if the current card can be Gang (Ming Gang)
    public static boolean checkGang(ArrayList<MahjongCard> cards, MahjongCard comingCard){
        ArrayList<String> repeatCards = new ArrayList<>();
        HashMap<String, Integer> cardCounts = new HashMap<>();
        for (MahjongCard card : cards) {
            if (!card.getIfPeng() && !card.getIfEat()) { // Cards that have been Peng or Eat cannot be Ming Gang again
                cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
            }
        }
        // Find cards that appear three times
        for (String name : cardCounts.keySet()) {
            if (cardCounts.get(name) >= 3) {
                repeatCards.add(name);
            }
        }
        // Check if the comingCard matches any card in repeatCards
        for (String name : repeatCards) {
            if (name.equals(comingCard.getName())) {
                return true;
            }
        }
        return false;
    }

    // Check if the current card can be Dark Gang
    public static boolean checkDarkGang(ArrayList<MahjongCard> cards, MahjongCard comingCard){
        ArrayList<String> repeatCards = new ArrayList<>();
        // Count the occurrence of each card
        HashMap<String, Integer> cardCounts = new HashMap<>();
        for (MahjongCard card : cards) {
            if (!card.getIfEat()) { // Cards that have been Eat cannot be Dark Gang again
                cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
            }
        }
        // Since the card is added to the hand first, check if there are four cards
        for (String name : cardCounts.keySet()) {
            if (cardCounts.get(name) == 4) {
                repeatCards.add(name);
            }
        }
        // Check if the comingCard matches any card in repeatCards
        for (String name : repeatCards) {
            if (name.equals(comingCard.getName())) {
                return true;
            }
        }
        return false;
    }

    // Check if the current card can be eaten
    public static boolean checkEat(ArrayList<MahjongCard> cards, MahjongCard comingCard) {
        ArrayList<MahjongCard> wan = insertIn(cards, 0);
        ArrayList<MahjongCard> tiao = insertIn(cards, 1);
        ArrayList<MahjongCard> tong = insertIn(cards, 2);
        return switch (comingCard.getColor()) {
            case 0 -> helpCheckEat(wan, comingCard);
            case 1 -> helpCheckEat(tiao, comingCard);
            case 2 -> helpCheckEat(tong, comingCard);
            default -> false;
        };
    }

    // Auxiliary method for checking Eat
    public static boolean helpCheckEat(ArrayList<MahjongCard> list, MahjongCard comingCard) {
        order(list);
        int comingValue = comingCard.getValue();
        // 遍历当前牌组，查找可以与comingCard组成顺子的牌
        for (int i = 0; i < list.size(); i++) {
            int currentValue = list.get(i).getValue();
            // 检查当前牌和前一张牌是否与comingCard组成顺子
            if (i > 0) {
                int prevValue = list.get(i - 1).getValue();
                if (prevValue == comingValue - 1 && currentValue == comingValue + 1) {
                    setChiSituation("OXO");
                    return true; // (prevValue, comingValue, currentValue) 组成顺子
                } else if (prevValue == comingValue + 1 && currentValue == comingValue + 2) {
                    setChiSituation("XOO");
                    return true;
                } else if (prevValue == comingValue - 2 && currentValue == comingValue - 1) {
                    setChiSituation("OOX");
                    return true;
                }
            }
            // 检查当前牌和后一张牌是否与comingCard组成顺子
            if (i < list.size() - 1) {
                int nextValue = list.get(i + 1).getValue(); // 获取下一张牌的值
                if (currentValue == comingValue - 1 && nextValue == comingValue + 1) {
                    setChiSituation("OXO");
                    return true; // (currentValue, comingValue, nextValue) 组成顺子
                } else if (currentValue == comingValue + 1 && nextValue == comingValue + 2) {
                    setChiSituation("XOO");
                    return true;
                } else if (currentValue == comingValue - 2 && nextValue == comingValue - 1) {
                    setChiSituation("OOX");
                    return true;
                }
            }
        }

        return false; // 没有找到可以组成顺子的组合
    }

    // Auxiliary method for checking Eat
    public static ArrayList<MahjongCard> insertIn(ArrayList<MahjongCard> list, int color){
        ArrayList<MahjongCard> filteredList = new ArrayList<>();
        // 遍历数组中的每一个元素
        for (MahjongCard card : list) {
            //如果没有吃碰杠过
            if(!card.getIfEat()&&!card.getIfGang()&&!card.getIfPeng()) { //碰过吃过或杠过的牌不能再吃
                // 如果元素等于指定值，则加入filteredList
                if (card.getColor() == color) {
                    filteredList.add(card);
                }
            }
        }
        // 返回新的ArrayList
        return filteredList;
    }

    // Move card to the specified position
    public static void move(MahjongCard card, Point from, Point to) {
        if (to.x != from.x) {
            double k = (1.0) * (to.y - from.y) / (to.x - from.x);
            double b = to.y - to.x * k;
            int flag;
            if (from.x < to.x)
                flag = 20;
            else {
                flag = -20;
            }
            for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
                double y = k * i + b;
                card.setLocation(i, (int) y);
            }
        }
        card.setLocation(to);
    }

    // Reposition hand cards
    public static void rePosition(GameJFrame m, ArrayList<MahjongCard> list, int flag) {
        Point p = new Point();
        // Starting position
        // Me
        if (flag == 0) {
            p.x = 650 - (list.size() + 1) * 30 / 2;
            p.y = 820;
        }
        // Right
        if (flag == 1) {
            p.x = 1120;
            p.y = (950 / 2) - (list.size() + 1) * 20 / 2;
        }
        // Cross
        if (flag == 2) {
            p.x = (1300 / 2) - (list.size() + 1) * 30 / 2;
            p.y = 200;
        }
        // Left
        if (flag == 3) {
            p.x = 170;
            p.y = (950 / 2) - (list.size() + 1) * 20 / 2;
        }
        for (MahjongCard card : list) {
            if (!card.getIfPeng() && !card.getIfGang() && !card.getIfEat()) {
                Other_Algorithm.move(card, card.getLocation(), p);
                m.getContentPane().setComponentZOrder(card, 0);
            }
            if (flag == 0 || flag == 2 && (!card.getIfPeng() && !card.getIfGang() && !card.getIfEat()))
                p.x += 35;
            else
                p.y += 25;
        }
    }

    // Sort the cards in the collection based on their value
    // Bubble sort algorithm
    public static void order(ArrayList<MahjongCard> list) {
        int n = list.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                MahjongCard o1 = list.get(j);
                MahjongCard o2 = list.get(j + 1);
                // Check if the card has already been Peng, Gang, or Eat
                if ((o1.getIfPeng() || o1.getIfGang() || o1.getIfEat()) && (o2.getIfPeng() || o2.getIfGang() || o2.getIfEat())) continue;  // Both have been Peng, Gang, or Eat, do not change the order
                if (o1.getIfPeng() || o1.getIfGang() || o1.getIfEat()) {
                    // o1 has been Peng, Gang, or Eat, should be placed at the back
                    swap(list, j, j + 1);
                    swapped = true;
                    continue;
                }
                if (o2.getIfPeng() || o2.getIfGang() || o2.getIfEat()) continue; // o2 has been Peng, Gang, or Eat, should be placed at the back
                // Get the first digit, determine the color
                int a1 = Integer.parseInt(o1.getName().substring(0, 1));
                int a2 = Integer.parseInt(o2.getName().substring(0, 1));
                // Get the subsequent value
                int b1 = Integer.parseInt(o1.getName().substring(2));
                int b2 = Integer.parseInt(o2.getName().substring(2));
                // If the color is the same, sort by value
                if ((a1 - a2) == 0) {
                    if (b1 > b2) {
                        swap(list, j, j + 1);
                        swapped = true;
                    }
                } else {
                    if (a1 > a2) {
                        swap(list, j, j + 1);
                        swapped = true;
                    }
                }
            }
            // If no swaps were made, the array is sorted, exit the loop
            if (!swapped) break;
        }
    }

    // Helper method: swap two elements in an ArrayList
    private static void swap(ArrayList<MahjongCard> list, int i, int j) {
        MahjongCard temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static String getChiSituation(){
        return chiSituation;
    }

    public static void setChiSituation(String situation){
        chiSituation = situation;
    }
}
