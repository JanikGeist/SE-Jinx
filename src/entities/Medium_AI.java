package entities;

/**
 * Medium level AI
 * - Decides if it has to roll the dice again if eye count is too low.
 * - Chooses the best possible card based on amount of colors on the field
 * - Doesn't use luck cards
 * */
public class Medium_AI extends Player {

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
    public int roll(){
        //check if AI is able to roll and if the current eye count is less than 3
        if(super.getRolls() < 2 && super.getDiceCount() < 3){
            //roll the dice
            return super.roll();
        }else{
            //if not able to roll, return current eye count
            return super.getDiceCount();
        }
    }


}
