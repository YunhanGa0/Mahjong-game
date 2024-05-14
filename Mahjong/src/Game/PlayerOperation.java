package Game;

import Algorithm.Hu_Algorithm;
import Algorithm.Other_Algorithm;
import Objects.MahjongCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Algorithm.Other_Algorithm.hideCards;

public class PlayerOperation extends Thread {

    //游戏主界面
    GameJFrame gameJFrame;

    //是否能走
    boolean isRun = true;

    //倒计时
    int i;

    public PlayerOperation(GameJFrame m, int i) {
        this.gameJFrame = m;
        this.i = i;
    }

    //控制游戏的主要流程，包括倒计时、判断玩家行动、更新游戏状态、和判断游戏结束条件等
    /**
     *
     * 游戏流程：
     *
     * 初始化棋盘，掷骰子比大小确定庄家，随后发牌
     *
     * 发牌后由庄家开始选择打出的牌，并打出
     *
     * 打出后的牌进入弃牌堆，之后对其他玩家开始检测是否可以吃，碰，杠，胡
     *
     * 如果可以的话，需要显示相对应的按钮，给玩家进行选择
     *（由于选择后回打乱出牌顺序，所以将三个情况放到checkBreak里统一返回参数）
     *
     * 选择过后执行相应操作后，注意turn的转换，然后下一个人继续出牌
     *
     *
     *庄家不摸牌，直接先出一张牌，进判断其他人是否break，没有则到第二个玩家，
     * 先发一张牌，检测自己有没有胡，有的话就显示胡牌按钮，没有则选择出的牌打出，检测break，没有就下一个
     *
     *
     *
     *
     */

    @Override
    public void run() {
        // 游戏开始的倒计时
        while (i > -1 && isRun) {
            gameJFrame.time[1].setText("倒计时:" + i--);
            sleep(1);
        }
        // 倒计时结束后的操作，这里可以添加发牌逻辑
        // 例如庄家刚摸到的牌需要打出
        if (i == -1) {
            // 玩家打出最后摸到的牌（牌在最后一个位置）
            gameJFrame.playerList.get(gameJFrame.turn).remove(gameJFrame.playerList.get(gameJFrame.turn).getLast()); // 删除并获取这张牌，然后放到弃牌堆
            // 弃牌堆.add(刚打出的牌);
            gameJFrame.currentList.add(gameJFrame.playerList.get(gameJFrame.turn).getLast());
        }
        // 初始化庄家，庄家从DealerFlag开始，也是第一个出牌的玩家
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

    // 处理单个玩家的回合
    private void processTurn(int playerIndex) {
        //如果起手14张，说明是庄家需要直接出牌，不需要发牌
        if(gameJFrame.playerList.get(playerIndex).size() == 14&&playerIndex == 0) {
            gameJFrame.chulord[0].setEnabled(true);
            for (MahjongCard cards : gameJFrame.playerList.get(0)) {
                cards.setCanClick(true);// 可被点击
            }
            timeWait(10, 0);  // 玩家有30秒时间进行操作
            gameJFrame.chulord[0].setVisible(false);
            gameJFrame.chulord[0].setEnabled(false);
        }else if (gameJFrame.playerList.get(playerIndex).size() == 14&&!(playerIndex == 0)){
                // 电脑玩家的操作
                computerPlayerAction(playerIndex);
        }else{
            //正常轮次开始先发牌再打
            // 给当前玩家发牌
            Other_Algorithm.addcards(playerIndex);
            // 如果是玩家，启用出牌按钮等待玩家操作
            if (playerIndex == 0) {
                gameJFrame.chulord[0].setEnabled(true);
                for (MahjongCard cards : gameJFrame.playerList.get(0)) {
                    cards.setCanClick(true);// 可被点击
                }
                timeWait(10, 0);  // 玩家有30秒时间进行操作
                gameJFrame.chulord[0].setEnabled(false);
            } else {
                // 电脑玩家的操作
                computerPlayerAction(playerIndex);
            }
        }

        // 检查其他玩家是否有吃、碰、杠、胡的机会
        if (Other_Algorithm.CheckBreak(gameJFrame.currentList.getLast(),playerIndex)) {
            playerIndex=Other_Algorithm.handlePlayerChoices(playerIndex);
            // 如果有机会，处理相应的逻辑
            if (playerIndex == 0) {
                gameJFrame.chulord[0].setEnabled(true);
                for (MahjongCard cards : gameJFrame.playerList.get(0)){
                    cards.setCanClick(true);// 可被点击
                }
                timeWait(10, 0);  // 玩家有10秒时间进行操作
                gameJFrame.chulord[0].setEnabled(false);
            } else {
                // 电脑玩家的操作
                computerPlayerAction(playerIndex);
            }
            gameJFrame.turn =(Other_Algorithm.handlePlayerChoices(playerIndex)+1)%4;
        }else{
            // 轮到下一个玩家
            gameJFrame.turn = (gameJFrame.turn + 1) % 4;
        }

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

    //定义一个方法用来暂停N秒
    //参数为等待的时间
    //因为线程中的sleep方法有异常，直接调用影响阅读
    public void sleep(int i) {
        try {
            Thread.sleep((long)i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //庄家旗帜位置
    public void setlord(int i) {
        Point point = new Point();
        if (i == 0) {
            point.x = 80;
            point.y = 20;
            gameJFrame.DealerFlag = 0;
        }
        if (i == 1) {
            point.x = 80;
            point.y = 430;
            gameJFrame.DealerFlag = 1;
        }
        if (i == 2) {
            point.x = 700;
            point.y = 20;
            gameJFrame.DealerFlag = 2;
        }
        if (i == 3) {
            point.x = 700;
            point.y = 20;
            gameJFrame.DealerFlag = 3;
        }
        gameJFrame.Dealer.setLocation(point);
        gameJFrame.Dealer.setVisible(true);
    }

    //Three computer player
    public void computer1() {
        timeWait(1, 1);
        ShowCard(1);
    }

    public void computer2() {
        timeWait(1, 2);
        ShowCard(2);
    }

    public void computer3() {
        timeWait(1, 3);
        ShowCard(3);
    }

    //倒计时限制玩家操作
    public void timeWait(int n, int player) {
        //if (gameJFrame.currentList.get(player).size() > 0)
        //   hideCards(gameJFrame.currentList.get(player));
        if (player == 0) {
            int i = n;
            while (gameJFrame.nextPlayer == false && i >= 0) {
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1 && player == 0) {
                ShowCard(0);
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

    //
    public void ShowCard(int playerIndex) {

        // 获取当前玩家手中的麻将牌列表
        ArrayList<MahjongCard> player = gameJFrame.playerList.get(playerIndex);

        if(playerIndex==0) {
            //遍历手上的牌，把要出的牌放到临时集合中
            for (int i = 0; i < player.size(); i++) {
                MahjongCard card = player.get(i);
                if (card.isClicked()) {
                    gameJFrame.currentList.add(card);
                    //在手上的牌中，去掉已经出掉的牌
                    player.remove(card);
                    //计算坐标并移动牌
                    //移动的目的是要出的牌移动到上方
                    Point point = new Point();
                    point.x = (1200 / 2) - 2 * 20 / 2;
                    point.y = 450;
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
            }

            //重新摆放剩余的牌
            Other_Algorithm.order(player);
            Other_Algorithm.rePosition(gameJFrame, player, playerIndex);

            // 展示出的麻将牌
            gameJFrame.currentList.getLast().turnFront();

        }else {
            //遍历手上的牌，把要出的牌放到临时集合中
            MahjongCard card = player.getLast();

            gameJFrame.currentList.add(card);
            //在手上的牌中，去掉已经出掉的牌
            player.remove(card);
            //计算坐标并移动牌
            //移动的目的是要出的牌移动到上方
            Point point = new Point();
            point.x = (1200 / 2) - 2 * 20 / 2;
            point.y = 450;
            Other_Algorithm.move(card, card.getLocation(), point);

            //重新摆放剩余的牌
            Other_Algorithm.order(player);
            Other_Algorithm.rePosition(gameJFrame, player, playerIndex);

            // 展示出的麻将牌
            gameJFrame.currentList.getLast().turnFront();

            System.out.println(gameJFrame.playerList.get(playerIndex).size());
        }
    }


    //检测是否有玩家胜利
    /**
     *
     * 这玩意儿条件有问题，checkhu只是检测手牌和最后一个
     * 到牌堆里的牌能否hu，从而提供一个hu牌按钮，而不能直接
     * 让游戏结束，条件是一个玩家点击胡牌按钮才算结束游戏
     *
     * @return
     */

    public boolean win () {
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

}


