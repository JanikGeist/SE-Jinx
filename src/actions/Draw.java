package actions;

import cards.LuckCard;
import entities.Player;

public class Draw extends Action{

    LuckCard drawnCard;

    public Draw(Player player, LuckCard card){
        super(player);
        this.drawnCard = card;
    }
}
