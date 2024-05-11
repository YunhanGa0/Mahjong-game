package Algorithm;

import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;

//判断吃，碰，杠,返回值到GameJFrame中进行后续操作
public class Other_Algorithm {

    GameJFrame g;

    public static boolean CheckPeng(ArrayList<MahjongCard> cards, MahjongCard comingCard){ // Method to check if can Peng
        ArrayList<MahjongCard> repeatCards = new ArrayList<>();

        // 计数每种牌出现的次数
        HashMap<MahjongCard, Integer> cardCounts = new HashMap<>();
        for (MahjongCard card : cards) {
            cardCounts.put(card, cardCounts.getOrDefault(card, 0) + 1);
        }

        // 找出出现至少两次的牌
        for (MahjongCard card : cardCounts.keySet()) {
            if (cardCounts.get(card) >= 2) {
                repeatCards.add(card);
            }
        }

        // 检查comingCard是否与repeatCards中的任何一个相同
        for (MahjongCard card : repeatCards) {
            if (card.equals(comingCard)) {
                return true;
            }
        }

        return false;
    }

    public static boolean CheckGang(){ // Method to check if can gang
        return false;
    }

    public static boolean CheckChi(){
        return false;
    }

    //移动牌（有移动的动画效果）
    public static void move(MahjongCard card, Point from, Point to) {
        if (to.x != from.x) {
            double k = (1.0) * (to.y - from.y) / (to.x - from.x);
            double b = to.y - to.x * k;
            int flag = 0;
            if (from.x < to.x)
                flag = 20;
            else {
                flag = -20;
            }
            for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
                double y = k * i + b;

                card.setLocation(i, (int) y);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        card.setLocation(to);
    }


    //利用牌的价值，将集合中的牌进行排序
    //o1是原来的，o2是新增的
    public static void order(ArrayList<MahjongCard> list) {
        Collections.sort(list, new Comparator<MahjongCard>() {
            @Override
            public int compare(MahjongCard o1, MahjongCard o2) {

                //获得最前面的数字，判断花色（1条2万3筒45678东西南北中910发白）
                int a1 = Integer.parseInt(o1.getName().substring(0, 1));
                int a2 = Integer.parseInt(o2.getName().substring(0, 1));

                //获取后面的值
                int b1 = Integer.parseInt(o1.getName().substring(2));
                int b2 = Integer.parseInt(o2.getName().substring(2));

                //倒序排列
                int flag = b2 - b1;

                //如果牌的花色一样，则按照价值排序
                if ((a2-a1) == 0) {
                    return flag;
                } else {
                    return a2-a1;
                }
            }
        });
    }

    //获取当前牌的价值
    public static int getValue(MahjongCard card) {
        int i = Integer.parseInt(card.getName().substring(2));
        if (card.getName().substring(2).equals("2"))
            i += 13;
        if (card.getName().substring(2).equals("1"))
            i += 13;
        if (getColor(card) == 5)
            i += 2;
        return i;
    }

    //获取当前牌的花色
    public static int getColor(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(0, 1));
    }

    //重新摆放牌(需要更改位置参数)
    public static void rePosition(GameJFrame m, ArrayList<MahjongCard> list, int flag) {
        Point p = new Point();
        if (flag == 0) {
            p.x = 50;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        if (flag == 1) {
            p.x = (800 / 2) - (list.size() + 1) * 21 / 2;
            p.y = 450;
        }
        if (flag == 2) {
            p.x = 700;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        int len = list.size();
        for (int i = 0; i < len; i++) {
            MahjongCard poker = list.get(i);
            move(poker, poker.getLocation(), p);
            m.container.setComponentZOrder(poker, 0);
            if (flag == 1)
                p.x += 21;
            else
                p.y += 15;
        }
    }

    //算分用的，注意翻倍情况
    public static int getScore(ArrayList<MahjongCard> list) {
        int count = 0;
        for (int i = 0, len = list.size(); i < len; i++) {
            MahjongCard card = list.get(i);
            if (card.getName().substring(0, 1).equals("5")) {
                count += 5;
            }
            if (card.getName().substring(2).equals("2")) {
                count += 2;
            }
        }
        return count;
    }

    //将牌视为不可见状态
    public static void hideCards(ArrayList<MahjongCard> list) {
        for (int i = 0, len = list.size(); i < len; i++) {
            list.get(i).setVisible(false);
        }
    }

    //解析牌的值，用于比较牌的大小
    public List getCardByName(List<MahjongCard> list, String n) {
        String[] name = n.split(",");
        ArrayList cardsList = new ArrayList();
        int j = 0;
        for (int i = 0, len = list.size(); i < len; i++) {
            if (j < name.length && list.get(i).getName().equals(name[j])) {
                cardsList.add(list.get(i));
                i = 0;
                j++;
            }
        }
        return cardsList;
    }


    class MahjongCardIndex {
        ArrayList<ArrayList<Integer>> indexList = new ArrayList<>();
    }

}
