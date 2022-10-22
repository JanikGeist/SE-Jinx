package actions;

import cards.Card;
import entities.Player;

public class Choose extends Action{

    //position of where the card was before
    public int xPos;
    public int yPos;

    public Card chosenCard;

    public Choose(Player player, Card card, int x, int y) {
        super(player);
        this.chosenCard = card;
        this.xPos = x;
        this.yPos = y;
    }
}
