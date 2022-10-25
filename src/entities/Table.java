package entities;

import cards.Card;
import cards.CardColor;
import cards.CardType;
import cards.LuckCard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Class representing a playing field containing 2 stacks of cards and the playing field
 * */
public class Table {

    // Stack containing all normal cards
    private  Stack<Card> cardStack;
    // Stack containing all luck cards
    private  Stack<LuckCard> luckStack;
    // Actual playing field with a 4x4 of normal cards
    private  Card[][] field;

    public Table(boolean readFromFile){
        this.cardStack = new Stack<>();
        this.luckStack = new Stack<>();
        this.field = new Card[4][4];

        //initialize all card stacks randomly or with config file
        if(readFromFile){
            readConfig();
        }
        else{
            initCards();
            initLuckCards();
        }
        initField();
    }
    public void setCardStack(Stack<Card> cards){
        this.cardStack=cards;
    }

    public void setField(Card[][] field) {
        this.field = field;
    }

    public void setLuckStack(Stack<LuckCard> luckStack) {
        this.luckStack = luckStack;
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
     * Function to reset the field if new round begins
     * */
    public void resetField(){
        if(this.cardStack.size() >= 16) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    this.field[i][j] = cardStack.pop();
                }
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
     * @return card at position x,y, null if there is none
     * */
    public Card getCard(int x,int y){

        Card selected = null;
        //find card at coords (x,y) on field
        try {
            selected = this.field[x][y];

            //remove card from field
            this.field[x][y] = null;

        }catch(Exception e){
            System.out.println("");
        }

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

    /**
     * Function to check if there is a card with the same value as x
     * @param x number to look for
     * @return boolean true if there is a card with the same value
     * */
    private boolean checkForCard(int x){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(this.field[i][j].getValue() == x){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    //TODO: Maybe rework this to represent the field more accurate
    public String toString(){
        StringBuilder ret = new StringBuilder("");

        for(int i = 0; i < 4; i++){
            ret.append("[");
            for(int j = 0; j < 4; j++){
                ret.append(this.field[i][j]+"  ");
                if(j < 3) {
                    ret.append(",");
                }
            }
            ret.append("]\n");
        }
        return ret.toString();
    }

    /**
     * get card order for both stacks from config file
     */
    public void readConfig(){
        String[] cardOrder;

        try{
            BufferedReader br = new BufferedReader(new FileReader("src/entities/configfile.csv"));

            String order = br.readLine();
            cardOrder = order.split(",");
            this.cardStack.clear();
            for(String cardName : cardOrder){
                String[] cardInfo = new String[2];
                cardInfo = cardName.split(" ");
                this.cardStack.add(new Card(CardColor.valueOf(cardInfo[0]),Integer.valueOf(cardInfo[1])));
            }
            order = br.readLine();
            cardOrder = order.split(",");
            this.luckStack.clear();
            for(String cardName : cardOrder){
                this.luckStack.add(new LuckCard(CardType.valueOf(cardName)));
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stack<Card> getCardStack() {
        return this.cardStack;
    }

    public Stack<LuckCard> getLuckStack() {
        return this.luckStack;
    }


}
