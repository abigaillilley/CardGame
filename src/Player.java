import Cards.Card;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> hand;
    private int playerNum;
    private final int totalNumPlayers;

    Player(ArrayList<Card> hand, int playerNum, int totalNumPlayers) {
        this.hand = hand;
        this.playerNum = playerNum;
        this.totalNumPlayers = totalNumPlayers;
    }


    public static synchronized Boolean turn()
    {

        int pickUpDeckNum = (this.playerNum - 1) % this.totalNumPlayers

        this.hand.add(
    }

}
