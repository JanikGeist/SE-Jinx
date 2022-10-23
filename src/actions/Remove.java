package actions;

import cards.Card;
import entities.Player;

public class Remove extends Action{

    Card removedCard;

    public Remove(Player player, Card card) {
        super(player);
        this.removedCard = card;
    }
}
