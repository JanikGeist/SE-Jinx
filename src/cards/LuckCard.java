package cards;

public class LuckCard {

    CardType cardType;

    /**
     * Constructor for a new Card
     * @param cT CardType enum
     * */
    public LuckCard(CardType cT){
        this.cardType = cT;
    }

    @Override
    public String toString (){
        return "" + this.cardType;
    }
}
