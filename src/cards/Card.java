package cards;

/**
 * Class representing a card from the game JINX
 * */
public class Card {

    private final CardColor cardColor;
    // between 1-6
    private final int value;
    private CardType typ;

    /**
     * Constructor for a new cards.Card
     * @param cC cards.CardColor enum
     * @param v CardValue [1,6] int
     * */
    public Card(CardColor cC, int v){
        this.cardColor = cC;
        this.value = v;
        this.typ=CardType.NORMAL;
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

    /**
     *
     * @return Card Typ
     */
    public CardType getTyp() {
        return typ;
    }

    @Override
    public String toString (){
        return "" + this.value + "/" + this.cardColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card c1 = (Card) o;

        return this.cardColor.equals(c1.cardColor);
    }
}
