package cards;

public class LuckCard {

    private CardType cardType;

    /**
     * Constructor for a new Card
     * @param cT CardType enum
     * */
    public LuckCard(CardType cT){
        this.cardType = cT;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    @Override
    public String toString (){
        return "" + this.cardType;
    }

}
