package cards;

/**
 * A game consists of 48 normal and 12 luck cards
 * */
public enum CardType {
    PLUSONE, //Player can increase count by one for each of these cards
    MINUSONE, //Player can decrease count by one for each of these cards
    EXTRATHROW, //Player can throw the dice again
    CARDSUM, //Player can take cards with combined values of the count
    ONETOTHREE, //Player can take a card with a value between 1-3
    FOURTOSIX,  //Player can take a card with a value between 4-6
}
