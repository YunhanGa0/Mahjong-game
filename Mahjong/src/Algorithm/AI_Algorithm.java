package Algorithm;

import Game.PlayerOperation;
import com.sun.source.tree.Tree;
import Game.GameJFrame;
import Objects.MahjongCard;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class AI_Algorithm {

    public static void EasyAI(GameJFrame gameJFrame,int playerIndex){
        ArrayList<MahjongCard> player= gameJFrame.getPlayerList().get(playerIndex);
        MahjongCard current=gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size()-1);
        if(Hu_Algorithm.checkHu(player)){
            HuCards(gameJFrame,playerIndex,current);
        }
        else if(Other_Algorithm.CheckGang(player,current)){
            GangCards(gameJFrame,playerIndex,current);
        }
        else if(Other_Algorithm.CheckPeng(player,current)){
            PengCards(gameJFrame,playerIndex);
        }
        else if(Other_Algorithm.CheckChi(player,current)){
            EatCards(gameJFrame,playerIndex);
        }
        else{ ShowCard(gameJFrame,playerIndex);}
    }

    //机器人出牌
    public static void ShowCard(GameJFrame gameJFrame, int playerIndex) {  //显示出去的牌的位置
        // 获取当前玩家手中的麻将牌列表
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        int i=player.size()-2;
        while(i>=0){
            MahjongCard card = player.get(i);
            //如果没有碰，吃，杠，说明牌可以出
            if (card.getifEat()==false&&card.getifPeng()==false&&card.getifGang()==false){
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
            point.x = 1040;
            point.y = 290+gameJFrame.num2*35;
            gameJFrame.num2++;
        }
        if(playerIndex==2){
            point.x = 820-gameJFrame.num3*35;
            point.y = 320;
            gameJFrame.num3++;
        }
        if(playerIndex==3){
            point.x = 260;
            point.y = 290+gameJFrame.num4*35;
            gameJFrame.num4++;
        }
        c.setLocation(point.x,point.y);
        //重新摆放剩余的牌
        Other_Algorithm.order(player);
        Other_Algorithm.rePosition(gameJFrame, player, playerIndex);
        // 展示出的麻将牌
        gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size()-1).turnFront();
    }

    //机器人的碰牌方法
    public static void PengCards(GameJFrame gameJFrame,int playerIndex){  //调位置
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
                        point.x = 850;
                        point.y = 370+j*35;
                    }
                    if(playerIndex==2){
                        point.x = 720+j*35;
                        point.y = 50;
                    }
                    if(playerIndex==3){
                        point.x = 80;
                        point.y = 70+j*35;
                    }
                    j++;
                    card.setCanClick(false);
                    //碰过的牌不能动了
                    card.setIfPeng(true);
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
            }
            //碰后出牌
            ShowCard(gameJFrame,playerIndex);
        }
    }

    //机器人开杠
    public static void GangCards(GameJFrame gameJFrame,int playerIndex, MahjongCard GangCard){  //调位置
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
                        point.x = 850;
                        point.y = 500 - 13 * 20 / 2;
                    }
                    if(playerIndex==2){
                        point.x = 720;
                        point.y = 50;
                    }
                    if(playerIndex==3){
                        point.x = 80;
                        point.y = 200 - 13 * 20 / 2;
                    }
                    Other_Algorithm.move(card, card.getLocation(), point);
                    //碰过的牌不能动了
                    card.setCanClick(false);
                    card.setIfGang(true);
                    j++;
                }
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
                            point.x = 850;
                            point.y = 500 - 13 * 20 / 2;
                        }
                        if(playerIndex==2){
                            point.x = 720;
                            point.y = 50;
                        }
                        if(playerIndex==3){
                            point.x = 80;
                            point.y = 200 - 13 * 20 / 2;
                        }
                        Other_Algorithm.move(card, card.getLocation(), point);
                        //碰过的牌不能动了
                        card.setCanClick(false);
                        card.setIfEat(true);
                        j++;
                    }
                } else if (getColor(card) == getColor(chiCard) && !bigger) { // 牌堆里没有比chiCard大一张的→找cc-1和cc-2移动
                    if (getSize(card) == (getSize(chiCard)-1) ||  getSize(card) == (getSize(chiCard)-2)) {
                        Point point = new Point();
                        if(playerIndex==1){
                            point.x = 850;
                            point.y = 500 - 13 * 20 / 2;
                        }
                        if(playerIndex==2){
                            point.x = 720;
                            point.y = 50;
                        }
                        if(playerIndex==3){
                            point.x = 80;
                            point.y = 200 - 13 * 20 / 2;
                        }
                        Other_Algorithm.move(card, card.getLocation(), point);
                        //碰过的牌不能动了
                        card.setCanClick(false);
                        card.setIfEat(true);
                        j++;
                    }
                }
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
