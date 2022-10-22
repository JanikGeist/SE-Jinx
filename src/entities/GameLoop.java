package entities;

import actions.*;
import cards.Card;
import cards.CardType;
import cards.LuckCard;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Class handling game logic
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
        init();
        //start the game loop
        loop();
    }

    /**
     * Function to initialize everything needed
     * */
    private void init(){
        System.out.println("Welcome to JINX! How many players do you wish to play with?");
        Scanner s = new Scanner(System.in);
        int playerCount = s.nextInt();
        // set size of players to user specified value
        this.players = new Player[playerCount];

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
            int ret = s.nextInt();
            if (ret > max || ret < min) {
                log("Choose a number in the specified range!" + "[" + min + "," + max + "]");
            } else {
                return ret;
            }
        }
    }

    /**
     * Main loop function handling the logic
     * TODO: Check for round end, if a player cant take a card. Do so every time a player chose an option i guess
     * TODO: Test this lol
     * */
    private void loop(){
        Random rand = new Random();


        //Display table
        log("\n" + table.toString());

        //run 3 rounds
        for(int j = 0; j < 3; j++){
            //current player
            int cP = 0;
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
                //as long as player can perform an action
                while(stillActive){
                    //current player
                    Player player = this.players[cP];
                    log("Your turn "+ player.getName() + "!");

                    //let player choose the action
                    log("""
                            Choose your action!
                            R - Roll the Dice
                            L - Play a luck card
                            C - Choose a card from the field
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
                            log("Which card would you like to play?\n");
                            for (int i = 0; i < luckCards.size(); i++) {
                                log(luckCards.get(i) + " - " + i);
                            }

                            //get chosen card pos in arraylist
                            int num = getPlayerInputINT(0, luckCards.size());
                            LuckCard lC = luckCards.get(num);
                            switch (lC.getCardType()) {
                                case CARDSUM:
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
                                    break;
                                case PLUSONE:
                                    //player can add one or two to the diceCount
                                    if (!usedCards.contains(lC)) {
                                        int z = 0;
                                        for (LuckCard luck : luckCards) {
                                            if (luck.getCardType() == CardType.PLUSONE) {
                                                z++;
                                            }
                                        }
                                        if (z == 2) {
                                            log("What would you like to add? [1,2]");
                                            int addNum = getPlayerInputINT(1, 2);
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
                                    break;
                                case MINUSONE:
                                    //player can subtract one or two from the diceCount
                                    if (!usedCards.contains(lC)) {
                                        int t = 0;
                                        for (LuckCard luck : luckCards) {
                                            if (luck.getCardType() == CardType.MINUSONE) {
                                                t++;
                                            }
                                        }
                                        if (t == 2) {
                                            log("What would you like to subtract? [1,2]");
                                            int subNum = getPlayerInputINT(1, 2);
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
                                    break;
                                case FOURTOSIX:
                                    //player can choose a card between 4-6
                                    log("Which number do you choose? [4,6]");
                                    diceCount = getPlayerInputINT(4, 6);
                                    //remove luckCard from players hand, since its single use!
                                    player.removeLuckCard(lC);
                                    actions.add(new Luck(player, lC));
                                    break;
                                case EXTRATHROW:
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
                                    break;
                                case ONETOTHREE:
                                    //player can choose a card between 1-3
                                    log("Which number do you choose? [1,3]");
                                    diceCount = getPlayerInputINT(1, 3);
                                    //remove luckCard from players hand, since its single use!
                                    player.removeLuckCard(lC);
                                    actions.add(new Luck(player, lC));
                                    break;
                            }
                        }
                        case "C" -> {
                            //Player chooses a card from the field
                            //only let player choose a card if he already rolled
                            if (diceCount == 0) {
                                log("Roll the dice first!");
                                break;
                            }
                            log("Which card would you like to take? Current diceCount: " + diceCount); //TODO: Check if player can choose this if he has already taken cards
                            log("Enter the cards position as x,y");
                            //get player input and parse it into coordinates
                            String[] coordsSTR = getPlayerInputSTR().split(",");
                            int[] coords = {Integer.parseInt(coordsSTR[0]), Integer.parseInt(coordsSTR[1])};

                            //get a card from the field
                            Card chosenOne = table.getCard(coords[0], coords[1]);
                            //add card to players hand
                            player.addCard(chosenOne);
                            //log that player has taken a card from the field
                            actions.add(new Choose(player, chosenOne, coords[0], coords[1]));
                            //end players turn, since he has chosen a card
                            stillActive = false;
                        }
                        default -> log("Not an option! Try Again!");
                    }
                }
                cP++;
            }

            //clean up after round
        }
    }

    /**
     * Function to let a player choose a card based on his eye count
     * @param diceCount current eye count as player chose this option
     * @param player who chose this option
     * @return true if player chose a valid combination and has the cards on his hand, false otherwise
     * */
    private boolean chooseCards(int diceCount, Player player){
        log("Your diceCount is "  + diceCount + " choose cards equivalent to that number!");
        log("To choose a card type the coordinates x,y");
        int sum = 0;
        ArrayList<Card> cards = new ArrayList<>();
        //let player select cards until he matches the diceCount
        int[][] coords = new int[10][10];
        while (sum != diceCount) {
            log("You need to match your diceCount: " + diceCount);
            log("Enter all cards you want to select like this: x,y;x,y;x,y;...;x,y if you want to break type BREAK");
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
                    cards.add(card);
                    sum += card.getValue();
                }
                if(sum != diceCount){
                    // sum doesn't match, put all cards back on the table
                    int z = 0;
                    for(Card c : cards){
                        this.table.addCard(coords[z][0],coords[z][1],c);
                    }
                    //clear collection of cards
                    cards.clear();
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
     * @param input array like x,y;x,y;...;x,y
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
                    //store coordinate as pairs
                    ret[i][0] = Integer.parseInt(coordSTR[0]);
                    ret[i][1] = Integer.parseInt(coordSTR[1]);
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
