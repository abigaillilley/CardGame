import Cards.*;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> hand;
    private int playerNum;
    private final int totalNumPlayers;

    Player(ArrayList<Card> hand, int playerNum, int totalNumPlayers) {
        this.hand = hand;
        this.playerNum = playerNum;
        this.totalNumPlayers = totalNumPlayers;
        //TODO look at concurrency lecture, don't use 'this' in a constructor????????????
    }


    public Boolean turn(ArrayList<CardDeck> deckArray)
            //returns true if a player has won on this turn
    {
        synchronized (Player.class) {

            int pickUpDeckIndex = this.playerNum - 1;
            CardDeck deck = deckArray.get(pickUpDeckIndex);
            Card topCard = deck.pickUp();
            hand.add(topCard);

            for (int i=0; i < totalNumPlayers; i++) {
                if (playerNum != hand.get(i).getValue()) {
                    deck.putDown(hand.get(i));
                    hand.remove(i);
                    break;
                }
            }
            return checkForWin(hand);
        }
    }

    public static synchronized Boolean checkForWin(ArrayList<Card> hand) {

        boolean allEqual = true;
        for (Card c : hand) {
            if(c.getValue() != hand.get(0).getValue()) {
                allEqual = false;
                break;
            }
        }
        return allEqual;
    }
}
