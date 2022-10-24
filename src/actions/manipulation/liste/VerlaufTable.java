package actions.manipulation.liste;


import cards.Card;
import cards.LuckCard;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Logger;

public class VerlaufTable  {
    private ArrayList<TableRunde> runden;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public VerlaufTable(TableRunde rundeStart){
        runden=new ArrayList<>();
        runden.add(rundeStart);
    }

    public void add(TableRunde runde)
    {
        this.runden.add(runde);
    }

    public void ausgabeRunden(){
        for (TableRunde runde:runden){
            logger.info("Feld: "+feldAusgabe(runde)+"\n Kartenstapel: "+runde.getNormals()+"\n LuckyStapel: "+runde.getLuckys()+"\n  Runde: "+runde.getNumber()            );
        }
    }
    public String feldAusgabe(TableRunde runde){
            StringBuilder ret = new StringBuilder("");

            for(int i = 0; i < 4; i++){
                ret.append("[");
                for(int j = 0; j < 4; j++){
                    ret.append(runde.getFeld()[i][j]+"  ");
                    if(j < 3) {
                        ret.append(",");
                    }
                }
                ret.append("]\n");
            }
            return ret.toString();
        }

    public ArrayList<TableRunde> getRunden() {
        return runden;
    }

    public int getRundenAnzahl(){
        int anzahl=runden.size();
        return anzahl;
    }

    public void setRunden(ArrayList<TableRunde> runden) {
        this.runden = runden;
    }

}

