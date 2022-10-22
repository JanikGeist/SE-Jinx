package actions;

import entities.Player;

public class Roll extends Action {

    public int number;

    public Roll(Player player, int number){
        super(player);
        this.number = number;
    }

}
