package actions.manipulation;



import actions.manipulation.liste.Element;
import actions.manipulation.liste.TableRunde;
import actions.manipulation.liste.VerlaufTable;
import actions.manipulation.liste.Verlaufsliste;

import java.util.Scanner;
import java.util.logging.Logger;

public class Manipulation {
    Verlaufsliste spieler;
    VerlaufTable tisch;

    boolean manipulieren =true;
    private int zurueck=0;
    private int vor=0;
    private Logger logger=Logger.getLogger(this.getClass().getName());
    Scanner sc = new Scanner(System.in);

    private Element auswahlSpieler;
    private TableRunde auswahlTisch;

    public Manipulation(Verlaufsliste spieler, VerlaufTable tisch){
        this.spieler=spieler;
        this.tisch=tisch;
        this.auswahlSpieler=spieler.getTail().getDavor();
    }

    public void manipulation(){

        while(manipulieren){
            logger.info("Welche Aktion wollen Sie ausfuehren?\n " +
                    "Druecken Sie die eins um einen Zug rueckgaening zu machen oder" +
                    "Druekcne Sie die zwei um einen Zug nach vorne zu springen oder" +
                    "Schreiben Sie end um das Spiel nicht zu manipulieren.");

            String auswahl = sc.next();
            if (auswahl.equals("1")){
                undo();
            }
            else if (auswahl.equals("2")){
                redo();
            }
            else if (auswahl.equals("end")){
                logger.info("Keine Aenderungen vorgenommen.");
                manipulieren=false;
            }
            else{
                logger.info("Bitte gib etwas sinnvolles ein.");
            }
        }
        logger.info("Wollen Sie die aenderungen uebernehmen druecken Sie die eins, wenn nicht druecken Sie irgendeine andere Taste."+
                "Sie sind "+zurueck+" Schritte zurueck gegangen und "+vor+" Schritte vorgegangen.");
        String fertig=sc.next();
        if (fertig.equals("1")){
            logger.info("Dieser Schritt ist nicht rueckgaengig zu machen.");
            zeitreise();
        }
        else{
            logger.info("Ohne Aenderungen fortfahren.");
        }

    }

    private void redo(){
        boolean laufen =true;
        int index=tisch.getRundenAnzahl();
        logger.info("Es wird ein Schritt zurueck gegangen.");
        this.auswahlTisch=tisch.getRunden().get(index+vor);
        while (laufen){
            logger.info("Noch einen? Druecken sie y fuer ja und n fuer nein");
            String eingabe=sc.next();
            if (eingabe.equals("y")){
                if(this.auswahlSpieler.getDanach()!= spieler.getTail()){
                    Element plazuhalter=this.auswahlSpieler.getDanach();
                    this.auswahlSpieler=plazuhalter;
                    this.auswahlTisch=tisch.getRunden().get(index+vor);
                    vor++;
                }
                else if (eingabe.equals("n")){
                    laufen=false;
                }
                else{
                    logger.info("Du kannst nicht vorspringen.");
                }
            }
            else{
                logger.info("Keine gueltige Eingabe.");
            }
        }

    }

    private void undo(){
        boolean laufen =true;
        int index=tisch.getRundenAnzahl();
        logger.info("Es wird ein Schritt zurueck gegangen.");
        this.auswahlTisch=tisch.getRunden().get(index-zurueck);
        while(laufen){
            logger.info("Noch einen? Druecken sie y fuer ja und n fuer nein");
            String eingabe=sc.next();
            if (eingabe.equals("y")){
                zurueck++;
                Element plazhalter=this.auswahlSpieler.getDavor();
                this.auswahlSpieler=plazhalter;
                this.auswahlTisch=tisch.getRunden().get(index-zurueck);

            }
            else if (eingabe.equals("n")){
                laufen=false;
            }
            else{
                logger.info("Keine gueltige Eingabe.");
            }
        }
    }

    public void zeitreise(){

    }
}
