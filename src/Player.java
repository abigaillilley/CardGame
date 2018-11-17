import Cards.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Player is used to control each player's actions.
 * This class is threaded to allow each player to play simultaneously.
 *
 * @author 670032202
 * @author 670048585
 */
public class Player implements Runnable {

    private ArrayList<Card> hand;
    private int playerNum;                                      //Player's individual identifying number
    private final int totalNumPlayers;
    private static volatile ArrayList<CardDeck> deckArray;

    private static int initialCheck = 0;
    private static final Object lock = new Object();            //Used as a lock during the initial hand check
    private static volatile AtomicBoolean gameWon = new AtomicBoolean(false);
    private int numTurns = 0;
    private static volatile int highestNumTurns = 0;
    private static String winner;
    private ArrayList<String> outputText = new ArrayList<>();

    /**
     * Player constructor.
     *
     * @param inputHand         Player's initial hand
     * @param inputPlayerNum    Player's unique player number
     * @param totalPlayers      Total number of players
     * @param inputCardDecks    Arraylist of all decks (CardDeck objects)
     */
    Player(ArrayList<Card> inputHand, int inputPlayerNum, int totalPlayers, ArrayList<CardDeck> inputCardDecks) {

        hand = inputHand;
        playerNum = inputPlayerNum;
        totalNumPlayers = totalPlayers;
        deckArray = inputCardDecks;
    }


    /**
     * Atomic method for when player takes a turn.
     * Players prefer cards with the same denomination as their player number and hold onto these.
     * The oldest non-preferred card in the hand is discarded.
     * If a card is picked up, a card is guaranteed to be discarded.
     *
     * @param pickUpDeck      CardDeck object to pick up card from
     * @param discardDeck     CardDeck object to discard a card to
     */
    private void turn(CardDeck pickUpDeck, CardDeck discardDeck) {

        if (!gameWon.get()) {

            Card topCard = pickUp(pickUpDeck);
            hand.add(topCard);

            addToOutput("Player " + getPlayerNum() + " draws " + topCard.getValue() + " from Deck " +
                    getPlayerNum());

            for (int i = 0; i < 4; i++) {

                if (playerNum != hand.get(i).getValue()) {
                                //If card value not of preferred denomination, discard it
                    putDown(hand.get(i), discardDeck);
                    int playerNum = getPlayerNum();

                    addToOutput("Player " + playerNum + " discards " + hand.get(i).getValue() +
                            " to Deck " + (playerNum + 1));
                    hand.remove(i);

                    addToOutput("Player " + playerNum + " current hand: " + getHandValues());

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


    /**
     * Checks if the cards in the hand all have the same value.
     * If they are, sets the class variable gameWon to true and prints an winning message to the terminal window.
     *
     * @param hand  Player's current hand
     */
    private void checkForWin(ArrayList<Card> hand) {

        synchronized (Player.class) {           //Prevents multiple players declaring they've won

            boolean allEqual = true;

            for (Card c : hand) {

                if (c.getValue() != hand.get(0).getValue()) {

                    allEqual = false;
                    break;                      //Stops unnecessary checks
                }
            }

            if (allEqual && !gameWon.get()) {   //Checks another player hasn't declared they've already won

                gameWon.set(true);
                winner = Thread.currentThread().getName();

                outputText.add(winner + " wins");

                System.out.println("**************************************************");
                System.out.println("**************************************************");
                System.out.println("***              Player " + playerNum + " has won              ***");
                System.out.println("**************************************************");
                System.out.println("**************************************************");
                System.out.println("***                  Game Over                 ***");
                System.out.println("**************************************************");
                System.out.println("**************************************************");
            }
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

    public int getPlayerNum() {
        return playerNum;
    }


    public ArrayList<Integer> getHandValues() {  //TODO make sure this is in the testing

        ArrayList<Integer> handArray = new ArrayList<>();


        for (Card card: hand) {
            handArray.add(card.getValue());
        }
        return handArray;
    }


    public ArrayList<String> getOutputText() {
        return outputText;
    }


    public void addToOutput(String text) {
        this.outputText.add(text);
    }


    public void run(){

        Thread.currentThread().setName("Player " + playerNum);

        synchronized (lock) {

            checkForWin(hand);
            initialCheck += 1;

            try {
                if (initialCheck < totalNumPlayers) {

                    lock.wait();

                } else {

                    lock.notifyAll();
                }

            } catch (InterruptedException i) {
                i.printStackTrace();   //TODO make a real catch statement????? comment that this never should occur, mention in documentation/report
            }
        }

        int pickUpDeckIndex = playerNum - 1;
        CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
        int discardDeckIndex = playerNum % totalNumPlayers;
        CardDeck discardDeck = deckArray.get(discardDeckIndex);

        while (!gameWon.get()) {

            if (pickUpDeck.getDeck().size() != 0) {

                turn(pickUpDeck, discardDeck);

            }
        }

        while (numTurns < highestNumTurns) {

            if (pickUpDeck.getDeck().size() != 0) {

                putDown(pickUp(pickUpDeck), discardDeck);
                numTurns += 1;
            }
        }

        String myName = Thread.currentThread().getName();

        if (!myName.equals(winner)) {

            outputText.add(winner + " has informed " + myName + " that " + winner + " has won");
        }

        outputText.add(myName + " exits");
        outputText.add(myName + " final hand: " + getHandValues());

    }
}