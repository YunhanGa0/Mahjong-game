package Algorithm;

import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.ArrayList;

public class AI_Algorithm {
    public static int cpg1Count=0;
    public static int cpg2Count=0;
    public static  int cpg3Count=0;
    public static boolean is1=false;
    public static boolean is2=false;
    public static boolean is3=false;

    public static void EasyAI(GameJFrame gameJFrame,int playerIndex){
        ArrayList<MahjongCard> player= gameJFrame.getPlayerList().get(playerIndex);
        if(gameJFrame.getCurrentList().size()==0) {
            ShowCard(gameJFrame,playerIndex);
        }else {
            MahjongCard current = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1);
            if (Hu_Algorithm.checkHu(player)) {
                HuCards(gameJFrame, playerIndex, current);
            } else if (Other_Algorithm.CheckGang(player, current)) {
                GangCards(gameJFrame, playerIndex, current);
            } else if (Other_Algorithm.CheckPeng(player, current)) {
                PengCards(gameJFrame, playerIndex);
            } else if (Other_Algorithm.CheckChi(player, current)) {
                EatCards(gameJFrame, playerIndex);
            } else {
                ShowCard(gameJFrame, playerIndex);
            }
        }
    }

    //机器人出牌
    public static void ShowCard(GameJFrame gameJFrame, int playerIndex) {  //显示出去的牌的位置
        // 获取当前玩家手中的麻将牌列表
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        int i=player.size()-2;
        while(i>=0){
            MahjongCard card = player.get(i);
            //如果没有碰，吃，杠，说明牌可以出
            if (!card.getIfEat() && !card.getIfPeng() && !card.getIfGang()){
                break;
            }else {i--;}
        }
        MahjongCard c=player.get(i);
        gameJFrame.getCurrentList().add(c);
        //在手上的牌中，去掉已经出掉的牌
        player.remove(c);
        //计算坐标并移动牌
        //移动的目的是要出的牌移动到上方
        Point point = new Point();
        if(playerIndex==1){
            if (gameJFrame.num1/13==0) {
                point.x = 1040;
                point.y = 290 + gameJFrame.num1 * 35;
            }else {
                point.x = 1000;
                point.y = 290 + (gameJFrame.num1-13) * 35;
            }
            gameJFrame.num1++;
        }
        if(playerIndex==2){
            if (gameJFrame.num2/13==0) {
                point.x = 820-gameJFrame.num2*35;
                point.y = 340;
            }else {
                point.x = 820-(gameJFrame.num2-13)*35;
                point.y = 390;
            }
            gameJFrame.num2++;
        }
        if(playerIndex==3){
            if (gameJFrame.num2/13==0) {
                point.x = 260;
                point.y = 290+gameJFrame.num3*35;
            }else {
                point.x = 300;
                point.y = 290+(gameJFrame.num3-13)*35;
            }
            gameJFrame.num3++;
        }
        c.setLocation(point.x,point.y);
        //重新摆放剩余的牌
        Other_Algorithm.order(player);
        Other_Algorithm.rePosition(gameJFrame, player, playerIndex);
        // 展示出的麻将牌
        gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size()-1).turnFront();
    }

    //机器人的碰牌方法
    public static void PengCards(GameJFrame gameJFrame,int playerIndex){
        //通过弃牌堆找到要碰的牌
        MahjongCard pengCard = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size()-1);
        //获取中自己手上所有的牌
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        if(Other_Algorithm.CheckPeng(player,pengCard)){
            //先将牌加入到自己的牌中
            player.add(pengCard);
            int j=0;
            //遍历玩家手牌找到此牌并放到指定位置
            for (MahjongCard card : player) {
                if (card.getName().equals(pengCard.getName())) {
                    Point point = new Point();
                    if(playerIndex==1){
                        point.x = 1120-cpg1Count*40;      //850
                        point.y = 610+j*35;  //370
                        is1=true;
                    }
                    if(playerIndex==2){
                        point.x = 850+j*35;   //720
                        point.y = 200+cpg2Count*50;        //50
                        is2=true;
                    }
                    if(playerIndex==3){
                        point.x = 170+cpg3Count*40;        //80
                        point.y = 120+j*35;   //70
                        is3=true;
                    }
                    j++;
                    card.turnFront();
                    card.setCanClick(false);
                    //碰过的牌不能动了
                    card.setIfPeng(true);
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
            }
            if (is1){
                cpg1Count++;
                is1=false;
            }
            if (is2){
                cpg2Count++;
                is2=false;
            }
            if (is3){
                cpg3Count++;
                is3=false;
            }
            //碰后出牌
            ShowCard(gameJFrame,playerIndex);
        }
    }

    //机器人开杠
    public static void GangCards(GameJFrame gameJFrame,int playerIndex, MahjongCard GangCard){
        //获取中自己手上所有的牌
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        if(Other_Algorithm.CheckPeng(player,GangCard)){
            //先将牌加入到自己的牌中
            player.add(GangCard);
            //计数
            int j=0;
            //遍历玩家手牌找到此牌并放到指定位置
            for (MahjongCard card : player) {
                if (card.getName().equals(GangCard.getName())) {
                    Point point = new Point();
                    if(playerIndex==1){
                        point.x = 1120-cpg1Count*40;      //850
                        point.y = 610+j*35;  //370
                        is1=true;
                    }
                    if(playerIndex==2){
                        point.x = 850+j*35;   //720
                        point.y = 200+cpg2Count*50;        //50
                        is2=true;
                    }
                    if(playerIndex==3){
                        point.x = 170+cpg3Count*40;        //80
                        point.y = 120+j*35;   //70
                        is3=true;
                    }
                    Other_Algorithm.move(card, card.getLocation(), point);
                    card.turnFront();
                    //碰过的牌不能动了
                    card.setCanClick(false);
                    card.setIfGang(true);
                    j++;
                }
            }
            if (is1){
                cpg1Count++;
                is1=false;
            }
            if (is2){
                cpg2Count++;
                is2=false;
            }
            if (is3){
                cpg3Count++;
                is3=false;
            }
            //杠后出牌
            ShowCard(gameJFrame,playerIndex);
        }
    }

    //机器人开吃
    public static void EatCards(GameJFrame gameJFrame,int playerIndex){
        //通过弃牌堆找到要碰的牌
        MahjongCard chiCard = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size()-1);
        int color = getColor(chiCard);
        int size = getSize(chiCard);
        //获取中自己手上所有的牌
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        if(Other_Algorithm.CheckChi(player,chiCard)){
            //先将牌加入到自己的牌中
            player.add(chiCard);
            //计数
            int j=0;
            boolean smaller = false;
            boolean bigger = false;
            for (MahjongCard card : player) {
                if (getColor(card) == color){
                    if (getSize(card) == getSize(chiCard)-1){
                        smaller = true;
                    } else if (getSize(card) == getSize(chiCard)+1) {
                        bigger = true;
                    }
                }
            }
            //遍历玩家手牌找到此牌并放到指定位置
            for (MahjongCard card : player) {
                if (getColor(card) == getColor(chiCard) && !smaller){ // 牌堆里没有比chiCard小一张的→找cc+1和cc+2移动
                    if (getSize(card) == (getSize(chiCard)+1) ||  getSize(card) == (getSize(chiCard)+2)) {
                        Point point = new Point();
                        if(playerIndex==1){
                            point.x = 1120-cpg1Count*40;      //850
                            point.y = 610+j*35;  //370
                            is1=true;
                        }
                        if(playerIndex==2){
                            point.x = 850+j*35;   //720
                            point.y = 200+cpg2Count*50;        //50
                            is2=true;
                        }
                        if(playerIndex==3){
                            point.x = 170+cpg3Count*40;        //80
                            point.y = 120+j*35;   //70
                            is3=true;
                        }
                        Other_Algorithm.move(card, card.getLocation(), point);
                        card.turnFront();
                        //碰过的牌不能动了
                        card.setCanClick(false);
                        card.setIfEat(true);
                        j++;
                    }
                } else if (getColor(card) == getColor(chiCard) && !bigger) { // 牌堆里没有比chiCard大一张的→找cc-1和cc-2移动
                    if (getSize(card) == (getSize(chiCard)-1) ||  getSize(card) == (getSize(chiCard)-2)) {
                        Point point = new Point();
                        if(playerIndex==1){
                            point.x = 1120-cpg1Count*40;      //850
                            point.y = 610+j*35;  //370
                            is1=true;
                        }
                        if(playerIndex==2){
                            point.x = 850+j*35;   //720
                            point.y = 200+cpg2Count*50;        //50
                            is2=true;
                        }
                        if(playerIndex==3){
                            point.x = 170+cpg3Count*40;        //80
                            point.y = 120+j*35;   //70
                            is3=true;
                        }
                        Other_Algorithm.move(card, card.getLocation(), point);
                        //碰过的牌不能动了
                        card.setCanClick(false);
                        card.setIfEat(true);
                        j++;
                    }
                }
            }
            if (is1){
                cpg1Count++;
                is1=false;
            }
            if (is2){
                cpg2Count++;
                is2=false;
            }
            if (is3){
                cpg3Count++;
                is3=false;
            }
            //碰后出牌
            ShowCard(gameJFrame,playerIndex);
        }
    }

    //机器人开胡
    public static void HuCards(GameJFrame gameJFrame,int playerIndex, MahjongCard HuCard){
        //获取中自己手上所有的牌
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        //将牌添加进去
        player.add(HuCard);
        //重新摆放全部的牌
        Other_Algorithm.order(player);
        Other_Algorithm.rePosition(gameJFrame, player, playerIndex);
        //展示胡的牌
        for(MahjongCard card:player){
            card.turnFront();
        }
    }

    public static int getColor(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(0, 1));
    }

    public static int getSize(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(2));
    }

}
