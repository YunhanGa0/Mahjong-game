package Objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//麻将类的oop实现
public class majiangpai extends JLabel implements MouseListener {

    GameJFrame gameJFrame;
    //牌是啥
    private String name;
    //正反
    private boolean up;
    //能否被点击
    private boolean Clickable=false;
    //当前状态：是否被点击
    private boolean clicked=false;

    public majiangpai(GameJFrame m, String name, boolean up){
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
        this.setSize(71,96);
        //把牌显示出来
        this.setVisible(true);
        //给每一张牌添加鼠标监听
        this.addMouseListener(this);

    }

    public majiangpai(GameJFrame gameJFrame, String name, boolean up, boolean Clickable, boolean clicked) {
        this.gameJFrame = gameJFrame;
        this.name = name;
        this.up = up;
        this.Clickable = Clickable;
        this.clicked = clicked;
    }

    //显示正面
    public void turnFront() {
        this.setIcon(new ImageIcon(/*图片链接*/));
        this.up = true;
    }


    //显示背面
    public void turnRear() {
        this.setIcon(new ImageIcon(/*图片链接*/));
        this.up = false;
    }

    //能不能被点击：能就上升，再被点就下去
    @Override
    public void mouseClicked(MouseEvent e) {
        if (Clickable) {
            Point from = this.getLocation();
            int step;
            if (clicked){
                step = 20;
            }else {
                step = -20;
            }
            clicked = !clicked;
            Point to = new Point(from.x, from.y + step);
            this.setLocation(to);
        }

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
    public boolean isCanClick() {
        return Clickable;
    }

    /**
     * 设置
     * @param canClick
     */
    public void setCanClick(boolean canClick) {
        this.Clickable = Clickable;
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

    public String toString() {
        return "Poker{gameJFrame = " + gameJFrame + ", name = " + name + ", up = " + up + ", Clickable = " + Clickable + ", clicked = " + clicked + "}";
    }
}
