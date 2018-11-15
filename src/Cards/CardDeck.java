package Cards;

import java.util.ArrayList;

public class CardDeck {


    private volatile ArrayList<Card> deck;
    private int deckNum;


    public CardDeck(ArrayList<Card> cards, int deckNumber){
        deck = cards;
        deckNum = deckNumber;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public int getDeckNum() {
        return deckNum;
    }
}
