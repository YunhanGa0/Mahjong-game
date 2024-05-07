package Game;


import Objects.MahjongCard;

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

        gameJFrame.turn = gameJFrame.DealerFlag;
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
            gameJFrame.DealerFlag = 1;
        }
        if (i == 0) {
            point.x = 80;
            point.y = 20;
            gameJFrame.DealerFlag = 0;
        }
        if (i == 2) {
            point.x = 700;
            point.y = 20;
            gameJFrame.DealerFlag = 2;
        }
        gameJFrame.Dealer.setLocation(point);
        gameJFrame.Dealer.setVisible(true);
    }

    public void turnOn(boolean flag) {
        gameJFrame.publishCard[0].setVisible(flag);
        gameJFrame.publishCard[1].setVisible(flag);
    }

    public void computer0() {
        timeWait(1, 0);
        //ShowCard(0);

    }

    public void computer1() {
        timeWait(1, 2);
        //ShowCard(2);

    }

    public void computer2() {
        timeWait(1, 2);
        //ShowCard(2);

    }


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

    public void ShowCard(int role) {
        int orders[] = new int[]{4, 3, 2, 1, 5};
        Model model = Common.getModel(gameJFrame.playerList.get(role), orders);
        ArrayList<String> list = new ArrayList<>();
        if (gameJFrame.time[(role + 1) % 3].getText().equals("不要") && gameJFrame.time[(role + 2) % 3].getText().equals("不要")) {
            if (model.a123.size() > 0) {
                list.add(model.a123.get(model.a123.size() - 1));
            } else if (model.a3.size() > 0) {
                if (model.a1.size() > 0) {
                    list.add(model.a1.get(model.a1.size() - 1));
                } else if (model.a2.size() > 0) {
                    list.add(model.a2.get(model.a2.size() - 1));
                }
                list.add(model.a3.get(model.a3.size() - 1));
            } else if (model.a112233.size() > 0) {
                list.add(model.a112233.get(model.a112233.size() - 1));
            } else if (model.a111222.size() > 0) {
                String name[] = model.a111222.get(0).split(",");

                if (name.length / 3 <= model.a1.size()) {
                    list.add(model.a111222.get(model.a111222.size() - 1));
                    for (int i = 0; i < name.length / 3; i++)
                        list.add(model.a1.get(i));
                } else if (name.length / 3 <= model.a2.size()) {
                    list.add(model.a111222.get(model.a111222.size() - 1));
                    for (int i = 0; i < name.length / 3; i++)
                        list.add(model.a2.get(i));
                }

            } else if (model.a2.size() > (model.a111222.size() * 2 + model.a3.size())) {
                list.add(model.a2.get(model.a2.size() - 1));
            } else if (model.a1.size() > (model.a111222.size() * 2 + model.a3.size())) {
                list.add(model.a1.get(model.a1.size() - 1));
            } else if (model.a4.size() > 0) {
                int sizea1 = model.a1.size();
                int sizea2 = model.a2.size();
                if (sizea1 >= 2) {
                    list.add(model.a1.get(sizea1 - 1));
                    list.add(model.a1.get(sizea1 - 2));
                    list.add(model.a4.get(0));

                } else if (sizea2 >= 2) {
                    list.add(model.a2.get(sizea1 - 1));
                    list.add(model.a2.get(sizea1 - 2));
                    list.add(model.a4.get(0));

                } else {
                    list.add(model.a4.get(0));

                }

            }
        } else {

            if (role != gameJFrame.DealerFlag) {
                int f = 0;
                if (gameJFrame.time[gameJFrame.DealerFlag].getText().equals("不要")) {
                    f = 1;
                }
                if ((role + 1) % 3 == gameJFrame.DealerFlag) {
                    if ((Common.jugdeType(gameJFrame.currentList.get((role + 2) % 3)) != PokerType.c1
                            || Common.jugdeType(gameJFrame.currentList.get((role + 2) % 3)) != PokerType.c2)
                            && gameJFrame.currentList.get(gameJFrame.DealerFlag).size() < 1)
                        f = 1;
                    if (gameJFrame.currentList.get((role + 2) % 3).size() > 0
                            && Common.getValue(gameJFrame.currentList.get((role + 2) % 3).get(0)) > 13)
                        f = 1;
                }
                if (f == 1) {
                    gameJFrame.time[role].setVisible(true);
                    gameJFrame.time[role].setText("不要");
                    return;
                }
            }

            int can = 0;
            if (role == gameJFrame.DealerFlag) {
                if (gameJFrame.playerList.get((role + 1) % 3).size() <= 5 || gameJFrame.playerList.get((role + 2) % 3).size() <= 5)
                    can = 1;
            } else {
                if (gameJFrame.playerList.get(gameJFrame.DealerFlag).size() <= 5)
                    can = 1;
            }

            ArrayList<MahjongCard> player;
            if (gameJFrame.time[(role + 2) % 3].getText().equals("不要"))
                player = gameJFrame.currentList.get((role + 1) % 3);
            else
                player = gameJFrame.currentList.get((role + 2) % 3);


            gameJFrame.currentList.get(role).clear();
            if (list.size() > 0) {
                Point point = new Point();
                if (role == 0)
                    point.x = 200;
                if (role == 2)
                    point.x = 550;
                if (role == 1) {
                    point.x = (770 / 2) - (gameJFrame.currentList.get(1).size() + 1) * 15 / 2;
                    point.y = 300;
                }
                point.y = (400 / 2) - (list.size() + 1) * 15 / 2;
                ArrayList<MahjongCard> temp = new ArrayList<>();
                for (int i = 0, len = list.size(); i < len; i++) {
                    List<MahjongCard> pokers = getCardByName(gameJFrame.playerList.get(role), list.get(i));
                    for (MahjongCard poker : pokers) {
                        temp.add(poker);
                    }
                }
                temp = Common.getOrder2(temp);
                for (MahjongCard poker : temp) {
                    Common.move(poker, poker.getLocation(), point);
                    point.y += 15;
                    gameJFrame.container.setComponentZOrder(poker, 0);
                    gameJFrame.currentList.get(role).add(poker);
                    gameJFrame.playerList.get(role).remove(poker);
                }
                Common.rePosition(gameJFrame, gameJFrame.playerList.get(role), role);
            } else {
                gameJFrame.time[role].setVisible(true);
                gameJFrame.time[role].setText("不要");
            }
            for (MahjongCard poker : gameJFrame.currentList.get(role))
                poker.turnFront();
        }
    }

    public boolean win () {
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


