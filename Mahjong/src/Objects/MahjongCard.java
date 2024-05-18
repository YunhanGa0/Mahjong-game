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

    private boolean ifGang=false;

    private boolean ifEat=false;


    public MahjongCard(GameJFrame m, String name, boolean up){
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
        this.setVisible(true);
        //给每一张牌添加鼠标监听
        this.addMouseListener(this);

    }

    //显示正面
    public void turnFront() {
        int i=Integer.parseInt(this.getName().substring(0, 1));
        int j= Integer.parseInt(this.getName().substring(2));
        //ImageIcon imageIcon = new ImageIcon("C:\\Users\\qwerty\\Documents\\WeChat Files\\wxid_8wwwtqtiuahv22\\FileStorage\\File\\2024-05\\MahjongPic\\MahjongPic\\tile"+i+j+".png"); // 创建一个图片图标
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\tile"+i+j+".png"); // 创建一个图片图标
        Image image = imageIcon.getImage(); // 获取图标的图片对象
        Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH); // 缩放图片以适应牌的大小
        this.setIcon(new ImageIcon(scaledImage)); // 设置牌的图标为缩放后的图片
        this.up = true;
    }

    //显示背面
    public void turnRear() {
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\tile01.png"); // 创建一个背面图标
        Image image = imageIcon.getImage(); // 获取图标的图片对象
        Image scaledImage = image.getScaledInstance(35, 38, Image.SCALE_SMOOTH); // 缩放图片以适应牌的大小
        this.setIcon(new ImageIcon(scaledImage)); // 设置牌的图标为缩放后的图片
        this.up = false;
    }



    //能不能被点击,能就上升，再被点就下去
    @Override
    public void mouseClicked(MouseEvent e) {
        if(ifPeng==false||ifEat==false||ifGang==false) {
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

    /**
     * 获取
     * @return gameJFrame
     */
    public GameJFrame getGameJFrame() {
        return gameJFrame;
    }

    /**
     * 设置
     * @param gameJFrame
     */
    public void setGameJFrame(GameJFrame gameJFrame) {
        this.gameJFrame = gameJFrame;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return up
     */
    public boolean isUp() {
        return up;
    }

    /**
     * 设置
     * @param up
     */
    public void setUp(boolean up) {
        this.up = up;
    }

    /**
     * 获取
     * @return canClick
     */
    public boolean isClickable() {
        return Clickable;
    }

    /**
     * 设置
     * @param canClick
     */
    public void setCanClick(boolean canClick) {
        this.Clickable = canClick;
    }

    /**
     * 获取
     * @return clicked
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * 设置
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean getifPeng(){
        return ifPeng;
    }

    public void setIfPeng(boolean ifPeng) {
        this.ifPeng = ifPeng;
    }

    public boolean getifGang(){
        return ifGang;
    }

    public void setIfGang(boolean ifGang) {
        this.ifGang = ifGang;
    }

    public boolean getifEat(){
        return ifEat;
    }

    public void setIfEat(boolean ifEat) {
        this.ifEat = ifEat;
    }



    public String toString() {
        return "Poker{gameJFrame = " + gameJFrame + ", name = " + name + ", up = " + up + ", Clickable = " + Clickable + ", clicked = " + clicked + "}";
    }
}
