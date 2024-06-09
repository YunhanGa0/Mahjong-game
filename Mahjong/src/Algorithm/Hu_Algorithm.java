package Algorithm;

import Objects.MahjongCard;

import java.util.ArrayList;

// Algorithm to determine if a hand is a winning hand
public class Hu_Algorithm {

    static int g_NeedHunCount;

    // Sort the hand (specifically for hands that have been split)
    public static void order(ArrayList<MahjongCard> list) {
        int t = Integer.parseInt(list.get(0).getName().substring(0, 1));
        int n = list.size();
        if (n == 0) {
            return;
        }
        if (t <= 2) { // If it is a number tile
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - 1 - i; j++) {
                    if (list.get(j).getValue() > list.get(j + 1).getValue()) {
                        MahjongCard temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
        } else { // If it is a wind tile
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - 1 - i; j++) {
                    if (list.get(j).getColor() >list.get(j + 1).getColor()) {
                        MahjongCard temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                        swapped = true;
                    }
                }
                if (!swapped) break;
            }
        }
    }

    // Separate the Mahjong tiles by suit (0 is joker, 1 is characters, 2 is bamboos, 3 is dots, 4 is winds)
    private static ArrayList<ArrayList<MahjongCard>> separateCards(ArrayList<MahjongCard> cards, MahjongCard Lai) {
        ArrayList<ArrayList<MahjongCard>> divided = new ArrayList<>();
        // Separate the hand by suit
        for (int i = 0; i < 5; i++) {
            divided.add(new ArrayList<>());
        }
        // Get the suit and value of Lai
        int ht = Lai.getColor();
        int hv = Lai.getValue();
        // Traverse the current hand and separate
        for (MahjongCard card : cards) {
            // Get the suit and value of the current tile
            int t = card.getColor();
            int v = card.getValue();
            if (t == 0 && (ht != t || hv != v)) { // Characters
                divided.get(1).add(card);
                order(divided.get(1));
            } else if (t == 1 && (ht != t || hv != v)) { // Bamboos
                divided.get(2).add(card);
                order(divided.get(2));
            } else if (t == 2 && (ht != t || hv != v)) { // Dots
                divided.get(3).add(card);
                order(divided.get(3));
            } else if (t >= 3 && (ht != t || hv != v)) { // Winds
                divided.get(4).add(card);
                order(divided.get(4));
            } else if (ht == t && hv == v) { // Lai
                divided.get(0).add(card);
                order(divided.get(0));
            }
        }
        return divided;
    }

    // Check if two tiles form a pair
    private static boolean test2Combine(MahjongCard card1, MahjongCard card2) {
        int t1 = card1.getColor();
        int t2 = card2.getColor();
        int v1 = card1.getValue();
        int v2 = card2.getValue();
        if (t1 <= 2) {
            return t1 == t2 && v1 == v2;
        } else {
            return t1 == t2;
        }
    }

    // Check if three tiles can form a sequence or a triplet
    private static boolean test3Combine(MahjongCard card1, MahjongCard card2, MahjongCard card3) {
        // If the three tiles have different types, return false
        int t1 = card1.getColor();
        int t2 = card2.getColor();
        int t3 = card3.getColor();
        if (t1 != t2 || t1 != t3) {
            return false;
        }
        // If the types are the same, continue checking
        int v1 = card1.getValue();
        int v2 = card2.getValue();
        int v3 = card3.getValue();
        // If they form a triplet
        if (v1 == v2 && v1 == v3) {
            return true;
        }
        // If it is a wind tile, return false
        if (t3 >= 3) {
            return false;
        }
        // Check if they form a sequence
        return (v1 + 1) == v2 && (v1 + 2) == v3;
    }

    // Get the number of jokers needed to complete the hand
    private static int getModNeedNum(int len, boolean isJiang) {
        // If the length is less than or equal to 0
        if (len <= 0) {
            return 0;
        }
        // Calculate the remainder when divided by 3
        int modNum = len % 3;
        int[] needNumArr;
        if (isJiang) {
            needNumArr = new int[]{2, 1, 0};
        } else {
            needNumArr = new int[]{0, 2, 1};
        }
        return needNumArr[modNum];
    }

    // Recursive function to calculate the number of jokers needed in the subarray
    private static void getNeedHunInSub(ArrayList<MahjongCard> subCards, int numOfLai) {
        // If it is already determined that no jokers are needed, return directly
        if (g_NeedHunCount == 0) {
            return;
        }
        // Get the length of the subarray
        int len = subCards.size();
        // If the current number of jokers plus the minimum number of jokers needed already exceeds the current record of the fewest jokers needed, return
        if (numOfLai + getModNeedNum(len, false) >= g_NeedHunCount) {
            return;
        }
        // Handle different cases based on the length of the subarray
        if (len == 0) {
            // If the subarray is empty, update the fewest jokers needed to smaller of the current count and the existing number of jokers
            g_NeedHunCount = Math.min(numOfLai, g_NeedHunCount);
        } else if (len == 1) {
            // If the subarray length is 1, at least two jokers are needed, update the fewest jokers needed
            g_NeedHunCount = Math.min(numOfLai + 2, g_NeedHunCount);
        } else if (len == 2) {
            // If the subarray length is 2, analyze the situation of the two tiles
            int t1 = subCards.get(0).getColor(); // Get the type of the first tile
            int v1 = subCards.get(0).getValue(); // Get the value of the first tile
            int t2 = subCards.get(1).getColor(); // Get the type of the second tile
            int v2 = subCards.get(1).getValue(); // Get the value of the second tile
            if (t1 >= 3) {
                // If it is a wind tile
                if (t1 == t2) {
                    // If the two wind tiles are the same, one joker is needed
                    g_NeedHunCount = Math.min(numOfLai + 1, g_NeedHunCount);
                }
            } else if ((v2 - v1) < 3) {
                // If it is a number tile and the difference in value between the two tiles is less than 3, also consider needing one joker
                g_NeedHunCount = Math.min(numOfLai + 1, g_NeedHunCount);
            }
        } else {
            // Handle the case where the array length is greater than 2
            int t = subCards.get(0).getColor(); // Type of the tile
            int v0 = subCards.get(0).getValue(); // Value of the first tile
            int arrLen = subCards.size();
            for (int i = 1; i < arrLen; i++) {
                // If the current number of jokers plus the minimum number of jokers needed already exceeds the current record of the fewest jokers needed, exit the loop
                if (numOfLai + getModNeedNum(len - 3, false) >= g_NeedHunCount) {
                    break;
                }
                // Get the value of the current tile
                int v1 = subCards.get(i).getValue();
                // If the difference in value between the current tile and the first tile is greater than 1, exit the loop
                if (v1 - v0 > 1) {
                    break;
                }
                // If the next two tiles are the same, skip these tiles and continue the loop
                if (i + 2 < arrLen && subCards.get(i + 2).getValue() == v1) {
                    continue;
                }
                // Check the next tile
                if (i + 1 < arrLen) {
                    // Get the first, current, and next tile
                    MahjongCard tmp1 = subCards.get(0);
                    MahjongCard tmp2 = subCards.get(i);
                    MahjongCard tmp3 = subCards.get(i + 1);
                    // Check if the three tiles can form a valid combination
                    if (test3Combine(tmp1, tmp2, tmp3)) { // If they can
                        // Try removing the three tiles and recursively check
                        subCards.remove(tmp1);
                        subCards.remove(tmp2);
                        subCards.remove(tmp3);
                        // First remove these three tiles, then check the number of jokers needed for the remaining tiles
                        getNeedHunInSub(subCards, numOfLai);
                        // Then add the removed tiles back
                        subCards.add(tmp1);
                        subCards.add(tmp2);
                        subCards.add(tmp3);
                        // Reorder the subarray
                        order(subCards);
                    }
                }
            }
            // Get the value of the second tile
            int t1 = subCards.get(1).getColor();
            // Further check the number of jokers needed
            if (numOfLai + getModNeedNum(len - 2, false) + 1 < g_NeedHunCount) {
                if (t >= 3) { // If it is a wind tile
                    if (t == t1) {
                        MahjongCard tmp1 = subCards.get(0);
                        MahjongCard tmp2 = subCards.get(1);
                        subCards.remove(tmp1);
                        subCards.remove(tmp2);
                        getNeedHunInSub(subCards, numOfLai + 1);
                        subCards.add(tmp1);
                        subCards.add(tmp2);
                        order(subCards);
                    }
                } else { // If it is a number tile
                    arrLen = subCards.size();
                    for (int i = 0; i < arrLen; i++) {
                        if (numOfLai + getModNeedNum(len - 2, false) + 1 >= g_NeedHunCount) {
                            break;
                        }
                        int v4 = subCards.get(i).getValue(); // Value of the current tile
                        // If the current tile and the next tile are the same, skip the current tile and continue the loop
                        if ((i + 1) != arrLen) {
                            int v5 = subCards.get(i + 1).getValue();
                            if (v4 == v5) {
                                continue;
                            }
                        }
                        int diff = v4 - v0; // Difference in value between the current tile and the first tile
                        // If the difference is less than 3, it means they can form a sequence or a triplet
                        if (diff < 3) {
                            MahjongCard tmp1 = subCards.get(0);
                            MahjongCard tmp2 = subCards.get(i);
                            subCards.remove(tmp1);
                            subCards.remove(tmp2);
                            getNeedHunInSub(subCards, numOfLai + 1);
                            subCards.add(tmp1);
                            subCards.add(tmp2);
                            order(subCards);
                            // If the difference is greater than 1, exit
                            if (diff >= 1) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            // Check if it is possible to form a valid combination by removing the first tile and adding two jokers
            if (numOfLai + getModNeedNum(len - 1, false) + 2 < g_NeedHunCount) {
                MahjongCard tmp = subCards.get(0);
                subCards.remove(tmp);
                getNeedHunInSub(subCards, numOfLai + 2);
                subCards.add(tmp);
                order(subCards);
            }
        }
    }

    // Check if the hand can be a winning hand
    private static boolean ifHu(int numOfLai, ArrayList<MahjongCard> cards) {
        ArrayList<MahjongCard> tmpArr = new ArrayList<>();
        for (MahjongCard card : cards) {
            tmpArr.add(card);
        }
        int arrLen = tmpArr.size();
        if (arrLen == 0) {
            return numOfLai >= 2;
        }
        if (numOfLai < getModNeedNum(arrLen, true)) {
            return false;
        }
        for (int i = 0; i < arrLen; i++) {
            if (i == arrLen - 1) {
                if (numOfLai > 0) {
                    MahjongCard tmp = tmpArr.get(i);
                    numOfLai--;
                    tmpArr.remove(tmpArr.get(i));
                    g_NeedHunCount = 4;
                    getNeedHunInSub(tmpArr, 0);
                    if (g_NeedHunCount <= numOfLai) {
                        return true;
                    }
                    numOfLai++;
                    tmpArr.add(tmp);
                    Other_Algorithm.order(tmpArr);
                }
            } else {
                if (i + 2 == arrLen || tmpArr.get(i).getValue() != tmpArr.get(i + 2).getValue()) {
                    if (test2Combine(tmpArr.get(i), tmpArr.get(i + 1))) {
                        MahjongCard tmp1 = tmpArr.get(i);
                        MahjongCard tmp2 = tmpArr.get(i + 1);
                        tmpArr.remove(tmp1);
                        tmpArr.remove(tmp2);
                        g_NeedHunCount = 4;
                        getNeedHunInSub(tmpArr, 0);
                        if (g_NeedHunCount <= numOfLai) {
                            return true;
                        }
                        tmpArr.add(tmp1);
                        tmpArr.add(tmp2);
                        order(tmpArr);
                    }
                    if (numOfLai > 0 && tmpArr.get(i).getValue() != tmpArr.get(i + 1).getValue()) {
                        numOfLai--;
                        MahjongCard tmp = tmpArr.get(i);
                        tmpArr.remove(tmp);
                        g_NeedHunCount = 4;
                        getNeedHunInSub(tmpArr, 0);
                        if (g_NeedHunCount <= numOfLai) {
                            return true;
                        }
                        numOfLai++;
                        tmpArr.add(tmp);
                        order(tmpArr);
                    }
                }
            }
        }
        return false;
    }

    // Check if the player can win with the discarded tile
    public static boolean checkHu(ArrayList<MahjongCard> cards, MahjongCard card, MahjongCard Lai) {
        ArrayList<MahjongCard> tepArr = new ArrayList<>();
        for (MahjongCard c : cards) {
            tepArr.add(c);
        }
        // Add the current Mahjong tile
        tepArr.add(card);
        if (tepArr.size() < 14) {
            return false;
        }
        ArrayList<ArrayList<MahjongCard>> sptArr = separateCards(tepArr, Lai);
        int curHunNum = sptArr.get(0).size();
        if (curHunNum > 3) {
            return true;
        }
        int[] ndHunArr = new int[4];
        for (int i = 1; i < 5; i++) {
            g_NeedHunCount = 4;
            getNeedHunInSub(sptArr.get(i), 0);
            ndHunArr[i - 1] = g_NeedHunCount;
        }
        boolean isHu;
        // Winning with characters as the pair
        int ndHunAll = ndHunArr[1] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(1));
            if (isHu) {
                return true;
            }
        }
        // Winning with bamboos as the pair
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(2));
            if (isHu) {
                return true;
            }
        }
        // Winning with dots as the pair
        ndHunAll = ndHunArr[0] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(3));
            if (isHu) {
                return true;
            }
        }
        // Winning with winds as the pair
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[2];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(4));
            return isHu;
        }
        return false;
    }

    // Check if the player's hand can win
    public static boolean checkHu(ArrayList<MahjongCard> cards, MahjongCard Lai) {
        ArrayList<MahjongCard> tepArr = new ArrayList<>();
        for (MahjongCard card : cards) {
            tepArr.add(card);
        }
        if (tepArr.size() < 14) { // If the hand size is less than 14, it does not meet the winning conditions
            return false;
        }
        ArrayList<ArrayList<MahjongCard>> sptArr = separateCards(tepArr, Lai);
        ArrayList<ArrayList<MahjongCard>> temptArr = separateCards(tepArr, Lai);
        int curHunNum = sptArr.get(0).size();
        if (curHunNum > 3) {
            return true;
        }
        int[] ndHunArr = new int[4];
        for (int i = 1; i < 5; i++) {
            g_NeedHunCount = 4;
            getNeedHunInSub(temptArr.get(i), 0);
            ndHunArr[i - 1] = g_NeedHunCount;
        }
        boolean isHu;
        // Winning with characters as the pair
        int ndHunAll = ndHunArr[1] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(1));
            if (isHu) {
                return true;
            }
        }
        // Winning with bamboos as the pair
        ndHunAll = ndHunArr[0] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(2));
            if (isHu) {
                return true;
            }
        }
        // Winning with dots as the pair
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(3));
            if (isHu) {
                return true;
            }
        }
        // Winning with winds as the pair
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[2];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = ifHu(hasNum, sptArr.get(4));
            return isHu;
        }
        return false;
    }
}
