package Objects;

import Game.GameJFrame;
import Game.ShinJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;

// OOP implementation of Mahjong cards
public class MahjongCard extends JLabel implements MouseListener, Serializable {

    private static final int CARD_WIDTH = 35;
    private static final int CARD_HEIGHT = 48;
    private static final int CLICK_OFFSET = 15;
    private static final String FRONT_IMAGE_PREFIX_CLASSIC = "/MahjongPic/tilee";
    private static final String FRONT_IMAGE_PREFIX_MODERN = "/MahjongPic/tile";
    private static final String REAR_IMAGE_CLASSIC = "/MahjongPic/back1.jpg";
    private static final String REAR_IMAGE_MODERN = "/MahjongPic/back0.jpg";

    private GameJFrame gameJFrame;
    private String name;
    private boolean up;
    private boolean clickable = false;
    private boolean clicked = false;
    private boolean ifPeng = false;
    private boolean ifGang = false;
    private boolean ifEat = false;

    // Constructor
    public MahjongCard(GameJFrame gameJFrame, String name, boolean up) {
        this.gameJFrame = gameJFrame;
        this.name = name;
        this.up = up;

        setCardFace();
        this.setSize(CARD_WIDTH, CARD_HEIGHT);
        this.setVisible(false);
        this.addMouseListener(this);
    }

    // Constructor for testing
    public MahjongCard(String name) {
        this.name = name;
    }

    // Show the front side of the card
    public void turnFront() {
        String prefix = ShinJFrame.getIsClassic() ? FRONT_IMAGE_PREFIX_CLASSIC : FRONT_IMAGE_PREFIX_MODERN;
        setCardImage(prefix + getImageIndex() + ".png");
        this.up = true;
    }

    // Show the rear side of the card
    public void turnRear() {
        String rearImage = ShinJFrame.getIsClassic() ? REAR_IMAGE_CLASSIC : REAR_IMAGE_MODERN;
        setCardImage(rearImage);
        this.up = false;
    }

    // Toggle card clicked state
    @Override
    public void mouseClicked(MouseEvent e) {
        if (clickable && !ifPeng && !ifGang && !ifEat) {
            toggleClicked();
            updateCardClickability();
        }
    }

    // Helper method to toggle the card click state
    private void toggleClicked() {
        this.clicked = !this.clicked;
        Point location = this.getLocation();
        location.y += this.clicked ? -CLICK_OFFSET : CLICK_OFFSET;
        this.setLocation(location);
    }

    // Restrict card clickability
    private void updateCardClickability() {
        ArrayList<MahjongCard> cards = gameJFrame.getPlayerList().get(0);  // Assuming index 0 is the human player
        for (MahjongCard card : cards) {
            if (card != this) {
                card.setClickable(!this.clicked);
            }
        }
    }

    // Set which side towards
    private void setCardFace() {
        if (this.up) {
            turnFront();
        } else {
            turnRear();
        }
    }

    //Get the index of card to set image
    private String getImageIndex() {
        int i = Integer.parseInt(this.name.substring(0, 1));
        int j = Integer.parseInt(this.name.substring(2));
        return i + "" + j;
    }

    //Set image for card
    private void setCardImage(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        this.setIcon(new ImageIcon(scaledImage));
    }

    // Get the color of the card
    public int getColor() {
        return Integer.parseInt(this.getName().substring(0, 1));
    }

    // Get the size of the card
    public int getValue() {
        return Integer.parseInt(this.getName().substring(2));
    }

    // Getters and setters methods
    public boolean getIfPeng() {
        return ifPeng;
    }

    public void setIfPeng(boolean ifPeng) {
        this.ifPeng = ifPeng;
    }

    public boolean getIfGang() {
        return ifGang;
    }

    public void setIfGang(boolean ifGang) {
        this.ifGang = ifGang;
    }

    public boolean getIfEat() {
        return ifEat;
    }

    public void setIfEat(boolean ifEat) {
        this.ifEat = ifEat;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
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

    @Override
    public String toString() {
        return "Poker{gameJFrame = " + gameJFrame + ", name = " + name + ", up = " + up + ", clickable = " + clickable + ", clicked = " + clicked + "}";
    }
}
