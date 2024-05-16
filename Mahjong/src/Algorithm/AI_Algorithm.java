package Algorithm;

import com.sun.source.tree.Tree;
import Objects.MahjongCard;
import java.util.ArrayList;
import java.util.TreeSet;
import Game.*;

public class AI_Algorithm {
    public void EasyAI(int playerIndex,MahjongCard current){
        ArrayList<MahjongCard> player=g.playerList.get(playerIndex);
        if(Hu_Algorithm.checkHu(player)){
            PlayerOperation.PengCards(playerIndex);
        }
        else if(Other_Algorithm.CheckGang(player,current)){
            PlayerOperation.PengCards(playerIndex);
        }
        else if(Other_Algorithm.CheckPeng(player,current)){
            PlayerOperation.PengCards(playerIndex);
        }
        else if(Other_Algorithm.CheckChi(player,current)){
            PlayerOperation.PengCards(playerIndex);
        }
        else{ PlayerOperation.ShowCard(playerIndex);}
    }

}
