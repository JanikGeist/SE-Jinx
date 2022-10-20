package cards;

public class LuckCard {

    CardType cardType;

    /**
     * Constructor for a new cards.Card
     * @param cT cards.CardType enum
     * */
    public LuckCard(CardType cT){
        this.cardType = cT;
    }

    @Override
    public String toString (){
        return "Luck Card: " + this.cardType;
    }
}
