import cards.Card;
import cards.CardColor;
import cards.CardType;
import cards.LuckCard;
import entities.Player;
import entities.Table;

public class Main {

    public static void main(String[] args) {
        Table t = new Table();

        t.initCards();
        t.initLuckCards();

        System.out.println("DONE");
    }
}
