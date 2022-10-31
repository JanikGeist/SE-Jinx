package entities;

import cards.Card;
import cards.LuckCard;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class representing a player
 * */
public class Player {

    private final String name;
    private final ArrayList<Card> cards;
    private final ArrayList<LuckCard> luckCards;

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

    //TODO: Add try/catch for exception handling

    /**
     * Function to get input of player as STRING
     */
    public String getPlayerInputSTR() {
        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    /**
     * Function to get input of player as INT
     */
    public int getPlayerInputINT(int min, int max) {
        Scanner s = new Scanner(System.in);
        while (true) {
            try {
                int ret = s.nextInt();
                if (ret > max || ret < min) {
                    log("Choose a number in the specified range!" + "[" + min + "," + max + "]");
                } else {
                    return ret;
                }
            } catch (Exception e) {
                log("Enter a valid Number!");
                //read line out of stream to clear it
                s.nextLine();
            }
        }
    }

    /**
     * Function to easily log a msg on the console
     */
    private void log(String msg) {
        System.out.println("[JINX] " + msg);
    }

    /**
     * shows player input on console
     *
     * @param msg
     */
    private void playerlog(String msg){
        System.out.println("[" + this.getName() + "] chose " + msg);
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
