package Game;

import Algorithm.Other_Algorithm;
import Objects.MahjongCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameJFrame extends JFrame implements ActionListener {

    //获取界面中的隐藏容器
    //现在统一获取了，后面直接用就可以了
    public Container container = null;

    //管理出牌一个按钮
    JButton chulord[] = new JButton[1];

    //管理胡牌一个按钮
    JButton hulord[] = new JButton[1];

    //管理吃牌，碰牌，杠牌三个按钮（顺序是0碰，1吃，2杠）
    JButton Other[] = new JButton[3];

    //庄家图标
    int DealerFlag;

    //表示出牌轮次
    int turn;

    //记录出牌次数
    int num1;

    //游戏界面中庄家的图标
    JLabel Dealer;

    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家当前要出的牌
    //索引0：中间的自己
    //索引1：右边的电脑玩家
    //索引2：对面的电脑玩家
    //索引3：左边的电脑玩家
    //3：对面的
    public static ArrayList<MahjongCard> currentList = new ArrayList<>();

    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家的牌
    public static ArrayList<ArrayList<MahjongCard>> playerList = new ArrayList<>();

    //牌盒，装所有的牌
    public static ArrayList<MahjongCard> MahjongCardList= new ArrayList<>();

    //记录发牌数
    public static int numb;

    //三个玩家前方的文本提示
    //索引0：中间的自己
    //索引1：右边的电脑玩家
    //索引2：对面的电脑玩家
    //索引3：左边的电脑玩家
    JTextField time[] = new JTextField[4];

    //多线程操控游戏流程
    PlayerOperation po;

    //下一个玩家可以出牌的状态
    boolean nextPlayer = false;

    public GameJFrame() {
        //设置图标
        setIconImage(Toolkit.getDefaultToolkit().getImage(""));

        //设置界面
        initJFrame();

        //添加组件
        initView();

        //界面显示出来
        //先展示界面再发牌，因为发牌里面有动画，界面不展示出来，动画无法展示
        this.setVisible(true);

        rollDice();

        //初始化牌
        //准备牌，洗牌，发牌
        new Thread(this::initCard).start();

        //打牌之前的准备工作
        //展示抢地主和不抢地主两个按钮并且再创建三个集合用来装三个玩家准备要出的牌
        initGame();
    }

    private void rollDice() {
        /*
        Random random = new Random();

        // 每个玩家掷骰子
        int[] diceResults = new int[4]; // 假设有四个玩家
        for (int j = 0; j < diceResults.length; j++) {
            diceResults[j] = random.nextInt(12) + 1;;
        }

        //图形化骰子到窗口
        for (int j = 0; j < diceResults.length; j++) {
            System.out.println(diceResults[j]);
        }

        // 确定骰子点数最大的玩家，设为庄家
        int maxDice = 0;
        int dealerIndex = 0;  // 庄家索引
        for (int j = 0; j < diceResults.length; j++) {
            if (diceResults[j] > maxDice) {
                maxDice = diceResults[j];
                dealerIndex = j;
            }
        }

         */
        //DealerFlag=dealerIndex;
        DealerFlag=0;
    }


    //初始化牌
    //准备牌，洗牌，发牌
    public void initCard() {

        for (int i = 1; i <= 10; i++) {
            if (i <= 3) {
                for (int j = 1; j <= 9; j++) {
                    for (int k = 0; k < 4; k++) {
                        MahjongCard card = new MahjongCard(this, i + "-" + j, false);
                        MahjongCardList.add(card);
                        container.add(card);
                        card.setCardImage("Mahjong/MahjongPic/tile"+i+j+".png"); // 设置牌的图片
                        card.setLocation(455, 315);
                    }
                }
            } else {
                for (int k = 0; k < 4; k++) {
                    MahjongCard card = new MahjongCard(this, i + "-" + 0, false);
                    MahjongCardList.add(card);
                    container.add(card);
                    card.setCardImage("Mahjong/MahjongPic/tile"+i+0+".png"); // 设置牌的图片
                    card.setLocation(455, 315);
                }
            }
        }

        //洗牌
        Collections.shuffle(MahjongCardList);

        //创建四个集合用来装四个玩家的牌，并把四个小集合放到大集合中方便管理
        ArrayList<MahjongCard> player0 = new ArrayList<>();
        ArrayList<MahjongCard> player1 = new ArrayList<>();
        ArrayList<MahjongCard> player2 = new ArrayList<>();
        ArrayList<MahjongCard> player3 = new ArrayList<>();



        //发牌
        for (int i=1;i<53;i++) {
            MahjongCard card = MahjongCardList.get(i);
            if (i % 4 == 0) {
                player0.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(180 + i * 7, 600));
                card.turnFront();
                numb++;
            } else if (i % 4 == 1) {
                player1.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(700, 60 + i * 5));
                numb++;
            } else if (i % 4 == 2) {
                player2.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(270 + (75 * i), 10));
                numb++;
            } else if (i % 4 == 3){
                player3.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(50, 60 + i * 5));
                numb++;
            }

            //把四个装着牌的小集合放到大集合中方便管理
            playerList.add(player0);
            playerList.add(player1);
            playerList.add(player2);
            playerList.add(player3);

            //把当前的牌至于最顶端，这样就会有牌依次错开且叠起来的效果
            container.setComponentZOrder(card, 0);
        }

        /*
        //先给庄家整一张
        if(DealerFlag==0){
            player0.add(MahjongCardList.get(numb));
        } else if (DealerFlag==1) {
            player1.add(MahjongCardList.get(numb));
        } else if (DealerFlag==2) {
            player2.add(MahjongCardList.get(numb));
        }else if (DealerFlag==3) {
            player3.add(MahjongCardList.get(numb));
        }
        numb++;

         */

        //给牌排序
        for (int i = 0; i <=3; i++) {
            //排序
            Other_Algorithm.order(playerList.get(i));
            //重新摆放顺序
            Other_Algorithm.rePosition(this, playerList.get(i), i);
            /*
            if (i!=0){
                for(int j=0;j<playerList.get(i).size();j++)
                playerList.get(i).get(j).turnRear();
            }

             */
        }

    }

    //打牌之前的准备工作
    private void initGame() {
        //展示自己前面的倒计时文本
        time[0].setVisible(true);
        //倒计时10秒
        po = new PlayerOperation(this, 10);
        //开启倒计时
        po.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == null) {
            //roll骰子决定庄家
            time[1].setText("是庄");
            po.isRun = true;
        } else if (e.getSource() == Other[0]) {
            //点击碰,进行碰的操作

        } else if (e.getSource() == Other[1]) {
            //点击吃，进行吃的操作

        } else if (e.getSource() == Other[2]) {
            //点击杠，进行杠的操作

        } else if (e.getSource() == hulord[0]) {
            //点击胡，进行胡的操作

        }else if (e.getSource() == chulord[0]) {
            //点击出牌

            //获取中自己手上所有的牌
            ArrayList<MahjongCard> player = playerList.get(0);

            //遍历手上的牌，把要出的牌放到临时集合中
            for (int i = 0; i < player.size(); i++) {
                MahjongCard card = player.get(i);
                if (card.isClicked()) {
                    currentList.add(card);
                    //在手上的牌中，去掉已经出掉的牌
                    player.remove(card);
                    //计算坐标并移动牌
                    //移动的目的是要出的牌移动到上方
                    Point point = new Point();
                    point.x = (960 / 2) - (num1 + 1) * 30 / 2;
                    point.y = 500;
                    num1++;
                    Other_Algorithm.move(card, card.getLocation(), point);
                }


                //重新摆放剩余的牌
                Other_Algorithm.order(player);
                Other_Algorithm.rePosition(this, player, 0);
                //隐藏文本提示
                time[0].setVisible(false);
                //下一个玩家可玩
                this.nextPlayer=true;

            }
        }
    }

    //添加组件(前端)
    public void initView() {

        //创建出牌的按钮
        JButton outCardBut = new JButton("出牌");
        outCardBut.setBounds(500, 550, 60, 20);
        outCardBut.addActionListener(this);
        outCardBut.setVisible(true);
        chulord[0] = outCardBut;
        container.add(outCardBut);

        //创建胡的按钮
        JButton huCardBut = new JButton("胡");
        huCardBut.setBounds(420, 400, 60, 20);
        huCardBut.addActionListener(this);
        huCardBut.setVisible(false);
        hulord[0] = huCardBut;
        container.add(huCardBut);

        //创建碰的按钮
        JButton pengCardBut = new JButton("碰");
        pengCardBut.setBounds(320, 400, 60, 20);
        pengCardBut.addActionListener(this);
        pengCardBut.setVisible(false);
        Other[0] = pengCardBut;
        container.add(pengCardBut);


        //创建吃的按钮
        JButton chiCardBut = new JButton("吃");
        chiCardBut.setBounds(320, 400, 60, 20);
        chiCardBut.addActionListener(this);
        chiCardBut.setVisible(false);
        Other[1] = chiCardBut;
        container.add(chiCardBut);

        //创建杠的按钮
        JButton gangCardBut = new JButton("出牌");
        gangCardBut.setBounds(320, 400, 60, 20);
        gangCardBut.addActionListener(this);
        gangCardBut.setVisible(false);
        Other[2] = gangCardBut;
        container.add(gangCardBut);

        //创建三个玩家前方的提示文字：倒计时
        //每个玩家一个
        //中间的自己是0
        //右边的电脑玩家是1
        //对面是2
        //左边的电脑玩家是3
        for (int i = 0; i < 4; i++) {
            time[i] = new JTextField("倒计时:");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(480, 570, 90, 20);
        time[1].setBounds(800, 350, 90, 20);
        time[2].setBounds(480, 200, 90, 20);
        time[3].setBounds(160, 350, 90, 20);


        //创建庄家图标
        Dealer = new JLabel(new ImageIcon("doudizhu\\image\\dizhu.png"));
        Dealer.setVisible(false);
        Dealer.setSize(40, 40);
        container.add(Dealer);

    }

    //设置界面
    public void initJFrame() {
        //设置标题
        this.setTitle("Mahjong Game");
        //设置大小
        this.setSize(960, 720);
        //设置关闭模式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口无法进行调节
        this.setResizable(false);
        //界面居中
        this.setLocationRelativeTo(null);
        //获取界面中的隐藏容器，以后直接用无需再次调用方法获取了
        container = this.getContentPane();
        //取消内部默认的居中放置
        container.setLayout(null);
        //设置背景颜色
        container.setBackground(Color.BLACK);
    }

    public ArrayList<ArrayList<MahjongCard>> getPlayerList(){
        return playerList;
    }

}
