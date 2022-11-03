package entities;

import cards.Card;
import cards.LuckCard;

import java.util.ArrayList;
import java.util.Random;

abstract class Participant {

    protected String name;
    protected ArrayList<Card> cards;
    protected ArrayList<LuckCard> luckCards;

    //needs to be reset every round
    protected int diceCount = 0;
    protected int rolls = 0;
    protected ArrayList<LuckCard> usedCards = new ArrayList<>();
    protected boolean active = true;

    protected Random rand = new Random();

    //Abstract Methods, to be implemented by subclasses
    public abstract String chooseAction();
    public abstract boolean chooseCard(Table table);
    public abstract boolean drawLuckCard(Table table);
    public abstract Card selectCard();
    public abstract boolean selectHighCard();
    public abstract LuckCard selectLuckCard();
    public abstract int roll();
    public abstract boolean mintomax(LuckCard lC, int min, int max);
    public abstract boolean cardSum(LuckCard lC, Table table);
    public abstract boolean extraThrow(LuckCard lC);

    /**
     * Constructor for a participant
     * */
    public Participant(String name){
        this.name = name;
        this.cards = new ArrayList<>();
        this.luckCards = new ArrayList<>();
    }

    //Methods, for all instances the same

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
     * Function to check if the turn of a player has to end because he cant choose a card
     *
     * @param table the current instance of the table
     * @return true if player has no option, false if he does
     */
    protected boolean checkEndRound(Table table) {
        Card[][] field = table.getField();
        for (Card[] row : field) {
            for (Card c : row) {
                if (c != null && c.getValue() == this.diceCount) {
                    //there is a card the player can choose
                    return false;
                }
            }
        }
        //there is no card the player can choose --> end his turn
        return true;
    }

    /**
     * Function to let the player subtract one of his diceCount
     * @param lC the card used by the player
     * */
    public boolean minusOne(LuckCard lC){

        //check if the player rolled the dice already
        if(this.rolls <= 0){
            log(name + ", roll the dice first!");
            return false;
        }

        //check if the subtraction makes sense
        if(this.diceCount <= 1){
            log(name + ", this action wouldnt make much sense!");
            return false;
        }

        //check if the player already played that card
        if(usedCards.contains(lC)){
            log(name + ", you have already played that card!");
            return false;
        }

        //reduce the players diceCount
        this.diceCount--;
        //add card to usedCards, so it cant be played twice
        usedCards.add(lC);

        log(name + ", your new eye count is: " + this.diceCount);
        return true;
    }

    /**
     * Function to let the player add one to his diceCount
     *
     * @param lC the card used by the player
     * */
    public boolean plusOne(LuckCard lC){

        //check if the player rolled the dice already
        if(this.rolls <= 0){
            log(name + ", roll the dice first!");
            return false;
        }

        //check if the addition makes sense
        if(this.diceCount >= 6){
            log(name + ", this action wouldnt make much sense!");
            return false;
        }

        //check if the player already played that card
        if(usedCards.contains(lC)){
            log(name + ", you have already played that card!");
            return false;
        }

        //increase the players diceCount
        this.diceCount++;
        //add card to usedCards, so it cant be played twice
        usedCards.add(lC);

        log(name + ", your new eye count is: " + this.diceCount);
        return true;
    }

    /**
     * Function to easily log a msg on the console
     *
     * */
    protected void log(String msg) {
        System.out.println("[JINX] " + msg);
    }

    /**
     * Function to see if player is still active
     * */
    public boolean isActive(){
        return this.active;
    }

    /**
     * Function to set the player in active status
     * */
    public void setActive(){
        this.active = true;
    }

    /**
     * Function to clear the used cards
     * */
    public void clearUsedCards(){
        this.usedCards.clear();
    }

    /**
     * Function to reset the rolls and the dice count
     * */
    public void resetRolls() {
        this.rolls = 0;
        this.diceCount = 0;
    }

    public int getDiceCount(){
        return this.diceCount;
    }

    public int getRolls(){
        return this.rolls;
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
