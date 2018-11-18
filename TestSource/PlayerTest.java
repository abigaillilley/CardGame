import Cards.Card;
import Cards.CardDeck;
import org.junit.Assert.*;
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
    public void getOutputText() {

        ArrayList<String> expectedArray = new ArrayList<>();
        expectedArray.add("Player's output strings");

        ArrayList<String> outputText = new ArrayList<>();

        ArrayList<Card> testHand = new ArrayList<>();
        ArrayList<CardDeck> deckArray = new ArrayList<>();

        Player testPlayer = new Player(testHand, 1, 2, deckArray);

        assertEquals(expectedArray, getOutputText());
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

        ArrayList<String> expected = new ArrayList<>();
        expected.add("String Initial Hand");

        ArrayList<Card> testHand = new ArrayList<>();
        ArrayList<CardDeck> deckArray = new ArrayList<>();

        Player testPlayer = new Player(testHand, 1, 2, deckArray);
        ArrayList<String> outputTest = new ArrayList<>();
        testPlayer.addToOutput("String initial hand");
        assertEquals(expected, testPlayer.outputTest);
    }

    @Test
    public void run() {
        // need to make sure discardDeck doesnt get rid of desired card
        // assert discardDeckIndex.getValue() != playerNum

        // make sure player doesnt discard anything if they havent picked up anything


    }
}
