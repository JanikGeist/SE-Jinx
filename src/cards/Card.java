package cards;

/**
 * Class representing a card from the game JINX
 * */
public class Card {

    private final CardColor cardColor;
    // between 1-6
    private final int value;

    /**
     * Constructor for a new cards.Card
     * @param cC cards.CardColor enum
     * @param v CardValue [1,6] int
     * */
    public Card(CardColor cC, int v){
        this.cardColor = cC;
        this.value = v;
    }

    /**
     * Gets card value
     * */
    public int getValue(){
        return this.value;
    }

    /**
     * Gets card color
     * */
    public CardColor getColor(){
        return this.cardColor;
    }

    @Override
    public String toString (){
        return "" + this.value + "/" + this.cardColor;
    }
}
