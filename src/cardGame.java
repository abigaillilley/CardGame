import Cards.*;
import FileIO.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;


/**
 * CardGame is used to execute the threaded card game.
 *
 * @author 670032202
 * @author 670048585
 */
public class CardGame {

    public static void main (String[] args){

        System.out.println("**************************************************");
        System.out.println("***          Welcome to the Card Game          ***");
        System.out.println("**************************************************");

        boolean validTotalNumPlayers = false;
        ArrayList<Integer> packHolder;

        while (!validTotalNumPlayers) {

            try {

                String totalNumPlayersStr = getInput("*** Please input the number of players:");
                int totalNumPlayers = Integer.parseInt(totalNumPlayersStr);

                if (totalNumPlayers> 1) {

                    System.out.println("*** Your game has " + totalNumPlayers+ " players");
                    validTotalNumPlayers = true;

                    packHolder = getPack(totalNumPlayers);

                    ArrayList<CardDeck> deckArray = distributeDeckCards(packHolder, totalNumPlayers);
                    ArrayList<Player> playerArray = distributePlayerCards(packHolder, totalNumPlayers, deckArray);

                    ArrayList<Thread> threadArray = new ArrayList<>();

                    for (Player player: playerArray) {
                        Thread playerThread = new Thread(player);
                        playerThread.start();
                        threadArray.add(playerThread);
                    }

                    try {
                        for (Thread thread : threadArray) {
                            thread.join();                  //Main thread waits for all other threads to complete
                        }
                    } catch (InterruptedException e){

                        e.printStackTrace();                //Threads not expected to be interrupted
                    }

                    try {

                        String userDir = System.getProperty("user.dir");
                        File outputFiles = new File("outputFiles");

                        for (File file :  outputFiles.listFiles()) { //Deletes output files from previous game
                                    //Directory outputFiles always exists, so NullPointerException will never be thrown

                            if (!file.isDirectory())
                                file.delete();                     //Result ignored
                        }

                        for (Player player: playerArray) {         //Creating uniquely named player output files

                            String playerFilename = "outputFiles/player" + player.getPlayerNum() + "_output.txt";

                            Path playerPath;
                            playerPath = Paths.get(userDir, playerFilename);

                            Writer playerWriter = new Writer(playerPath);
                            playerWriter.add(player.getOutputText());
                            playerWriter.close();
                        }

                        for (CardDeck deck: deckArray) {            //Creating uniquely named final deck output files

                            String deckFilename = "outputFiles/deck" + deck.getDeckNum() + "_output.txt";

                            Path deckPath;
                            deckPath = Paths.get(userDir, deckFilename);

                            ArrayList<Integer> cardNumbers = new ArrayList<>();
                            for (Card card: deck.getDeck()) {
                                cardNumbers.add(card.getValue());
                            }

                            ArrayList<String> deckOutput = new ArrayList<>();
                            deckOutput.add("Deck " + deck.getDeckNum() + " contents: " + cardNumbers);

                            Writer playerWriter = new Writer(deckPath);
                            playerWriter.add(deckOutput);
                            playerWriter.close();
                        }
                    } catch (IOException i) { i.printStackTrace();}
                } else {

                    System.out.println("*** Invalid number of players (must be an integer greater than or equal to 2)");
                }

            } catch (InputMismatchException | NumberFormatException e) {

                System.out.println("*** Invalid input, input must be an integer greater than or equal to 2");
            }
        }
    }

    /**
     * Gets user input of a pack file and validates the pack with the following conditions:
     *      - Length is 8 times the number of players
     *      - All numbers are non-negative integers
     *
     * @param numPlayers Number of players
     * @return Integer Arraylist holding a valid pack
     */
    private static ArrayList<Integer> getPack(int numPlayers) {

        Boolean gotPack = false;
        String fileIn;
        ArrayList<Integer> packHolder = new ArrayList<>();

        while (!gotPack) {                                           //Loops until a valid pack is input by the user

            fileIn = getInput("*** Please input a valid pack file:");

            Path fullPath;
            String userDir = System.getProperty("user.dir");
            fullPath = Paths.get(userDir, fileIn);

            try {
                Boolean validPack = true;                           //Assumes pack is valid

                Reader packReader = new Reader(fullPath);
                String[] pack = packReader.next();

                while (pack != null) {
                    int testNum = Integer.parseInt(pack[0]);

                    if (testNum >= 0) {                             //Number valid

                        packHolder.add(testNum);

                    } else {
                        System.out.println("*** Invalid (negative) number found in pack");
                        packHolder.clear();                         //Resets arrarylist for next pack the user inputs
                        validPack = false;
                        break;                                      //Pack not valid, no need to check the rest
                    }
                    pack = packReader.next();
                }

                if (validPack) {

                    if (packHolder.size() == numPlayers*8) {

                        gotPack = true;

                    } else {
                        System.out.println("*** Incorrect number of cards");
                    }
                }
                packReader.close();

            } catch (IOException e) {

                System.out.println("*** Error found in file path");

            } catch (NumberFormatException e) {

                System.out.println("*** Non integer value found in pack");
            }
        }
        return packHolder;
    }


    /**
     * Creates 'n' Player objects and distributes the first 4n cards among the players round-robin style.
     * Add statement about the player's initial hand for the output file.
     *
     * @param pack              An valid pack in the form of an integer arraylist
     * @param totalNumPlayers   Integer, total number of players
     * @param deckArray         Arraylist of deck objects
     * @return Arraylist of all player objects
     */
    private static ArrayList<Player> distributePlayerCards(ArrayList<Integer> pack, int totalNumPlayers, ArrayList<CardDeck> deckArray) {

        ArrayList<Player> playerArray = new ArrayList<>();

        for (int i=0; i < totalNumPlayers; i++) {

            ArrayList<Card> hand = new ArrayList<>();

            for (int j = i; j < (4*totalNumPlayers); j+=totalNumPlayers) {          //Creates hand round-robin style
                Card nextCard = new Card(pack.get(j));
                hand.add(nextCard);
            }

            Player player = new Player(hand, i+1, totalNumPlayers, deckArray);
            player.addToOutput("Player " + player.getPlayerNum() + " initial hand: " + player.getHandValues());

            playerArray.add(player);
        }
        return playerArray;
    }


    /**
     * Creates 'n' CardDeck objects and distributes the second 4n cards among the decks round-robin style.
     *
     * @param pack              An valid pack in the form of an integer arraylist
     * @param totalNumPlayers   Integer, total number of players
     * @return Arraylist of all CardDeck objects
     */
    private static ArrayList<CardDeck> distributeDeckCards(ArrayList<Integer> pack, int totalNumPlayers) {

        ArrayList<CardDeck> deckArray = new ArrayList<>();

        for (int i = 0; i < totalNumPlayers; i++) {

            int deckCardsStartIndex = (totalNumPlayers * 4);
                                                     //Use second half of the pack (first half distributed to players)

            ArrayList<Card> deck = new ArrayList<>();

            for (int j = deckCardsStartIndex + i; j < (8 * totalNumPlayers); j += totalNumPlayers) {
                                                    //Distribute cards round-robin style
                Card nextCard = new Card(pack.get(j));
                deck.add(nextCard);
            }

            deckArray.add(new CardDeck(deck, i + 1));
        }
        return deckArray;
    }


    /**
     * Displays a prompt and awaits next string user input.
     *
     * @param prompt    The prompt to be given.
     * @return The String the user returned
     */
    private static String getInput(String prompt){

        System.out.println(prompt);
        Scanner inputScanner = new Scanner(System.in);

        return inputScanner.nextLine();
    }
}