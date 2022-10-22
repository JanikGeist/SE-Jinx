package entities;

import cards.Card;
import cards.LuckCard;

import java.util.ArrayList;

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

    @Override
    public String toString(){
        return "" + this.name + "\n" + this.cards.toString() + "Luck Cards: " + this.luckCards.toString();
    }
}
