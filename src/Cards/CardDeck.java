package Cards;

import java.util.ArrayList;

/**
 * Stores an ArrayList of the cards in a deck and its identifying number.
 */
public class CardDeck {

    private volatile ArrayList<Card> deck;
    private int deckNum;

    /**
     * CardDeck constructor.
     * @param cards         ArrayList of Card objects in the deck
     * @param deckNumber    Unique deck number
     */
    public CardDeck(ArrayList<Card> cards, int deckNumber){
        deck = cards;
        deckNum = deckNumber;
    }

    /**
     * Getter for Card objects in the deck.
     * @return ArrayList of Card objects in the deck
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }

    /**
     * Updates the deck attribute.
     * @param deck CardDeck object to be updated
     */
    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    /**
     * Getter for the deck's number
     * @return CardDeck's number
     */
    public int getDeckNum() {
        return deckNum;
    }
}
