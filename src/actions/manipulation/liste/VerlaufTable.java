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
        Array[][] halter = new Array[runde.getFeld().length][runde.getFeld()[0].length];

        String ausgabe="";
        String auschnitt="";
        for (int a=0; a<halter.length;a++){
            for (int b=0; b<halter[0].length; b++)
            {
                auschnitt=halter[a+1][b+1].toString();
                ausgabe=ausgabe+auschnitt;
            }
            ausgabe=ausgabe+"\n";
        }
        return ausgabe;
    }
}
