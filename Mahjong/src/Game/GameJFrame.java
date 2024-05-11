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

    //管理胡牌和杠牌两个按钮
    JButton landlord[] = new JButton[2];

    //管理吃牌和碰牌两个按钮
    JButton publishCard[] = new JButton[2];

    int DealerFlag;
    int turn;

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
    ArrayList<ArrayList<MahjongCard>> currentList = new ArrayList<>();

    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家的牌
    ArrayList<ArrayList<MahjongCard>> playerList = new ArrayList<>();


    //牌盒，装所有的牌
    ArrayList<MahjongCard> MahjongCardList;

    //三个玩家前方的文本提示
    //索引0：中间的自己
    //索引1：右边的电脑玩家
    //索引2：对面的电脑玩家
    //索引3：左边的电脑玩家
    JTextField time[] = new JTextField[4];

    //用户操作，涉及多线程
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

        //初始化牌
        //准备牌，洗牌，发牌
        new Thread(this::initCard).start();

        //打牌之前的准备工作
        //展示抢地主和不抢地主两个按钮并且再创建三个集合用来装三个玩家准备要出的牌
        initGame();
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
                        card.setLocation(350, 150);
                        card.setCardImage("Mahjong/MahjongPic/tile"+i+j+".png"); // 设置牌的图片
                    }
                }
            } else {
                for (int k = 0; k < 4; k++) {
                    MahjongCard card = new MahjongCard(this, i + "-" + 0, false);
                    MahjongCardList.add(card);
                    container.add(card);
                    card.setLocation(350, 150);
                    card.setCardImage("Mahjong/MahjongPic/tile"+i+0+".png"); // 设置牌的图片
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

        for (int i=0;i<52;i++){
            MahjongCard card= MahjongCardList.get(i);
            if (i%4==0){
                player0.add(card);
            }else if(i%4==1){
                player1.add(card);
            }else if(i%4==2){
                player2.add(card);
                card.turnFront();
            }else {
                player3.add(card);
            }

            //给庄多发一张牌


            //把四个装着牌的小集合放到大集合中方便管理
            playerList.add(player0);
            playerList.add(player1);
            playerList.add(player2);
            playerList.add(player3);

            //把当前的牌至于最顶端，这样就会有牌依次错开且叠起来的效果
            container.setComponentZOrder(card, 0);

        }

        //给牌排序
        for (int i = 0; i < 3; i++) {
            //排序
            Other_Algorithm.order(playerList.get(i));
            //重新摆放顺序
            Other_Algorithm.rePosition(this, playerList.get(i), i);
        }

    }

    private void loadMahjongCards() {

    }

    //打牌之前的准备工作
    private void initGame() {
        //创建四个集合用来装四个玩家准备要出的牌
        for (int i = 0; i < 4; i++) {
            ArrayList<MahjongCard> list = new ArrayList<>();
            //添加到大集合中方便管理
            currentList.add(list);
        }

        //展示roll骰子按钮
        //

        //展示自己前面的倒计时文本
        time[1].setVisible(true);
        //倒计时10秒
        po = new PlayerOperation(this, 30);
        //开启倒计时
        po.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //点击Roll骰子

        //点击出牌

        //创建一个临时的集合，用来存放当前要出的牌
        ArrayList<MahjongCard> c = new ArrayList<>();
        //获取中自己手上所有的牌
        ArrayList<MahjongCard> player2 = playerList.get(0);

        //遍历手上的牌，把要出的牌都放到临时集合中
        for (int i = 0; i < player2.size(); i++) {
            MahjongCard card = player2.get(i);
            if (card.isClicked()) {
                c.add(card);
            }

            int flag = 0;
            //判断，如果电脑玩家是否可以吃/碰/杠/胡
            if (time[0].getText().equals("不要") && time[2].getText().equals("不要")) {
                if (Other_Algorithm.CheckPeng()){
                    flag = 1;
                }
            } else {

            }

            //把当前要出的牌，放到大集合中统一管理

            //在手上的牌中，去掉已经出掉的牌


            //计算坐标并移动牌
            //移动的目的是要出的牌移动到上方

            //重新摆放剩余的牌

            //隐藏文本提示

            //下一个玩家可玩
        }

    }

    //添加组件
    public void initView() {
        //创建roll骰子的按钮
        JButton robBut = new JButton("roll骰子");
        //设置位置
        robBut.setBounds(320, 400, 75, 20);
        //添加点击事件
        robBut.addActionListener(this);
        //设置隐藏
        robBut.setVisible(false);
        //添加到数组中统一管理
        landlord[0] = robBut;
        //添加到界面中
        container.add(robBut);

        //创建不抢的按钮
        JButton noBut = new JButton("不     抢");
        //设置位置
        noBut.setBounds(420, 400, 75, 20);
        //添加点击事件
        noBut.addActionListener(this);
        //设置隐藏
        noBut.setVisible(false);
        //添加到数组中统一管理
        landlord[1] = noBut;
        //添加到界面中
        container.add(noBut);

        //创建出牌的按钮
        JButton outCardBut = new JButton("出牌");
        outCardBut.setBounds(320, 400, 60, 20);
        outCardBut.addActionListener(this);
        outCardBut.setVisible(false);
        publishCard[0] = outCardBut;
        container.add(outCardBut);

        //创建不要的按钮
        JButton noCardBut = new JButton("不要");
        noCardBut.setBounds(420, 400, 60, 20);
        noCardBut.addActionListener(this);
        noCardBut.setVisible(false);
        publishCard[1] = noCardBut;
        container.add(noCardBut);


        //创建三个玩家前方的提示文字：倒计时
        //每个玩家一个
        //左边的电脑玩家是0
        //中间的自己是1
        //右边的电脑玩家是2
        //对面是3
        for (int i = 0; i < 4; i++) {
            time[i] = new JTextField("倒计时:");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(140, 230, 60, 20);
        time[1].setBounds(374, 360, 60, 20);
        time[2].setBounds(620, 230, 60, 20);
        time[3].setBounds(374, 230, 60, 20);


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
        this.setSize(830, 620);
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
        container.setBackground(Color.LIGHT_GRAY);
    }

}
