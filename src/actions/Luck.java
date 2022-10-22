package actions;

import cards.LuckCard;
import entities.Player;

public class Luck extends Action{

    public LuckCard playedCard;

    public Luck(Player player, LuckCard card){
        super(player);
        this.playedCard = card;
    }

}
