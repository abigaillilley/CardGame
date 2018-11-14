package Cards;

import java.util.ArrayList;

public class CardDeck {

    private volatile ArrayList<Card> deck;

    public CardDeck(ArrayList<Card> cards){

        this.deck = cards;
    }

    public Card pickUp() throws InterruptedException {

        try {
            while (deck.size() == 0) {
                wait();
            }
            Card topCard = deck.get(0);
            deck.remove(0);
            return topCard;
        }
        catch(InterruptedException e) {
            return null;
        }
    }

    public void putDown(Card discardCard) {

        deck.add(discardCard);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
