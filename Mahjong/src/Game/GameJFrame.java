package Game;

import Algorithm.Other_Algorithm;
import Objects.MahjongCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameJFrame extends JFrame implements ActionListener {

    // Container for hiding components in the interface
    public Container container = null;
    // Button for managing card play
    JButton[] Chu = new JButton[1];
    // Button for managing Hu (winning)
    JButton[] Hu = new JButton[1];
    // Buttons for managing Chi, Peng, and Gang actions (0: Peng, 1: Chi, 2: Gang, 3: AnGang)
    JButton[] Other = new JButton[4];

    // Countdown text prompt in front of the player
    JTextField[] time = new JTextField[4];
    // Dealer icon
    int DealerFlag;
    // Indicates the current turn
    int turn;
    // Tracks the number of player card plays
    public int num0;
    // Tracks the number of card plays for the three AI players
    public int num1;
    public int num2;
    public int num3;

    // Tracks the number of Chi, Peng, and Gang actions
    private static int cpgCount = 0;
    // State indicating if the next player can play
    boolean nextPlayer = false;
    // Indicates if the player has performed a Chi, Peng, or Gang action
    boolean conti = false;
    // Tracks the number of dealt cards
    public int numb;

    // Discard pile
    public ArrayList<MahjongCard> currentList = new ArrayList<>();

    // Nested collections
    // The main collection contains three sub-collections
    // Each sub-collection contains the cards for each player
    public ArrayList<ArrayList<MahjongCard>> playerList = new ArrayList<>();

    // Card box containing all the cards
    public ArrayList<MahjongCard> MahjongCardList = new ArrayList<>();

    // Dealer icon in the game interface
    JLabel Dealer;

    // Multithreaded operation to control the game flow
    PlayerOperation po;

    // Background image
    private JLabel background;

    public GameJFrame() {
        // Set icon
        setIconImage(Toolkit.getDefaultToolkit().getImage(""));
        // BGM
        new Thread(() -> playBackgroundMusic(getClass().getResource("MahjongPic/BGM.wav").getPath())).start();
        // Initialize the interface
        initJFrame();
        // Add components
        initView();
        // Show the interface
        // Display the interface first, then deal cards because dealing cards has animations that cannot be displayed if the interface is not visible
        this.setVisible(true);
        //Roll dice to determine dealer
        rollDice();
        // Initialize the dealer icon
        initDealerFlag();
        // Initialize cards
        // Prepare cards, shuffle, and deal
        new Thread(this::initCard).start();
        // Pre-game preparation
        initGame();
    }

    // Roll the dice and show the results to determine the dealer
    private void rollDice() {
        Random random = new Random();
        while (true) {
            // Each player rolls two dice
            int[][] diceResults = new int[4][2]; // Assume four players, each with two dice
            int[] totalResults = new int[4]; // Total points for each player
            for (int j = 0; j < diceResults.length; j++) {
                diceResults[j][0] = random.nextInt(6) + 1;
                diceResults[j][1] = random.nextInt(6) + 1;
                totalResults[j] = diceResults[j][0] + diceResults[j][1];
            }
            // Graphically display dice results in the window
            for (int j = 0; j < diceResults.length; j++) {
                System.out.println("Player " + (j + 1) + " dice results: " + diceResults[j][0] + ", " + diceResults[j][1]);
                // Display dice images in the window
                showDiceImages(j, diceResults[j][0], diceResults[j][1]);
            }
            // Determine the player with the highest dice roll and set as the dealer
            int maxDice = 0;
            int dealerIndex = 0;  // Dealer index
            boolean tie = false;
            for (int j = 0; j < totalResults.length; j++) {
                if (totalResults[j] > maxDice) {
                    maxDice = totalResults[j];
                    dealerIndex = j;
                    tie = false;
                } else if (totalResults[j] == maxDice) {
                    tie = true; // Mark as a tie if there is a tie
                }
            }
            if (!tie) { // If no tie, determine the dealer
                // Set the turn to the dealer
                DealerFlag = dealerIndex;
                setFlag(dealerIndex); // Call setFlag to display the dealer icon
                break;
            } else {
                System.out.println("Tie detected. Re-rolling the dice.");
            }

        }
    }

    private void showDiceImages(int playerIndex, int dice1, int dice2) {
        // Display dice images in the window based on player index and dice results
        String diceImagePath1 = "C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\Dice" + dice1 + ".png";
        String diceImagePath2 = "C:\\Users\\qwerty\\Downloads\\MahjongPic\\MahjongPic\\Dice" + dice2 + ".png";

        JLabel diceLabel1 = new JLabel(new ImageIcon(diceImagePath1));
        JLabel diceLabel2 = new JLabel(new ImageIcon(diceImagePath2));
        diceLabel1.setSize(55, 55);
        diceLabel2.setSize(55, 55);

        // Add a name to each label
        diceLabel1.setName("DiceLabel1");
        diceLabel2.setName("DiceLabel2");

        // Set the position of the dice images
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
        container.setComponentZOrder(background, container.getComponentCount() - 1); // Ensure the background image is always at the bottom
    }

    // Hide the dice
    public void hideDice() {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel label) {
                if (label.getIcon() != null && label.getIcon().toString().contains("Dice")) {
                    label.setVisible(false);
                }
            }
        }
    }

    // Get the joker card for this round
    public MahjongCard getLai() {
        // Draw a joker card from the bottom of the deck
        MahjongCard Lai = MahjongCardList.get(135);
        // Turn it face up
        Lai.turnFront();
        // Place it in the center of the table
        Lai.setLocation(650, 450);
        Lai.setVisible(true);
        container.add(Lai);
        container.setComponentZOrder(Lai, 0);
        return Lai;
    }

    // Initialize cards
    // Prepare cards, shuffle cards, deal cards
    public void initCard() {
        addCards();
        Collections.shuffle(MahjongCardList);
        dealCards();
        organizePlayerCards();
        getLai();
    }

    private void addCards() {
        for (int i = 0; i <= 9; i++) {
            if (i <= 2) { // Add character, bamboo, and circle tiles
                addTiles(i, 1, 9, 4);
            } else { // Add wind tiles
                addTiles(i, 0, 0, 4);
            }
        }
    }

    private void addTiles(int type, int start, int end, int count) {
        for (int j = start; j <= end; j++) {
            for (int k = 0; k < count; k++) {
                MahjongCard card = new MahjongCard(this, type + "-" + j, false);
                MahjongCardList.add(card);
                container.add(card);
                card.setVisible(false);
                card.setLocation(650, 450);
            }
        }
    }

    private void dealCards() {
        ArrayList<MahjongCard> player0 = new ArrayList<>();
        ArrayList<MahjongCard> player1 = new ArrayList<>();
        ArrayList<MahjongCard> player2 = new ArrayList<>();
        ArrayList<MahjongCard> player3 = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            MahjongCard card = MahjongCardList.get(i);
            if (i % 4 == 0) {
                addCardToPlayer(player0, card);
                card.turnFront();
            } else if (i % 4 == 1) {
                addCardToPlayer(player1, card);
            } else if (i % 4 == 2) {
                addCardToPlayer(player2, card);
            } else {
                addCardToPlayer(player3, card);
            }
        }
        playerList.add(player0);
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
    }

    private void addCardToPlayer(ArrayList<MahjongCard> player, MahjongCard card) {
        player.add(card);
        card.setVisible(true);
        numb++;
        container.setComponentZOrder(card, 0);
    }

    private void organizePlayerCards() {
        for (int i = 0; i <= 3; i++) {
            Other_Algorithm.order(playerList.get(i));
            Other_Algorithm.rePosition(this, playerList.get(i), i);
        }
    }

    // Preparation before playing
    private void initGame() {
        // Display countdown text in front of the player
        time[0].setVisible(true);
        // 10 seconds countdown
        po = new PlayerOperation(this, 5);
        // Start countdown
        po.start();
    }

    // Dealer flag position
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

    // Set dealer icon in initialization method
    private void initDealerFlag() {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/MahjongPic/flag.png"));
        Image originalImage = originalIcon.getImage();
        // Set the size after scaling
        int width = 60;
        int height = 60;
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        Dealer = new JLabel(scaledIcon);
        Dealer.setVisible(false);
        Dealer.setSize(width, height);
        container.add(Dealer);
        container.setComponentZOrder(Dealer, 0);
    }

    // Event triggered by button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Other[0]) { // Click Peng (Pong), perform Peng operation
            handlePeng();
        } else if (e.getSource() == Other[1]) { // Click Chi (Chow), perform Chi operation
            handleEat();
        } else if (e.getSource() == Other[2]) { // Click Gang, perform Gang operation
            handleGang();
        } else if (e.getSource() == Other[3]) { // Click to perform AnGang operation
            handleDarkGang();
        } else if (e.getSource() == Hu[0]) { // Click Hu, perform Hu operation
            handleHu();
        } else if (e.getSource() == Chu[0]) { // Click to discard
            handleDiscard();
        }
    }

    private void handlePeng() {
        MahjongCard pengCard = currentList.get(currentList.size() - 1);
        ArrayList<MahjongCard> player = playerList.get(0);
        player.add(pengCard);
        moveCards(player, pengCard, "Peng");
        cpgCount++;
        this.conti = true;
        PlayerOperation.setAction(true);
    }

    private void handleEat() {
        MahjongCard chiCard = currentList.get(currentList.size()-1);
        int color = chiCard.getColor();
        ArrayList<MahjongCard> player = playerList.get(0);
        if(Other_Algorithm.checkEat(player,chiCard)) {
            Other_Algorithm.order(player);
            ArrayList<MahjongCard> sequence = new ArrayList<>();
            sequence.add(chiCard);
            for (MahjongCard card : player) {
                boolean ifSame = false;
                for (MahjongCard card1 : sequence) {
                    if (card.getColor() == card1.getColor() && card.getValue() == card1.getValue()) {
                        ifSame = true;
                        break;
                    }
                }
                if (ifSame) {
                    continue;
                }
                if (Objects.equals(Other_Algorithm.getChiSituation(), "XOO")) {
                    // Add it to sequence is 1 less than chiCard
                    if (card.getColor() == color && card.getValue() == chiCard.getValue() + 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                    //Add it to sequence is 1 bigger than chiCard
                    else if (card.getColor() == color && card.getValue() == chiCard.getValue()+ 2) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                } else if (Objects.equals(Other_Algorithm.getChiSituation(), "OXO")) {
                    // Add it to sequence is 1 less than chiCard
                    if (card.getColor() == color && card.getValue() == chiCard.getValue() - 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                    // Add it to sequence is 1 bigger than chiCard
                    else if (card.getColor() == color && card.getValue() == chiCard.getValue() + 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                } else if (Objects.equals(Other_Algorithm.getChiSituation(), "OOX")) {
                    // Add it to sequence is 1 less than chiCard
                    if (card.getColor()== color && card.getValue() == chiCard.getValue() - 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                    // Add it to sequence is 1 bigger than chiCard
                    else if (card.getColor() == color && card.getValue() == chiCard.getValue() - 2) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                }
                if (sequence.size() == 3) {
                    break;
                }
            }
            Other_Algorithm.order(sequence);
            int j = 0;
            for (MahjongCard card : sequence) {
                Point point = new Point();
                point.x = 220 + j * 35;
                point.y = 820 - (cpgCount * 50);
                Other_Algorithm.move(card, card.getLocation(), point);
                card.setClickable(false);
                card.setIfEat(true);
                j++;
            }
        }
    }

    private void handleGang() {
        MahjongCard gangCard = currentList.get(currentList.size() - 1);
        ArrayList<MahjongCard> player = playerList.get(0);
        if (Other_Algorithm.checkGang(player, gangCard)) {
            player.add(gangCard);
            moveCards(player, gangCard, "Gang");
            cpgCount++;
            this.conti = true;
            PlayerOperation.setAction(true);
        }
    }

    private void handleDarkGang() {
        ArrayList<MahjongCard> player = playerList.get(0);
        MahjongCard gangCard = player.get(player.size() - 1);
        if (Other_Algorithm.checkDarkGang(player, gangCard)) {
            moveCards(player, gangCard, "Gang");
            cpgCount++;
            this.conti = true;
            PlayerOperation.setAction(true);
        }
    }

    private void handleHu() {
        ArrayList<MahjongCard> player = playerList.get(0);
        if (turn == 0) {
            Other_Algorithm.order(player);
            Other_Algorithm.rePosition(this, player, 0);
        } else {
            MahjongCard huCard = currentList.get(currentList.size() - 1);
            player.add(huCard);
            Other_Algorithm.order(player);
            Other_Algorithm.rePosition(this, player, 0);
        }
        for (MahjongCard card : player) {
            card.turnFront();
        }
        this.conti = true;
        PlayerOperation.setAction(true);
    }

    private void handleDiscard() {
        ArrayList<MahjongCard> player = playerList.get(0);
        for (int i = 0; i < player.size(); i++) {
            MahjongCard card = player.get(i);
            if (card.isClicked()) {
                currentList.add(card);
                player.remove(card);
                moveCardToDiscard(card);
                card.setClickable(false);
                Other_Algorithm.order(player);
                Other_Algorithm.rePosition(this, player, 0);
                time[0].setVisible(false);
                this.nextPlayer = true;
                break;
            }
        }
    }

    private void moveCardToDiscard(MahjongCard card) {
        Point point = new Point();
        if (num0 / 13 == 0) {
            point.x = 430 + num0 * 35;
            point.y = 690;
        } else {
            point.x = 430 + (num0 - 13) * 35;
            point.y = 640;
        }
        num0++;
        Other_Algorithm.move(card, card.getLocation(), point);
    }

    private void moveCards(ArrayList<MahjongCard> player, MahjongCard card, String action) {
        int j = 0;
        for (MahjongCard c : player) {
            if (c.getName().equals(card.getName())) {
                Point point = new Point(220 + j * 35, 820 - (cpgCount * 50));
                Other_Algorithm.move(c, c.getLocation(), point);
                c.setClickable(false);
                switch (action) {
                    case "Peng" -> c.setIfPeng(true);
                    case "Eat" -> c.setIfEat(true);
                    case "Gang" -> c.setIfGang(true);
                }
                j++;
            }
        }
    }

    // Add components
    public void initView() {
        createButton("Discard", 640, 750, Chu, 0);
        createButton("Hu", 780, 750, Hu, 0);
        createButton("Peng", 710, 750, Other, 0);
        createButton("Eat", 570, 750, Other, 1);
        createButton("Gang", 500, 750, Other, 2);
        createButton("AnGang", 500, 750, Other, 3);
        createCountdownTexts();
        createDealerIcon();
    }

    private void createButton(String text, int x, int y, JButton[] buttonArray, int index) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 60, 20);
        button.addActionListener(this);
        button.setVisible(false);
        buttonArray[index] = button;
        container.add(button);
    }

    private void createCountdownTexts() {
        for (int i = 0; i < 4; i++) {
            time[i] = new JTextField("Countdown:e",30);
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(550, 780, 200, 20);
        time[0].setOpaque(false); // Make button non-opaque
        time[0].setBackground(new Color(0, 0, 0, 0)); // Set background color to transparent
        time[1].setBounds(948, 560, 90, 20);
        time[2].setBounds(628, 300, 90, 20);
        time[3].setBounds(278, 560, 90, 20);
    }

    private void createDealerIcon() {
        Dealer = new JLabel(new ImageIcon(""));
        Dealer.setVisible(false);
        Dealer.setSize(40, 40);
        container.add(Dealer);
    }

    // Initialize the game interface
    public void initJFrame() {
        // Set title
        this.setTitle("Mahjong Game");
        // Set size
        this.setSize(1344, 1008);
        // Set close operation
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set window not resizable
        this.setResizable(false);
        // Center the interface
        this.setLocationRelativeTo(null);
        // Get the hidden container of the interface, use directly without calling method again
        container = this.getContentPane();
        // Cancel the default center placement
        container.setLayout(null);
        // Set background image
        background = new JLabel(new ImageIcon(getClass().getResource("/MahjongPic/Table.jpg")));
        background.setSize(this.getSize()); // Set background image size to match JFrame size
        container.add(background); // Add background label
        container.setComponentZOrder(background, container.getComponentCount() - 1); // Set background label to bottom layer
    }

    // Set background music
    public static void playBackgroundMusic(String filePath) {
        try {
            // Get audio input stream
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file does not exist: " + filePath);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            // Get audio format
            AudioFormat format = audioStream.getFormat();
            // Get audio data line
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            // Open audio data line
            audioClip.open(audioStream);
            // Set loop play
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            // Start playing
            audioClip.start();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Audio file read error: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio device unavailable: " + e.getMessage());
        }
    }

    // Get the hand card list of all players
    public ArrayList<ArrayList<MahjongCard>> getPlayerList() {
        return playerList;
    }

    // Get the number of dealt cards
    public int getNumb() {
        return numb;
    }

    // Update the number of dealt cards
    public void changeNumb() {
        numb++;
    }

    // Get discard pile
    public ArrayList<MahjongCard> getCurrentList() {
        return currentList;
    }

    // Get card box
    public ArrayList<MahjongCard> getMahjongCardList() {
        return MahjongCardList;
    }


    // Update display area
    public void adjustPosition(int playerIndex) {
        if (playerIndex == 0) {
            num0--;
        } else if (playerIndex == 1) {
            num1--;
        } else if (playerIndex == 2) {
            num2--;
        } else if (playerIndex == 3) {
            num3--;
        }
    }

    // Get the GUI container
    public Container getContainer() {
        return container;
    }

    // Get the index of the current turn player
    public int getTurn() {
        return turn;
    }
}
