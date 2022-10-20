import cards.Card;
import cards.CardColor;
import cards.CardType;
import cards.LuckCard;
import entities.Player;

public class Main {

    public static void main(String[] args) {
        Player p = new Player("Hans");
        Card c = new Card(CardColor.GREEN,10);
        Card c2 = new Card(CardColor.GREEN,1);
        Card c3 = new Card(CardColor.GREEN,4);
        LuckCard cL = new LuckCard(CardType.EXTRATHROW);
        LuckCard cL2 = new LuckCard(CardType.EXTRATHROW);
        LuckCard cL3 = new LuckCard(CardType.EXTRATHROW);
        p.addLuckCard(cL);
        p.addLuckCard(cL2);
        p.addLuckCard(cL3);
        p.addCard(c);
        p.addCard(c2);
        p.addCard(c3);
        p.removeCard(2);
        System.out.println(p.getCards());
        System.out.println(p.getLuckCards());
    }
}
