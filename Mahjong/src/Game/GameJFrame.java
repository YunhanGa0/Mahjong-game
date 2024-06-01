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
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameJFrame extends JFrame implements ActionListener {

    //获取界面中的隐藏容器
    public Container container = null;

    //管理出牌一个按钮
    JButton[] Chu = new JButton[1];

    //管理胡牌一个按钮
    JButton[] Hu = new JButton[1];

    //管理吃牌，碰牌，杠牌三个按钮（顺序是0碰，1吃，2杠，3暗杠）
    JButton[] Other = new JButton[4];

    //三个玩家前方的倒计时文本提示
    //索引0：中间的自己
    //索引1：右边的电脑玩家
    //索引2：对面的电脑玩家
    //索引3：左边的电脑玩家
    JTextField[] time = new JTextField[4];

    //庄家图标
    int DealerFlag;

    //表示出牌轮次
    int turn;

    //记录出牌次数
    public int num0;
    public int num1;
    public int num2;
    public int num3;

    //记录chi peng gang发生次数
    private static int cpgCount = 0;


    //下一个玩家可以出牌的状态
    boolean nextPlayer = false;

    //玩家是否进行操作的状态
    boolean conti=false;

    //记录发牌数
    public int numb;

    //装着每一个玩家当前要出的牌
    public ArrayList<MahjongCard> currentList = new ArrayList<>();

    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家的牌
    public ArrayList<ArrayList<MahjongCard>> playerList = new ArrayList<>();

    //牌盒，装所有的牌
    public ArrayList<MahjongCard> MahjongCardList= new ArrayList<>();

    //游戏界面中庄家的图标
    JLabel Dealer;

    //多线程操控游戏流程
    PlayerOperation po;

    public GameJFrame() {
        //设置图标
        setIconImage(Toolkit.getDefaultToolkit().getImage(""));

        // BGM
        new Thread(() -> playBackgroundMusic(getClass().getResource("/MahjongPic/BGM.wav").getPath())).start();

        //设置界面
        initJFrame();

        //添加组件
        initView();

        //初始化庄家图标
        initDealerFlag();

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

//Roll骰子并显示大小确定庄家
private void rollDice() {
    Random random = new Random();

    while (true) {
        // 每个玩家掷两个骰子
        int[][] diceResults = new int[4][2]; // 假设有四个玩家，每个玩家两个骰子
        int[] totalResults = new int[4]; // 每个玩家的总点数
        for (int j = 0; j < diceResults.length; j++) {
            diceResults[j][0] = random.nextInt(6) + 1;
            diceResults[j][1] = random.nextInt(6) + 1;
            totalResults[j] = diceResults[j][0] + diceResults[j][1];
        }

        // 图形化骰子到窗口
        for (int j = 0; j < diceResults.length; j++) {
            System.out.println("Player " + (j + 1) + " dice results: " + diceResults[j][0] + ", " + diceResults[j][1]);
            // 在窗口中显示骰子图像
            showDiceImages(j, diceResults[j][0], diceResults[j][1]);
        }

        // 确定骰子点数最大的玩家，设为庄家
        int maxDice = 0;
        int dealerIndex = 0;  // 庄家索引
        boolean tie = false;
        for (int j = 0; j < totalResults.length; j++) {
            if (totalResults[j] > maxDice) {
                maxDice = totalResults[j];
                dealerIndex = j;
                tie = false;
            } else if (totalResults[j] == maxDice) {
                tie = true; // 如果有平局，则标记为平局
            }
        }

        if (!tie) { // 如果没有平局，确定庄家
            // 设置为庄家的回合
            DealerFlag = dealerIndex;
            setFlag(dealerIndex); // 调用setFlag方法显示庄家图标
            break;
        } else {
            System.out.println("Tie detected. Re-rolling the dice.");
        }

    }
}

    private void showDiceImages(int playerIndex, int dice1, int dice2) {
        // 根据玩家索引和骰子点数在窗口中显示骰子图像
        String diceImagePath1 = "C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\Dice" + dice1 + ".png";
        String diceImagePath2 = "C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\Dice" + dice2 + ".png";

        JLabel diceLabel1 = new JLabel(new ImageIcon(diceImagePath1));
        JLabel diceLabel2 = new JLabel(new ImageIcon(diceImagePath2));
        diceLabel1.setSize(55, 55);
        diceLabel2.setSize(55, 55);

        // 给每个标签加一个名字
        diceLabel1.setName("DiceLabel1");
        diceLabel2.setName("DiceLabel2");

        // 设置骰子图像位置
        Point point1 = new Point();
        Point point2 = new Point();
        switch (playerIndex) {
            case 0 -> {
                point1.setLocation(600, 700);
                point2.setLocation(680, 700);
            }
            case 1 -> {
                point1.setLocation(950, 450);
                point2.setLocation(1030, 450);
            }
            case 2 -> {
                point1.setLocation(600, 300);
                point2.setLocation(680, 300);
            }
            case 3 -> {
                point1.setLocation(220, 450);
                point2.setLocation(300, 450);
            }
        }

        diceLabel1.setLocation(point1);
        diceLabel2.setLocation(point2);

        container.add(diceLabel1);
        container.add(diceLabel2);
        container.setComponentZOrder(diceLabel1, 0);
        container.setComponentZOrder(diceLabel2, 0);
    }

    public void hideDice() {
        // 遍历所有组件，找到所有的JLabel并隐藏
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel label) {
                if (label.getIcon() != null && label.getIcon().toString().contains("Dice")) {
                    label.setVisible(false);
                }
            }
        }
    }

    public MahjongCard getLai(){
        //牌底摸一张赖子牌
        MahjongCard Lai=MahjongCardList.get(135);
        //翻到正面
        Lai.turnFront();
        //放到牌桌中央
        Lai.setLocation(650, 450);
        Lai.setVisible(true);
        System.out.println(Lai.getName());
        return Lai;
    }

    //初始化牌
    //准备牌，洗牌，发牌
    public void initCard() {
        //添加牌到牌盒中
        for (int i = 0; i <= 9; i++) {
            if (i <= 2) { //添加万，条，筒
                for (int j = 1; j <= 9; j++) {
                    for (int k = 0; k < 4; k++) {
                        MahjongCard card = new MahjongCard(this, i + "-" + j, false);
                        MahjongCardList.add(card);
                        container.add(card);
                        //card.setCardImage("Mahjong/MahjongPic/tile"+i+j+".png"); // 设置牌的图片
                        card.setVisible(false);
                        card.setLocation(650, 450);
                    }
                }
            } else { //添加风牌
                for (int k = 0; k < 4; k++) {
                    MahjongCard card = new MahjongCard(this, i + "-" + 0, false);
                    MahjongCardList.add(card);
                    container.add(card);
                    //card.setCardImage("Mahjong/MahjongPic/tile"+i+0+".png"); // 设置牌的图片
                    card.setVisible(false);
                    card.setLocation(650, 450);//455,315
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
        for (int i=0;i<52;i++) {
            MahjongCard card = MahjongCardList.get(i);
            if (i % 4 == 0) {
                player0.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(180 + i * 7, 600));
                card.setVisible(true);
                card.turnFront();
                numb++;
            } else if (i % 4 == 1) {
                player1.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(700, 60 + i * 5));
                card.setVisible(true);
                //card.turnFront();
                numb++;
            } else if (i % 4 == 2) {
                player2.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(270 + (75 * i), 10));
                card.setVisible(true);
                //card.turnFront();
                numb++;
            } else {
                player3.add(card);
                //Other_Algorithm.move(card,card.getLocation(),new Point(50, 60 + i * 5));
                card.setVisible(true);
                //card.turnFront();
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

        for (int i = 0; i <=3; i++) {
            //给牌排序
            Other_Algorithm.order(playerList.get(i));
            //重新摆放顺序
            Other_Algorithm.rePosition(this, playerList.get(i), i);
        }

        //展示赖子牌
        getLai();

    }

    //打牌之前的准备工作
    private void initGame() {
        //展示自己前面的倒计时文本
        time[0].setVisible(true);
        //倒计时10秒
        po = new PlayerOperation(this, 5);
        //开启倒计时
        po.start();

    }

    //庄家旗帜位置
    public void setFlag(int i) {
        Point point = new Point();
        switch (i) {
            case 0 -> {
                point.x = 160;
                point.y = 825;
                DealerFlag = 0;
            }
            case 1 -> {
                point.x = 1200;
                point.y = 730;
                DealerFlag = 1;
            }
            case 2 -> {
                point.x = 900;
                point.y = 120;
                DealerFlag = 2;
            }
            case 3 -> {
                point.x = 70;
                point.y = 160;
                DealerFlag = 3;
            }
        }
        Dealer.setLocation(point);
        Dealer.setVisible(true);
    }


    //在初始化方法中设置庄家图标
    private void initDealerFlag() {
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\flag.png");
        Image originalImage = originalIcon.getImage();
        // 设置缩放后的大小
        int width = 40;
        int height = 40;
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        Dealer = new JLabel(scaledIcon);
        Dealer.setVisible(false);
        Dealer.setSize(width, height);
        container.add(Dealer);
    }


    //点击按钮进行的事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Other[0]) { //点击碰,进行碰的操作
            //通过弃牌堆找到要碰的牌
            MahjongCard pengCard = currentList.get(currentList.size()-1);
            //获取中自己手上所有的牌
            ArrayList<MahjongCard> player = playerList.get(0);
            //先将牌加入到自己的牌中
            player.add(pengCard);
            //计数
            int j=0;
            //遍历玩家手牌找到此牌并放到指定位置
            for (MahjongCard card : player) {
                if (card.getName().equals(pengCard.getName())) {
                    Point point = new Point();
                    point.x = 220 + j * 35;    //200
                    point.y = 820 -(cpgCount*50);             //600
                    Other_Algorithm.move(card, card.getLocation(), point);
                    //碰过的牌不能动了
                    card.setCanClick(false);
                    //碰后牌不参与出牌操作
                    card.setIfPeng(true);
                    j++;
                }
            }
            cpgCount++;
            //操作完成
            this.conti=true;
            //更新是否进行操作的状态
            PlayerOperation.setAction(true);
        } else if (e.getSource() == Other[1]) { //点击吃，进行吃的操作
            //通过弃牌堆找到要吃的牌
            MahjongCard chiCard = currentList.get(currentList.size()-1);
            int color = getColor(chiCard);
            //获取中自己手上所有的牌
            ArrayList<MahjongCard> player = playerList.get(0);
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
                            point.x = 220 + j * 35;    //200
                            point.y = 820-(cpgCount*50);             //600
                            Other_Algorithm.move(card, card.getLocation(), point);
                            //碰过的牌不能动了
                            card.setCanClick(false);
                            card.setIfEat(true);
                            j++;
                        }
                    } else if (getColor(card) == getColor(chiCard) && !bigger) { // 牌堆里没有比chiCard大一张的→找cc-1和cc-2移动
                        if (getSize(card) == (getSize(chiCard)-1) ||  getSize(card) == (getSize(chiCard)-2)) {
                            Point point = new Point();
                            point.x = 220 + j * 35;    //200
                            point.y = 820-(cpgCount*50);             //600
                            Other_Algorithm.move(card, card.getLocation(), point);
                            //碰过的牌不能动了
                            card.setCanClick(false);
                            card.setIfEat(true);
                            j++;
                        }
                    }
                }
            }
            cpgCount++;
            this.conti=true;
            PlayerOperation.setAction(true);
        } else if (e.getSource() == Other[2]) { //点击杠，进行杠的操作
            //通过弃牌堆找到要杠的牌
            MahjongCard gangCard = currentList.get(currentList.size()-1);
            //获取中自己手上所有的牌
            ArrayList<MahjongCard> player = playerList.get(0);
            if(Other_Algorithm.CheckGang(player,gangCard)) {
                //先将牌加入到自己的牌中
                player.add(gangCard);
                //计数
                int j = 0;
                //遍历玩家手牌找到此牌并放到指定位置
                for (MahjongCard card : player) {
                    if (card.getName().equals(gangCard.getName())) {
                        Point point = new Point();
                        point.x = 220 + j * 35;    //200
                        point.y = 820-(cpgCount*50);             //550
                        Other_Algorithm.move(card, card.getLocation(), point);
                        //杠过的牌不能动了
                        card.setCanClick(false);
                        card.setIfGang(true);
                        j++;
                    }
                }
                cpgCount++;
                this.conti=true;
                PlayerOperation.setAction(true);
            }
        } else if(e.getSource() == Other[3]){
            //获取中自己手上所有的牌
            ArrayList<MahjongCard> player = playerList.get(0);
            //刚摸到的牌是杠牌
            MahjongCard gangCard = player.get(player.size()-1);
            if(Other_Algorithm.CheckGang(player,gangCard)) {
                //计数
                int j = 0;
                //遍历玩家手牌找到此牌并放到指定位置
                for (MahjongCard card : player) {
                    if (card.getName().equals(gangCard.getName())) {
                        Point point = new Point();
                        point.x = 220 + j * 35;    //200
                        point.y = 820-(cpgCount*50);             //550
                        Other_Algorithm.move(card, card.getLocation(), point);
                        //杠过的牌不能动了
                        card.setCanClick(false);
                        card.setIfGang(true);
                        j++;
                    }
                }
                cpgCount++;
                this.conti=true;
                PlayerOperation.setAction(true);
            }
        }else if (e.getSource() == Hu[0]) { //点击胡，进行胡的操作
            //获取中自己手上所有的牌
            ArrayList<MahjongCard> player = playerList.get(0);
            //获取到胡到的那张牌
            MahjongCard huCard= currentList.get(currentList.size()-1);
            //加入到自己的牌中
            player.add(huCard);
            //重新摆放手牌
            Other_Algorithm.order(player);
            Other_Algorithm.rePosition(this, player, 0);
            //展示胡了的牌
            for(MahjongCard card:player){
                card.turnFront();
            }
            this.conti=true;
            PlayerOperation.setAction(true);
        }else if (e.getSource() == Chu[0]) { //点击出牌
            //获取玩家手牌
            ArrayList<MahjongCard> player = playerList.get(0);
            //遍历手上的牌，把要出的牌放到临时集合中
            for (int i = 0; i < player.size(); i++) {
                MahjongCard card = player.get(i);
                if (card.isClicked()) {
                    currentList.add(card);
                    //在手上的牌中，去掉已经出掉的牌
                    player.remove(card);
                    //计算坐标并移动牌到展示区
                    Point point = new Point();
                    if (num0/13==0){
                        point.x = 430 + (num0) * 35;
                        point.y = 690;
                    }
                    else {
                        point.x = 430 + (num0-13) * 35;
                        point.y = 640;
                    }
                    num0++;
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
                //重新摆放剩余的牌
                Other_Algorithm.order(player);
                Other_Algorithm.rePosition(this, player, 0);
                //隐藏倒计时提示
                time[0].setVisible(false);
                //下一个玩家可玩
                this.nextPlayer=true;
            }
        }
    }

    //添加组件(前端)
    public void initView() {  //出牌按钮，调位置

        //创建出牌的按钮
        JButton outCardBut = new JButton("出牌");
        outCardBut.setBounds(640, 750, 60, 20); //500 550
        outCardBut.addActionListener(this);
        outCardBut.setVisible(false);
        Chu[0] = outCardBut;
        container.add(outCardBut);

        //创建胡的按钮
        JButton huCardBut = new JButton("胡");
        huCardBut.setBounds(780, 750, 60, 20);  //320 500
        huCardBut.addActionListener(this);
        huCardBut.setVisible(false);
        Hu[0] = huCardBut;
        container.add(huCardBut);

        //创建碰的按钮
        JButton pengCardBut = new JButton("碰");
        pengCardBut.setBounds(710, 750, 60, 20);  //430 550
        pengCardBut.addActionListener(this);
        pengCardBut.setVisible(false);
        Other[0] = pengCardBut;
        container.add(pengCardBut);

        //创建吃的按钮
        JButton chiCardBut = new JButton("吃");
        chiCardBut.setBounds(570, 750, 60, 20);  //320 400
        chiCardBut.addActionListener(this);
        chiCardBut.setVisible(false);
        Other[1] = chiCardBut;
        container.add(chiCardBut);

        //创建杠的按钮
        JButton gangCardBut = new JButton("杠");
        gangCardBut.setBounds(500, 750, 60, 20); //320 400
        gangCardBut.addActionListener(this);
        gangCardBut.setVisible(false);
        Other[2] = gangCardBut;
        container.add(gangCardBut);

        //创建暗杠的按钮
        JButton AnGangCardBut = new JButton("暗杠");
        AnGangCardBut.setBounds(500, 750, 60, 20); //320 400
        AnGangCardBut.addActionListener(this);
        AnGangCardBut.setVisible(false);
        Other[3] = AnGangCardBut;
        container.add(AnGangCardBut);

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
        time[0].setBounds(628, 780, 100, 20); //480 570
        time[1].setBounds(948, 560, 90, 20); //800 350
        time[2].setBounds(628, 300, 90, 20); //480 200
        time[3].setBounds(278, 560, 90, 20); //160 350

        //创建庄家图标
        Dealer = new JLabel(new ImageIcon(""));
        Dealer.setVisible(false);
        Dealer.setSize(40, 40);
        container.add(Dealer);

    }

    //设置界面
    public void initJFrame() {
        //设置标题
        this.setTitle("Mahjong Game");
        //设置大小
        this.setSize(1344, 1008);
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
        //设置背景图片
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/MahjongPic/Table.jpg")));
        //JLabel background = new JLabel(new ImageIcon("/Volumes/中转/软工课设/Mahjong-game/MahjongPic/微信图片_20240517182844.jpg"));
        background.setSize(this.getSize());  // 设置背景图片大小与 JFrame 大小匹配
        container.add(background);  // 添加背景标签
        container.setComponentZOrder(background, container.getComponentCount() - 1); // 将背景标签置于底层
        //设置背景颜色
        container.setBackground(Color.BLACK);
    }
    public static void playBackgroundMusic(String filePath) {
        try {
            // 获取音频输入流
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("音频文件不存在: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            // 获取音频格式
            AudioFormat format = audioStream.getFormat();
            // 获取音频数据行
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            // 打开音频数据行
            audioClip.open(audioStream);
            // 设置循环播放
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            // 开始播放
            audioClip.start();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("不支持的音频文件格式: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("音频文件读取错误: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("音频设备不可用: " + e.getMessage());
        }
    }



    public ArrayList<ArrayList<MahjongCard>> getPlayerList(){
        return playerList;
    }

    public int getNumb(){
        return numb;
    }

    public void changeNumb(){numb++;}

    public ArrayList<MahjongCard> getCurrentList(){
        return currentList;
    }

    public ArrayList<MahjongCard> getMahjongCardList(){
        return MahjongCardList;
    }

    public static int getColor(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(0, 1));
    }

    public static int getSize(MahjongCard card) {
        return Integer.parseInt(card.getName().substring(2));
    }

    public void adjustPosition(int playerIndex){
        if(playerIndex==0){ num0--;}
        else if(playerIndex==1){ num1--;}
        else if(playerIndex==2){ num2--;}
        else if(playerIndex==3){num3--;}
    }

}
