package Algorithm;

import Objects.MahjongCard;

import java.util.ArrayList;

public class Hu_Algorithm {

    // 判断胡牌的主函数
    public static boolean canHu(ArrayList<MahjongCard> cards, MahjongCard wildCard) {
        int[] handCards = new int[34];
        int wildCardCount = 0;

        // 分类计数，区分普通牌和万能牌
        for (MahjongCard card : cards) {
            if (card.getName().equals(wildCard.getName())) {
                wildCardCount++;
            } else {
                int index = getCardIndex(card);
                handCards[index]++;
            }
        }

        return canHuWithWildCards(handCards, wildCardCount);
    }

    // 胡牌递归检查函数，使用万能牌
    private static boolean canHuWithWildCards(int[] handCards, int wildCardCount) {
        // 检查普通胡牌条件（不使用万能牌）
        if (checkHu(handCards, 0)) {
            return true;
        }

        // 如果有万能牌，尝试每种可能的替代
        if (wildCardCount > 0) {
            for (int i = 0; i < handCards.length; i++) {
                if (handCards[i] < 4) { // 确保不会超过4张牌的限制
                    handCards[i]++;
                    if (canHuWithWildCards(handCards, wildCardCount - 1)) {
                        return true;
                    }
                    handCards[i]--; // 回溯
                }
            }
        }
        return false;
    }

    // 检查是否可以胡牌，不考虑万能牌
    private static boolean checkHu(int[] handCards, int pairsCount) {
        if (pairsCount > 1) return false; // 只能有一个对子
        if (allTilesMatched(handCards, pairsCount)) return true;

        for (int i = 0; i < handCards.length; i++) {
            if (handCards[i] >= 2) {
                handCards[i] -= 2; // 尝试作为对子
                if (checkHu(handCards, pairsCount + 1)) {
                    return true;
                }
                handCards[i] += 2; // 回溯
            }

            if (i <= 31 && i % 9 < 7 && handCards[i] > 0 && handCards[i + 1] > 0 && handCards[i + 2] > 0) {
                // 尝试作为顺子
                handCards[i]--;
                handCards[i + 1]--;
                handCards[i + 2]--;
                if (checkHu(handCards, pairsCount)) {
                    return true;
                }
                handCards[i]++;
                handCards[i + 1]++;
                handCards[i + 2]++; // 回溯
            }
        }
        return false;
    }

    // 辅助函数：计算牌的索引
    private static int getCardIndex(MahjongCard card) {
        int type = Integer.parseInt(card.getName().substring(0, 1)); // 类型：万、条、筒
        int num = Integer.parseInt(card.getName().substring(2)); // 数字
        return (type - 1) * 9 + (num - 1);
    }

    // 判断所有牌是否已经匹配完毕
    private static boolean allTilesMatched(int[] handCards, int pairsCount) {
        for (int count : handCards) {
            if (count != 0) return false;
        }
        return pairsCount == 1; // 必须正好用完一对作为将
    }
}