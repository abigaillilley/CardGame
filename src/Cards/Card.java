package Cards;

/**
 * Stores the denomination of a card
 *
 * @author 670032202
 * @author 670048585
 */
public class Card {

    private final int value;

    /**
     * Card constructor.
     * @param value Card denomination
     */
    public Card(int value){

        this.value = value;
    }


    /**
     * Getter for card denomination.
     * @return int value of the card's denomination.
     */
    public int getValue(){

        return value;
    }
}
