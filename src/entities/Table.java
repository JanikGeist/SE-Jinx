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
 * Class representing a playing field containing 2 stacks of cards
 * */
public class Table {

    // Stack containing all normal cards
    Stack<Card> cardStack;
    // Stack containing all luck cards
    Stack<LuckCard> luckStack;
    // Actual playing field with a 4x4 of normal cards
    Card[][] mid;

    public Table(){
        this.cardStack = new Stack<>();
        this.luckStack = new Stack<>();
        this.mid = new Card[4][4];

        //initialize all card stacks
        initCards();
        initLuckCards();
    }

    //TODO: Add config to give the option of set card distribution
    /**
     * Function to init the card stack
     * */
    public void initCards(){

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
    public void initLuckCards(){

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
    public void initField(){

    }

    /**
     * Function to draw a card from the top of the stack
     * */
    public Card drawCard(){
        return null;
    }

    /**
     * Function to draw a luckCard from the luckCard stack
     * */
    public LuckCard drawLuckCard(){
        return null;
    }

    @Override
    //TODO: Maybe rework this to represent the field more accurate
    public String toString(){
        return mid.toString();
    }
}
