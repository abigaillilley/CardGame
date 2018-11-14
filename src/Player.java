import Cards.*;

import java.util.ArrayList;

public class Player implements Runnable {
    //TODO run() method?????????????????????????

    private ArrayList<String> outputText = new ArrayList<>();
    private ArrayList<Card> hand;
    private int playerNum;
    private final int totalNumPlayers;
    private int numTurns = 0;
    public static volatile int highestNumTurns = 0;
    public static volatile ArrayList<CardDeck> deckArray;

    Player(ArrayList<Card> inputHand, int inputPlayerNum, int totalPlayers, ArrayList<CardDeck> inputCardDecks) {
        hand = inputHand;
        playerNum = inputPlayerNum;
        totalNumPlayers = totalPlayers;
        deckArray = inputCardDecks;
    }

    public Boolean turn(ArrayList<CardDeck> deckArray) {
            //returns true if a player has won on this turn{
        synchronized (Player.class) {

            int pickUpDeckIndex = this.playerNum - 1;
            CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
            try {
                Card topCard = pickUpDeck.pickUp();
                if (topCard != null) {
                    hand.add(topCard);
                    addToOutput("Player " + getPlayerNum() + " draws " + topCard.getValue() + " from Deck " + getPlayerNum());
                    int discardDeckIndex = playerNum % totalNumPlayers;
                    CardDeck discardDeck = deckArray.get(discardDeckIndex);

                    for (int i=0; i < totalNumPlayers; i++) {

                        if (playerNum != hand.get(i).getValue()) {

                            discardDeck.putDown(hand.get(i));
                            addToOutput("Player " + getPlayerNum() + " discards " + hand.get(i).getValue() + " to Deck " + (discardDeckIndex + 1));
                            hand.remove(i);

                            ArrayList<String> cardValues = new ArrayList<>();
                            for (Card card: hand) {

                                cardValues.add(Integer.toString(card.getValue()));

                            }

                            addToOutput("Player " + getPlayerNum() + " current hand: " + cardValues);

                            numTurns += 1;
                            if (numTurns > highestNumTurns) {

                                highestNumTurns = numTurns;
                            }

                            break;
                        }
                    }

                }
            } catch(InterruptedException e) {}

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

    public void evenTurns() {
    //while your tuns is less than highestTurns, then pickup and put down card
        while (numTurns < highestNumTurns) {
            int pickUpDeckIndex = this.playerNum - 1;
            CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
            try {
                Card transferCard = pickUpDeck.pickUp();
                if (transferCard != null) {
                    int discardDeckIndex = playerNum % totalNumPlayers;
                    CardDeck discardDeck = deckArray.get(discardDeckIndex);
                    discardDeck.putDown(transferCard);

                    numTurns += 1;
                }
            } catch (InterruptedException e ) {}
        }
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

    public void run(){
        Boolean gameWon = false;
        while (!gameWon){
            gameWon = turn(deckArray);
        }

        evenTurns();


    }
}
