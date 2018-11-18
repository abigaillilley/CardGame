package Cards;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardDeckTest {

    private CardDeck testDeck;
    private ArrayList<Card> expected = new ArrayList<>();

    @Before
    public void initialise() {

        for (int i = 1; i <5; i++) {
            expected.add(new Card(i));
        }
        testDeck = new CardDeck(expected, 888);
    }

    @Test
    public void getDeck() {
        ArrayList<Card> actual = testDeck.getDeck();
        assertEquals(expected, actual);
    }

    @Test
    public void setDeck() {
        //ArrayList<Card>
        //testDeck.setDeck();
    }

    @Test
    public void getDeckNum() {
        int expectedDeckNum = 4;
        // not done?

        assertEquals(expectedDeckNum, testDeck.getDeckNum());
    }
}