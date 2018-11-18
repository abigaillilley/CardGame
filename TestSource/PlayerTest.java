import Cards.Card;
import Cards.CardDeck;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getPlayerNum() {
        ArrayList<Card> testHand = new ArrayList<>();
        ArrayList<CardDeck> deckArray = new ArrayList<>();

        Player testPlayer = new Player(testHand, 1, 2, deckArray);
        int expected = 1;

        assertEquals(expected, testPlayer.getPlayerNum());
    }

    @Test
    public void getHandValues() {

        ArrayList<Card> testHand = new ArrayList<>();
        for(int i=1; i < 5; i++) {
            Card testCard = new Card(i);
            testHand.add(testCard);
        }
        ArrayList<CardDeck> deckArray = new ArrayList<>();

        for (int i = 0; i < 4; i++) {

            CardDeck testDeck = new CardDeck(testHand, 1);
            deckArray.add(testDeck);
        }
        Player testPlayer = new Player(testHand, 1, 2, deckArray);
        assertEquals(testHand, testPlayer.getHandValues());

    }
}