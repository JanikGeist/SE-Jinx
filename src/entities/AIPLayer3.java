package entities;

import cards.Card;
import cards.CardColor;
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

    Card cardOnTable=null;

    @Override
    public String chooseAction(Table table) {
        this.cardOnTable=null;
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
        int[] coloursOnTable = this.findAmountByColour(table);
        int index = 0;
        int amount = coloursOnTable[0];
        //gets index of smallest amount of cards of a colour
        for(int a = 0; a<coloursOnTable.length; a++){
            if(coloursOnTable[a]<amount||amount==0){
                index=a;
                amount=coloursOnTable[a];
            }
        }
        while(this.cardOnTable==null){
            String[] availableCards = this.findValidCards(table);
            //if player has no cards
            CardColor cardColor = null;
            if(this.cards.size()==0){
                log("I don't have any cards.");
                switch (index){
                    case 0:
                        cardColor=CardColor.RED;
                        break;

                }

            }
        }
        return "C";
        //TODO try to use extrathrow if no card is available
        //TODO drawLuckCard, override
        //TODO draw luck card, depending on other players' score
        //TODO use luck card
        //TODO remove card at end of round
        //TODO end method with return "C" -> choose a card to take from table, perhabs save coord in var, then return in getinput method
    }
    //TODO getplayerinputcoord override
    //TODO use cardsum, use immediately, when available
    //TODO use1to3
    //TODO use4to6

    /**
     * finds all cards the AI could take
     *
     * @param table
     * @return
     */
    private String[] findValidCards(Table table){
        String[] cards = new String[16];
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
        //row, y-coordinate
        int ycoord = 1;
        for(Card[] card:table.getField()) {
            //x-coordinate
            int xcoord = 1;
            for (Card c : card) {
                if(c!=null){
                    if(this.diceCount==c.getValue()){
                        cards[index]=ycoord + "," + xcoord;
                        index++;
                    }
                    else{
                        int a=plus;
                        int b=minus;
                        //all cards that can be taken with plusone
                        while(a!=0){
                            if(this.diceCount+a==c.getValue()){
                                cards[index]=ycoord + "," + xcoord;
                                index++;
                            }
                        }
                        //all cards that can be taken with minusone
                        while(b!=0){
                            if(this.diceCount-b==c.getValue()){
                                cards[index]=ycoord + "," + xcoord;
                                index++;
                            }
                        }
                    }
                }
                xcoord++;
            }
            ycoord++;
        }
        return cards;
    }

    /**
     * gets the amount of cards on table by colour
     * in order red, green, blue, yellow, purple, orange, grey, white
     *
     * @param table
     * @return
     */
    public int[] findAmountByColour(Table table){
        int[] cardByColour = new int[8];
        for(Card[] card:table.getField()) {
            for (Card c : card) {
                if(c!=null){
                    switch (c.getColor()){
                        case RED -> cardByColour[0]++;
                        case GREEN -> cardByColour[1]++;
                        case BLUE -> cardByColour[2]++;
                        case YELLOW -> cardByColour[3]++;
                        case PURPLE -> cardByColour[4]++;
                        case ORANGE -> cardByColour[5]++;
                        case GREY -> cardByColour[6]++;
                        case WHITE -> cardByColour[7]++;
                    }
                }
            }
        }
        return cardByColour;
    }
}
