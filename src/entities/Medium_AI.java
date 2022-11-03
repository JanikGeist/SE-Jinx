package entities;

import cards.Card;
import cards.LuckCard;

/**
 * Medium level AI
 * - Decides if it has to roll the dice again if eye count is too low.
 * - Chooses the best possible card based on amount of colors on the field
 * - Doesn't use luck cards
 * */
public class Medium_AI extends Participant {

    /**
     * Constructor for a new player
     *
     * @param name name of player
     */
    public Medium_AI(String name) {
        super(name);
    }

    /**
     * New choose action function to determine the next move of the AI
     *
     * @return the next move as String ["R","L","C"]
     * */
    @Override
    public String chooseAction(){
        //roll the dice again, if the eye count is not acceptable
        System.out.println("AI_CHOOSE ACTION");
        return null;
    }

    @Override
    public boolean chooseCard(Table table) {
        return false;
    }

    @Override
    public boolean drawLuckCard(Table table) {
        return false;
    }

    @Override
    public Card selectCard() {
        return null;
    }

    @Override
    public boolean selectHighCard() {
        return false;
    }

    @Override
    public LuckCard selectLuckCard() {
        return null;
    }

    @Override
    public int roll(){
        //check if AI is able to roll and if the current eye count is less than 3
        if(this.rolls < 2 && this.diceCount < 3){
            //roll the dice
            this.diceCount = rand.nextInt(6) + 1;
            rolls++;
        }
        return this.diceCount;
    }

    @Override
    public boolean mintomax(LuckCard lC, int min, int max) {
        return false;
    }

    @Override
    public boolean cardSum(LuckCard lC, Table table) {
        return false;
    }

    @Override
    public boolean extraThrow(LuckCard lC) {
        return false;
    }


}
