import org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getPlayerNum() {
        // returns an integer greater than 0
        // for player num in player array
    }

    @Test
    public void getOutputText() {
        // returns an arraylist
        //String expectedArray = { }

        //void assertArrayEquals( expectedArray, resultArray);
    }
    @Test
    public void getHandValues() {
        // assert hand equals arraylist with 4 cards
        ArrayList<Integer> expected = new ArrayList<>();
        assertEquals(expected, Player.getHandValues());

    }
    @Test
    public void addToOutput() {
    }

    @Test
    public void run() {
        // need to make sure discardDeck doesnt get rid of desired card
        // assert discardDeckIndex.getValue() != playerNum

        // make sure player doesnt discard anything if they havent picked up anything

    }
}