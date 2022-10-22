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

        GameLoop game = new GameLoop();

        game.run();

        System.out.println("DONE");
    }
}
