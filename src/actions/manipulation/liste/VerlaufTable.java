package actions.manipulation.liste;


import cards.Card;
import cards.LuckCard;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Listenartige Darstellung der Veranederungen auf dem Spielfeld, Kartenstapel und Lucky Karten Stapel
 */
public class VerlaufTable  {
    private ArrayList<TableRunde> runden;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public VerlaufTable(TableRunde rundeStart){
        runden=new ArrayList<>();
        runden.add(rundeStart);
    }

    /**
     *
     * @param runde ergaenz die letzte Runde zur Liste
     */
    public void add(TableRunde runde)
    {
        this.runden.add(runde);
    }

    /**
     * gibt die Runden nacheinander aus
     */
    public void ausgabeRunden(){
        for (TableRunde runde:runden){
            logger.info("Feld: "+feldAusgabe(runde)+"\n Kartenstapel: "+runde.getNormals()+"\n LuckyStapel: "+runde.getLuckys()+"\n  Runde: "+runde.getNumber()            );
        }
    }

    /**
     *
     * @param runde die runde aus der das Spielfeld dargestellt werden soll
     * @return eine String Darstellung des Spielfeldes der uebergebenen Runde
     */
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

    /**
     *
     * @return Die Liste der Runden
     */
    public ArrayList<TableRunde> getRunden() {
        return runden;
    }

    /**
     *
     * @return Die Anzahl der Veraenderungen bisher
     */
    public int getRundenAnzahl(){
        int anzahl=runden.size();
        return anzahl;
    }

    /**
     *
     * @param runden ueberschreibt die Liste der bisherigen Runden
     */
    public void setRunden(ArrayList<TableRunde> runden) {
        this.runden = runden;
    }

}

