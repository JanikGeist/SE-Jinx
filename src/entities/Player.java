package entities;

import actions.Choose;
import actions.Roll;
import actions.manipulation.liste.TableRunde;
import cards.Card;
import cards.LuckCard;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Class representing a player
 * */
public class Player {

    private final String name;
    private final ArrayList<Card> cards;
    private final ArrayList<LuckCard> luckCards;

    private int diceCount = 0;

    //needs to be reset after each round
    private int rolls = 0;

    //used to roll the dice
    Random rand = new Random();

    /**
     * Constructor for a new player
     * @param name name of player
     * */
    public Player(String name){
        this.name = name;
        this.cards = new ArrayList<Card>();
        this.luckCards = new ArrayList<LuckCard>();
    }

    /**
     * returns current hand of player
     * */
    public ArrayList<Card> getCards(){
        return this.cards;
    }

    /**
     * returns current luckcards of player
     * */
    public ArrayList<LuckCard> getLuckCards(){
        return this.luckCards;
    }

    /**
     * returns players name
     * */
    public String getName(){
        return this.name;
    }

    /**
     * Adds a card to the players hand
     * */
    public void addCard(Card card){
        this.cards.add(card);
    }

    /**
     * Adds a card to the players luck cards
     * */
    public void addLuckCard(LuckCard luckCard){
        this.luckCards.add(luckCard);
    }

    public void removeLuckCard(LuckCard luckCard){
        this.luckCards.remove(luckCard);
    }

    /**
     * Removes a card from the players hand
     * @param pos position of card in players hand, counting from 0!
     * @return null if no card was found otherwise card in position pos
     * */
    public Card removeCard(int pos){
        try{
            Card card = this.cards.get(pos);
            this.cards.remove(pos);
            return card;
        }catch (IndexOutOfBoundsException e){
            System.out.println("[ERROR] No Card found!");
            return null;
        }
    }
    /**
     * Overloaded removeCard function to remove a card by reference
     * @param card card to be removed
     * @return true if card was remove, false if card was not found
     * */
    public boolean removeCard(Card card){
        try{
            this.cards.remove(card);
            return true;
        }catch (Exception e){
            System.out.println("[ERROR] No Card found!");
            return false;
        }
    }

    /**
     * Calculates the current score of the player
     * @return current score as int
     * */
    public int getScore(){
        int score = 0;

        for(Card c : this.cards){
            score += c.getValue();
        }

        return score;
    }


    /**
     * Function to let the player choose a card
     * @param table the current playing field
     * @return true if card was chosen, false if no card was chosen
     * */
    public boolean chooseCard(Table table){
        //check if player is able to choose a card
        if(this.diceCount <= 0){
            log("Roll the dice first!");
            return false;
        }

        log("Which card would you like to take? Current diceCount: " + diceCount);
        log("Enter the cards position as y,x");

        //TODO swap this with the real input function!
        Scanner s = new Scanner(System.in);
        String[] coordsSTR = s.nextLine().split(",");
        //TODO do it!

        try{
            //subtract one to get back to array counting
            int[] coords = {Integer.parseInt(coordsSTR[0]) - 1, Integer.parseInt(coordsSTR[1]) - 1};

            //get a card from the field
            Card chosenOne = table.getCard(coords[0], coords[1]);

            if (chosenOne == null) {
                log("There is no card at that position!");
                return false;
            } else if (chosenOne.getValue() != diceCount) {
                log("You can only choose a card equal to the value of your diceCount!");
                table.addCard(coords[0], coords[1], chosenOne);
                return false;
            }
            //add card to players hand
            addCard(chosenOne);



            //log that player has taken a card from the field
            //TODO do the logging for undo/redo here
            /*
            actions.add(new Choose(player, chosenOne, coords[0], coords[1]));
            verlauf.zugEinfuegen(player, chosenOne.getTyp(), chosenOne.getColor(), chosenOne.getValue(), "ZahlDazuVonTisch");
            tabelVerlauf.add( new TableRunde(table.getField(), table.getCardStack(), table.getLuckStack()));
             */

            //signal that player has chosen a card successfully
            return true;
        } catch (Exception e) {
            log("Choose a valid combination!");
            return false;
        }
    }

    /**
     * Lets the player roll the dice
     * Automaticly sets the players diceCount
     * @return the number the player rolled, current DiceCount if nothing changed
     * */
    public int roll(){
        if(this.rolls >= 2){
            log("You can´t roll again! Your current eye count is " + diceCount);
            return this.diceCount;
        }

        //Player rolls the dice
        this.diceCount = rand.nextInt(6) + 1;
        //log action of player for rollbacks
        //TODO log the roll of the player here for RE/UNDO

        this.rolls++;

        log("You rolled a " + this.diceCount + "!");
        return this.diceCount;
    }

    /**
     * Function to easily log a msg on the console
     * */
    private void log(String msg) {
        System.out.println("[JINX] " + msg);
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder("");

        if(this.cards.size() == 0 && this.luckCards.size() == 0){
            return this.name + "\n[]\n[]";
        }

        ret.append(this.name);
        ret.append("\n");
        ret.append("[");
        for(int i = 0; i < this.cards.size(); i++){
            ret.append(cards.get(i).toString());
            if(i < this.cards.size() - 1){
                ret.append(",");
            }
        }
        ret.append("]\n");
        ret.append(" [");
        for(int i = 0; i < this.luckCards.size(); i++){
            ret.append(luckCards.get(i).toString());
            if(i < this.luckCards.size() - 1){
                ret.append(",");
            }
        }
        ret.append("]\n");

        return ret.toString();
    }
}
