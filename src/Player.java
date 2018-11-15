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

    //public Boolean turn(ArrayList<CardDeck> deckArray) {
    public void turn(ArrayList<CardDeck> deckArray) {
        System.out.println(Thread.currentThread().getName() + " IN TURN");
            //returns true if a player has won on this turn{
        synchronized (Player.class) {
            System.out.println(Thread.currentThread().getName() + " IN sync TURN");
            checkForWin(hand);
            //if (!checkForWin(hand)) {
            if (!gameWon.get()) {

                int pickUpDeckIndex = this.playerNum - 1;
                CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
                try {
                    Card topCard = pickUp(pickUpDeck);
                    if (topCard != null) {
                        hand.add(topCard);
                        addToOutput("Player " + getPlayerNum() + " draws " + topCard.getValue() + " from Deck " + getPlayerNum());
                        int discardDeckIndex = playerNum % totalNumPlayers;
                        CardDeck discardDeck = deckArray.get(discardDeckIndex);

                        for (int i = 0; i < 4; i++) {

                            if (playerNum != hand.get(i).getValue()) {

                                putDown(hand.get(i), discardDeck);
                                addToOutput("Player " + getPlayerNum() + " discards " + hand.get(i).getValue() + " to Deck " + (discardDeckIndex + 1));
                                hand.remove(i);

                                ArrayList<String> cardValues = new ArrayList<>();
                                for (Card card : hand) {

                                    cardValues.add(Integer.toString(card.getValue()));

                                }

                                addToOutput("Player " + getPlayerNum() + " current hand: " + cardValues);

                                break;
                            }
                        } checkForWin(hand);


                        numTurns += 1;
                        if (numTurns > highestNumTurns) {

                            highestNumTurns = numTurns;
                        }
                        System.out.println( "----regular turns-----------------------" + Thread.currentThread().getName() + " Num turns " + numTurns + " highest " +highestNumTurns);




                        //test to view play'er hand after eac turn
                        ArrayList<String> cardValues = new ArrayList<>();
                        for (Card card : hand) {
                            cardValues.add(Integer.toString(card.getValue()));
                        }
                        System.out.println("Player " + getPlayerNum() + " current hand: " + cardValues);

                    }
                } catch (InterruptedException e) {}

                //return checkForWin(hand);
            } //else { return checkForWin(hand); }
        }
    }

//    public static synchronized Boolean checkForWin(ArrayList<Card> hand) {
//
//        boolean allEqual = true;
//        for (Card c : hand) {
//            if(c.getValue() != hand.get(0).getValue()) {
//                allEqual = false;
//                break;
//            }
//        }
//        return allEqual;
//    }

    public void checkForWin(ArrayList<Card> hand) {
        synchronized (Player.class) {
            System.out.println(Thread.currentThread().getName() + " IN checkForWin");

            boolean allEqual = true;
            for (Card c : hand) {
                if (c.getValue() != hand.get(0).getValue()) {
                    allEqual = false;
                    break;
                }
            }

//            numTurns += 1;
//            if (numTurns > highestNumTurns) {
//
//                highestNumTurns = numTurns;
//            }
//            System.out.println( "----regular turns-----------------------" + Thread.currentThread().getName() + " Num turns " + numTurns + " highest " +highestNumTurns);


            if (allEqual) {
                gameWon.set(true);
            }
        }
    }

    public void evenTurns() {
    //while your tuns is less than highestTurns, then pickup and put down card
        //while (numTurns < highestNumTurns) {
            int pickUpDeckIndex = this.playerNum - 1;
            CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);
            try {
                Card transferCard = pickUp(pickUpDeck);
                if (transferCard != null) {
                    int discardDeckIndex = playerNum % totalNumPlayers;
                    CardDeck discardDeck = deckArray.get(discardDeckIndex);
                    putDown(transferCard, discardDeck);

                    numTurns += 1;
                    System.out.println( "--even turns-------------------------" + Thread.currentThread().getName() + " Num turns " + numTurns + " highest " +highestNumTurns);

                }
            } catch (InterruptedException e ) {}
        //}
    }

    public Card pickUp(CardDeck cardDeck) throws InterruptedException {

        ArrayList<Card> deck = cardDeck.getDeck();

        synchronized (Player.class) {
            try {
                while (deck.size() == 0) {
                    Thread.sleep(100);
                }

                //if (deck.size() != 0) {
                    Card topCard = deck.get(0);
                    System.out.println(Thread.currentThread().getName() + " picked up card " + topCard.getValue());
                    deck.remove(0);
                    return topCard;
                //} else {
                   // System.out.println("no cards in deck"); }
                    //return null;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

    public void putDown(Card discardCard, CardDeck discardDeck) {

        ArrayList<Card> deck = discardDeck.getDeck();
        deck.add(discardCard);
        discardDeck.setDeck(deck);



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

    public ArrayList<String> getOutputText() {
        return outputText;
    }

    public void addToOutput(String text) {

        this.outputText.add(text);
    }

    public void run(){

        int pickUpDeckIndex = this.playerNum - 1;
        CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);

        while (!gameWon.get()){
            //System.out.println(Thread.currentThread().getName() + "   " + gameWon.get());
            //int pickUpDeckIndex = this.playerNum - 1;
            //CardDeck pickUpDeck = deckArray.get(pickUpDeckIndex);

            //System.out.println(Thread.currentThread().getName() + " game won???????" + gameWon);
            if (pickUpDeck.getDeck().size() != 0) {
                //gameWon.set(turn(deckArray));
                turn(deckArray);
            } else {
                System.out.println(Thread.currentThread().getName() +"      empty deck");
            }


//            //test decks are working properly
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



        synchronized (Player.class) {

            //evenTurns();

            System.out.println("--------hand--------" + Thread.currentThread().getName());
            for (Card card : hand) {
                System.out.println(card.getValue());
            }


            for (CardDeck deck1 : deckArray) {
                System.out.println("---------deck---------");
                ArrayList<Card> cards2 = deck1.getDeck();
                for (Card card : cards2) {
                    System.out.println(card.getValue());
                }
            }
        }

        //4
        //evenTurns();


    }
}
