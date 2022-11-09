package entities;

import cards.Card;
import cards.CardColor;
import cards.CardType;
import cards.LuckCard;

import java.util.ArrayList;

public class AIPLayer3 extends Player{

    /**
     * Constructor for a new AIplayer with level 3
     *
     * @param name name of player
     */
    public AIPLayer3(String name) {
        super(name);
    }

    String cardOnTable=null;
    private boolean active = true;

    @Override
    public String chooseAction(Table table) {
        this.cardOnTable=null;
        if(this.rolls==0){
            this.log("First I roll the dice!");
            return "R";
        }
        CardColor[] desiredColour = new CardColor[8];
        int col=0;
        String[] availableCards = this.findValidCards(table);
        for(String cardcoord:availableCards){
            log(cardcoord);
        }
        //if player has no cards
        if(this.cards.size()==0){
            int[] coloursOnTable = this.findAmountByColour(table);
            int amount = 100;
            int c=0;
            //gets index of smallest amount of cards of a colour
            for(int b =0; b<8; b++){
                for(int a = 0; a<coloursOnTable.length; a++){
                    if(coloursOnTable[a]<amount||amount==0){
                        amount=coloursOnTable[a];
                        c=a;
                        }
                    }
                coloursOnTable[c]=100;
                CardColor adding=CardColor.GREEN;
            switch(c) {
                case 0:
                    adding = CardColor.RED;
                    break;
                case 1:
                    adding = CardColor.GREEN;
                    break;
                case 2:
                    adding = CardColor.BLUE;
                    break;
                case 3:
                    adding = CardColor.YELLOW;
                    break;
                case 4:
                    adding = CardColor.PURPLE;
                    break;
                case 5:
                    adding = CardColor.ORANGE;
                    break;
                case 6:
                    adding = CardColor.GREY;
                    break;
                case 7:
                    adding = CardColor.WHITE;
                    break;

            }
                desiredColour[b]=adding;
            amount=100;
            }

        //if player already has cards
        }else{
            //calculates value in hand by colour
            int[] valueByColour = new int[8];
                for (Card c : this.cards) {
                        switch (c.getColor()){
                            case RED -> valueByColour[0]=valueByColour[0]+c.getValue();
                            case GREEN -> valueByColour[1]=valueByColour[1]+c.getValue();
                            case BLUE -> valueByColour[2]=valueByColour[2]+c.getValue();
                            case YELLOW -> valueByColour[3]=valueByColour[3]+c.getValue();
                            case PURPLE -> valueByColour[4]=valueByColour[4]+c.getValue();
                            case ORANGE -> valueByColour[5]=valueByColour[5]+c.getValue();
                            case GREY -> valueByColour[6]=valueByColour[6]+c.getValue();
                            case WHITE -> valueByColour[7]=valueByColour[7]+c.getValue();
                        }
                }
                int val=-1;
                int c=0;
                //order card colours by value in hand
                for(int a=0; a<8;a++){
                    for(int b=0;b<8;b++){
                        if(valueByColour[b]>val){
                            val=valueByColour[b];
                            c=b;
                        }
                    }
                    valueByColour[c]=-2;
                    val=-1;
                    CardColor cardColor=CardColor.GREEN;
                    switch (c){
                        case 0:
                            cardColor=CardColor.RED;
                            break;
                        case 1:
                            cardColor=CardColor.GREEN;
                            break;
                        case 2:
                            cardColor=CardColor.BLUE;
                            break;
                        case 3:
                            cardColor=CardColor.YELLOW;
                            break;
                        case 4:
                            cardColor=CardColor.PURPLE;
                            break;
                        case 5:
                            cardColor=CardColor.ORANGE;
                            break;
                        case 6:
                            cardColor=CardColor.GREY;
                            break;
                        case 7:
                            cardColor=CardColor.WHITE;
                            break;
                    }
                    desiredColour[a]=cardColor;
                }
        }

        if(cardOnTable==null){
            for(CardColor currColour:desiredColour){
                log(currColour.name());
                for(String card:availableCards){
                    if(card!=null){
                        log("hellow");
                        String[] coord = card.split(",");
                        log(card);
                        Card[][] field = table.getField();
                        Card c = field[Integer.parseInt(coord[0])-1][Integer.parseInt(coord[1])-1];
                        if((c.getColor().equals(currColour))){
                            log("saved");
                            this.cardOnTable=card;
                            break;
                            //TODO wenn noch zeit ist, höchste karte suchen, evtl in neues array speichern und davon die höchste suchen
                        }
                    }
                }
                if(cardOnTable!=null){
                    break;
                }
            }
        }
        //if no matching card was found, roll again
        if(cardOnTable==null&&this.rolls<2){
            return "R";
        }
        //if a card was found, take it
        else if(cardOnTable!=null){
            return "C";
        }
        //if no card was found and player cant roll again
        else if(cardOnTable==null&&this.rolls==2){
            this.cardOnTable="1,1";
            for(int a = 0;table.getField().length>a;a++){
                for(int b=0; table.getField().length>b;b++) {
                    if(table.getField()[a][b]==null){
                        this.cardOnTable=(a+1) + "," + (b+1);
                    }
                }
            }
            return "C";
        }
        return null;
        //TODO try to use extrathrow if no card is available
        //TODO drawLuckCard, override
        //TODO draw luck card, depending on other players' score
        //TODO use luck card
    }
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

    @Override
    public int[] getPlayerInputCoord() {
        String line = this.cardOnTable;
        String[] coordsSTR = line.split(",");

        String coord=coordsSTR[0] + "," + coordsSTR[1];
        this.log("chosen: " + coord);
        int[] coordInt = new int[2];
        coordInt[0]=Integer.parseInt(coordsSTR[0]);
        coordInt[1]=Integer.parseInt(coordsSTR[1]);
        return coordInt;
    }

    @Override
    public boolean selectHighCard(){

        //check if the player is able to drop a card
        if(this.cards.size() == 0){
            log(this.getName() + " has no cards to drop after this round!");
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

        log(this.getName() + ", you finished the round! Choose a card to drop!");
        //List all cards available to choose
        for (int i = 0; i < maxCards.size(); i++) {
            log(maxCards.get(i) + " - " + i);
        }

        CardColor[] Colour = new CardColor[8];
        //calculates value in hand by colour
        int[] valueByColour = new int[8];
        for (Card c : this.cards) {
            switch (c.getColor()){
                case RED -> valueByColour[0]=valueByColour[0]+c.getValue();
                case GREEN -> valueByColour[1]=valueByColour[1]+c.getValue();
                case BLUE -> valueByColour[2]=valueByColour[2]+c.getValue();
                case YELLOW -> valueByColour[3]=valueByColour[3]+c.getValue();
                case PURPLE -> valueByColour[4]=valueByColour[4]+c.getValue();
                case ORANGE -> valueByColour[5]=valueByColour[5]+c.getValue();
                case GREY -> valueByColour[6]=valueByColour[6]+c.getValue();
                case WHITE -> valueByColour[7]=valueByColour[7]+c.getValue();
            }
        }
        int val=-1;
        int c=0;
        //order card colours by value in hand
        for(int a=0; a<8;a++){
            for(int b=0;b<8;b++){
                if(valueByColour[b]>val){
                    val=valueByColour[b];
                    c=b;
                }
            }
            valueByColour[c]=-2;
            val=-1;
            CardColor cardColor=CardColor.GREEN;
            switch (c){
                case 0:
                    cardColor=CardColor.RED;
                    break;
                case 1:
                    cardColor=CardColor.GREEN;
                    break;
                case 2:
                    cardColor=CardColor.BLUE;
                    break;
                case 3:
                    cardColor=CardColor.YELLOW;
                    break;
                case 4:
                    cardColor=CardColor.PURPLE;
                    break;
                case 5:
                    cardColor=CardColor.ORANGE;
                    break;
                case 6:
                    cardColor=CardColor.GREY;
                    break;
                case 7:
                    cardColor=CardColor.WHITE;
                    break;
            }
            Colour[c]=cardColor;
        }

        boolean set=false;
        int removing=0;
        for(int a= Colour.length-1;a>=0;a--){
            for(int b=0;b<maxCards.size();b++){
                if(maxCards.get(b).getColor().equals(Colour[a])){
                    removing=b;
                    set=true;
                    break;
                }
            }
            if(set){
                break;
            }
        }

        while(true){
            try{
                this.cards.remove(maxCards.get(removing));
                log("Card "+removing+" was chosen.");
                return true;
            }catch (Exception e){
                log("Not a valid choice.");
                this.cards.remove(maxCards.get(0));
                return true;
            }
        }
    }
}
