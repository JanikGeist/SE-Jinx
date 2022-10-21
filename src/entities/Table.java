package entities;

import cards.Card;
import cards.CardColor;
import cards.CardType;
import cards.LuckCard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Class representing a playing field containing 2 stacks of cards and the playing field
 * */
public class Table {

    // Stack containing all normal cards
    private final Stack<Card> cardStack;
    // Stack containing all luck cards
    private final Stack<LuckCard> luckStack;
    // Actual playing field with a 4x4 of normal cards
    private final Card[][] field;

    public Table(){
        this.cardStack = new Stack<>();
        this.luckStack = new Stack<>();
        this.field = new Card[4][4];

        //initialize all card stacks
        initCards();
        initLuckCards();
        initField();
    }

    //TODO: Add config to give the option of set card distribution
    /**
     * Function to init the card stack
     * */
    private void initCards(){

        Card[] cards = new Card[48];
        //generate 8*6 Cards
        for(int j=0; j<8; j++) {
            for (int i = 0; i < 6; i++) {
                cards[j*6+i] = new Card(CardColor.values()[j],i+1);
            }
        }

        //randomly shuffle the cards
        List<Card> lC = Arrays.asList(cards);
        Collections.shuffle(lC);

        //add all cards to the Stack
        this.cardStack.addAll(lC);
    }

    /**
     * Function to init the luckCard stack
     * */
    private void initLuckCards(){

        LuckCard[] luckCards = new LuckCard[12];
        //generate 2*6 Cards
        for(int j=0; j<2; j++) {
            for (int i = 0; i < 6; i++) {
                luckCards[j*6+i] = new LuckCard(CardType.values()[i]);
            }
        }

        //randomly shuffle the cards
        List<LuckCard> lC = Arrays.asList(luckCards);
        Collections.shuffle(lC);

        //add all cards to the Stack
        this.luckStack.addAll(lC);
    }

    /**
     * Function to setup the play field
     * */
    private void initField(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                this.field[i][j] = cardStack.pop();
            }
        }
    }

    /**
     * Function to draw a luckCard from the luckCard stack
     * */
    public LuckCard drawLuckCard(){
        return luckStack.pop();
    }

    /**
     * Function to get the field
     * */
    public Card[][] getField(){
        return this.field;
    }

    /**
     * Function to remove a card from the field.
     * @param x x coordinate of card
     * @param y y coordinate of card
     * @return card at position x,y
     * */
    public Card getCard(int x,int y){

        //find card at coords (x,y) on field
        Card selected = this.field[x][y];

        //remove card from field
        this.field[x][y] = null;

        //return selected Card
        return selected;
    }

    /**
     * Function to add a card to the field
     * @param x x coordinate of card
     * @param y y coordinate of card
     * @param c card to be added
     * @return true/false, success/failed
     * */
    public boolean addCard(int x, int y, Card c){
        if(this.field[x][y] == null){
            this.field[x][y] = c;
            return true;
        }else{
            return false;
        }
    }

    @Override
    //TODO: Maybe rework this to represent the field more accurate
    public String toString(){
        StringBuilder ret = new StringBuilder("");

        for(int i = 0; i < 4; i++){
            ret.append("[");
            for(int j = 0; j < 4; j++){
                ret.append(this.field[i][j]);
                if(j < 3) {
                    ret.append(",");
                }
            }
            ret.append("]\n");
        }
        return ret.toString();
    }
}
