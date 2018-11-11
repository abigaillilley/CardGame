package Cards;

import java.util.ArrayList;

public class CardDeck {


    //private final int deckNumber;
    private volatile ArrayList<Card> deck;


    CardDeck(ArrayList<Card> cards){

        //this.deckNumber = deckNumber;
        this.deck = cards;
    }

    public Card pickUp(){

        Card topCard = deck.get(0);
        deck.remove(0);
        return topCard;

    }

    public void putDown(Card discardCard) {

        deck.add(discardCard);
    }



}
