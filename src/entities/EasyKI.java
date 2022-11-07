package entities;

import cards.Card;
import cards.CardColor;

public class EasyKI extends Player{

    public EasyKI (String name){
        super(name);
    }

    @Override
    public String chooseAction(Table table){
        log("Your turn " + this.name + "! Eye count - " + this.diceCount);
        log(this.toString());

        //roll if not rolled yet
        if(this.rolls == 0){
            log(this.name + "[AI], i didnt roll the dice yet!");
            return "R";
        }
        //check if there is a card that can be picked, if not and rolls are available --> roll
        if(cardAvailable(table) == null && this.rolls < 2){
            log(this.name + "[AI], there is no card i want...I´ll roll again!");
            return "R";
        }

        log(this.name + "[AI], i will choose a card now!");
        //nothing left to do, possibly end round
        return "C";

    }

    private Card cardAvailable(Table table){
        Card[][] karten= table.getField();
        for (int a=0; a<karten.length; a++){
            for (int b=0; b<karten[0].length; b++){
                Card c= table.checkCard(a,b);
                if (c!=null){
                    if (c.getValue()==this.diceCount){
                        System.out.println(c.getValue());
                        return c;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean chooseCard(Table table){
        //check if the AI has to end its turn because it has no options to pick a card
        if(checkEndRound(table)){
            log(this.name + "[AI], there is no card you could choose!");
            //set AI inactive to end its turn;
            this.active = false;
            return false;
        }
        //find the best possible card to pick from the field
        Card chosenOne = cardAvailable(table);

        //add card to AIs hand and remove it from field
        addCard(table.getCard(chosenOne));
        //set the AI as inactive, since this action ended its turn
        this.active = false;
        //signal that the AI has chosen a card successfully
        return true;
    }

    @Override
    public boolean selectHighCard(){
        Card away= new Card(CardColor.BLUE,0);
        if (this.cards.size()!=0){
            for (Card c:this.cards){
                if (c.getValue()>=away.getValue()){
                    away=c;
                }
            }
            this.cards.remove(away);
        }
        return false;
    }

    @Override
    public boolean drawLuckCard(Table table){
        log(this.name + "[AI], i would never waste points for a luck card!");
        return false;
    }
}
