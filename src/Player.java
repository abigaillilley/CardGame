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
    private static volatile int highestNumTurns = 0;
    private int numTurns = 0;
    private static String winner;
    private ArrayList<String> outputText = new ArrayList<>();

    /**
     * Player constructor.
     * @param inputHand         Player's initial hand
     * @param inputPlayerNum    unique player number
     * @param totalPlayers      Total number of players
     * @param inputCardDecks    Arraylist of all decks (CardDeck objects)
     */
    public Player(ArrayList<Card> inputHand, int inputPlayerNum, int totalPlayers, ArrayList<CardDeck> inputCardDecks) {

        hand = inputHand;
        playerNum = inputPlayerNum;
        totalNumPlayers = totalPlayers;
        deckArray = inputCardDecks;
    }


    /**
     * Atomic method to simulate a player taking a turn.
     * Players prefer cards with the same denomination as their player number and hold onto these.
     * The oldest non-preferred card in the hand is discarded.
     * @param deckArray   Array of all decks
     */
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

                        addToOutput("Player " + getPlayerNum() + " current hand: " + getHandValues());

                        break;
                    }
                }
                numTurns += 1;
                if (numTurns > highestNumTurns) {
                    highestNumTurns = numTurns;
                }

                checkForWin(hand);
            }
        }
    }


    /**
     * Checks if the cards in the hand all have the same value.
     * If they are, sets the class variable gameWon to true and prints an winning message to the terminal window.
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

            if (allEqual && !gameWon.get()) {   //Checks another player hasn't already declared they've won

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


    /**
     * Removes and returns top card from the deck.
     * @param cardDeck  CardDeck to get the top card from
     * @return Top card from from deck i.e. Card that's been in the deck the longest
     */
    private Card pickUp(CardDeck cardDeck){

        ArrayList<Card> deck = cardDeck.getDeck();

        Card topCard = deck.get(0);
                //Items shift down the Arraylist as lower indexed items are removed, so index 0 has the oldest card
        deck.remove(0);

        return topCard;
    }

    /**
     * Adds the card to the bottom of the deck.
     * @param discardCard   Card to be discarded
     * @param discardDeck   Deck to add the discarded card to
     */
    private void putDown(Card discardCard, CardDeck discardDeck) {

        ArrayList<Card> deck = discardDeck.getDeck();
        deck.add(discardCard);                   //Adds to the end of the Arraylist
        discardDeck.setDeck(deck);
    }


    /**
     * Getter for a player's number
     * @return Player's number
     */
    public int getPlayerNum() {
        return playerNum;
    }


    /**
     * Getter for a player's output text
     * @return Arraylist of the player's output strings
     */
    public ArrayList<String> getOutputText() {
        return outputText;
    }


    /**
     * Adds a string to be output in the player's output file.
     * @param text String to be added to the player's output ArrayList
     */
    public void addToOutput(String text) {
        this.outputText.add(text);
    }


    /**
     * Getter for the denominations of the cards in a player's hand
     * @return Integer ArrayList of the card denominations in the player's hand
     */
    public ArrayList<Integer> getHandValues() {

        ArrayList<Integer> handArray = new ArrayList<>();

        for (Card card: hand) {
            handArray.add(card.getValue());
        }
        return handArray;
    }


    /**
     * Runnable method that allows players to play simultaneously.
     */
    public void run(){

        Thread.currentThread().setName("Player " + playerNum);

        synchronized (lock) {                            //Ensures only one player declares they've won

            checkForWin(hand);
            initialCheck += 1;

            try {
                if (initialCheck < totalNumPlayers) {
                                                         //Wait until all players have completed an initial check
                    lock.wait();

                } else {

                    lock.notifyAll();
                }

            } catch (InterruptedException i) {
                i.printStackTrace();                     //Not expecting thread to be interrupted
            }
        }

        int pickUpDeckIndex = playerNum - 1;                        //Deck to player's 'left'
        CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
        int discardDeckIndex = playerNum % totalNumPlayers;         //Deck to player's 'right'
        CardDeck discardDeck = deckArray.get(discardDeckIndex);

        while (!gameWon.get()) {

            if (pickUpDeck.getDeck().size() != 0) {

                turn(deckArray);
            }
        }

        while (numTurns < highestNumTurns) {            //For even decks at the end of the game, every player has to
                                                        //take the same number of turns
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