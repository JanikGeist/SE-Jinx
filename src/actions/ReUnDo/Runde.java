package actions.ReUnDo;

import entities.Player;
import entities.Table;

import java.util.ArrayList;


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

    public void setDavor(Runde davor) {
        this.davor = davor;
    }
    public Runde getDavor() {
        return davor;
    }

    public void setDahinter(Runde dahinter) {
        this.dahinter = dahinter;
    }
    public Runde getDahinter() {
        return dahinter;
    }

    public ArrayList<Player> getSpieler() {
        return spieler;
    }
    public Table getTischStand() {
        return tischStand;
    }
}