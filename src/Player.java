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

    Player(ArrayList<Card> inputHand, int inputPlayerNum, int totalPlayers, ArrayList<CardDeck> inputCardDecks) {

        hand = inputHand;
        playerNum = inputPlayerNum;
        totalNumPlayers = totalPlayers;
        deckArray = inputCardDecks;

    }


    private void turn(ArrayList<CardDeck> deckArray) {

        //testing
        System.out.println(Thread.currentThread().getName() + " IN TURN");

        synchronized (Player.class) {

            //testing
            System.out.println(Thread.currentThread().getName() + " IN sync TURN");

            checkForWin(hand);

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

                            //testing
                            ArrayList<String> cardValues = new ArrayList<>();
                            for (Card card : hand) {
                                cardValues.add(Integer.toString(card.getValue()));
                            }
                            addToOutput("Player " + getPlayerNum() + " current hand: " + cardValues);

                            break;
                        }
                    } checkForWin(hand);

                    numTurns += 1;
                    if (numTurns > highestNumTurns) { highestNumTurns = numTurns; }

                    //testing
                    System.out.println( "----regular turns-----------------------" + Thread.currentThread().getName() + " Num turns " + numTurns + " highest " +highestNumTurns);


                    //testing : view player's hand after each turn
                    ArrayList<String> cardValues = new ArrayList<>();
                    for (Card card : hand) {
                        cardValues.add(Integer.toString(card.getValue()));
                    }
                    System.out.println("Player " + getPlayerNum() + " current hand: " + cardValues);

                }
            }
        }
    }


    private void checkForWin(ArrayList<Card> hand) {

        synchronized (Player.class) {

            //testing
            System.out.println(Thread.currentThread().getName() + " IN checkForWin");

            boolean allEqual = true;

            for (Card c : hand) {

                if (c.getValue() != hand.get(0).getValue()) {

                    allEqual = false;
                    break;
                }
            }

            if (allEqual) {
                gameWon.set(true);
            }
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

            //testing
            System.out.println( "--even turns-------------------------" + Thread.currentThread().getName() + " Num turns " + numTurns + " highest " +highestNumTurns);

        }
    }


    private Card pickUp(CardDeck cardDeck){

        ArrayList<Card> deck = cardDeck.getDeck();

        synchronized (Player.class) { //TODO does this actually need to be synchronised????
            try {
                while (deck.size() == 0) { //TODO restructure this, empty deck is handled in run()
                    Thread.sleep(100);
                }

                Card topCard = deck.get(0);

                //testing
                System.out.println(Thread.currentThread().getName() + " picked up card " + topCard.getValue());

                deck.remove(0);
                return topCard;

            } catch (InterruptedException e) { //TODO don't need this if thread not sleeping, but if it is what do we put here?? (3)
                return null;
            }
        }
    }


    private void putDown(Card discardCard, CardDeck discardDeck) {

        ArrayList<Card> deck = discardDeck.getDeck();
        deck.add(discardCard);
        discardDeck.setDeck(deck);


        //testing
        System.out.println(Thread.currentThread().getName() + " discarded card " + discardCard.getValue());
        ArrayList<Integer> test = new ArrayList<>();
        for (Card card: deck) {
            test.add(card.getValue());
        }
        System.out.println(test);

    }


    public ArrayList<Card> getHand() {
        return hand;
    }


    public int getPlayerNum() {
        return playerNum;
    }


    public ArrayList<String> getOutputText() { //will be used to write files
        return outputText;
    }


    public void addToOutput(String text) {
        this.outputText.add(text);
    }


    public void run(){

        int pickUpDeckIndex = this.playerNum - 1;
        CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);

        while (!gameWon.get()){

            if (pickUpDeck.getDeck().size() != 0) {

                turn(deckArray);

            } else {

                //testing
                System.out.println(Thread.currentThread().getName() +"      empty deck");
                //TODO make thread sleep here rather than in pickup???
            }


//            //testing decks are working properly
//            ArrayList<Integer> test = new ArrayList<>();
//            for (CardDeck deck : deckArray) {
//                for (Card card : deck.getDeck()) {
//                    test.add(card.getValue());
//                }
//                System.out.println(test);
            //}

        }
        System.out.println(Thread.currentThread().getName() + "-------------------------EXITS WHILE LOOP" + "   " + gameWon.get());

        while (numTurns < highestNumTurns) {
            if (pickUpDeck.getDeck().size() != 0) {
                evenTurns();
            }
        }


        //testing -- literally this whole synchronised block
        synchronized (Player.class) {

            System.out.println("--------hand--------" + Thread.currentThread().getName());
            for (Card card : hand) {
                System.out.println(card.getValue());
            }


            for (CardDeck deck1 : deckArray) {
                System.out.println("---------deck---------" + Thread.currentThread().getName());
                ArrayList<Card> cards2 = deck1.getDeck();
                for (Card card : cards2) {
                    System.out.println(card.getValue());
                }
            }
        }
    }
}
