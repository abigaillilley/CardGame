import Cards.*;

import java.util.ArrayList;

public class Player {
    //TODO run() method?????????????????????????

    private ArrayList<String> outputText = new ArrayList<>();
    private ArrayList<Card> hand;
    private int playerNum;
    private final int totalNumPlayers;

    Player(ArrayList<Card> inputHand, int inputPlayerNum, int totalPlayers) {
        hand = inputHand;
        playerNum = inputPlayerNum;
        totalNumPlayers = totalPlayers;
    }


    public Boolean turn(ArrayList<CardDeck> deckArray)
            //returns true if a player has won on this turn
    {
        synchronized (Player.class) {

            int pickUpDeckIndex = this.playerNum - 1;
            CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
            Card topCard = pickUpDeck.pickUp();
            hand.add(topCard);

            this.addToOutput("Player " + this.getPlayerNum() + " draws " + topCard.getValue() + " from Deck " + this.getPlayerNum());

            int discardDeckIndex = this.playerNum % this.totalNumPlayers;
            CardDeck discardDeck = deckArray.get(discardDeckIndex);

            for (int i=0; i < totalNumPlayers; i++) {

                if (playerNum != hand.get(i).getValue()) {

                    discardDeck.putDown(hand.get(i));

                    this.addToOutput("Player " + this.getPlayerNum() + " discards " + hand.get(i).getValue() + " to Deck " + (discardDeckIndex + 1));

                    hand.remove(i);

                    ArrayList<String> cardValues = new ArrayList<>();
                    for (Card card: hand) {
                        cardValues.add(Integer.toString(card.getValue()));
                    }
                    this.addToOutput("Player " + this.getPlayerNum() + " current hand: " + cardValues);

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

    public ArrayList<Card> getHand() {

        return hand;
    }

    public int getPlayerNum() {

        return playerNum;
    }

    public ArrayList<String> getOutputText() {
        return outputText;
    }

    public void addToOutput(String text) {

        this.outputText.add(text);
    }
}
