import Cards.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class Player implements Runnable {

    private ArrayList<String> outputText = new ArrayList<>();
    private ArrayList<Card> hand;
    private int playerNum;
    private final int totalNumPlayers;
    private int numTurns = 0;
    private static volatile int highestNumTurns = 0;
    private static volatile ArrayList<CardDeck> deckArray;
    private static AtomicBoolean gameWon = new AtomicBoolean(false);
    private static String winner;


    Player(ArrayList<Card> inputHand, int inputPlayerNum, int totalPlayers, ArrayList<CardDeck> inputCardDecks) {

        hand = inputHand;
        playerNum = inputPlayerNum;
        totalNumPlayers = totalPlayers;
        deckArray = inputCardDecks;
    }


    private void turn(ArrayList<CardDeck> deckArray) {

        if (!gameWon.get()) {

            int pickUpDeckIndex = this.playerNum - 1;
            CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);

            Card topCard = pickUp(pickUpDeck);

            if (topCard != null) {

                hand.add(topCard);
                addToOutput("Player " + getPlayerNum() + " draws " + topCard.getValue() + " from Deck " +
                        getPlayerNum());

                int discardDeckIndex = playerNum % totalNumPlayers;
                CardDeck discardDeck = deckArray.get(discardDeckIndex);

                for (int i = 0; i < 4; i++) {

                    if (playerNum != hand.get(i).getValue()) {

                        putDown(hand.get(i), discardDeck);
                        addToOutput("Player " + getPlayerNum() + " discards " + hand.get(i).getValue() +
                                " to Deck " + (discardDeckIndex + 1));
                        hand.remove(i);

                        //testing TODO is this testing or legit code??????
                        ArrayList<String> cardValues = new ArrayList<>();
                        for (Card card : hand) {
                            cardValues.add(Integer.toString(card.getValue()));
                        }
                        addToOutput("Player " + getPlayerNum() + " current hand: " + cardValues);

                        break;
                    }
                }
                checkForWin(hand);

                numTurns += 1;
                if (numTurns > highestNumTurns) {
                    highestNumTurns = numTurns;
                }
            }
        }
    }


    private static synchronized void checkForWin(ArrayList<Card> hand) {

        boolean allEqual = true;

        for (Card c : hand) {

            if (c.getValue() != hand.get(0).getValue()) {

                allEqual = false;
                break;
            }
        }

        if (allEqual && !gameWon.get()) {
            gameWon.set(true);
            winner = Thread.currentThread().getName();
        }
    }


    private void evenTurns() {
        //while your turns is less than highestTurns, then pickup and put down card

        int pickUpDeckIndex = this.playerNum - 1;
        CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
        Card transferCard = pickUp(pickUpDeck);

        if (transferCard != null) {

            int discardDeckIndex = playerNum % totalNumPlayers;
            CardDeck discardDeck = deckArray.get(discardDeckIndex);
            putDown(transferCard, discardDeck);

            numTurns += 1;
        }
    }


    private Card pickUp(CardDeck cardDeck){

        ArrayList<Card> deck = cardDeck.getDeck();

        Card topCard = deck.get(0);
        deck.remove(0);

        return topCard;
    }


    private void putDown(Card discardCard, CardDeck discardDeck) {

        ArrayList<Card> deck = discardDeck.getDeck();
        deck.add(discardCard);
        discardDeck.setDeck(deck);
    }


    public ArrayList<Card> getHand() { //TODO method never used?????
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

        Thread.currentThread().setName("Player " + playerNum);

        int pickUpDeckIndex = this.playerNum - 1;
        CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);

        synchronized (Player.class) {
            checkForWin(hand);
        }

        //testing
        //System.out.println(Thread.currentThread().getName() + "  " +numTurns);

        try {
            while (!gameWon.get()) {

                if (pickUpDeck.getDeck().size() != 0) {

                    turn(deckArray);

                    //testing
                    System.out.println(Thread.currentThread().getName() + "  " +numTurns);

                } else {

                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {

            System.out.println("Interrupted!"); //TODO make a real catch statement?????
        }

        while (numTurns < highestNumTurns) {

            if (pickUpDeck.getDeck().size() != 0) {
                evenTurns();
            }
        }

        String myName = Thread.currentThread().getName();
        if (myName.equals(winner)) {

            outputText.add(myName + " wins");

            System.out.println("**************************************************");
            System.out.println("**************************************************");
            System.out.println("***              Player " + playerNum + " has won              ***");
            System.out.println("**************************************************");
            System.out.println("**************************************************");
            System.out.println("***                  Game Over                 ***");
            System.out.println("**************************************************");
            System.out.println("**************************************************");

        } else {

            outputText.add(winner + " has informed " + myName + " that " + winner + " has won");
        }

        ArrayList<Integer> finalHand = new ArrayList<>();

        for (Card card: hand) {
            finalHand.add(card.getValue());
        }

        outputText.add(myName + " exits");
        outputText.add(myName + " final hand: " + finalHand);
    }
}
