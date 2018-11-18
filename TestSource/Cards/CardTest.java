package Cards;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void getValue() {

        int expected = 888;
        Card testCard = new Card(expected);

        assertEquals(expected, testCard.getValue());

    }
}