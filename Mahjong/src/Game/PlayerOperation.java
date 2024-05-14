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
    @Override
    public void run() {

        gameJFrame.turn = gameJFrame.DealerFlag;
        while (true) {
            //爷的回合
            if (gameJFrame.turn == 0) {
                //如果他们仨没有吃碰杠打断顺序那我就能摸牌出牌
                if (gameJFrame.CheckBreak() && gameJFrame.CheckBreak() &&gameJFrame.CheckBreak() )
                    gameJFrame.chulord[0].setEnabled(false);
                else {
                    GameJFrame.addcards(0);
                    gameJFrame.chulord[0].setEnabled(true);
                }
                turnOn(true);
                timeWait(30, 1);
                turnOn(false);
                //下一个人的回合
                gameJFrame.turn = (gameJFrame.turn + 1) % 4;
                if (win())
                    break;
            }
            //电脑1的回合
            if (gameJFrame.turn == 1) {
                gameJFrame.addcards(1);
                computer1();
                gameJFrame.turn = (gameJFrame.turn + 1) % 4;
                if (win())
                    break;
            }
            //电脑2的回合
            if (gameJFrame.turn == 2) {
                gameJFrame.addcards(2);
                computer2();
                gameJFrame.turn = (gameJFrame.turn + 1) % 4;
                if (win())
                    break;
            }
            //电脑3的回合
            if (gameJFrame.turn == 3) {
                gameJFrame.addcards(3);
                computer3();
                gameJFrame.turn = (gameJFrame.turn + 1) % 4;
                if (win())
                    break;
            }
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

    public void turnOn(boolean flag) {
        gameJFrame.chulord[0].setVisible(flag);
        gameJFrame.chulord[1].setVisible(flag);
    }

    //Three computer player
    public void computer1() {
        timeWait(1, 2);
        ShowCard(1);
    }

    public void computer2() {
        timeWait(1, 3);
        ShowCard(2);
    }

    public void computer3() {
        timeWait(1, 4);
        ShowCard(3);
    }

    //倒计时限制玩家操作
    public void timeWait(int n, int player) {
        if (gameJFrame.currentList.get(player).size() > 0)
            hideCards(gameJFrame.currentList.get(player));
        if (player == 0) {
            int i = n;
            while (gameJFrame.nextPlayer == false && i >= 0) {
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1 && player == 0) {
                ShowCard(1);
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
        ArrayList<MahjongCard> player=gameJFrame.getplayer(playerIndex);

        // 获取当前出牌玩家手中的麻将牌列表
        ArrayList<MahjongCard> c = new ArrayList<>();

        // 将选择的麻将牌从玩家手牌中移除，并更新玩家的手牌显示位置
        for (int i = 0; i < player.size(); i++) {
            MahjongCard card = player.get(i);
            if (card.isClicked()) {
                c.add(card);
            }

            //把当前要出的牌，放到大集合中统一管理
            gameJFrame.getcurrentList().set(1, c);
            //在手上的牌中，去掉已经出掉的牌
            player.removeAll(c);

            //计算坐标并移动牌
            //移动的目的是要出的牌移动到上方
            Point point = new Point();
            point.x = (770 / 2) - (c.size() + 1) * 15 / 2;
            point.y = 300;
            for (int j = 0, len = c.size(); j < len; j++) {
                MahjongCard poker = c.get(i);
                Other_Algorithm.move(poker, poker.getLocation(), point);
                point.x += 30;
            }

            //重新摆放剩余的牌
            Other_Algorithm.rePosition(gameJFrame, player, 1);
        }

        // 展示出的麻将牌
        for (MahjongCard cards : gameJFrame.currentList.get(playerIndex)){
            cards.turnFront();
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


