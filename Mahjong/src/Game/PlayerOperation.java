package Game;


import Game.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void run() {

        gameJFrame.turn = gameJFrame.dizhuFlag;
        while (true) {

            if (gameJFrame.turn == 1) {

                if (gameJFrame.time[0].getText().equals("不要") && gameJFrame.time[2].getText().equals("不要"))
                    gameJFrame.publishCard[1].setEnabled(false);
                else {
                    gameJFrame.publishCard[1].setEnabled(true);
                }
                turnOn(true);
                timeWait(30, 1);
                turnOn(false);
                gameJFrame.turn = (gameJFrame.turn + 1) % 3;
                if (win())
                    break;
            }
            if (gameJFrame.turn == 0) {
                computer0();
                gameJFrame.turn = (gameJFrame.turn + 1) % 3;
                if (win())
                    break;
            }
            if (gameJFrame.turn == 2) {
                computer2();
                gameJFrame.turn = (gameJFrame.turn + 1) % 3;
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
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setlord(int i) {
        Point point = new Point();
        if (i == 1) {
            point.x = 80;
            point.y = 430;
            gameJFrame.dizhuFlag = 1;
        }
        if (i == 0) {
            point.x = 80;
            point.y = 20;
            gameJFrame.dizhuFlag = 0;
        }
        if (i == 2) {
            point.x = 700;
            point.y = 20;
            gameJFrame.dizhuFlag = 2;
        }
        gameJFrame.dizhu.setLocation(point);
        gameJFrame.dizhu.setVisible(true);
    }

    public void turnOn(boolean flag) {
        gameJFrame.publishCard[0].setVisible(flag);
        gameJFrame.publishCard[1].setVisible(flag);
    }

    public void computer0() {
        timeWait(1, 0);
        ShowCard(0);

    }

    public void computer2() {
        timeWait(1, 2);
        ShowCard(2);

    }



    public List getCardByName(List<Poker> list, String n) {
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

    public void AI(List<String> model, List<Poker> player, List<String> list, int role) {

    }

    public void timeWait(int n, int player) {

        if (gameJFrame.currentList.get(player).size() > 0)
            Common.hideCards(gameJFrame.currentList.get(player));
        if (player == 1) {
            int i = n;

            while (gameJFrame.nextPlayer == false && i >= 0) {
                gameJFrame.time[player].setText("倒计时:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1 && player == 1) {

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

    public boolean win() {
        for (int i = 0; i < 3; i++) {
            if (gameJFrame.playerList.get(i).size() == 0) {
                String s;
                if (i == 1) {
                    s = "恭喜你，胜利了!";
                } else {
                    s = "恭喜电脑" + i + ",赢了! 你的智商有待提高哦";
                }
                for (int j = 0; j < gameJFrame.playerList.get((i + 1) % 3).size(); j++)
                    gameJFrame.playerList.get((i + 1) % 3).get(j).turnFront();
                for (int j = 0; j < gameJFrame.playerList.get((i + 2) % 3).size(); j++)
                    gameJFrame.playerList.get((i + 2) % 3).get(j).turnFront();
                JOptionPane.showMessageDialog(gameJFrame, s);
                return true;
            }
        }
        return false;
    }
}
