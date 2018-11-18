import Cards.Card;
import Cards.CardDeck;
import org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getPlayerNum() {

        // for player num in player array

        assertEquals("Cant be player zero", 1, 0);
    }

    @Test
    public void getOutputText() {
        // returns an arraylist
        //String expectedArray = { }

        //void assertArrayEquals( expectedArray, resultArray);
    }
    @Test
    public void getHandValues() {
        ArrayList<Card> testHand = new ArrayList<>();

        for(int i=1; i < 5; i++) {
            Card testCard = new Card(i);
            testHand.add(testCard);
        }

        ArrayList<CardDeck> deckArray = new ArrayList<>();
        for(int i=0; i < 4; i++) {
            CardDeck testDeck = new CardDeck(testHand, 1);
            deckArray.add(testDeck);
        }
        Player testPlayer = new Player(testHand, 1, 2, deckArray);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);

        assertEquals(expected, testPlayer.getHandValues());

    }
    @Test
    public void addToOutput() {
        // make sure input is added
    }

    @Test
    public void run() {
        // need to make sure discardDeck doesnt get rid of desired card
        // assert discardDeckIndex.getValue() != playerNum

        // make sure player doesnt discard anything if they havent picked up anything

    }
}
