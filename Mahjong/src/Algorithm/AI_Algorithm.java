package Algorithm;

import Game.GameJFrame;
import Game.PlayerOperation;
import Objects.MahjongCard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class AI_Algorithm {

    private static int cpg1Count = 0;
    private static int cpg2Count = 0;
    private static int cpg3Count = 0;
    private static boolean is1 = false;
    private static boolean is2 = false;
    private static boolean is3 = false;

    // Implementation of simple AI
    public static void easyAI(GameJFrame gameJFrame, int playerIndex) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);

        if (gameJFrame.getTurn() == playerIndex) {
            if (Hu_Algorithm.checkHu(player, gameJFrame.getLai())) {
                selfHu(gameJFrame, playerIndex);
            } else if (Other_Algorithm.checkDarkGang(player, player.get(player.size() - 1))) {
                darkGangCards(gameJFrame, playerIndex, player.get(player.size() - 1));
            } else {
                showCard(gameJFrame, playerIndex, true);
            }
        } else {
            MahjongCard current = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1);
            if (Hu_Algorithm.checkHu(player, gameJFrame.getLai())) {
                huCards(gameJFrame, playerIndex, current);
            } else if (Other_Algorithm.checkGang(player, current)) {
                gangCards(gameJFrame, playerIndex, current);
            } else if (Other_Algorithm.checkPeng(player, current)) {
                pengCards(gameJFrame, playerIndex);
            } else if (Other_Algorithm.checkEat(player, current)) {
                eatCards(gameJFrame, playerIndex);
            }
        }
    }

    public static void advancedAI(GameJFrame gameJFrame, int playerIndex) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);

        if (gameJFrame.getTurn() == playerIndex) {
            if (Hu_Algorithm.checkHu(player, gameJFrame.getLai())) {
                selfHu(gameJFrame, playerIndex);
            } else if (Other_Algorithm.checkDarkGang(player, player.get(player.size() - 1))) {
                darkGangCards(gameJFrame, playerIndex, player.get(player.size() - 1));
            } else {
                showCard(gameJFrame, playerIndex, false);
            }
        } else {
            MahjongCard current = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1);
            if (Hu_Algorithm.checkHu(player, gameJFrame.getLai())) {
                huCards(gameJFrame, playerIndex, current);
            } else if (Other_Algorithm.checkGang(player, current)) {
                gangCards(gameJFrame, playerIndex, current);
            } else if (Other_Algorithm.checkPeng(player, current)) {
                pengCards(gameJFrame, playerIndex);
            } else if (Other_Algorithm.checkEat(player, current)) {
                eatCards(gameJFrame, playerIndex);
            }
        }
    }



    // AI card playing method
    public static void showCard(GameJFrame gameJFrame, int playerIndex, boolean isEasy) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        MahjongCard cardToPlay = null;

        if (isEasy) {
            int i = player.size() - 2;
            while (i >= 0) {
                MahjongCard card = player.get(i);
                if (!card.getIfEat() && !card.getIfPeng() && !card.getIfGang() && !Objects.equals(card.getName(), gameJFrame.getLai().getName())) {
                    cardToPlay = card;
                    break;
                }
                i--;
            }
        } else {
            for (MahjongCard card : player) {
                if (!card.getIfEat() && !card.getIfPeng() && !card.getIfGang() && !Objects.equals(card.getName(), gameJFrame.getLai().getName())) {
                    if (cardToPlay == null || !isUsefulCard(card, player)) {
                        cardToPlay = card;
                    }
                }
            }
        }
        if (cardToPlay == null) {
            cardToPlay = player.get(player.size() - 1);
        }

        gameJFrame.getCurrentList().add(cardToPlay);
        player.remove(cardToPlay);

        Point point = calculateCardPosition(gameJFrame, playerIndex);
        cardToPlay.setLocation(point.x, point.y);
        Other_Algorithm.order(player);
        Other_Algorithm.rePosition(gameJFrame, player, playerIndex);
        gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1).turnFront();
    }

    // Determine if a card is useful
    private static boolean isUsefulCard(MahjongCard card, ArrayList<MahjongCard> player) {
        int color = card.getColor();
        int size = card.getValue();
        for (MahjongCard c : player) {
            if (c != card && c.getColor() == color && Math.abs(c.getValue() - size) <= 2) {
                return true;
            }
        }
        return false;
    }

    // Calculate the position of a card
    private static Point calculateCardPosition(GameJFrame gameJFrame, int playerIndex) {
        Point point = new Point();
        if (playerIndex == 1) {
            if (gameJFrame.num1 / 13 == 0) {
                point.x = 1040;
                point.y = 290 + gameJFrame.num1 * 35;
            } else {
                point.x = 1000;
                point.y = 290 + (gameJFrame.num1 - 13) * 35;
            }
            gameJFrame.num1++;
        } else if (playerIndex == 2) {
            if (gameJFrame.num2 / 13 == 0) {
                point.x = 820 - gameJFrame.num2 * 35;
                point.y = 340;
            } else {
                point.x = 820 - (gameJFrame.num2 - 13) * 35;
                point.y = 390;
            }
            gameJFrame.num2++;
        } else if (playerIndex == 3) {
            if (gameJFrame.num3 / 13 == 0) {
                point.x = 260;
                point.y = 290 + gameJFrame.num3 * 35;
            } else {
                point.x = 300;
                point.y = 290 + (gameJFrame.num3 - 13) * 35;
            }
            gameJFrame.num3++;
        }
        return point;
    }

    // AI Peng method
    public static void pengCards(GameJFrame gameJFrame, int playerIndex) {
        MahjongCard pengCard = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1);
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);

        if (Other_Algorithm.checkPeng(player, pengCard)) {
            player.add(pengCard);
            Other_Algorithm.order(player);
            int j = 0;
            for (MahjongCard card : player) {
                if (card.getName().equals(pengCard.getName())) {
                    Point point = calculateOperationPoint(playerIndex, j);
                    j++;
                    card.turnFront();
                    card.setClickable(false);
                    card.setIfPeng(true);
                    Other_Algorithm.move(card, card.getLocation(), point);
                }
            }
            incrementCpgCount();
            showCard(gameJFrame, playerIndex, true);
        }
    }

    // AI Gang method
    public static void gangCards(GameJFrame gameJFrame, int playerIndex, MahjongCard gangCard) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);

        if (Other_Algorithm.checkGang(player, gangCard)) {
            player.add(gangCard);
            PlayerOperation.addCards(playerIndex);
            int j = 0;
            for (MahjongCard card : player) {
                if (card.getName().equals(gangCard.getName())) {
                    Point point = calculateOperationPoint(playerIndex, j);
                    Other_Algorithm.move(card, card.getLocation(), point);
                    card.turnFront();
                    card.setClickable(false);
                    card.setIfGang(true);
                    j++;
                }
            }
            incrementCpgCount();
            showCard(gameJFrame, playerIndex, true);
        }
    }

    // AI Dark Gang method
    public static void darkGangCards(GameJFrame gameJFrame, int playerIndex, MahjongCard gangCard) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        if (Other_Algorithm.checkDarkGang(player, gangCard)) {
            int j = 0;
            for (MahjongCard card : player) {
                if (card.getName().equals(gangCard.getName())) {
                    Point point = calculateOperationPoint(playerIndex, j);
                    Other_Algorithm.move(card, card.getLocation(), point);
                    card.turnFront();
                    card.setClickable(false);
                    card.setIfGang(true);
                    j++;
                }
            }
            incrementCpgCount();
            PlayerOperation.addCards(playerIndex);
            showCard(gameJFrame, playerIndex, true);
        }
    }

    // AI Eat method
    public static void eatCards(GameJFrame gameJFrame, int playerIndex) {
        MahjongCard chiCard = gameJFrame.getCurrentList().get(gameJFrame.getCurrentList().size() - 1);
        int color = chiCard.getColor();
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);

        if (Other_Algorithm.checkEat(player, chiCard)) {
            player.add(chiCard);
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
                    if (card.getColor() == color && card.getValue() == chiCard.getValue() + 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                    else if (card.getColor() == color && card.getValue() == chiCard.getValue() + 2) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                } else if (Objects.equals(Other_Algorithm.getChiSituation(), "OXO")) {
                    if (card.getColor() == color && card.getValue() == chiCard.getValue() - 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                    else if (card.getColor() == color && card.getValue() == chiCard.getValue() + 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
                } else if (Objects.equals(Other_Algorithm.getChiSituation(), "OOX")) {
                    if (card.getColor() == color && card.getValue() == chiCard.getValue() - 1) {
                        card.setIfEat(true);
                        sequence.add(card);
                    }
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
            Other_Algorithm.order(sequence);
            for (MahjongCard card : sequence) {
                Other_Algorithm.move(card, card.getLocation(), calculateOperationPoint(playerIndex, j));
                card.turnFront();
                card.setClickable(false);
                card.setIfEat(true);
                j++;
            }
            incrementCpgCount();
            showCard(gameJFrame,playerIndex,true);
        }
    }
    // AI Hu method
    public static void huCards(GameJFrame gameJFrame, int playerIndex, MahjongCard huCard) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        player.add(huCard);
        Other_Algorithm.order(player);
        Other_Algorithm.rePosition(gameJFrame, player, playerIndex);

        for (MahjongCard card : player) {
            card.turnFront();
        }
    }

    public static void selfHu(GameJFrame gameJFrame, int playerIndex) {
        ArrayList<MahjongCard> player = gameJFrame.getPlayerList().get(playerIndex);
        Other_Algorithm.order(player);
        Other_Algorithm.rePosition(gameJFrame, player, playerIndex);

        for (MahjongCard card : player) {
            card.turnFront();
        }
    }

    // Calculate the position for Peng, Gang, and Chi
    private static Point calculateOperationPoint(int playerIndex, int j) {
        Point point = new Point();
        if (playerIndex == 1) {
            point.x = 1158 - cpg1Count * 40;
            point.y = 630 + j * 35;
            is1 = true;
        } else if (playerIndex == 2) {
            point.x = 850 + j * 35;
            point.y = 152 + cpg2Count * 50;
            is2 = true;
        } else if (playerIndex == 3) {
            point.x = 132 + cpg3Count * 40;
            point.y = 120 + j * 35;
            is3 = true;
        }
        return point;
    }

    // Increment the count for Peng, Gang, and Chi
    private static void incrementCpgCount() {
        if (is1) {
            cpg1Count++;
            is1 = false;
        }
        if (is2) {
            cpg2Count++;
            is2 = false;
        }
        if (is3) {
            cpg3Count++;
            is3 = false;
        }
    }

}
