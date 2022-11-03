package actions.ReUnDo;

import entities.Participant;
import entities.Player;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Eine doppeltverkette Liste zum chronologischen Darstellen der einzelnen Runden im Spiel
 */
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
        this.aktuellePosition=tail;
    }


    /**
     * setzt eine neue Runde ans ende des Verlaufs
     * @param neu Runde die eingefuegt werden soll
     */
    public void rundeHinzufuegen(Runde neu){
        Runde halter= tail.getDavor();
        halter.setDahinter(neu);
        tail.setDavor(neu);
        neu.setDavor(halter);
        neu.setDahinter(tail);
    }

    /**
     *
     * @param runde die geprueft werden soll, ob sie der Anfang oder das Ende ist
     * @return ob es Kopf oder Ende ist
     */
    public boolean headOrTail(Runde runde){
        boolean leer=false;
        if (runde.equals(head)||runde.equals(tail)||runde.equals(null)){
            leer=true;
        }
        return leer;
    }

    /**
     * zum Ausgeben von Text Messages
     * @param msg
     */
    private void log(String msg) {
        System.out.println("[JINX] " + msg);
    }

    /**
     * Menue zum Waehlen der geplanten Re und Un Do Schritte
     * @return
     */
    public Runde jump(){
        aktuellePosition= tail;


        while(true){
            log("""
            Choose your Manipulation!
            S - Show regular status
            J - choosen status
            K - jump back
            L - jump further
            P - leave
                    \n""");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input.equals("S")){
                log("regular status:\n");
                rundeAnzeigen(tail.getDavor());
            }
            else if (input.equals("J")){
                if (!headOrTail(aktuellePosition)){
                    log("new choosen status:");
                    rundeAnzeigen(aktuellePosition);
                }
                else{
                    log("No new status choosen.");
                }

            }
            else if (input.equals("K")){
                unDo();
            }
            else if (input.equals("L")){
                reDo();
            }
            else if (input.equals("P")){
                if (!headOrTail(aktuellePosition)){
                    return aktuellePosition;
                }
                return tail;
            }
            else{
                log("incorrect input.");
            }
        }
    }

    /**
     * geht im Verlauf einen Schritt zurueck
     */
    public void unDo(){
        if (!headOrTail(aktuellePosition.getDavor())){
            log("One step back");
            aktuellePosition=aktuellePosition.getDavor();
        }
        else{
            log("already at the beginning");
        }

    }

    /**
     * geht im Verlauf einen Schritt vor
     */
    public void reDo(){
        if (!aktuellePosition.equals(tail)){
            log("One step further");
            aktuellePosition=aktuellePosition.getDahinter();
        }
        else{
            log("already at the end");
        }
    }

    /**
     * Gibt im Terminal eine einzelne Uebergeben Runde aus
     * @param auswahl einzelne Runde
     */
    public void rundeAnzeigen(Runde auswahl){
        for (Participant player: auswahl.getSpieler()){
            logger.info("Spieler: "+player.getName()+"\n" +
                    "Handkarten: "+player.getCards()+"\n" +
                    "LuckyKarten: "+player.getLuckCards()+"\n");
        }
        logger.info("Tisch Kartenstapel: "+auswahl.getTischStand().getCardStack()+"\n" +
                "Tisch Luckykartenstapel: "+auswahl.getTischStand().getLuckStack()+"\n" +
                "Spielfeld:\n "+auswahl.getTischStand().toString());
    }

    /**
     * gibt im Terminal den ganzen Verlauf aus
     */
    public void verlaufAnzeigen(){
        int runde =1;
        Runde start =head.getDahinter();
        while (!start.equals(tail)){
            logger.info(runde+". Zug: \n"+runde);
            for (Participant player: start.getSpieler()){
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

    /**
     *
     * @return das Ende der Liste
     */
    public Runde getTail() {
        return tail;
    }
}
