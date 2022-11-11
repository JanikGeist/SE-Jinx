package entities;

import cards.Card;
import java.util.ArrayList;
import java.util.Comparator;

public class MediumAI extends Player{
    /**
     * Constructor for a new player
     *
     * @param name name of player
     */
    public MediumAI(String name) {
        super(name);
    }

    /**
     * Override of the chooseAction function of a normal player
     *
     * AI decides what to do based on rollCount, diceCount and the combination of cards on the field
     * AI always tries to roll higher than 3 and then choose the color with the least appearances on the field
     * AI rolls as long as no suitable card is found or no more rolls are left
     * */
    @Override
    public String chooseAction(Table table){

        log("Your turn " + this.name + "! Eye count - " + this.diceCount);
        log(this.toString());

        //roll if not rolled yet
        if(this.rolls == 0){
            log(this.name + "[AI], i didnt roll the dice yet!");
            return "R";
        }

        //roll again if diceCount is too low
        if(this.diceCount < 3 && this.rolls < 2){
            log(this.name + "[AI], my diceCount is way to low!");
            return "R";
        }

        //check if there is a card that can be picked, if not and rolls are available --> roll
        if(getBestCard(table) == null && this.rolls < 2){
            log(this.name + "[AI], there is no card i want...I´ll roll again!");
            return "R";
        }

        log(this.name + "[AI], i will choose a card now!");
        //nothing left to do, possibly end round
        return "C";
    }

    /**
     * Function to let the AI choose a card
     * Sets isActive according to the AIs action
     * AI chose a card --> isActive = false;
     * AI wasn't able to choose a card --> isActive = false
     * @param table the current playing field
     * @return true if card was chosen, false if no card was chosen
     * */
    @Override
    public boolean chooseCard(Table table){
        //check if the AI has to end its turn because it has no options to pick a card
        if(checkEndRound(table)){
            log(this.name + "[AI], there is no card you could choose!");
            //set AI inactive to end its turn;
            this.active = false;
            return false;
        }

        //find the best possible card to pick from the field
        Card chosenOne = getBestCard(table);

        //add card to AIs hand and remove it from field
        addCard(table.getCard(chosenOne));
        //set the AI as inactive, since this action ended its turn
        this.active = false;
        //signal that the AI has chosen a card successfully
        return true;
    }

    /**
     * Function to let the AI choose wich of its highest cards it wants to drop
     * Medium AI chooses the card color with the least occurrences in his hand
     * */
    @Override
    public boolean selectHighCard(){

        log(this.name + "[AI], you finished the round! Choose a card to drop!");

        //check if the AI is able to drop a card
        if(this.cards.size() == 0){
            log(this.name + "[AI], i have no cards to drop after this round!");
            return false;
        }


        //look for highest card the player has!
        ArrayList<Card> maxCards = new ArrayList<>();
        int currentHigh = 0;
        Card[] hand = this.cards.toArray(new Card[0]);

        for (Card c : hand) {
            if (c.getValue() > currentHigh) {
                currentHigh = c.getValue();
            }
        }

        for (Card c : hand) {
            if (c.getValue() == currentHigh) {
                maxCards.add(c);
            }
        }

        //AI has only one card to drop
        if(maxCards.size() == 1){
            log(this.name + "[AI], i only have one card to drop...");
            this.cards.remove(maxCards.get(0));
            return true;
        }

        //initialize dropCard with the first available card
        Card dropCard = maxCards.get(0);

        int i = this.cards.size();
        int count = this.cards.size();
        //try to find the card with the least occurrences
        for(Card mxC : maxCards) {
            for (Card c : this.cards) {
                //decrease i if there is a card with the same color as the current high card in the players hand
                if (c.getColor() == mxC.getColor()){
                    i--;
                }
            }
            //check if there is a card with lower count than the one before
            if(i < count){
                count = i;
                //reset i
                i = this.cards.size();
                //select the currently best card to drop
                dropCard = mxC;
            }
        }

        //remove the card from the AIs hand
        this.cards.remove(dropCard);

        return true;
    }

    /**
     * Function to let the AI choose to draw a luck card
     * Medium AI will never draw or use a luck card
     * */
    @Override
    public boolean drawLuckCard(Table table, Player[] players){
        log(this.name + "[AI], i would never waste points for a luck card!");
        return false;
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
                if(card != null){
                    //add card to specific color array
                    allColors.get(card.getColor().getValue()).add(card);
                }
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
                if(c.getValue() == this.diceCount){
                    return c;
                }
            }
        }

        return null;
    }
}
