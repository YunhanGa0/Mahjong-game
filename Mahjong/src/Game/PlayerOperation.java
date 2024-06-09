package Game;

import Algorithm.AI_Algorithm;
import Algorithm.Hu_Algorithm;
import Algorithm.Other_Algorithm;
import Objects.MahjongCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerOperation extends Thread {

    // Main game interface
    static GameJFrame gameJFrame;
    // Countdown timer
    Integer i;
    // Flag indicating if the player has taken an action (Peng, Gang, Eat)
    static boolean actionTaken = false;

    public PlayerOperation(GameJFrame m, int i) {
        this.gameJFrame = m;
        this.i = i;
    }

    // Method to pause for N seconds
    // Parameter is the wait time in seconds
    public void sleep(int i) {
        try {
            Thread.sleep((long) i * 900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Countdown timer to limit player card playing operation
    public void timeWait(int n, int player) {
        if (player == 0) { // Player's turn
            int i = n;
            while (!gameJFrame.nextPlayer && i >= 0) {
                gameJFrame.time[player].setText("Countdown:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1) {
                gameJFrame.Chu[0].setVisible(false);
                // Actions after countdown ends, play the drawn card or the last clicked card
                ArrayList<MahjongCard> play = gameJFrame.getPlayerList().get(0);
                if (checkClicked()) {
                    // Actions after countdown ends, play the drawn card or the last clicked card
                    for (int j = 0; j < play.size(); j++) {
                        MahjongCard card = play.get(j);
                        if (card.isClicked() && (!card.getIfGang() && !card.getIfPeng() && !card.getIfEat())) {
                            gameJFrame.currentList.add(card);
                            play.remove(card);
                            // Calculate coordinates and move the card
                            Point point = new Point();
                            if (gameJFrame.num0 / 13 == 0) {
                                point.x = 430 + (gameJFrame.num0) * 35;
                                point.y = 690;
                            } else {
                                point.x = 430 + (gameJFrame.num0 - 13) * 35;
                                point.y = 640;
                            }
                            gameJFrame.num0++;
                            card.setClickable(false);
                            Other_Algorithm.move(card, card.getLocation(), point);
                        }
                    }
                } else {
                    // If no card is clicked, play the drawn card
                    int num = play.size() - 1;
                    while (num >= 0) {
                        MahjongCard card = play.get(num);
                        if (!card.isClicked() && !card.getIfEat() && !card.getIfPeng() && !card.getIfGang() && !Objects.equals(card.getName(), gameJFrame.getLai().getName())) {
                            break;
                        } else {
                            num--;
                        }
                    }
                    MahjongCard card = play.get(num);
                    gameJFrame.currentList.add(card);
                    play.remove(card);
                    // Calculate coordinates and move the card
                    Point point = new Point();
                    if (gameJFrame.num0 / 13 == 0) {
                        point.x = 430 + (gameJFrame.num0) * 35;
                        point.y = 690;
                    } else {
                        point.x = 430 + (gameJFrame.num0 - 13) * 35;
                        point.y = 640;
                    }
                    gameJFrame.num0++;
                    card.setClickable(false);
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
                // Reposition remaining cards
                Other_Algorithm.order(play);
                Other_Algorithm.rePosition(gameJFrame, play, 0);
                // Show the played Mahjong card
                gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1).turnFront();
                sleep(1);
            }
            gameJFrame.nextPlayer = false;
        } else {
            for (int i = n; i >= 0; i--) {
                sleep(1);
                gameJFrame.time[player].setText("Countdown:" + i);
            }
        }
        gameJFrame.time[player].setVisible(false);
    }

    // Countdown timer to limit player Peng, Gang, Eat operation
    public void timeWaitOther(int n, int player) {
        if (player == 0) { // Player's turn
            int i = n;
            while (!gameJFrame.conti && i >= 0) {
                gameJFrame.time[player].setText("Countdown:" + i);
                gameJFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            gameJFrame.conti = false;
        } else {
            for (int i = n; i >= 0; i--) {
                sleep(1);
                gameJFrame.time[player].setText("Countdown:" + i);
                gameJFrame.time[player].setVisible(true);
            }
        }
        gameJFrame.time[player].setVisible(false);
    }

    // Control the main flow of the game, including countdown, judging player actions, updating game status, and checking game end conditions
    @Override
    public void run() {
        // Game start, roll dice to determine dealer
        while (i > -1) {
            gameJFrame.time[0].setText("Roll dice to determine dealer:");
            sleep(2);
            i--;
        }
        if (i == -1) {
            // Show dealer information
            switch (gameJFrame.DealerFlag) {
                case 0 -> gameJFrame.time[0].setText("You are the dealer");
                case 1 -> gameJFrame.time[0].setText("Next player is the dealer");
                case 2 -> gameJFrame.time[0].setText("Opponent is the dealer");
                case 3 -> gameJFrame.time[0].setText("Previous player is the dealer");
            }
            // Hide dice
            gameJFrame.hideDice();
        }
        // Set dealer, dealer is the first player to play
        gameJFrame.turn = gameJFrame.DealerFlag;
        // Main game loop
        while (!win()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) { // Handle current player's card playing logic
                // Draw card for the current player (robot)
                addCards(gameJFrame.turn);
                // Player's hand cards can be clicked
                for (MahjongCard cards : gameJFrame.playerList.get(0)) {
                    if (!cards.getIfPeng() && !cards.getIfGang() && !cards.getIfEat()) {
                        cards.setClickable(true);
                    }
                }
                if (gameJFrame.turn == 0) { // If it's the player's turn
                    // First check for self-draw and dark gang
                    if (Hu_Algorithm.checkHu(gameJFrame.playerList.get(0), gameJFrame.getLai())) { // Check for self-draw
                        // If true, show Hu button
                        gameJFrame.Hu[0].setVisible(true);
                        timeWaitOther(setTime() / 2, 0);
                        gameJFrame.Hu[0].setVisible(false);
                        // If the player chooses Hu, end the game loop
                        if (win() && actionTaken) {
                            actionTaken = false;
                            break;
                        }
                        // If not, continue playing
                        gameJFrame.Chu[0].setVisible(true);
                        timeWait(setTime(), 0);  // Player card playing countdown
                        gameJFrame.Chu[0].setVisible(false);
                    } else if (Other_Algorithm.checkDarkGang(gameJFrame.playerList.get(0), gameJFrame.playerList.get(0).get(gameJFrame.playerList.get(0).size() - 1))) { // Check for dark gang
                        // If true, show dark gang button
                        gameJFrame.Other[3].setVisible(true);
                        timeWaitOther(setTime() / 2, 0);
                        gameJFrame.Other[3].setVisible(false);
                        // If dark gang is taken, draw a card and then play
                        if (actionTaken) {
                            addCards(0);
                        }
                        // If not, continue playing
                        gameJFrame.Chu[0].setVisible(true);
                        timeWait(setTime(), 0);
                        gameJFrame.Chu[0].setVisible(false);
                        actionTaken = false;
                    } else {
                        // If no self-draw and dark gang, perform play card operation
                        gameJFrame.Chu[0].setVisible(true);
                        timeWait(setTime(), 0);
                        gameJFrame.Chu[0].setVisible(false);
                    }
                } else { // Computer player's turn
                    computerPlayerAction(gameJFrame.turn);
                }
                if (win()) { // Check if any player (robot) self-draws
                    break;
                }
                // After playing a card, loop to check if any player can perform Hu, Gang, Peng, Eat operation
                for (int j = (gameJFrame.turn + 1) % 4; j != gameJFrame.turn; j = (j + 1) % 4) {
                    // Get the played card
                    MahjongCard lastCard = gameJFrame.currentList.get(gameJFrame.currentList.size() - 1);
                    // Check if other players can Hu
                    if (Hu_Algorithm.checkHu(gameJFrame.playerList.get(j), lastCard, gameJFrame.getLai())) { // If Hu is possible
                        if (j == 0) { // Player operation
                            gameJFrame.Hu[0].setVisible(true);
                            timeWaitOther(setTime() / 2, 0);
                            gameJFrame.Hu[0].setVisible(false);
                            if (win() && actionTaken) {
                                break;
                            }
                        } else { // Computer operation
                            computerPlayerAction(j);
                            if (win()) {
                                break;
                            }
                        }
                        gameJFrame.turn = j;
                        break;
                    }
                    // Check if other players can Gang
                    if (Other_Algorithm.checkGang(gameJFrame.playerList.get(j), lastCard)) {
                        if (j == 0) { // Player operation
                            gameJFrame.Other[2].setVisible(true);
                            timeWaitOther(setTime() / 2, 0);
                            gameJFrame.Other[2].setVisible(false);
                            if (actionTaken) {
                                // Draw a card first
                                addCards(j);
                                gameJFrame.Chu[0].setVisible(true);
                                timeWait(setTime(), 0);
                                gameJFrame.Chu[0].setVisible(false);
                            }
                            actionTaken = false;
                        } else { // Computer operation
                            computerPlayerAction(j);
                        }
                        gameJFrame.adjustPosition(gameJFrame.turn); // Update display area
                        gameJFrame.turn = j;
                        break;
                    }
                    // Check if other players can Peng
                    if (Other_Algorithm.checkPeng(gameJFrame.playerList.get(j), lastCard)) {
                        if (j == 0) { // Player operation
                            gameJFrame.Other[0].setVisible(true);
                            timeWaitOther(setTime() / 2, 0);
                            gameJFrame.Other[0].setVisible(false);
                            if (actionTaken) {
                                gameJFrame.Chu[0].setVisible(true);
                                timeWait(setTime(), 0);
                                gameJFrame.Chu[0].setVisible(false);
                            }
                            actionTaken = false;
                        } else { // Computer operation
                            computerPlayerAction(j);
                        }
                        gameJFrame.adjustPosition(gameJFrame.turn); // Update display area
                        gameJFrame.turn = j;
                        break;
                    }
                    // Check if other players can Eat
                    if (Other_Algorithm.checkEat(gameJFrame.playerList.get(j), lastCard)&&(gameJFrame.turn+1)%4==j) {
                        if (j == 0) { // Player operation
                            gameJFrame.Other[1].setVisible(true);
                            timeWaitOther(setTime() / 2, 0);
                            gameJFrame.Other[1].setVisible(false);
                            if (actionTaken) {
                                gameJFrame.Chu[0].setVisible(true);
                                timeWait(setTime(), 0);
                                gameJFrame.Chu[0].setVisible(false);
                            }
                            actionTaken = false;
                        } else { // Computer operation
                            computerPlayerAction(j);
                        }
                        gameJFrame.adjustPosition(gameJFrame.turn); // Update display area
                        gameJFrame.turn = j;
                        break;
                    }
                }
                // If the player did not perform Peng, Gang, Eat, move to the next player
                if (!actionTaken) {
                    gameJFrame.turn = (gameJFrame.turn + 1) % 4;
                }
                sleep(1);
            }
        }
    }

    // Computer player's action
    private void computerPlayerAction(int playerIndex) {
        switch (playerIndex) {
            case 1 -> computer1();
            case 2 -> computer2();
            case 3 -> computer3();
        }
    }

    // Three computer players
    public void computer1() {
        if (DifficultyJFrame.isEasy) {
            timeWait(4, 1);
            AI_Algorithm.easyAI(gameJFrame, 1);
        } else if (DifficultyJFrame.isMiddle) {
            timeWait(3, 1);
            AI_Algorithm.easyAI(gameJFrame, 1);
        } else if (DifficultyJFrame.isHard) {
            timeWait(2, 1);
            AI_Algorithm.advancedAI(gameJFrame, 1);
        }
    }

    public void computer2() {
        if (DifficultyJFrame.isEasy) {
            timeWait(4, 2);
            AI_Algorithm.easyAI(gameJFrame, 2);
        } else if (DifficultyJFrame.isMiddle) {
            timeWait(3, 2);
            AI_Algorithm.advancedAI(gameJFrame, 2);
        } else if (DifficultyJFrame.isHard) {
            timeWait(2, 2);
            AI_Algorithm.advancedAI(gameJFrame, 2);
        }
    }

    public void computer3() {
        if (DifficultyJFrame.isEasy) {
            timeWait(4, 3);
            AI_Algorithm.easyAI(gameJFrame, 3);
        } else if (DifficultyJFrame.isMiddle) {
            timeWait(3, 3);
            AI_Algorithm.easyAI(gameJFrame, 3);
        } else if (DifficultyJFrame.isHard) {
            timeWait(2, 3);
            AI_Algorithm.advancedAI(gameJFrame, 3);
        }
    }

    // Draw card operation
    public static void addCards(int playerIndex) {
        // Get the new card
        MahjongCard newCard = gameJFrame.getMahjongCardList().get(gameJFrame.getNumb());
        // Add to graphical container
        gameJFrame.getContainer().add(newCard);
        // Add the card to the player's hand
        gameJFrame.getPlayerList().get(playerIndex).add(newCard);
        // Position the card
        Point destination;
        switch (playerIndex) {
            case 0 -> destination = new Point(950, 820);
            case 1 -> destination = new Point(1120, 270);
            case 2 -> destination = new Point(380, 200);
            case 3 -> destination = new Point(170, 700);
            default -> throw new IllegalStateException("Unexpected player index: " + playerIndex);
        }
        Other_Algorithm.move(newCard, new Point(650, 450), destination);
        if (playerIndex == 0) {
            newCard.setClickable(true);
            newCard.turnFront();
        }
        newCard.setVisible(true);
        // Move the new card to the top layer
        gameJFrame.getContainer().setComponentZOrder(newCard, 0);
        // Increment counter
        gameJFrame.changeNumb();
    }

    // Check if any card in the player's hand has been clicked
    public boolean checkClicked() {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(0);
        for (MahjongCard card : player) {
            if (card.isClicked()) {
                return true;
            }
        }
        return false;
    }

    // Check if any player has won
    public boolean win() {
        if (gameJFrame.getNumb() > 122) {
            String message = "Unfortunately, it's a draw";
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < gameJFrame.playerList.get(i % 4).size(); j++)
                    gameJFrame.playerList.get((i + 1) % 4).get(j).turnFront();
            }
            JOptionPane.showMessageDialog(gameJFrame, message);
            return true;
        }
        for (int i = 0; i < 4; i++) {
            if (Hu_Algorithm.checkHu(gameJFrame.playerList.get(i), gameJFrame.getLai())) {
                String message;
                if (i == 0) {
                    message = "Congratulations, you win!";
                } else {
                    message = "Congratulations to Computer " + i + ", you lose! Better luck next time.";
                }
                for (int j = 0; j < gameJFrame.playerList.get((i + 1) % 4).size(); j++)
                    gameJFrame.playerList.get((i + 1) % 4).get(j).turnFront();
                for (int j = 0; j < gameJFrame.playerList.get((i + 2) % 4).size(); j++)
                    gameJFrame.playerList.get((i + 2) % 4).get(j).turnFront();
                for (int j = 0; j < gameJFrame.playerList.get((i + 3) % 4).size(); j++)
                    gameJFrame.playerList.get((i + 3) % 4).get(j).turnFront();
                for (int j = 0; j < gameJFrame.playerList.get(i % 4).size(); j++)
                    gameJFrame.playerList.get(i % 4).get(j).turnFront();
                JOptionPane.showMessageDialog(gameJFrame, message);
                return true;
            }
        }
        return false;
    }

    // Set time limit based on the chosen game difficulty
    public int setTime() {
        if (DifficultyJFrame.isEasy) {
            return 25;
        }
        if (DifficultyJFrame.isMiddle) {
            return 15;
        }
        if (DifficultyJFrame.isHard) {
            return 10;
        }
        return 0;
    }

    // Update the action taken status
    public static void setAction(boolean action) {
        actionTaken = action;
    }
}
