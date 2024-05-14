package Algorithm;

import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Game.GameJFrame.*;


//判断吃，碰，杠,返回值到GameJFrame中进行后续操作
public class Other_Algorithm {

    static GameJFrame gameJFrame;

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

    public static boolean CheckGang(ArrayList<MahjongCard> cards, MahjongCard comingCard){ // Method to check if can gang
        return false;
    }

    public static boolean CheckChi(ArrayList<MahjongCard> cards, MahjongCard comingCard){
        return false;
    }

    public static boolean CheckBreak(MahjongCard comingCard,int playerIndex){
        int i=(playerIndex+1)%4;
        while(i!=playerIndex){
            ArrayList<MahjongCard> temp=gameJFrame.playerList.get(i);
            if(CheckPeng(temp,comingCard)||CheckChi(temp,comingCard)||CheckGang(temp,comingCard)){
                return true;
            }else{
                i=(i+1)%4;
            }
        }
        return false;
    }

    public static int handlePlayerChoices(int playerIndex) {
        int i=(playerIndex+1)%4;
        MahjongCard comingCard=gameJFrame.currentList.getLast();
        while(i!=playerIndex){
            ArrayList<MahjongCard> temp=gameJFrame.playerList.get(i);
            if(CheckPeng(temp,comingCard)||CheckChi(temp,comingCard)||CheckGang(temp,comingCard)){
                break;
            }else{
                i=(i+1)%4;
            }
        }
        return i;
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
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        card.setLocation(to);
    }

    //出牌后放牌
    public static void moveto(MahjongCard card, int playerIndex){

    }

    //重新摆放牌(需要更改位置参数)
    public static void rePosition(GameJFrame m, ArrayList<MahjongCard> list, int flag) {
        Point p = new Point();
        //me
        if (flag == 0) {
            p.x = (960 / 2) - (list.size() + 1) * 30 / 2;
            p.y = 600;
        }
        //right
        if (flag == 1) {
            p.x = 850;
            p.y = (680 / 2) - (list.size() + 1) * 20 / 2;
        }
        //cross
        if (flag == 2) {
            p.x = (960 / 2) - (list.size() + 1) * 30 / 2;
            p.y = 50;
        }
        //left
        if (flag == 3) {
            p.x = 80;
            p.y = (680 / 2) - (list.size() + 1) * 20 / 2;

        }
        int len = list.size();
        for (int i = 0; i < len; i++) {
            MahjongCard poker = list.get(i);
            Other_Algorithm.move(poker, poker.getLocation(), p);
            m.container.setComponentZOrder(poker, 0);
            if (flag == 0 || flag == 2)
                p.x += 30;
            else
                p.y += 20;
        }
    }

    //给玩家摸牌，同时move到指定位置
    public static void addcards(int playerIndex){

        MahjongCard newCard=MahjongCardList.get(numb);
        //将牌放入玩家牌盒
        playerList.get(playerIndex).add(newCard);
        if (playerIndex==0){
            newCard.setCanClick(true);
        }
        numb++;
        /*
        //move到指定位置
        if(playerIndex==0){
            move(newCard,new Point(),null);
        }
        if(playerIndex==1){
            move(newCard,null,null);
        }
        if(playerIndex==2){
            move(newCard,null,null);
        }
        if(playerIndex==3){
            move(newCard,null,null);
        }

         */
    }


    //利用牌的价值，将集合中的牌进行排序
    //o1是原来的，o2是新增的
    public static void order(ArrayList<MahjongCard> list) {
        Collections.sort(list, new Comparator<MahjongCard>() {
            @Override
            public int compare(MahjongCard o1, MahjongCard o2) {

                //获得最前面的数字，判断花色（1万2条3筒45678东西南北中910发白）
                int a1 = Integer.parseInt(o1.getName().substring(0, 1));
                int a2 = Integer.parseInt(o2.getName().substring(0, 1));

                //获取后面的值
                int b1 = Integer.parseInt(o1.getName().substring(2));
                int b2 = Integer.parseInt(o2.getName().substring(2));

                //倒序排列
                int flag = b1 - b2;

                //如果牌的花色一样，则按照价值排序
                if ((a1-a2) == 0) {
                    return flag;
                } else {
                    return a1-a2;
                }
            }
        });
    }


    //获取当前牌的花色
    public static int getColor(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(0, 1));
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

}
