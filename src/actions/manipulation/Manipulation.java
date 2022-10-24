package actions.manipulation;


import actions.manipulation.liste.Verlaufsliste;

import java.util.Scanner;
import java.util.logging.Logger;

public class Manipulation extends Verlaufsliste {

    private Logger logger=Logger.getLogger(this.getClass().getName());
    Scanner sc = new Scanner(System.in);

    public void manipulation(){
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
        }
    }

    private void redo(){}

    private void undo(){}
}
