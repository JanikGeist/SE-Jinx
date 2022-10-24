package actions.manipulation.liste;

import cards.Card;
import cards.LuckCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Logger;

public class TableRunde {
    static int number=0;

    private Card[][] feld = new Card[4][4];
    private Stack<Card>normals = new Stack<>();
    private Stack<LuckCard> luckys = new Stack<>();

    public TableRunde (Card[][] feld, Stack<Card> normals, Stack<LuckCard> luckys){
        number++;

        this.normals.addAll(normals);
        this.luckys.addAll(luckys);

        this.feld=copy(feld);

    }

    public static Card[][] copy(Card[][] src) {
        if (src == null) {
            return null;
        }

        Card[][] copy = new Card[src.length][];

        for (int i = 0; i < src.length; i++) {
            copy[i] = new Card[src[i].length];
            System.arraycopy(src[i], 0, copy[i], 0, src[i].length);
        }

        return copy;
    }

    public Card[][] getFeld() {
        return feld;
    }

    public Stack<Card> getNormals() {
        return normals;
    }

    public Stack<LuckCard> getLuckys() {
        return luckys;
    }

    public int getNumber() {
        return number;
    }
}
