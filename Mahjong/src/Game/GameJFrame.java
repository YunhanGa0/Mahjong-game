package Game;

import Objects.MahJongCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;


public class GameJFrame extends JFrame implements ActionListener {

    //获取界面中的隐藏容器
    //现在统一获取了，后面直接用就可以了
    public Container container = null;

    //管理胡牌和杠牌两个按钮
    JButton landlord[] = new JButton[2];

    //管理吃牌和碰牌两个按钮
    JButton publishCard[] = new JButton[2];

    int dizhuFlag;
    int turn;

    //游戏界面中庄家的图标
    JLabel dizhu;


    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家当前要出的牌
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    //3：对面的
    ArrayList<ArrayList<MahJongCard>> currentList = new ArrayList<>();

    //集合嵌套集合
    //大集合中有三个小集合
    //小集合中装着每一个玩家的牌
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    //3:对面的
    ArrayList<ArrayList<MahJongCard>> playerList = new ArrayList<>();

    //底牌
    ArrayList<MahJongCard> lordList = new ArrayList<>();

    //牌盒，装所有的牌
    ArrayList<MahJongCard> pokerList = new ArrayList();

    //三个玩家前方的文本提示
    //0索引：左边的电脑玩家
    //1索引：中间的自己
    //2索引：右边的电脑玩家
    JTextField time[] = new JTextField[3];

    //用户操作，涉及多线程的知识
    PlayerOperation po;

    boolean nextPlayer = false;

    public GameJFrame() {
        //设置图标
        setIconImage(Toolkit.getDefaultToolkit().getImage("doudizhu\\image\\dizhu.png"));
        //设置界面
        initJframe();
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

        ArrayList<Integer> list= new ArrayList<>();
        HashMap<Integer,String> pai = new HashMap<>();
        String[] color= {"条","万","筒"};
        String[] number= {"1","2","3","4","5","6","7","8","9"};

        int numb=1;

        for (String c : color) {
            for (String n : number) {
                for (int i = 0; i < 4; i++){
                    pai.put(numb, c + n);
                    list.add(numb);
                    numb++;
                }
            }
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"东风");
            list.add(numb);
            numb++;
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"西风");
            list.add(numb);
            numb++;
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"南风");
            list.add(numb);
            numb++;
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"北风");
            list.add(numb);
            numb++;
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"红中");
            list.add(numb);
            numb++;
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"白板");
            list.add(numb);
            numb++;
        }

        for(int i=0;i<4;i++){
            pai.put(numb,"发财");
            list.add(numb);
            numb++;
        }

        Collections.shuffle(list);

        TreeSet<Integer> player1=new TreeSet<>();
        TreeSet<Integer> player2=new TreeSet<>();
        TreeSet<Integer> player3=new TreeSet<>();
        TreeSet<Integer> player4=new TreeSet<>();

        for (int i=0;i<52;i++){
            int majiang= list.get(i);
            if (i%4==0){
                player1.add(majiang);
            }else if(i%4==1){
                player2.add(majiang);
            }else if(i%4==2){
                player3.add(majiang);
            }else {
                player4.add(majiang);
            }
        }

    }

    //打牌之前的准备工作
    private void initGame() {
        //创建三个集合用来装三个玩家准备要出的牌

    }

    @Override
    public void actionPerformed(ActionEvent e) {

            //点击Roll骰子

            //点击出牌

            //创建一个临时的集合，用来存放当前要出的牌

            //获取中自己手上所有的牌

            //遍历手上的牌，把要出的牌都放到临时集合中

            //判断，如果电脑玩家是否可以吃/碰/杠/胡



                //把当前要出的牌，放到大集合中统一管理

                //在手上的牌中，去掉已经出掉的牌


                //计算坐标并移动牌
                //移动的目的是要出的牌移动到上方

                //重新摆放剩余的牌

                //隐藏文本提示

                //下一个玩家可玩

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
        dizhu = new JLabel(new ImageIcon("doudizhu\\image\\dizhu.png"));
        dizhu.setVisible(false);
        dizhu.setSize(40, 40);
        container.add(dizhu);

    }

    //设置界面
    public void initJframe() {
        //设置标题
        this.setTitle("斗地主");
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
