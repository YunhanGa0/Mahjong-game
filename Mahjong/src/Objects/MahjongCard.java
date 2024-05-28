package Objects;

import Game.GameJFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//麻将类的oop实现
public class MahjongCard extends JLabel implements MouseListener {

    GameJFrame gameJFrame;
    //牌是啥
    private String name;
    //正反
    private boolean up;
    //能否被点击
    private boolean Clickable = false;
    //当前状态：是否被点击
    private boolean clicked = false;

    //当前是否被进行碰操作
    private boolean ifPeng = false;

    private boolean ifGang= false;

    private boolean ifEat= false;


    public MahjongCard(GameJFrame m, String name, boolean up){
        this.gameJFrame=m;
        this.name=name;
        this.up=up;
        if (this.up){
            //正面
            this.turnFront();
        }else {
            //背面
            this.turnRear();
        }
        //牌的大小
        this.setSize(35,48);
        //把牌显示出来
        this.setVisible(false);
        //给每一张牌添加鼠标监听
        this.addMouseListener(this);

    }

    public MahjongCard(String name){
        this.name=name;
    }

    //显示正面
    public void turnFront() {
        int i=Integer.parseInt(this.getName().substring(0, 1));
        int j= Integer.parseInt(this.getName().substring(2));
        ImageIcon imageIcon = new ImageIcon("/Volumes/中转/软工课设/Mahjong-game/MahjongPic/tile"+i+j+".png"); // 创建一个图片图标
        //ImageIcon imageIcon = new ImageIcon("C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\tile"+i+j+".png"); // 创建一个图片图标
        Image image = imageIcon.getImage(); // 获取图标的图片对象
        Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH); // 缩放图片以适应牌的大小
        this.setIcon(new ImageIcon(scaledImage)); // 设置牌的图标为缩放后的图片
        this.up = true;
    }

    //显示背面
    public void turnRear() {
        ImageIcon imageIcon = new ImageIcon("/Volumes/中转/软工课设/Mahjong-game/Mahjong/MahjongPic/tile01.png"); // 创建一个背面图标
        //ImageIcon imageIcon = new ImageIcon("C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\back0.png");
        Image image = imageIcon.getImage(); // 获取图标的图片对象
        Image scaledImage = image.getScaledInstance(35,48, Image.SCALE_SMOOTH); // 缩放图片以适应牌的大小
        this.setIcon(new ImageIcon(scaledImage)); // 设置牌的图标为缩放后的图片
        this.up = false;
    }


    //能不能被点击,能就上升，再被点就下去
    @Override
    public void mouseClicked(MouseEvent e) {
        if(!ifPeng || !ifEat || !ifGang) {
            if (Clickable) {
                Point from = this.getLocation();
                int step;
                if (clicked) {
                    step = 15;
                } else {
                    step = -15;
                }
                clicked = !clicked;
                Point to = new Point(from.x, from.y + step);
                this.setLocation(to);
            }
        }
    }

    public boolean getIfPeng(){
        return ifPeng;
    }

    public void setIfPeng(boolean ifPeng) {
        this.ifPeng = ifPeng;
    }

    public boolean getIfGang(){
        return ifGang;
    }

    public void setIfGang(boolean ifGang) {
        this.ifGang = ifGang;
    }

    public boolean getIfEat(){
        return this.ifEat;
    }

    public void setIfEat(boolean ifEat) {
        this.ifEat = ifEat;
    }

    public String getName() {
        return name;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanClick(boolean canClick) {
        this.Clickable = canClick;
    }

    public boolean isClicked() {
        return clicked;
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public String toString() {
        return "Poker{gameJFrame = " + gameJFrame + ", name = " + name + ", up = " + up + ", Clickable = " + Clickable + ", clicked = " + clicked + "}";
    }
}
