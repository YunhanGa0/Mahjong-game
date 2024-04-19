package Algorithm;

import java.util.ArrayList;
import java.util.List;

//判断是否胡牌的算法
public class Hu_Algorithm {
        /**
         * 判断手牌是否可以胡牌,使用选将拆分法来实现
         * @param handCards
         * <pre>
         * 手牌的格式必须转换为如下数据格式,数组的下标表示牌的类型共34种类型,数组的值表示这个类型牌的数量<br>
         * cards[0]                   =                 2<br>
         *      1万                                     共有2张 <br>
         * int[] cards = {<br>
         *          2,0,0,0,0,0,0,0,0, //-- 1-9万<br>
         *          0,0,0,0,0,0,0,0,0, //-- 1-9条<br>
         *          0,0,0,0,0,0,0,0,0, //-- 1-9筒<br>
         *          0,0,0,0,0,0,0 //- 东南西北中发白<br>
         *     };
         *</pre>
         *
         * @return true可以胡  false
         */
        public static boolean checkHandCardsCanWin(int[] handCards) {

            int[] cards = new int[34];
            int cardsCount = 0;
            for (int i = 0; i < 34; ++i) {
                cards[i] = handCards[i];
                cardsCount+= handCards[i];
            }
            //当手牌数量不满足3n+2时不构成胡牌条件
            if (!(cardsCount >= 2 && cardsCount <= 14 && (cardsCount - 2) % 3 == 0)) {
                return false;
            }
            /*
             * 用来存储所有可以做将的牌型
             */
            List<Integer> eyeList = new ArrayList<>();
            /*
             *   遍历所有的牌类型，找到牌的数量大于等于2的牌
             */
            for (int i = 0; i < 34; ++i) {
                //这种类型牌最小值
                int min = (i / 9) * 9;
                //为种类型牌最大值
                int max = min + 8;
                //字牌的特殊处理,字牌的个数为7而其它类型数量为9
                if (max == 35) max = 33;
                if (cards[i] == 1 &&
                        (i - 2 < min || cards[i - 2] == 0) &&
                        (i - 1 < min || cards[i - 1] == 0) &&
                        (i + 1 > max || cards[i + 1] == 0) &&
                        (i + 2 > max || cards[i + 2] == 0)) {
                    //这种散牌直接无法胡,除非有赖子牌的情况下
                    return false;
                }
                if (cards[i] >= 2) {
                    eyeList.add(i);
                }
            }

            /*
             * 遍历所有的将来判断是否可以胡
             */
            boolean win = false;

            /*
             * 如果没有任何的将直接断定无法胡牌
             */
            if (eyeList.size() == 0){
                return false;
            }else{
                int[] checkedCache = {0, 0, 0, 0};
                for (int i = 0; i < eyeList.size(); i++) {
                    //将牌所在牌数组中的索引,后面可以根据这个索引直接从牌数组中获取牌的数量
                    int eyeIndex = eyeList.get(i);
                    //获取将牌的数量
                    int n = cards[eyeIndex];
                    //首先将[将牌]从牌堆中移除,当此将无法完成胡牌条件时,在将此牌放回牌堆,利用回溯法再进行判定下一张将牌
                    cards[eyeIndex] -= 2;
                    win = handleCardsWithoutEye(cards,eyeIndex / 9,checkedCache);
                    cards[eyeIndex] = n;
                    if (win) {
                        break;
                    }
                }
            }
            return win;
        }


        /**
         * 将手牌分开不同的花色进行分别判定,如果万，条，筒，字牌每种花色都能满足胡牌条件则此手牌一定可以胡
         * @param cards     hand cards
         * @param eye_color
         * @param checkedCache
         * @return whether can hu
         */
        private static boolean handleCardsWithoutEye(int[] cards,int eyeColor, int[] checkedCache) {
            /*
             *  遍历万,条,筒三种花色依次判定此种花色是否可以构造胡牌条件,如果每种花色都可以构造胡牌条件3n则此手牌一定可以胡
             *  这里利用的是分而治之的思想
             */
            for (int i = 0; i < 3; i++) {
                /*
                 * 参数中传入的将的花色是否和当前遍历的花色一样,如果一样则不处理,如果不一样则将其它花色的胡的判定结果存储起来
                 * 方便下次在遍历同样花色的将的时候,其它的花色的判定直接从缓存中获取不需要在重新的判定,提升算法效率
                 * 比如当前传入的将为 7万 我们可以把 条,筒两种花色的判定结果保存起来
                 * 当传入的将为 8万时  我们可以直接从缓存中获取到其它 条,筒花色的判定结果无需重新判定
                 */
                int cacheIndex = -1;
                if (eyeColor != i) {
                    cacheIndex = i;
                }
                /*
                 * 遍历手牌中指定花色的牌
                 */
                boolean win = checkNormalCardsWin(cards, i * 9, i * 9 + 8,cacheIndex,checkedCache);
                /*
                 * 当前花色如果不是传入的将的花色则将判定结果进行存储 1表示判定成功 2表示判定失败
                 */
                if (cacheIndex >0 && win){
                    checkedCache[i] = 1;
                }
                if (cacheIndex >0 && !win){
                    checkedCache[i] = 2;
                }
                if (!win){
                    return false;
                }
            }
            /*
             * 处理字牌花色,字牌的花色为 3
             */
            int cacheIndex = -1;
            if (eyeColor != 3){
                cacheIndex = 3;
            }
            return checkZiCardsWin(cards,cacheIndex,checkedCache);
        }

        /**
         * 检查当前花色是否满足胡牌条件 即是否满足 3n
         * @param cards 手牌
         * @param beginIndex 当前花色开始索引
         * @param endIndex 当前花色结束索引
         * @param cacheIndex 要查询的缓存的花色索引
         * @param checkedCache 缓存
         * @return 是否可以胡 true / false
         */
        private static boolean checkNormalCardsWin(int[] cards, int beginIndex, int endIndex,int cacheIndex, int[] checkedCache) {
            /*
             * 如果当前要判定的花色与将的花色一样那么 cacheIndex 值为-1,不从缓存中获取
             * 否则从缓存中拿判定的结果,无需重新判定
             */
            if (cacheIndex >= 0) {
                int n = checkedCache[cacheIndex];
                if (n > 0) {
                    return n - 1 == 0;
                }
            }

            /*
             * 将当前花色中所有的牌组成一个数字，方便后面进行判定.因为万，条，筒每种花色都有9张牌，所以我们可以使用一个9位数的数字来表示这个花色
             * 此数字从低位到高位分别保存1万-9万 或 1-9条 1-9筒,位数表示牌的类型,比如个位表示1万 十位表示2万 千万表示3万,而位数上面的值表示牌的数量
             * 这其实和我们之前使用数组的道理是一样的,数组的索引表示牌类型，数组的值表示此牌的个数
             * 举个例子，如下这个数字  101000111 它表示有 1个1万 1个2万  1个3万  0个4万5万6万 1个7万 0个8万 1个9万,示意图如下所示：
             *
             * 1        0       1       0       0       0       1           1       1
             * 9万      8万     7万      6万     5万     4万      3万         2万      1万
             */
            int n = 0;
            for (int i = beginIndex; i <= endIndex; i++) {
                n = n * 10 + cards[i];
            }
            /*
             * 0表示此花色己经没有牌,满足3n
             */
            if (n == 0) {
                return true;
            }
            /*
             * 检查当前花色牌的数量是否满足 3n
             * 由于n是由多张牌组成,我们要判断此数字上面所有位数上面的数字的和是否能被3整除
             * 由简单的数学知识得知: 想要判断一个数字上面所有位数上面的和能被整除，只要此数字能被3整除即可
             */
            if (n  % 3 != 0) {
                return false;
            }

            /*
             * 开始拆分此数字n,如果数字n可以被拆分成n个顺子或者刻子则可以胡
             */
            return splitCards(n);
        }

        /**
         * 拆分这个数字，直到无法拆分为止，当这个数字为0时表示可以完全拆除成功
         * @param n
         * @return
         */
        private static boolean splitCards(int n) {
            int p = 0;
            while (true) {
                if (n == 0) return true;
                /*
                 * 找到低位数上不为0的数字
                 */
                while (n > 0) {
                    p = n % 10;//获取个位数
                    n = n / 10;//将n去掉低位数
                    if (p != 0) break;
                }
                /*
                 * 1和4是一样的处理方法 4可以拆分为 1和3,3直接是刻子不用作处理
                 */
                if (p == 1 || p == 4) {
                    return singleNumHandle(n);

                } else if (p == 2) {
                    return doubleNumHandle(n);

                } else if (p == 3) {
                    //刻字不用作处理
                }
            }
        }

        /**
         * 单个数字的处理，需要拆分为  1 1 1 形式
         * @param n
         * @return
         */
        private static boolean singleNumHandle(int n) {
            //获取此数字前面的p1
            int p1 = n % 10;
            //获取此数字前面的p2
            int p2 = (n % 100) / 10;

            if (p1 == 0) {
                return false;
            } else {
                n -= 1;
            }

            if (p2 == 0) {
                return false;
            } else {
                n -= 10;
            }
            //当n ==0 表示此花色的牌完全满足 3n法则 可以胡牌
            if (n == 0) {
                return true;
            }

            return splitCards(n);
        }

        /**
         * 尾数为2的处理方法，形式为  2 2 2
         * @param n
         * @return
         */
        private static boolean doubleNumHandle(int n) {
            //获取此数字前面的p1
            int p1 = n % 10;
            //获取此数字前面的p2
            int p2 = (n % 100) / 10;

            if (p1 < 2) {
                return false;
            } else {
                n -= 2;
            }

            if (p2 < 2) {
                return false;
            } else {
                n -= 20;
            }
            //当n ==0 表示此花色的牌完全满足 3n法则 可以胡牌
            if (n == 0) {
                return true;
            }

            return splitCards(n);
        }

        /**
         * 字牌花色的处理逻辑
         * @param cards
         * @param cacheIndex
         * @param cache
         * @return
         */
        private static boolean checkZiCardsWin(int[] cards, int cacheIndex, int[] checkedCache) {
            /*
             * 如果当前要判定的花色与将的花色一样那么 cacheIndex 值为-1,不从缓存中获取
             * 否则从缓存中拿判定的结果,无需重新判定
             */
            if (cacheIndex >= 0) {
                int n = checkedCache[cacheIndex];
                if (n > 0) {
                    return n - 1 == 0;
                }
            }
            /*
             * 字牌的索引为 27至 34 字牌的判定很简单因为字牌只能是组成刻子不能组成顺子,所以我们只需要判定此牌的个数是否为3即可
             */
            for (int i = 27; i < 34; i++) {
                int n = cards[i];
                if (n == 0) {
                    continue;
                }
                if (n != 3){
                    return false;
                }
            }
            return true;
        }
}
