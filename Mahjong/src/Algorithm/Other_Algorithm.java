package Algorithm;

import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.*;

//判断吃，碰，杠,返回值到GameJFrame中进行后续操作
public class Other_Algorithm {

    public static boolean CheckPeng(ArrayList<MahjongCard> cards, MahjongCard comingCard){ // Method to check if can Peng
        ArrayList<String> repeatCards = new ArrayList<>();
        // 计数每种牌出现的次数
        HashMap<String, Integer> cardCounts = new HashMap<>();
        for (MahjongCard card : cards) {
            if(!card.getIfPeng()) {
                cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
            }
        }
        // 找出出现至少两次的牌
        for (String name : cardCounts.keySet()) {
            if (cardCounts.get(name) >= 2) {
                repeatCards.add(name);
            }
        }
        // 检查comingCard是否与repeatCards中的任何一个相同
        for (String name : repeatCards) {
            if (name.equals(comingCard.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean CheckGang(ArrayList<MahjongCard> cards, MahjongCard comingCard){ // Method to check gang
        ArrayList<String> repeatCards = new ArrayList<>();
        // 计数每种牌出现的次数
        HashMap<String, Integer> cardCounts = new HashMap<>();
        for (MahjongCard card : cards) {
            if(!card.getIfPeng()) {
                cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
            }
        }
        // 找出出现三次的牌
        for (String name : cardCounts.keySet()) {
            if (cardCounts.get(name) >= 4) {
                repeatCards.add(name);
            }
        }
        // 检查comingCard是否与repeatCards中的任何一个相同
        for (String name : repeatCards) {
            if (name.equals(comingCard.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean CheckChi(ArrayList<MahjongCard> cards, MahjongCard comingCard){
        ArrayList<MahjongCard> wan = insertIn(cards,1);
        ArrayList<MahjongCard> tiao = insertIn(cards,2);
        ArrayList<MahjongCard> tong = insertIn(cards,3);
        switch(getColor(comingCard))
        {
            case 1 :
                helpCheckChi(wan,comingCard);
            case 2 :
                helpCheckChi(tiao,comingCard);
            case 3 :
                helpCheckChi(tong,comingCard);
            default :
                return false;
        }
    }

    public static boolean helpCheckChi(ArrayList<MahjongCard> list, MahjongCard comingCard){
        HashSet<Integer> newValues = new HashSet<>();

        for (int i = 0; i < list.size(); i++) {
            int current = getColor(list.get(i));

            // Check for n, n+1 consecutive pair
            if (i < list.size() - 1 && getColor(list.get(i + 1)) == current + 1) {
                if (current - 1 >= 1) {
                    newValues.add(current - 1);
                }
                if (getColor(list.get(i + 1)) + 1 <= 9) {
                    newValues.add(getColor(list.get(i + 1)) + 1);
                }
            }

            // Check for n, n+2 pair
            if (i < list.size() - 2 && getColor(list.get(i + 2)) == current + 2) {
                if (getColor(list.get(i + 1)) >= 1 && getColor(list.get(i + 1)) <= 9) {
                    newValues.add(getColor(list.get(i + 1)));
                }
            }
        }
        // Check if the input value is in the newValues set
        return newValues.contains(getColor(comingCard));
    }

    public static ArrayList<MahjongCard> insertIn(ArrayList<MahjongCard> list, int color){
        ArrayList<MahjongCard> filteredList = new ArrayList<>();
        // 遍历数组中的每一个元素
        for (MahjongCard card : list) {
            // 如果元素等于指定值，则加入filteredList
            if (getColor(card) == color) {
                filteredList.add(card);
            }
        }
        // 返回新的ArrayList
        return filteredList;
    }

    //移动牌（有移动的动画效果）
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
                /*
                try {
                    Thread.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                 */
            }
        }
        card.setLocation(to);
    }

    //重新摆放牌(需要更改位置参数)
    public static void rePosition(GameJFrame m, ArrayList<MahjongCard> list, int flag) {
        Point p = new Point();
        //启始位置
        //me
        if (flag == 0) {
            p.x = (1300 / 2) - (list.size() + 1) * 30 / 2;
            p.y = 820;
        }
        //right
        if (flag == 1) {
            p.x = 1120;
            p.y = (950 / 2) - (list.size() + 1) * 20 / 2;
        }
        //cross
        if (flag == 2) {
            p.x = (1300 / 2) - (list.size() + 1) * 30 / 2;
            p.y = 200;
        }
        //left
        if (flag == 3) {
            p.x = 170;
            p.y = (950 / 2) - (list.size() + 1) * 20 / 2;
        }

        //int len = list.size();
        for (MahjongCard card : list) {
            if (!card.getIfPeng() && !card.getIfGang() && !card.getIfEat()) {
                Other_Algorithm.move(card, card.getLocation(), p);
                m.container.setComponentZOrder(card, 0);
            }
            if (flag == 0 || flag == 2 && (!card.getIfPeng() && !card.getIfGang() && !card.getIfEat()))
                p.x += 35;
            else
                p.y += 25;
        }
    }

    //利用牌的价值，将集合中的牌进行排序
    //o1是原来的，o2是新增的
    public static void order(ArrayList<MahjongCard> list) {
        Collections.sort(list, (o1, o2) -> {

            // 检查是否已经碰或杠
            if ((o1.getIfPeng()||o1.getIfGang()) && (o2.getIfPeng()||o2.getIfGang())) return 0;  // 都碰或杠过，不调整顺序
            if (o1.getIfPeng()||o1.getIfGang()) return 1;  // o1碰或杠过，应放后面
            if (o2.getIfPeng()||o2.getIfGang()) return -1; // o2碰或杠过，应放后面

            //获得最前面的数字，判断花色
            int a1 = Integer.parseInt(o1.getName().substring(0, 1));
            int a2 = Integer.parseInt(o2.getName().substring(0, 1));

            //获取后面的值
            int b1 = Integer.parseInt(o1.getName().substring(2));
            int b2 = Integer.parseInt(o2.getName().substring(2));

            //如果牌的花色一样，则按照价值排序
            if ((a1-a2) == 0) {
                return b1-b2;
            } else {
                return a1-a2;
            }
        });
    }

    public static int getColor(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(0, 1));
    }

}
