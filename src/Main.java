import cards.Card;
import cards.CardColor;
import entities.Player;

public class Main {

    public static void main(String[] args) {
        Player p = new Player("Hans");
        Card c = new Card(CardColor.GREEN,10);
        Card c2 = new Card(CardColor.GREEN,1);
        Card c3 = new Card(CardColor.GREEN,4);
        p.addCard(c);
        p.addCard(c2);
        p.addCard(c3);
        System.out.println(p.getCards());
    }
}
