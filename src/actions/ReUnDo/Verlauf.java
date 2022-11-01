package actions.ReUnDo;

import entities.Player;

import java.util.Scanner;
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
        this.aktuellePosition=tail;
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
        if (runde.equals(head)||runde.equals(tail)||runde.equals(null)){
            leer=true;
        }
        return leer;
    }

    private void log(String msg) {
        System.out.println("[JINX] " + msg);
    }

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

    public void unDo(){
        if (!headOrTail(aktuellePosition.getDavor())){
            log("One step back");
            aktuellePosition=aktuellePosition.getDavor();
        }
        else{
            log("already at the beginning");
        }

    }

    public void reDo(){
        if (!aktuellePosition.equals(tail)){
            log("One step further");
            aktuellePosition=aktuellePosition.getDahinter();
        }
        else{
            log("already at the end");
        }
    }

    public void rundeAnzeigen(Runde auswahl){
        for (Player player: auswahl.getSpieler()){
            logger.info("Spieler: "+player.getName()+"\n" +
                    "Handkarten: "+player.getCards()+"\n" +
                    "LuckyKarten: "+player.getLuckCards()+"\n");
        }
        logger.info("Tisch Kartenstapel: "+auswahl.getTischStand().getCardStack()+"\n" +
                "Tisch Luckykartenstapel: "+auswahl.getTischStand().getLuckStack()+"\n" +
                "Spielfeld:\n "+auswahl.getTischStand().toString());
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

    public Runde getTail() {
        return tail;
    }
}
