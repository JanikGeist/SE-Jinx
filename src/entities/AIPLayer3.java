package entities;

import actions.Luck;
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

    boolean usecsum=true;
    int myRound=1;
    String cardOnTable=null;
    private boolean active = true;

    @Override
    public String chooseAction(Table table) {
        if(this.myRound!=GameLoop.currentRound){
            this.myRound++;
            this.cardOnTable=null;
        }
        if (this.cardOnTable == null) {
            if (this.rolls == 0) {
                this.playerlog("First I roll the dice!");
                return "R";
            }
            CardColor[] desiredColour = new CardColor[8];
            int col = 0;
            String[] availableCards = this.findValidCards(table);
            //if player has no cards
            if (this.cards.size() == 0) {
                playerlog("I will try to take a card with few other cards of that colour on the table.");
                int[] coloursOnTable = this.findAmountByColour(table);
                int amount = 100;
                int c = 0;
                //gets index of smallest amount of cards of a colour
                for (int b = 0; b < 8; b++) {
                    for (int a = 0; a < coloursOnTable.length; a++) {
                        if (coloursOnTable[a] < amount || amount == 0) {
                            amount = coloursOnTable[a];
                            c = a;
                        }
                    }
                    coloursOnTable[c] = 100;
                    CardColor adding = CardColor.GREEN;
                    switch (c) {
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
                    desiredColour[b] = adding;
                    amount = 100;
                }

                //if player already has cards
            } else {
                playerlog("I already have cards. I will try to get a card of the same colour so I can keep them.");
                //calculates value in hand by colour
                int[] valueByColour = new int[8];
                for (Card c : this.cards) {
                    switch (c.getColor()) {
                        case RED -> valueByColour[0] = valueByColour[0] + c.getValue();
                        case GREEN -> valueByColour[1] = valueByColour[1] + c.getValue();
                        case BLUE -> valueByColour[2] = valueByColour[2] + c.getValue();
                        case YELLOW -> valueByColour[3] = valueByColour[3] + c.getValue();
                        case PURPLE -> valueByColour[4] = valueByColour[4] + c.getValue();
                        case ORANGE -> valueByColour[5] = valueByColour[5] + c.getValue();
                        case GREY -> valueByColour[6] = valueByColour[6] + c.getValue();
                        case WHITE -> valueByColour[7] = valueByColour[7] + c.getValue();
                    }
                }
                int val = -1;
                int c = 0;
                //order card colours by value in hand
                for (int a = 0; a < 8; a++) {
                    for (int b = 0; b < 8; b++) {
                        if (valueByColour[b] > val) {
                            val = valueByColour[b];
                            c = b;
                        }
                    }
                    valueByColour[c] = -2;
                    val = -1;
                    CardColor cardColor = CardColor.GREEN;
                    switch (c) {
                        case 0:
                            cardColor = CardColor.RED;
                            break;
                        case 1:
                            cardColor = CardColor.GREEN;
                            break;
                        case 2:
                            cardColor = CardColor.BLUE;
                            break;
                        case 3:
                            cardColor = CardColor.YELLOW;
                            break;
                        case 4:
                            cardColor = CardColor.PURPLE;
                            break;
                        case 5:
                            cardColor = CardColor.ORANGE;
                            break;
                        case 6:
                            cardColor = CardColor.GREY;
                            break;
                        case 7:
                            cardColor = CardColor.WHITE;
                            break;
                    }
                    desiredColour[a] = cardColor;
                }
            }
            if (cardOnTable == null) {
                for (CardColor currColour : desiredColour) {
                    for (String card : availableCards) {
                        if (card != null) {
                            String[] coord = card.split(",");
                            Card[][] field = table.getField();
                            Card c = field[Integer.parseInt(coord[0]) - 1][Integer.parseInt(coord[1]) - 1];
                            if ((c.getColor().equals(currColour))) {
                                this.cardOnTable = card;
                                if (this.diceCount != c.getValue()) {
                                    playerlog("I might need a luckcard to get the card i want.");
                                    return "L";
                                }
                                break;
                            }
                        }
                    }
                    if (cardOnTable != null) {
                        break;
                    }
                }
            }
            //if no matching card was found, roll again
            if (cardOnTable == null && this.rolls < 2) {
                return "R";
            }
            if (cardOnTable == null && this.rolls == 2) {
                for (LuckCard luckCard : this.getLuckCards()) {
                    if (luckCard.getCardType() == CardType.EXTRATHROW && !(usedCards.contains(luckCard))) {
                        return "L";
                    }
                }
            }
            //if a card was found, take it
            else if (cardOnTable != null) {
                return "C";
            }
        }
        for(LuckCard luckCard:this.getLuckCards()){
            if(luckCard.getCardType()==CardType.CARDSUM&&usecsum){
                playerlog("I will try to use my cardsum card. I cannot get a card otherwise.");
                return "L";
            }
        }
        //if no card was found and player cant roll again
        String[] availableCards = this.findValidCards(table);
            if(availableCards[0]==null){
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
            this.rolls=0;
            this.cardOnTable=null;
            //TODO fix
            playerlog("Something is wrong, I have to start over.");
        return this.chooseAction(table);
    }

    /**
     * finds all cards the AI could take
     *
     * @param table
     * @return
     */
    private String[] findValidCards(Table table){
        boolean oneThree = false;
        boolean fourSix=false;
        for(LuckCard luckCard:this.getLuckCards()){
            if(luckCard.getCardType().equals(CardType.ONETOTHREE)){
                oneThree=true;
            }
            else if(luckCard.getCardType().equals(CardType.FOURTOSIX)){
                fourSix=true;
            }
        }
        String[] cards = new String[16];
        int index = 0;
        int plus = 0;
        int minus = 0;
        for(LuckCard luckCard:this.getLuckCards()){
            //compareTo returns 0 if they are equal
            if(luckCard.getCardType().compareTo(CardType.PLUSONE)==0&&!this.usedCards.contains(luckCard)){
                plus++;
            }
            else if(luckCard.getCardType().compareTo(CardType.MINUSONE)==0&&!this.usedCards.contains(luckCard)){
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
                    else if(oneThree&&c.getValue()<4){
                            cards[index]=ycoord + "," + xcoord;
                            index++;
                    }
                    else if(fourSix&&c.getValue()>3){
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
                            a--;
                        }
                        //all cards that can be taken with minusone
                        while(b!=0){
                            if(this.diceCount-b==c.getValue()){
                                cards[index]=ycoord + "," + xcoord;
                                index++;
                            }
                            b--;
                        }
                    }
                }
                xcoord++;
            }
            ycoord++;
        }
        playerlog("These are the cards I could take:");
        for(String card:cards){
            if(card!=null){
                playerlog(card);
            }
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
        this.cardOnTable=null;
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

    @Override
    public Card selectCard(Player[] players) {
        if(this.myRound==3){
            playerlog("The game is over, I dont need more cards.");
            return null;
        }
        Card card1=null;
        CardColor[] desiredColour = new CardColor[8];
        boolean draw=true;
        for(Player p:players){
            //if a player has at least the same score
            if(p.getScore()>=(this.getScore())){
                if(p!=this){
                    draw=false;
                    playerlog("I want to keep my cards. My opponent's score is too high.");
                }
            }
        }
        if(draw){
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
            ArrayList<Card> smallCards=new ArrayList<>();
            for(int a=1; a<7;a++){
                for(Card card:this.cards){
                    if(card.getValue()==a){
                        smallCards.add(card);
                    }
                }
                if(!smallCards.isEmpty()){
                    break;
                }
            }
            CardColor[] order=new CardColor[8];
            int index=0;
            for(int a=desiredColour.length-1;a>=0;a--){
                order[index]=desiredColour[a];
                index++;
            }
            for(CardColor color:order){
                for(Card card:smallCards){
                    if(card.getColor().equals(color)){
                        card1=card;
                        break;
                    }
                }
                if(card1!=null){
                    break;
                }
            }
        }
        if(card1!=null){
            playerlog("I will trade " + card1.getColor() + " " + card1.getValue() + ". My score is good enough compared to my opponents.");
        }
        return card1;
    }

    @Override
    public LuckCard selectLuckCard(Table table) {
        //check if player has luck cards
        if(this.getLuckCards().size() == 0){
            log(this.getName() + ", you dont have any luck cards!");
            return null;
        }
        //EXTRATHROW
        if(this.cardOnTable==null&&this.rolls==2){
            for(LuckCard luckCard:this.getLuckCards()){
                if(luckCard.getCardType()==CardType.EXTRATHROW&&!this.usedCards.contains(luckCard)){
                    playerlog("I want to throw again. I will use my luckcard!");
                    return luckCard;
                }
            }
        }
        else if(this.cardOnTable!=null){
            String line = this.cardOnTable;
            String[] coordsSTR = line.split(",");
            String coord=coordsSTR[0] + "," + coordsSTR[1];
            int[] coordInt = new int[2];
            coordInt[0]=Integer.parseInt(coordsSTR[0]);
            coordInt[1]=Integer.parseInt(coordsSTR[1]);
            Card c = table.getField()[coordInt[0]-1][coordInt[1]-1];
            if(c.getValue()==this.diceCount-1){
                //MINUSONE
                for(LuckCard luckCard:this.getLuckCards()){
                    if(luckCard.getCardType()==CardType.MINUSONE){
                        playerlog("I need a smaller dice count. I will use my luckcard!");
                        return luckCard;
                    }
                }
            } else if (c.getValue()==this.diceCount-1) {
                //PLUSONE
                for(LuckCard luckCard:this.getLuckCards()){
                    if(luckCard.getCardType()==CardType.PLUSONE){
                        playerlog("I need a smaller dice count. I will use my luckcard!");
                        return luckCard;
                    }
                }
            }
            else if(c.getValue()>3&&this.diceCount!=c.getValue()){
                for(LuckCard luckCard:this.getLuckCards()){
                    if(luckCard.getCardType()==CardType.FOURTOSIX){
                        return luckCard;
                    }
                }
            }
            else if(c.getValue()<4&&this.diceCount!=c.getValue()){
                for(LuckCard luckCard:this.getLuckCards()){
                    if(luckCard.getCardType()==CardType.ONETOTHREE){
                        return luckCard;
                    }
                }
            }
            else{
                for(LuckCard luckCard:this.getLuckCards()){
                    if(luckCard.getCardType()==CardType.CARDSUM){
                        return luckCard;
                    }
                }
            }
        }
        return this.getLuckCards().get(0);
    }

    public int playerInputNumberInRange(int min, int max){
        if(min<this.diceCount&&max>this.diceCount){
            return this.diceCount;
        }
        return min;
    }

    @Override
    public String getPlayerInputMultipleCoordinates(Table table) {
        ArrayList<Card> cardsToTake = new ArrayList<>();
        String coordinates="";
        for(Card[] row:table.getField()){
            for(Card card:row){
                if(card!=null){
                    if(card.getValue()<this.diceCount){
                        cardsToTake.add(card);
                    }
                }
            }
        }
        ArrayList<Card> cardByVal= new ArrayList<>();
        for(int a=diceCount-1;a>0;a--){
            for(Card card:cardsToTake){
                if(card.getValue()==a){
                    cardByVal.add(card);
                }
            }
        }
        Card card1=null;
        Card card2=null;
        for(int a=0; a<cardByVal.size();a++){
            for(int b=0; b<cardByVal.size();b++){
                if(a==b){
                    b++;
                }
                else{
                    if(cardByVal.get(a).getValue()+cardByVal.get(b).getValue()==this.diceCount){
                        card1=cardByVal.get(a);
                        card2=cardByVal.get(b);
                        break;
                    }
                }
            }
            if(card1!=null){
                break;
            }
        }
        if(card1==null||card2==null){
            usecsum=false;
            playerlog("My Luckcard is not helping me here! I have to try something different");
            return "0";
        }
        String coordinate1="";
        String coordinate2="";
        int y=0;
        int x=0;
        for(Card[] row:table.getField()){
            y++;
            for(Card card:row){
                x++;
                if(card==card1){
                    coordinate1=x+","+y;
                } else if (card==card2) {
                    coordinate2=x+","+y;
                }
            }
        }
        coordinates=coordinate1+";"+coordinate2;
        usecsum=false;
        return coordinates;
    }

    public void playerlog(String msg){
        System.out.println("[AI]" + msg);
    }
}
