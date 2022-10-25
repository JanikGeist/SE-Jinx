package actions.manipulation.liste;

import cards.CardColor;
import cards.CardType;
import entities.Player;

import java.util.logging.Logger;

/**
 * Stellt den verlauf der Spielerhandlungen da
 */
public class Verlaufsliste {
    private Element head;
    private Element tail;
    public Element position;
    private Logger logger=Logger.getLogger(this.getClass().getName());
    private int laenge=1;


    public Verlaufsliste(){
        this.head=new Element(null,null,null,0,null);
        this.tail=new Element(null,null,null,0,null);

        this.head.setDanach(tail);
        this.tail.setDavor(head);

        this.position=tail;
    }

    /**
     *
     * @param player der Spieler der den zug gemacht hat
     * @param typ Der Typ der Karte die gespielt wurde
     * @param farbe Die Farbe der Karte, die gespielt wurde
     * @param wert Der Wert der Karte die gespielt wurde
     * @param aktion Die Art und Weise wie die Karte verwedet wurde
     */
    public void zugEinfuegen(Player player, CardType typ, CardColor farbe, int wert, String aktion){ //int aendern
        Element zug = new Element(player,typ,farbe,wert,aktion);
        Element halter = this.tail.getDavor();
        if (halter==head){
            head.setDanach(zug);
            tail.setDavor(zug);
            zug.setDavor(head);
            zug.setDanach(tail);
        }
        else{
            halter.setDanach(zug);
            zug.setDavor(halter);
            zug.setDanach(tail);
            tail.setDavor(zug);
        }
    }

    /**
     * Leert den gesamten Verlauf
     */
    public void verlaufsListeReseten(){
        this.position=tail;
        this.head.setDanach(tail);
        this.tail.setDavor(head);
    }

    /**
     * Gibt den bisherigen Zug Verlauf aus
     */
    public void verlaufAnzeigen(){
        Element start =head.getDanach();
        while(start!=tail){
            logger.info("Zug:"+start.getAktion()+" Spieler:"+start.getPlayer().getName()+" Karte: "+start.getTyp()+" "+start.getFarbe()+" "+start.getWert()+"\n");
            start=start.getDanach();
        }        
    }

    /**
     *
     * @return die Anzahl der bisherigen Zuege
     */
    public int getLaenge() {
        Element h=head;
        while(h.getDanach()!=tail){
            this.laenge++;
            h=h.getDanach();
        }
        return laenge;
    }

    /**
     *
     * @return liefert den Head der Liste
     */
    public Element getHead() {
        return head;
    }

    /**
     *
     * @return liefert den tail der Liste
     */
    public Element getTail() {
        return tail;
    }
}