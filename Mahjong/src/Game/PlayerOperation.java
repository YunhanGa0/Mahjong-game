package Game;

import Algorithm.AI_Algorithm;
import Algorithm.Hu_Algorithm;
import Algorithm.Other_Algorithm;
import Objects.MahjongCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static Game.GameJFrame.numb;

public class PlayerOperation extends Thread {

    //游戏主界面
    static GameJFrame gameJFrame;

    //是否能走
    boolean isRun = true;

    // 检查其他玩家的操作（碰、杠、吃）
    static boolean actionTaken = false;

    //倒计时
    int i;

    public PlayerOperation(GameJFrame m, int i) {
        this.gameJFrame = m;
        this.i = i;
    }

    //定义一个方法用来暂停N秒
    //参数为等待的时间
    //因为线程中的sleep方法有异常，直接调用影响阅读
    public void sleep(int i) {
        try {
            Thread.sleep((long)i * 800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //倒计时限制玩家出牌操作
    public void timeWait(int n, int player) {
        if (player == 0) {
            int i = n;
            while (gameJFrame.nextPlayer == false && i >= 0) {
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1 && player == 0) {
                // 倒计时结束后的操作，打出摸到的牌或最后一次点击的牌
                // 弃牌堆(刚打出的牌);
                //遍历手上的牌，把要出的牌放到临时集合中
                ArrayList<MahjongCard> play=gameJFrame.playerList.get(0);
                for (int j = 0; j < play.size(); j++) {
                    MahjongCard card = play.get(j);
                    if (card.isClicked()&&(card.getifGang()==false&&card.getifPeng()==false&&card.getifEat()==false)) {
                        gameJFrame.currentList.add(card);
                        //在手上的牌中，去掉已经出掉的牌
                        play.remove(card);
                        //计算坐标并移动牌
                        //移动的目的是要出的牌移动到上方
                        Point point = new Point();
                        point.x = 430 + gameJFrame.num1 * 35;
                        point.y = 680;
                        gameJFrame.num1++;
                        Other_Algorithm.move(card, card.getLocation(), point);
                    }
                }
                if (gameJFrame.currentList.size()==0){
                    MahjongCard card=play.get(play.size()-1);
                    gameJFrame.currentList.add(card);
                    //在手上的牌中，去掉已经出掉的牌
                    play.remove(card);
                    //计算坐标并移动牌
                    //移动的目的是要出的牌移动到上方
                    Point point = new Point();
                    point.x = 430 + gameJFrame.num1 * 35;
                    point.y = 680;
                    gameJFrame.num1++;
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
            }
            gameJFrame.nextPlayer = false;
        } else {
            for (int i = n; i >= 0; i--) {
                sleep(1);
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
            }
        }
        gameJFrame.time[player].setVisible(false);
    }

    //倒计时限制玩家吃碰杠操作
    public void timeWaitOther(int n, int player) {
        if (player == 0) {
            int i = n;
            while (gameJFrame.conti == false && i >= 0) {
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            //不做操作没有影响
            gameJFrame.conti = false;
        } else {
            for (int i = n; i >= 0; i--) {
                sleep(1);
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
            }
        }
        gameJFrame.time[player].setVisible(false);
    }

    //控制游戏的主要流程，包括倒计时、判断玩家行动、更新游戏状态、和判断游戏结束条件等
    /**
     *
     * 游戏流程：
     * 初始化棋盘，掷骰子比大小确定庄家，随后发牌
     * 发牌后由庄家开始选择打出的牌，并打出
     * 打出后的牌进入弃牌堆，之后对其他玩家开始检测是否可以吃，碰，杠，胡
     * 如果可以的话，需要显示相对应的按钮，给玩家进行选择
     *（由于选择后回打乱出牌顺序，所以将三个情况放到checkBreak里统一返回参数）
     * 选择过后执行相应操作后，注意turn的转换，然后下一个人继续出牌
     *庄家不摸牌，直接先出一张牌，进判断其他人是否break，没有则到第二个玩家，
     * 先发一张牌，检测自己有没有胡，有的话就显示胡牌按钮，没有则选择出的牌打出，检测break，没有就下一个
     *
     */

    @Override
    public void run() {
        // 游戏开始,掷骰子定庄阶段
        while (i > -1 && isRun) {
            gameJFrame.time[1].setText("Roll骰子定庄家:");
            sleep(1);
            i--;
        }
        if (i == -1){
            if(gameJFrame.DealerFlag==0){
                gameJFrame.time[1].setText("你是庄家");
            }
            if(gameJFrame.DealerFlag==1){
                gameJFrame.time[1].setText("下家是庄家");
            }
            if(gameJFrame.DealerFlag==2){
                gameJFrame.time[1].setText("对家是庄家");
            }
            if(gameJFrame.DealerFlag==3){
                gameJFrame.time[1].setText("上家是庄家");
            }
        }
        // 初始化庄家，庄家是第一个出牌的玩家
        gameJFrame.turn = gameJFrame.DealerFlag;
        // 主游戏循环
        while (true) {
            // 检查是否有玩家已经胡牌
            if (win()) {
                break;
            }
            // 处理当前玩家的出牌逻辑
            processTurn(gameJFrame.turn);
        }
    }

    // 处理每个玩家的回合
    private void processTurn(int playerIndex) {
        // 给当前玩家发牌
        addcards(playerIndex);
        // 手牌可被点击
        for (MahjongCard cards : gameJFrame.playerList.get(0)) {
            cards.setCanClick(true);
        }
        //如果是✌️
        if (playerIndex == 0) { // 如果是玩家
            // 首先检测有没有胡和暗杠
            if(Hu_Algorithm.checkHu(gameJFrame.playerList.get(0))){
                //进行胡牌操作
                gameJFrame.hulord[0].setVisible(true);
                timeWaitOther(10,0);timeWait(10, 0);  // 玩家有10秒时间进行选择
                gameJFrame.hulord[0].setVisible(false);
            } else if(Other_Algorithm.CheckGang(gameJFrame.playerList.get(0),gameJFrame.playerList.get(0).get(gameJFrame.playerList.get(0).size()-1))){
                //进行暗杠牌操作
                gameJFrame.Other[2].setVisible(true);
                timeWaitOther(10, 0);  // 玩家有10秒时间进行操作
                gameJFrame.Other[2].setVisible(false);
                //如果杠了，得出牌
                if(actionTaken==true) {
                    gameJFrame.chulord[0].setVisible(true);
                    timeWait(10, 0);  // 玩家有10秒时间进行操作
                    gameJFrame.chulord[0].setVisible(false);
                }
                //判定值更新
                actionTaken=false;
            }else {
                //如果没有胡和杠的操作，那就只进行出牌操作
                gameJFrame.chulord[0].setVisible(true);
                timeWait(10, 0);  // 玩家有30秒时间进行操作
                gameJFrame.chulord[0].setVisible(false);//出牌按钮不可见
            }
        } else {
            // 电脑玩家的操作
            computerPlayerAction(playerIndex);
        }

        //检测是否有玩家可以进行操作
        // i出牌，放炮，其他人胡牌
        for (int j = (gameJFrame.turn + 1) % 4; j != gameJFrame.turn; j = (j + 1) % 4) {

            //获取玩家刚打出的牌
            MahjongCard lastCard = gameJFrame.currentList.get(gameJFrame.currentList.size() - 1);
            if (Hu_Algorithm.CheckHu(gameJFrame.playerList.get(j), lastCard)) {
                if (j == 0) { // 玩家操作
                    gameJFrame.hulord[0].setVisible(true);
                    timeWaitOther(5, 0);  // 玩家有5秒时间选择碰还是不碰
                    gameJFrame.hulord[0].setVisible(false);
                } else { // 电脑操作
                    computerPlayerAction(j);
                }
                if(gameJFrame.turn==0){gameJFrame.num1--;}
                if(gameJFrame.turn==1){gameJFrame.num2--;}
                if(gameJFrame.turn==2){gameJFrame.num3--;}
                if(gameJFrame.turn==3){gameJFrame.num4--;}
                gameJFrame.turn = j;
                break;
            }
            //判定是否能杠
            if (Other_Algorithm.CheckGang(gameJFrame.playerList.get(j), lastCard)) {
                if (j == 0) { // 玩家操作
                    gameJFrame.Other[2].setVisible(true);
                    timeWaitOther(10, 0);  // 玩家有10秒时间进行操作
                    gameJFrame.Other[2].setVisible(false);
                    if(actionTaken==true) {
                        gameJFrame.chulord[0].setVisible(true);
                        timeWait(10, 0);  // 玩家有10秒时间进行操作
                        gameJFrame.chulord[0].setVisible(false);
                    }
                    actionTaken=false;
                } else { // 电脑操作
                    computerPlayerAction(j);
                }
                if(gameJFrame.turn==0){gameJFrame.num1--;}
                if(gameJFrame.turn==1){gameJFrame.num2--;}
                if(gameJFrame.turn==2){gameJFrame.num3--;}
                if(gameJFrame.turn==3){gameJFrame.num4--;}
                gameJFrame.turn = j;
                break;
            }

            //判定是否能碰
            if (Other_Algorithm.CheckPeng(gameJFrame.playerList.get(j), lastCard)) {
                if (j == 0) { // 玩家操作
                    gameJFrame.Other[0].setVisible(true);
                    timeWaitOther(10, 0);  // 玩家有5秒时间选择碰还是不碰
                    gameJFrame.Other[0].setVisible(false);
                    if(actionTaken==true) {
                        gameJFrame.chulord[0].setVisible(true);
                        timeWait(10, 0);  // 玩家有10秒时间进行操作
                        gameJFrame.chulord[0].setVisible(false);
                    }
                    actionTaken=false;
                } else { // 电脑操作
                    computerPlayerAction(j);
                }
                if(gameJFrame.turn==0){gameJFrame.num1--;}
                if(gameJFrame.turn==1){gameJFrame.num2--;}
                if(gameJFrame.turn==2){gameJFrame.num3--;}
                if(gameJFrame.turn==3){gameJFrame.num4--;}
                gameJFrame.turn = j;
                break;
            }

            //判断是否能吃
            if (Other_Algorithm.CheckChi(gameJFrame.playerList.get(j), lastCard)) {
                if (j == 0) { // 玩家操作
                    gameJFrame.Other[1].setVisible(true);
                    timeWaitOther(5, 0);  // 玩家有5秒时间选择吃还是不吃
                    gameJFrame.Other[1].setVisible(false);
                    if(actionTaken==true) {
                        gameJFrame.chulord[0].setVisible(true);
                        timeWait(10, 0);  // 玩家有10秒时间进行操作
                        gameJFrame.chulord[0].setVisible(false);
                    }
                    actionTaken = false;
                } else { // 电脑操作
                    computerPlayerAction(j);
                }
                if(gameJFrame.turn==0){gameJFrame.num1--;}
                if(gameJFrame.turn==1){gameJFrame.num2--;}
                if(gameJFrame.turn==2){gameJFrame.num3--;}
                if(gameJFrame.turn==3){gameJFrame.num4--;}
                gameJFrame.turn = j;
                break;
            }
        }
        // 如果没有其他玩家进行碰、杠、吃等操作，则轮到下一个玩家出牌
        if (actionTaken==false) {
            gameJFrame.turn = (gameJFrame.turn + 1) % 4;
            actionTaken=false;
        }
        sleep(1);
    }

    // 电脑玩家的行动
    private void computerPlayerAction(int playerIndex) {
        switch (playerIndex) {
            case 1:
                computer1();
                break;
            case 2:
                computer2();
                break;
            case 3:
                computer3();
                break;
        }
    }

    //Three computer player
    public void computer1() {
        timeWait(1, 1);
        AI_Algorithm.EasyAI(gameJFrame,1);
    }

    public void computer2() {
        timeWait(1, 2);
        AI_Algorithm.EasyAI(gameJFrame,2);
    }

    public void computer3() {
        timeWait(1, 3);
        AI_Algorithm.EasyAI(gameJFrame,3);
    }

    //给玩家摸牌，同时move到指定位置
    public void addcards(int playerIndex){
        MahjongCard newCard=gameJFrame.getMahjongCardList().get(numb);
        if(playerIndex==0){
            Other_Algorithm.move(newCard, new Point(650, 450),new Point(950,820));
        }
        //将牌放入玩家牌盒
        gameJFrame.getPlayerList().get(playerIndex).add(newCard);
        newCard.turnFront();
        if (playerIndex==0){
            newCard.setCanClick(true);
        }
        numb++;
    }

    //检测是否有玩家胜利
    public boolean win () {
        if(gameJFrame.numb==122){
            String s="可惜，流局了";
            JOptionPane.showMessageDialog(gameJFrame, s);
            return true;
        }
        for (int i = 0; i < 4; i++) {
            if (Hu_Algorithm.checkHu(gameJFrame.playerList.get(i))) {
                String s;
                if (i == 0) {
                    s = "恭喜你，胜利了!";
                } else {
                    s = "恭喜电脑" + i + ",赢了! 你的智商有待提高哦";
                }
                for (int j = 0; j < gameJFrame.playerList.get((i + 1) % 4).size(); j++)
                    gameJFrame.playerList.get((i + 1) % 4).get(j).turnFront();
                for (int j = 0; j < gameJFrame.playerList.get((i + 2) % 4).size(); j++)
                    gameJFrame.playerList.get((i + 2) % 4).get(j).turnFront();
                for (int j = 0; j < gameJFrame.playerList.get((i + 3) % 4).size(); j++)
                    gameJFrame.playerList.get((i + 2) % 4).get(j).turnFront();
                JOptionPane.showMessageDialog(gameJFrame, s);
                return true;
            }
        }
        return false;
    }

    public static void setaction(boolean action){
        actionTaken=action;
    }

}


