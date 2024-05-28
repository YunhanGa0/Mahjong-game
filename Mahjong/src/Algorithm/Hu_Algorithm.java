package Algorithm;

import Objects.MahjongCard;

import java.util.ArrayList;
import java.util.List;

//判断是否胡牌的算法
public class Hu_Algorithm {
    static int g_NeedHunCount;

    public static void order(ArrayList<MahjongCard> list) {
        list.sort((o1, o2) -> {
            //获得最前面的数字，判断花色
            int a1 = Integer.parseInt(o1.getName().substring(0, 1));
            int a2 = Integer.parseInt(o2.getName().substring(0, 1));

            //获取后面的值
            int b1 = Integer.parseInt(o1.getName().substring(2));
            int b2 = Integer.parseInt(o2.getName().substring(2));

            //如果牌的花色一样，则按照价值排序
            if ((a1 - a2) == 0) {
                return b1 - b2;
            } else {
                return a1 - a2;
            }
        });
    }

    // 按照花色分离麻将数组（0是混，1是万，2是条，3是筒，4是字）
    private static ArrayList<ArrayList<MahjongCard>> separateArr(ArrayList<MahjongCard> mjArr, MahjongCard hunMj) {
        ArrayList<ArrayList<MahjongCard>> reArr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            reArr.add(new ArrayList<>());
        }
        int ht =  Integer.parseInt(hunMj.getName().substring(0, 1));
        int hv = Integer.parseInt(hunMj.getName().substring(2));
        for (MahjongCard mj : mjArr) {
            int t = Integer.parseInt(mj.getName().substring(0, 1));
            int v = Integer.parseInt(mj.getName().substring(2));
            if(t==0&&ht!=t){
                reArr.get(1).add(mj);
                order(reArr.get(1));
            }else if(t==1&&ht!=t){
                reArr.get(2).add(mj);
                order(reArr.get(2));
            }else if(t==2&&ht!=t){
                reArr.get(3).add(mj);
                order(reArr.get(3));
            }else if(t>=3&&ht!=t){
                reArr.get(4).add(mj);
                order(reArr.get(4));
            }else if(ht==t&&hv==v){
                reArr.get(0).add(mj);
                order(reArr.get(0));
            }
        }
        return reArr;
    }

    // 检查两张牌是否能组成有效组合
    private static boolean test2Combine(MahjongCard MahjongCard1, MahjongCard MahjongCard2) {
        int t1 = Integer.parseInt(MahjongCard1.getName().substring(0, 1));
        int t2 = Integer.parseInt(MahjongCard2.getName().substring(0, 1));
        int v1 = Integer.parseInt(MahjongCard1.getName().substring(2));
        int v2 = Integer.parseInt(MahjongCard2.getName().substring(2));
        return t1 == t2 && v1 == v2;
    }

    // 检查三张牌是否能组成有效组合
    private static boolean test3Combine(MahjongCard MahjongCard1, MahjongCard MahjongCard2,MahjongCard MahjongCard3) {
        //类型不同返回否
        int t1 = Integer.parseInt(MahjongCard1.getName().substring(0, 1));
        int t2 = Integer.parseInt(MahjongCard2.getName().substring(0, 1));
        int t3 = Integer.parseInt(MahjongCard3.getName().substring(0, 1));
        if (t1 != t2 || t1 != t3) {
            return false;
        }
        //如果类型一样，继续检测
        int v1 = Integer.parseInt(MahjongCard1.getName().substring(2));
        int v2 = Integer.parseInt(MahjongCard2.getName().substring(2));
        int v3 = Integer.parseInt(MahjongCard3.getName().substring(2));
        //如果组成刻子
        if (v1 == v2 && v1 == v3) {
            return true;
        }
        //不是条万筒返回否
        if (t3 == 3) {
            return false;
        }
        //检查是否为顺子
        return (v1 + 1) == v2 && (v1 + 2) == v3;
    }

    // 获取需要赖子的数量
    private static int getModNeedNum(int arrLem, boolean isJiang) {
        if (arrLem <= 0){
            return 0;
        }
        int modNum = arrLem % 3;
        int[] needNumArr = {0, 2, 1};
        if (isJiang) {
            needNumArr = new int[]{2, 1, 0};
        }
        return needNumArr[modNum];
    }

    // 递归函数计算子数组中需要的赖子数
    private static void getNeedHunInSub(ArrayList<MahjongCard> subArr, int hNum) {
        // 如果已确定不需要赖子，直接返回
        if (g_NeedHunCount == 0) {
            return;
        }
        // 获取子数组长度
        int lArr = subArr.size();

        // 如果当前赖子数加上按最小组合需要的赖子数已经超过了当前记录的最少赖子数，返回
        if (hNum + getModNeedNum(lArr, false) >= g_NeedHunCount) {
            return;
        }
        // 根据子数组的长度处理不同情况
        if (lArr == 0) {
            // 如果子数组为空，更新最少赖子数为当前计数和已有赖子数的较小值
            g_NeedHunCount = Math.min(hNum, g_NeedHunCount);
        } else if (lArr == 1) {
            // 如果子数组长度为1，至少需要两个赖子，更新最少赖子数
            g_NeedHunCount = Math.min(hNum + 2, g_NeedHunCount);
        } else if (lArr == 2) {
            // 如果子数组长度为2，分析两张牌的情况
            int t = Integer.parseInt(subArr.get(0).getName().substring(0, 1)); // 获取第一张牌的类型
            int v0 = Integer.parseInt(subArr.get(0).getName().substring(2)) ; // 获取第一张牌的数值
            int v1 = Integer.parseInt(subArr.get(1).getName().substring(2)) ; // 获取第二张牌的数值
            if (t >=3) {
                // 如果是字牌
                if (v0 == v1) {
                    // 如果两张字牌相同，则需要一个赖子
                    g_NeedHunCount = Math.min(hNum + 1, g_NeedHunCount);
                }
            } else if ((v1 - v0) < 3) {
                // 如果是数牌且两张牌的数值差小于3，也认为需要一个赖子
                g_NeedHunCount = Math.min(hNum + 1, g_NeedHunCount);
            }
        } else if(lArr>=3){
            // 处理数组长度大于2的情况
            int t = Integer.parseInt(subArr.get(0).getName().substring(0, 1)); // 牌的类型
            int v0 = Integer.parseInt(subArr.get(0).getName().substring(2)); // 第一张牌的数值
            int arrLen=subArr.size();
            for (int i = 1; i < arrLen; i++) {
                if (hNum + getModNeedNum(lArr - 3, false) >= g_NeedHunCount) {
                    break;
                }
                int v1 = Integer.parseInt(subArr.get(i).getName().substring(2));
                if (v1 - v0 > 1) {
                    break;
                }
                if (i + 2 < arrLen && Integer.parseInt(subArr.get(i + 2).getName().substring(2)) == v1) {
                    continue;
                }
                if (i + 1 < arrLen) {
                    MahjongCard tmp1 = subArr.get(0);
                    MahjongCard tmp2 = subArr.get(i);
                    MahjongCard tmp3 = subArr.get(i + 1);
                    if (test3Combine(tmp1, tmp2, tmp3)) {
                        // 尝试移除三张牌并递归检查
                        subArr.remove(tmp1);
                        subArr.remove(tmp2);
                        subArr.remove(tmp3);
                        getNeedHunInSub(subArr, hNum);
                        subArr.add(tmp1);
                        subArr.add(tmp2);
                        subArr.add(tmp3);
                        order(subArr);
                    }
                }
            }
            int v1 = Integer.parseInt(subArr.get(1).getName().substring(2));
            // 进一步检查需要赖子的情况
            if (hNum + getModNeedNum(lArr - 2, false) + 1 < g_NeedHunCount) {
                if (t == 4 ) {
                    if(v0 == v1) {
                        MahjongCard tmp1 = subArr.get(0);
                        MahjongCard tmp2 = subArr.get(1);
                        subArr.remove(tmp1);
                        subArr.remove(tmp2);
                        getNeedHunInSub(subArr, hNum + 1);
                        subArr.add(tmp1);
                        subArr.add(tmp2);
                        order(subArr);
                    }
                }
                else{
                    arrLen=subArr.size();
                    for(int i=0;i<arrLen;i++){
                        if(hNum + getModNeedNum(lArr-2,false) +1  >= g_NeedHunCount){
                            break;
                        }
                        int v4 = Integer.parseInt(subArr.get(i).getName().substring(2));
                        if((i+1)!=arrLen){
                            int v5 = Integer.parseInt(subArr.get(i+1).getName().substring(2));
                            if(v4==v5){
                                continue;
                            }
                        }
                        int diff=v4-v0;
                        if(diff<3){
                            MahjongCard tmp1 = subArr.get(0);
                            MahjongCard tmp2 = subArr.get(i);
                            subArr.remove(tmp1);
                            subArr.remove(tmp2);
                            getNeedHunInSub(subArr, hNum + 1);
                            subArr.add(tmp1);
                            subArr.add(tmp2);
                            order(subArr);
                        }
                        if(diff>=1){
                            break;
                        }
                    }
                }
            }
            if (hNum + getModNeedNum(lArr - 1, false) + 2 < g_NeedHunCount) {
                MahjongCard tmp = subArr.get(0);
                subArr.remove(tmp);
                getNeedHunInSub(subArr, hNum + 2);
                subArr.add(tmp);
                order(subArr);
            }
        }
    }

    // 检查牌组是否能胡牌
    private static boolean canHu(int hunNum, ArrayList<MahjongCard> arr) {
        ArrayList<MahjongCard> tmpArr = new ArrayList<>(arr);
        int arrLen = tmpArr.size();
        if (arrLen == 0) {
            return hunNum >= 2;
        }
        if (hunNum < getModNeedNum(arrLen, true)) {
            return false;
        }
        for (int i = 0; i < arrLen; i++) {
            if (i == arrLen - 1) {
                if (hunNum > 0) {
                    MahjongCard tmp = tmpArr.get(i);
                    hunNum--;
                    tmpArr.remove(tmpArr.get(i));
                    g_NeedHunCount = 4;
                    getNeedHunInSub(tmpArr, 0);
                    if (g_NeedHunCount <= hunNum) {
                        return true;
                    }
                    hunNum++;
                    tmpArr.add(tmp);
                    Other_Algorithm.order(tmpArr);
                }
            } else {
                if (i + 2 == arrLen || Integer.parseInt(tmpArr.get(i).getName().substring(2)) != Integer.parseInt(tmpArr.get(i + 2) .getName().substring(2))) {
                    if (test2Combine(tmpArr.get(i), tmpArr.get(i + 1))) {
                        MahjongCard tmp1 = tmpArr.get(i);
                        MahjongCard tmp2 = tmpArr.get(i + 1);
                        tmpArr.remove(tmp1);
                        tmpArr.remove(tmp2);
                        g_NeedHunCount = 4;
                        getNeedHunInSub(tmpArr, 0);
                        if (g_NeedHunCount<= hunNum) {
                            return true;
                        }
                        tmpArr.add(tmp1);
                        tmpArr.add(tmp2);
                        Other_Algorithm.order(tmpArr);
                    }
                    if (hunNum > 0 && Integer.parseInt(tmpArr.get(i).getName().substring(2)) != Integer.parseInt(tmpArr.get(i + 1)  .getName().substring(2))){
                        hunNum--;
                        MahjongCard tmp = tmpArr.get(i);
                        tmpArr.remove(tmp);
                        g_NeedHunCount= 4;
                        getNeedHunInSub(tmpArr, 0);
                        if (g_NeedHunCount<= hunNum) {
                            return true;
                        }
                        hunNum++;
                        tmpArr.add(tmp);
                        Other_Algorithm.order(tmpArr);
                    }
                }
            }
        }
        return false;
    }

    // 主函数：检查是否胡牌
    // 测试是否胡牌
    public static boolean CheckHu( ArrayList<MahjongCard> mjArr,MahjongCard mj, MahjongCard hunMj) {
        ArrayList<MahjongCard> tmpArr = new ArrayList<>(mjArr);
        //加入当前麻将
        tmpArr.add(mj);
        ArrayList<ArrayList<MahjongCard>> sptArr = separateArr(tmpArr, hunMj);
        int curHunNum = sptArr.get(0).size();
        if (curHunNum > 3) {
            return true;
        }
        int[] ndHunArr = new int[4];
        for (int i = 1; i < 5; i++) {
            g_NeedHunCount= 4;
            getNeedHunInSub(sptArr.get(i), 0);
            ndHunArr[i - 1] = g_NeedHunCount;
        }
        boolean isHu;
        // 将在万中
        int ndHunAll = ndHunArr[1] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(1));
            if (isHu) {
                return true;
            }
        }
        // 将在饼中
        ndHunAll = ndHunArr[0] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(2));
            if (isHu) {
                return true;
            }
        }
        // 将在条中
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(3));
            if (isHu) {
                return true;
            }
        }
        // 将在风中
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[2];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(4));
            return isHu;
        }
        return false;
    }

    public static boolean CheckHu(ArrayList<MahjongCard> mjArr, MahjongCard hunMj) {
        ArrayList<MahjongCard> tmpArr = new ArrayList<>(mjArr);
        ArrayList<ArrayList<MahjongCard>> sptArr = separateArr(tmpArr, hunMj);
        int curHunNum = sptArr.get(0).size();
        if (curHunNum > 3) {
            return true;
        }
        int[] ndHunArr = new int[4];
        for (int i = 1; i < 5; i++) {
            g_NeedHunCount= 4;
            getNeedHunInSub(sptArr.get(i), 0);
            ndHunArr[i - 1] = g_NeedHunCount;
        }
        boolean isHu;
        // 将在万中
        int ndHunAll = ndHunArr[1] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(1));
            if (isHu) {
                return true;
            }
        }
        // 将在饼中
        ndHunAll = ndHunArr[0] + ndHunArr[2] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(2));
            if (isHu) {
                return true;
            }
        }
        // 将在条中
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[3];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(3));
            if (isHu) {
                return true;
            }
        }
        // 将在风中
        ndHunAll = ndHunArr[0] + ndHunArr[1] + ndHunArr[2];
        if (ndHunAll <= curHunNum) {
            int hasNum = curHunNum - ndHunAll;
            isHu = canHu(hasNum, sptArr.get(4));
            return isHu;
        }
        return false;
    }


}
