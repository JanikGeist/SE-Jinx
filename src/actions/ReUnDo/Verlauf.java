package actions.ReUnDo;

import entities.Player;

import java.util.logging.Logger;

public class Verlauf {

    private Runde head;
    private Runde tail;
    private Runde aktuellePosition;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Verlauf(){
        this.head=new Runde(null,null);
        this.tail=new Runde(null,null);

        this.head.setDahinter(tail);
        this.tail.setDavor(head);
        this.aktuellePosition=tail.getDavor();
    }


    public void rundeHinzufuegen(Runde neu){
        Runde halter= tail.getDavor();
        halter.setDahinter(neu);
        tail.setDavor(neu);
        neu.setDavor(halter);
        neu.setDahinter(tail);
    }

    public boolean headOrTail(Runde runde){
        boolean leer=false;
        if (runde.equals(head)||runde.equals(tail)){
            leer=true;
        }
        return leer;
    }

    public void verlaufAnzeigen(){
        int runde =1;
        Runde start =head.getDahinter();
        while (!start.equals(tail)){
            logger.info(runde+". Zug: \n"+runde);
            for (Player player: start.getSpieler()){
                logger.info("Spieler: "+player.getName()+"\n" +
                        "Handkarten: "+player.getCards()+"\n" +
                        "LuckyKarten: "+player.getLuckCards()+"\n");
            }
            logger.info("Tisch Kartenstapel: "+start.getTischStand().getCardStack()+"\n" +
                    "Tisch Luckykartenstapel: "+start.getTischStand().getLuckStack()+"\n" +
                    "Spielfeld:\n "+start.getTischStand().toString());
            runde++;
            start=start.getDahinter();
        }
    }

}
