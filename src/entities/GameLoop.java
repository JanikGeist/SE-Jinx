package entities;

import actions.*;
import cards.Card;
import cards.CardType;
import cards.LuckCard;

import java.util.*;

/**
 * Class handling game logic
 * TODO Change the way y and x are used to determine a place on the field
 * */
public class GameLoop {

    Table table;
    Player[] players;

    ArrayList<Action> actions;

    public GameLoop(){
        this.table = new Table();
        this.actions = new ArrayList<>();
    }

    /**
     * Call this function to run the game
     * */
    public void run(){
        //init all required fields for the first time
        //init();
        this.players = new Player[]{new Player("a"), new Player("b")};
        for(Player p : players){
            p.addLuckCard(new LuckCard(CardType.PLUSONE));
            p.addLuckCard(new LuckCard(CardType.MINUSONE));
            p.addLuckCard(new LuckCard(CardType.MINUSONE));
        }
        //start the game loop
        loop();
    }

    /**
     * Function to initialize everything needed
     * */
    private void init(){
        System.out.println("Welcome to JINX! How many players do you wish to play with?");
        while(true){
            Scanner s = new Scanner(System.in);
            int playerCount = s.nextInt();
            if(playerCount < 2 || playerCount > 4){
                System.out.println("This game is designed for 2-4 Players! Choose again!");
            }else{
                // set size of players to user specified value
                this.players = new Player[playerCount];
                break;
            }
        }

        // init all players
        initPlayers();
    }

    //TODO: Add try/catch for exception handling
    /**
     * Function to get input of player as STRING
     * */
    private String getPlayerInputSTR(){
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    /**
     * Function to get input of player as INT
     * */
    private int getPlayerInputINT(int min, int max){
        Scanner s = new Scanner(System.in);
        while(true){
            try{
                int ret = s.nextInt();
                if (ret > max || ret < min) {
                    log("Choose a number in the specified range!" + "[" + min + "," + max + "]");
                } else {
                    return ret;
                }
            }catch (Exception e){
                log("Enter a valid Number!");
                //read line out of stream to clear it
                s.nextLine();
            }
        }
    }

    /**
     * Main loop function handling the logic
     * TODO: Split into smaller methods, make it easier to control the loop from the outside
     * */
    private void loop(){
        Random rand = new Random();

        //run 3 rounds
        for(int j = 0; j < 3; j++){
            //current player
            int cP = 0;
            //player that finished a round
            int finisher = 0;
            //set false if player has no actions left --> terminating round
            boolean notOver = true;
            //each round iterate over all players until a player cant take a card anymore
            while(notOver){
                //eye count of players dice roll
                int diceCount = 0;
                //number of rolls player already had
                int rolls = 0;
                //check if player is still able to perform actions
                boolean stillActive = true;
                //track played luckCards to avoid multiple uses
                ArrayList<LuckCard> usedCards = new ArrayList<>();
                //current player
                Player player = this.players[cP];
                //as long as player can perform an action
                while(stillActive){

                    log("\n" + this.table.toString());

                    log("Your turn "+ player.getName() + "! Eye count - " + diceCount );
                    log(player.toString());
                    //let player choose the action
                    log("""
                            Choose your action!
                            R - Roll the Dice
                            L - Play a luck card
                            C - Choose a card - this might end the round!
                            """);

                    String select = getPlayerInputSTR();

                    switch (select) {
                        case "R" -> {
                            if (rolls >= 2) {
                                log("You can´t roll again! Your current eye count is " + diceCount);
                                break;
                            }
                            //Player rolls the dice
                            diceCount = rand.nextInt(6) + 1;
                            //log action of player for rollbacks
                            actions.add(new Roll(player, diceCount));
                            rolls++;
                            log("You rolled a " + diceCount + "!");
                        }
                        case "L" -> {
                            //Player plays a luck card
                            ArrayList<LuckCard> luckCards = player.getLuckCards();
                            if(luckCards.isEmpty()){
                                log("You dont have any luck cards!");
                                break;
                            }
                            log("Which card would you like to play? Type 0 to EXIT\n");
                            for (int i = 0; i < luckCards.size(); i++) {
                                log(luckCards.get(i) + " - " + (i + 1));
                            }

                            //get chosen card pos in arraylist
                            int num = getPlayerInputINT(0, luckCards.size()) - 1;
                            if(num == -1){
                                break;
                            }
                            LuckCard lC = luckCards.get(num);
                            switch (lC.getCardType()) {
                                case CARDSUM -> {
                                    if (diceCount == 0) {
                                        log("Roll the dice first!");
                                        break;
                                    }
                                    //player can choose cards based on rolled number
                                    if (!usedCards.contains(lC)) {
                                        //let the player choose a combination of cards matching the diceCount
                                        if (chooseCards(diceCount, player)) {
                                            usedCards.add(lC);
                                            stillActive = false;
                                        }
                                    } else {
                                        log("You played that card already!");
                                    }
                                }
                                case PLUSONE -> {
                                    if (diceCount == 0) {
                                        log("Roll the dice first!");
                                        break;
                                    }
                                    if(diceCount >= 6){
                                        log("This wouldn't make much sense!");
                                        break;
                                    }
                                    //player can add one or two to the diceCount
                                    if (!usedCards.contains(lC)) {
                                        int z = 0;
                                        LuckCard otherCard = null;
                                        for (LuckCard luck : luckCards) {
                                            if (luck.getCardType() == CardType.PLUSONE) {
                                                z++;
                                                if(luck != lC){
                                                    otherCard = luck;
                                                }
                                            }
                                        }
                                        if (otherCard != null) {
                                            log("What would you like to add? [1,2]");
                                            int addNum = getPlayerInputINT(1, 2);
                                            if(addNum == 2 && diceCount > 4){
                                                log("This wouldn't make much sense!");
                                                break;
                                            }else if(addNum == 2){
                                                //Make sure both cards are added to used cards if the player plays both
                                                usedCards.add(otherCard);
                                            }
                                            diceCount += addNum;
                                        } else {
                                            diceCount++;
                                            log("Your new eye count is " + diceCount);
                                        }
                                        usedCards.add(lC);
                                        actions.add(new Luck(player, lC));
                                    } else {
                                        log("You played that card already!");
                                    }
                                }
                                case MINUSONE -> {
                                    if (diceCount == 0) {
                                        log("Roll the dice first!");
                                        break;
                                    }
                                    if(diceCount <= 1){
                                        log("This wouldn't make much sense!");
                                        break;
                                    }
                                    //player can subtract one or two from the diceCount
                                    if (!usedCards.contains(lC)) {
                                        LuckCard otherCard = null;
                                        int t = 0;
                                        for (LuckCard luck : luckCards) {
                                            if (luck.getCardType() == CardType.MINUSONE) {
                                                t++;
                                                if(luck != lC){
                                                    otherCard = luck;
                                                }
                                            }
                                        }
                                        if (otherCard != null) {
                                            log("What would you like to subtract? [1,2]");
                                            int subNum = getPlayerInputINT(1, 2);
                                            if(subNum == 2 && diceCount < 3){
                                                log("This wouldn't make much sense!");
                                                break;
                                            }else if(subNum == 2){
                                                //make sure to add both cards to used cards to prevent double usage
                                                usedCards.add(otherCard);
                                            }
                                            diceCount -= subNum;
                                        } else {
                                            diceCount--;
                                            log("Your new eye count is " + diceCount);
                                        }
                                        usedCards.add(lC);
                                        actions.add(new Luck(player, lC));
                                    } else {
                                        log("You already played that card!");
                                    }
                                }
                                case FOURTOSIX -> {
                                    //player can choose a card between 4-6
                                    log("Which number do you choose? [4,6]");
                                    diceCount = getPlayerInputINT(4, 6);
                                    //remove luckCard from players hand, since its single use!
                                    player.removeLuckCard(lC);
                                    actions.add(new Luck(player, lC));
                                }
                                case EXTRATHROW -> {
                                    if (diceCount < 2) {
                                        log("You still have an extra throw!");
                                        break;
                                    }
                                    //player can throw the dice again
                                    if (!usedCards.contains(lC)) {
                                        int k = 0;
                                        for (LuckCard luck : luckCards) {
                                            if (luck.getCardType() == CardType.EXTRATHROW) {
                                                k++;
                                            }
                                        }
                                        //roll the dice
                                        diceCount = rand.nextInt(6) + 1;
                                        log("You roll the dice again and roll a " + diceCount);
                                        //check if player could roll again with second card
                                        if (k == 2) {
                                            log("Would you like to roll the dice again? [y/n]");
                                            String again = getPlayerInputSTR();
                                            if (again.equals("y")) {
                                                diceCount = rand.nextInt(6) + 1;
                                                log("You roll the dice again and roll a " + diceCount);
                                            }
                                        }
                                        //make sure player can use card only once per round!
                                        usedCards.add(lC);
                                        actions.add(new Luck(player, lC));
                                    } else {
                                        log("You already played that card!");
                                    }
                                }
                                case ONETOTHREE -> {
                                    //player can choose a card between 1-3
                                    log("Which number do you choose? [1,3]");
                                    diceCount = getPlayerInputINT(1, 3);
                                    //remove luckCard from players hand, since its single use!
                                    player.removeLuckCard(lC);
                                    actions.add(new Luck(player, lC));
                                }
                            }
                        }
                        case "C" -> {
                            //Player chooses a card from the field
                            //only let player choose a card if he already rolled
                            if (diceCount == 0) {
                                log("Roll the dice first!");
                                break;
                            }

                            //check if the player is able to pick a card, if not the round turn!
                            if(checkEndRound(diceCount)){
                                //round
                                notOver = false;
                                stillActive = false;
                                finisher = cP;
                                break;
                            }

                            log("Which card would you like to take? Current diceCount: " + diceCount);
                            log("Enter the cards position as y,x");
                            //get player input and parse it into coordinates
                            String[] coordsSTR = getPlayerInputSTR().split(",");
                            try {
                                //subtract one to get back to array counting
                                int[] coords = {Integer.parseInt(coordsSTR[0]) - 1, Integer.parseInt(coordsSTR[1]) - 1};


                                //get a card from the field
                                Card chosenOne = table.getCard(coords[0], coords[1]);

                                if (chosenOne == null) {
                                    log("There is no card at that position!");
                                    break;
                                } else if (chosenOne.getValue() != diceCount) {
                                    log("You can only choose a card equal to the value of your diceCount!");
                                    this.table.addCard(coords[0], coords[1], chosenOne);
                                    break;
                                }
                                //add card to players hand
                                player.addCard(chosenOne);
                                //log that player has taken a card from the field
                                actions.add(new Choose(player, chosenOne, coords[0], coords[1]));
                                //end players turn, since he has chosen a card
                                stillActive = false;
                            }catch (Exception e){
                                log("Choose a valid combination!");
                            }
                        }
                        default -> log("Not an option! Try Again!");
                    }
                }
                //make sure current player always loops
                cP = (cP + 1) % (players.length);
            }
            //clean up after round

            log("Round Over! All your cards will be checked and possibly removed now");
            //check which cards the players have to drop
            for(Player p : players){
                checkPlayerHand(p);
            }

            //let finisher drop his highest card
            ArrayList<Card> drops = dropHighCard(players[finisher]);
            if(drops.size() > 0) {
                log(players[finisher].getName() + ", choose a card you wish to drop");
                for (int i = 0; i < drops.size(); i++) {
                    log(drops.get(i) + " - " + i);
                }


                int selection = getPlayerInputINT(0,drops.size() - 1);
                //remove the card
                players[finisher].removeCard(drops.get(selection));
                log(drops.get(selection) + " has been removed from your hand!");

            }else{
                log(players[finisher].getName() + ", has no cards available to drop...");
            }

            //let each player draw a luckCard
            drawLuckCards(finisher);

            //deal new cards
            this.table.resetField();
        }
    //all 3 rounds ended, calculate score here
    log("Game Over!");
    }

    /**
     * Function to handle the drawing of luck cards at the end a round
     * TODO Split this up into 2 methods, the choosing can be handled by a single method
     * */
    private void drawLuckCards(int finisher){
        //start with the finisher
        Card[] hand = players[finisher].getCards().toArray(new Card[0]);
        if(hand.length > 0) {
            log(players[finisher].getName() + ", choose a card you wish to drop to draw a luckCard");
            //list all cards he has
            for (int i = 0; i < hand.length; i++) {
                log(hand[i] + " - " + i);
            }
            log("Enter " + hand.length + "to not pick a card");
            //ask for input
            int input = getPlayerInputINT(0, hand.length);
            //only remove card if input is valid and player wants to do so!
            if (!(input < 0 || input >= hand.length)) {
                //remove the card from players hand
                players[finisher].removeCard(hand[input]);
                //get new card from luckCardStack
                LuckCard lC = this.table.drawLuckCard();
                //add luckCard to players hand
                players[finisher].addLuckCard(lC);
                //log both actions!
                actions.add(new Remove(players[finisher], hand[input]));
                actions.add(new Draw(players[finisher], lC));
            }
        }else{
            log(players[finisher].getName() + ", has no cards to exchange for a luck card!");
        }

        //handle the rest of the players
        for(Player p : players){
            //finisher already drew his card
            if(p != players[finisher]) {
                // get the hand of the current player
                hand = p.getCards().toArray(new Card[0]);
                if (hand.length > 0) {
                    log(p.getName() + ", choose a card you wish to drop to draw a luckCard");
                    for (int i = 0; i < hand.length; i++) {
                        log(hand[i] + " - " + i);
                    }
                    log("Enter " + hand.length + "to not pick a card");
                    //ask player which card he wants to drop
                    int input = getPlayerInputINT(0, hand.length);
                    if (input < 0 || input >= hand.length) {
                        return;
                    } else {
                        //remove card from players hand
                        p.removeCard(hand[input]);
                        //draw and add new luckCard
                        LuckCard lC = this.table.drawLuckCard();
                        p.addLuckCard(lC);

                        //log actions
                        actions.add(new Remove(p, hand[input]));
                        actions.add(new Draw(p, lC));
                    }
                }else {
                    log(p.getName() + ", has no Cards to exchange!");
                }
            }
        }
    }

    /**
     * Function to remove the highest card from the finisher
     * */
    private ArrayList<Card> dropHighCard(Player p){
        ArrayList<Card> maxCards = new ArrayList<>();
        int currentHigh = 0;
        Card[] hand = p.getCards().toArray(new Card[0]);

        for(Card c : hand){
            if(c.getValue() > currentHigh){
                currentHigh = c.getValue();
            }
        }

        for(Card c : hand){
            if(c.getValue() == currentHigh){
                maxCards.add(c);
            }
        }

        return maxCards;
    }
    /**
     * Function to remove cards from player hand if round ends
     * */
    private void checkPlayerHand(Player p){
        Card[] hand = p.getCards().toArray(new Card[0]);
        Card[][] field = this.table.getField();

        for(Card cP : hand){
            for(Card[] row: field){
                for(Card cF : row){
                    if(cF != null && cP.getColor() == cF.getColor()){
                        //Player has a card with the same color on his hand --> remove it!
                        p.removeCard(cP);
                    }
                }
            }
        }
    }

    /**
     * Function to check if the turn of a player has to end because he cant choose a card
     * @param diceCount the current eye count of the player
     * @return true if player has no option, false if he does
     * */
    private boolean checkEndRound(int diceCount){
        Card[][] field = this.table.getField();
        for(Card[] row : field){
            for(Card c : row){
                if(c != null && c.getValue() == diceCount){
                    //there is a card the player can choose
                    return false;
                }
            }
        }
        //there is no card the player can choose --> end his turn
        return true;
    }

    /**
     * Function to let a player choose a card based on his eye count
     * @param diceCount current eye count as player chose this option
     * @param player who chose this option
     * @return true if player chose a valid combination and has the cards on his hand, false otherwise
     * */
    private boolean chooseCards(int diceCount, Player player){
        log("Your diceCount is "  + diceCount + " choose cards equivalent to that number!");

        int sum = 0;
        ArrayList<Card> cards = new ArrayList<>();
        //let player select cards until he matches the diceCount
        int[][] coords = new int[10][10];
        while (sum != diceCount) {
            log("You need to match your diceCount: " + diceCount);
            log("Enter all cards you want to select like this: y,x;y,x;y,x;...;y,x if you want to break type BREAK");
            //get input from player
            String input = getPlayerInputSTR();
            if(input.equals("BREAK")){
                break;
            }
            //parse input
            coords = parseCoordinateInput(input);
            //only proceed if player entered correct coordinate format
            if (coords == null) {
                log("You entered a wrong format! Try again!");
            }else{
                for (int[] coord : coords) {
                    Card card = this.table.getCard(coord[0], coord[1]);
                    if(card != null){
                        cards.add(card);
                        sum += card.getValue();
                    }
                }
                if(sum != diceCount){
                    // sum doesn't match, put all cards back on the table
                    int z = 0;
                    for(Card c : cards){
                        this.table.addCard(coords[z][0],coords[z][1],c);
                    }
                    //clear collection of cards
                    cards.clear();
                    //reset the sum counter
                    sum = 0;
                }
            }
        }

        if(sum == diceCount){
            //player entered a correct combination of cards!
            int y = 0;
            for(Card c : cards){
                player.addCard(c);
                actions.add(new Choose(player,c,coords[y][0],coords[y][1])); //TODO: CHECK IF THIS WORKS :/
            }
            log(player.toString());
            return true;
        }else{
            //player stopped card selection
            log("Stopping Card selection!");
            return false;
        }
    }

    /**
     * Function to parse a string of coordinates into a 2D integer array
     * @param input array like y,x;y,x;...;y,x
     * @return int[][] of coord-pairs, null if error
     * */
    private int[][] parseCoordinateInput(String input){
            try{
                //split string into coordinate segments
                String[] coordPair = input.split(";");
                //init int[][] with correct dimensions
                int[][] ret = new int[coordPair.length][2];
                int i = 0;
                for(String s: coordPair){
                    //split coordinate segements into coordinates
                    String[] coordSTR = s.split(",");
                    //store coordinate as pairs, account for array counting
                    ret[i][0] = Integer.parseInt(coordSTR[0]) - 1;
                    ret[i][1] = Integer.parseInt(coordSTR[1]) - 1;
                    i++;
                }
                return ret;
            }catch (Exception e){
                //return null if something went wrong
                return null;
            }
    }

    /**
     * Function to reset the game
     * */
    private void resetGame(){
        //create new table
        this.table = new Table();
        //delete all players
        this.players = null;

        //restart game;
        init();
    }

    /**
     * Function to easily log a msg on the console
     * */
    private void log(String msg){
        System.out.println("[JINX] " + msg);
    }

    //TODO: Fail save, only let player enter valid names
    /**
     * Function to initialize all players by name
     * */
    private void initPlayers(){

        Scanner s = new Scanner(System.in);

        //create as many players as needed
        for(int i = 0; i < players.length; i++){
            //ask player for name, until confirmed
            while(true) {
                log("Welcome Player" + (i + 1) + " whats your name?");
                String name = s.nextLine();
                log("Are you sure your Name is: " + name + " [y/n]");
                String con = s.nextLine();
                //check confirmation
                if (con.equals("y")) {
                    //created player with entered name
                    players[i] = new Player(name);
                    break;
                }
            }
        }
    }
}
