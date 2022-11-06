package entities;

import cards.Card;
import cards.CardColor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MediumAI extends Player{
    /**
     * Constructor for a new player
     *
     * @param name name of player
     */
    public MediumAI(String name) {
        super(name);
    }

    @Override
    public String chooseAction(Table table){

        //roll if not rolled yet
        if(rolls == 0){
            return "R";
        }


        //roll again if diceCount is too low
        if(diceCount < 3 && rolls < 2){
            return "R";
        }

        //check if there is a card that can be picked, if not and rolls are available --> roll
        if(getBestCard(table) == null && rolls < 2){
            return "R";
        }

        //nothing left to do, possibly end round
        return "C";
    }

    /**
     * Function to determine the best card to draw with the current diceCount
     *
     * @return best possible card, null if there is no card
     * */
    private Card getBestCard(Table table){

        //Each position is representing a color of a card
        ArrayList<ArrayList<Card>> allColors = new ArrayList<>();

        //populate each color position with an arrayList
        for(int i=0; i<8; i++){
            allColors.add(new ArrayList<>());
        }

        //iterate over each card on the table
        for(int x=0; x < 4; x++){
            for(int y=0; y < 4; y++){
                //check the card in current position
                Card card = table.checkCard(x,y);
                //add card to specific color array
                allColors.get(card.getColor().getValue()).add(card);
            }
        }

        //Custom comparator to sort arrayList by size of its contents
        Comparator<ArrayList<Card>> arrayListComparator = new Comparator<ArrayList<Card>>() {
            @Override
            public int compare(ArrayList<Card> o1, ArrayList<Card> o2) {
                return Integer.compare(o1.size(), o2.size());
            }
        };

        //sort allColors based on the arrayList sizes, the smallest first
        allColors.sort(arrayListComparator);

        //go over each color array and look for the first appearance of diceCount
        for (ArrayList<Card> cardArray : allColors){
            for(Card c : cardArray){
                if(c.getValue() == diceCount){
                    return c;
                }
            }
        }

        return null;
    }
}
