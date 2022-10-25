package actions.manipulation.liste;

import cards.CardColor;
import cards.CardType;
import entities.Player;

/**
 * Darstellugn einer einzelnen Spieleraktion
 */
public class Element {
    private Element danach;
    private Element davor;
    private Player player;
    private CardType typ;
    private CardColor farbe;
    private int wert;
    private String aktion;

    public Element(Player player,CardType typ, CardColor farbe,int wert, String aktion) {
        davor = null;
        danach = null;
        this.player=player;
        this.typ=typ;
        this.farbe=farbe;
        this.wert=wert;
        this.aktion=aktion;
    }

    public void setDanach(Element danach) {
        this.danach=danach;
    }
    public void setDavor(Element davor) {
       this.davor=davor;
    }
    public Element getDavor() {
        return davor;
    }
    public Element getDanach() {
        return danach;
    }
    public CardType getTyp() {
        return typ;
    }
    public CardColor getFarbe() {
        return farbe;
    }
    public int getWert() {
        return wert;
    }
    public String getAktion() {
        return aktion;
    }
    public Player getPlayer() {
        return player;
    }
}

