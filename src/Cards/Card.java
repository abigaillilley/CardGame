package Cards;

public class Card {

    private final int value;
    private static int counter = 0;

    Card(int value){

        this.value = value;
    }

    int getValue(){
        return value;
    }

}
