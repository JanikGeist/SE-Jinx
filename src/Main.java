import cards.Card;
import cards.CardColor;
import cards.CardType;
import cards.LuckCard;
import entities.GameLoop;
import entities.Player;
import entities.Table;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // does user want to load a default config
        boolean config = false;
        while(true){

            System.out.println("Do you wish to load a config file? [y/n]");
            Scanner s = new Scanner(System.in);
            String con = s.nextLine();

            if ("y".equals(con)) {
                config = true;
                break;
            } else if ("n".equals(con)) {
                System.out.println("Shuffling the cards!");
                break;
            } else {
                System.out.println("Not an option! Try again!");
            }
        }

        GameLoop game = new GameLoop(config);

        game.run();

        System.out.println("DONE");
    }
}
