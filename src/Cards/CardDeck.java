package Cards;

import java.util.ArrayList;

public class CardDeck {


    //private final int deckNumber;
    private volatile ArrayList<Card> deck;


    public CardDeck(ArrayList<Card> cards){

        //this.deckNumber = deckNumber;
        this.deck = cards;
    }

    public Card pickUp(){
        //TODO check there is actually a card to be picked up
        // if not then the thread needs to wait
        Card topCard = deck.get(0);
        deck.remove(0);
        return topCard;

    }

    public void putDown(Card discardCard) {

        deck.add(discardCard);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
