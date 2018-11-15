package Cards;

import java.util.ArrayList;

public class CardDeck {


    private volatile ArrayList<Card> deck;


    public CardDeck(ArrayList<Card> cards){
        this.deck = cards;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }
}
