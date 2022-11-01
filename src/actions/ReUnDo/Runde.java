package actions.ReUnDo;

import entities.Player;
import entities.Table;

import java.util.ArrayList;


/**
 * Abbild einer gespielten Runde mit allen Spielvariablen, die sich veraendern
 */
public class Runde {

    private ArrayList<Player> spieler;
    private Table tischStand;

    private Runde davor;
    private Runde dahinter;

    public Runde(ArrayList<Player> spieler, Table tischStand){
        this.spieler=spieler;
        this.tischStand=tischStand;
        this.davor=null;
        this.dahinter=null;
    }

    /**
     * fuegt eine neue Runde vor dieser ein
     * @param davor wird eingefuegt
     */
    public void setDavor(Runde davor) {
        this.davor = davor;
    }

    /**
     *
     * @return gibt die vorherige Runde zurueck
     */
    public Runde getDavor() {
        return davor;
    }

    /**
     * fuegt eine neue Runde hinter diese ein
     * @param dahinter die dahinter eingefuegt werden soll
     */
    public void setDahinter(Runde dahinter) {
        this.dahinter = dahinter;
    }

    /**
     *
     * @return die folgende Runde
     */
    public Runde getDahinter() {
        return dahinter;
    }

    /**
     *
     * @return die Spieler der Runde und somit ihre Spielstaende
     */
    public ArrayList<Player> getSpieler() {
        return spieler;
    }

    /**
     *
     * @return die Tisch positionen der Runde
     */
    public Table getTischStand() {
        return tischStand;
    }
}