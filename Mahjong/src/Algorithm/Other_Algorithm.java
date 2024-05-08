package Algorithm;

import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//判断吃，碰，杠,返回值到GameJFrame中进行后续操作
public class Other_Algorithm {


    public boolean CheckPeng(){
        return false;
    }

    public boolean CheckGang(){
        return false;
    }

    public boolean CheckChi(){
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
    public static void order(ArrayList<MahjongCard> list) {
        //此时可以改为lambda表达式
        Collections.sort(list, new Comparator<MahjongCard>() {
            @Override
            public int compare(MahjongCard o1, MahjongCard o2) {

                int a1 = Integer.parseInt(o1.getName().substring(0, 1));
                int a2 = Integer.parseInt(o2.getName().substring(0, 1));

                int b1 = Integer.parseInt(o1.getName().substring(2));
                int b2 = Integer.parseInt(o2.getName().substring(2));

                //计算牌的价值，利用牌的价值进行排序

                //计算大小王牌的价值
                if (a1 == 5) {
                    b1 += 100;
                }
                if (a2 == 5) {
                    b2 += 100;
                }

                //计算A的价值
                if (b1 == 1) {
                    b1 += 20;
                }
                if (b2 == 1) {
                    b2 += 20;
                }
                //计算2的价值
                if (b1 == 2) {
                    b1 += 30;
                }
                if (b2 == 2) {
                    b2 += 30;
                }

                //倒序排列
                int flag = b2 - b1;

                //如果牌的价值一样，则按照花色排序
                if (flag == 0) {
                    return a2 - a1;
                } else {
                    return flag;
                }
            }
        });
    }

    //重新摆放牌
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

    public static int getColor(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(0, 1));
    }

    public static int getValue(MahjongCard poker) {
        int i = Integer.parseInt(poker.getName().substring(2));
        if (poker.getName().substring(2).equals("2"))
            i += 13;
        if (poker.getName().substring(2).equals("1"))
            i += 13;
        if (getColor(poker) == 5)
            i += 2;
        return i;
    }

    public static void getMax(MahjongCardIndex pokerIndex, ArrayList<MahjongCard> list) {
        int count[] = new int[14];
        for (int i = 0; i < 14; i++)
            count[i] = 0;

        for (int i = 0, len = list.size(); i < len; i++) {
            if (getColor(list.get(i)) == 5)// 王
                count[13]++;
            else
                count[getValue(list.get(i)) - 1]++;
        }
        ArrayList<ArrayList<Integer>> indexList = pokerIndex.indexList;
        for (int i = 0; i < 14; i++) {
            switch (count[i]) {
                case 1:
                    indexList.get(0).add(i + 1);
                    break;
                case 2:
                    indexList.get(1).add(i + 1);
                    break;
                case 3:
                    indexList.get(2).add(i + 1);
                    break;
                case 4:
                    indexList.get(3).add(i + 1);
                    break;
            }
        }
    }

    public static void hideCards(ArrayList<MahjongCard> list) {
        for (int i = 0, len = list.size(); i < len; i++) {
            list.get(i).setVisible(false);
        }
    }

    class MahjongCardIndex {
        ArrayList<ArrayList<Integer>> indexList = new ArrayList<>();
    }

}
