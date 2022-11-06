package entities;

import cards.Card;
import cards.CardType;
import cards.LuckCard;

public class AIPLayer3 extends Player{

    /**
     * Constructor for a new AIplayer with level 3
     *
     * @param name name of player
     */
    public AIPLayer3(String name) {
        super(name);
    }

    @Override
    public String chooseAction(Table table) {
        if(this.rolls==0){
            this.log("First I roll the dice!");
            this.roll();
        }
        //checks if cards of value 4 to 6 are on table
        boolean highCards4 = false;
        boolean highCards5 = false;
        boolean highCards6 = false;
        for(Card[] card:table.getField()){
            for(Card c:card){
                if(c!=null) {
                    if (c.getValue() == 4) {
                        highCards4 = true;
                    } else if (c.getValue() == 5) {
                        highCards5 = true;
                    } else if (c.getValue() == 6) {
                        highCards6 = true;
                    }
                }
            }
        }
        if(highCards4||highCards5||highCards6){
            log("There are cards of high values on the table.");
            while(this.rolls<2&&this.diceCount<4){
                log("I'll try to get a higher number.");
                this.roll();
            }
        }
        Card[] availableCards = this.findValidCards(table);
        //TODO try to use extrathrow if no card is available

        //TODO draw luck card
        //TODO use luck card
        //TODO remove card at end of round
        //TODO end method with return "C" -> choose a card to take from table, perhabs save coord in var, then return in getinput method
        return super.chooseAction(table);
    }
    //TODO getplayerinputcoord override

    /**
     * finds all cards the AI could take
     *
     * @param table
     * @return
     */
    private Card[] findValidCards(Table table){
        Card[] cards = new Card[16];
        int index = 0;
        int plus = 0;
        int minus = 0;
        for(LuckCard luckCard:this.getLuckCards()){
            //compareTo returns 0 if they are equal
            if(luckCard.getCardType().compareTo(CardType.PLUSONE)==0&&this.usedCards.contains(luckCard)){
                plus++;
            }
            else if(luckCard.getCardType().compareTo(CardType.MINUSONE)==0&&this.usedCards.contains(luckCard)){
                minus++;
            }
        }
        for(Card[] card:table.getField()) {
            for (Card c : card) {
                if(c!=null){
                    if(this.diceCount==c.getValue()){
                        cards[index]=c;
                        index++;
                    }
                    else{
                        int a=plus;
                        int b=minus;
                        //all cards that can be taken with plusone
                        while(a!=0){
                            if(this.diceCount+a==c.getValue()){
                                cards[index]=c;
                                index++;
                            }
                        }
                        //all cards that can be taken with minusone
                        while(b!=0){
                            if(this.diceCount-b==c.getValue()){
                                cards[index]=c;
                                index++;
                            }
                        }
                    }
                }
            }
        }
        return cards;
    }
}
