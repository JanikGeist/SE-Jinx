package actions;

import cards.Card;
import entities.Player;

public class Drop extends Action{
    public Card droppedCard;

    //position of where the card was dropped in the field
    public int xPos;
    public int yPos;

    public Drop(Player player, Card card, int x, int y){
        super(player);
        this.droppedCard = card;
        this.xPos = x;
        this.yPos = y;
    }

}
