import Cards.*;
import FileIO.Reader;
import FileIO.Writer;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;



public class cardGame {

    /**
     * execute the program
     *
     */
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

                    packHolder = getPack(totalNumPlayers); //Integer array of card numbers

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
                            thread.join();
                        }
                    } catch (InterruptedException e){

                        e.printStackTrace(); //TODO do we want to do this here???
                    }


                    try {
                        for (Player player: playerArray) {

                            //testing
                            //System.out.println(player.getOutputText());

                            String playerFilename = "outputFiles/player" + player.getPlayerNum() + "_output.txt";
                            File playerFile = new File( playerFilename);
                            playerFile.createNewFile();


                            Path playerPath;
                            String userDir = System.getProperty("user.dir");
                            playerPath = Paths.get(userDir, playerFilename);

                            //testing
                            System.out.println(playerPath);


                            //Path playerPath = Paths.get(userDir, "/..", membersFilename);

                            Writer playerWriter = new Writer(playerPath);
                            playerWriter.add(player.getOutputText());
                            playerWriter.close();
                        }
                    } catch (IOException i) { i.printStackTrace();}


//                    //testing - outputs initial hands
//                    for (Player player :playerArray){
//                        System.out.println("--------hand--------");
//                        ArrayList<Card> hand = player.getHand();
//                        for (Card card: hand) {
//                            System.out.println(card.getValue());
//                        }
//                    }
//                    //testing - outputs initial decks
//                    for (CardDeck deck1 :deckArray){
//                        System.out.println("---------deck---------");
//                        ArrayList<Card> cards2= deck1.getDeck();
//                        for (Card card: cards2) {
//                            System.out.println(card.getValue());
//                        }
//                    }

                } else {

                    System.out.println("*** Invalid number of players (must be an integer greater than or equal to 2)");
                }

            } catch (InputMismatchException | NumberFormatException e) {

                System.out.println("*** Invalid input, input must be an integer greater than or equal to 2");
            }
        }
    }


    private static ArrayList<Integer> getPack(int numPlayers) {

        Boolean gotPack = false;
        String fileIn;
        ArrayList<Integer> packHolder = new ArrayList<>();
        while (!gotPack) {

            fileIn = getInput("*** Please input a valid pack file:");

            Path fullPath;
            String userDir = System.getProperty("user.dir");
            fullPath = Paths.get(userDir, fileIn);

            try {
                Boolean validPack = true;

                Reader packReader = new Reader(fullPath);
                String[] pack = packReader.next();

                while (pack != null) {
                    int testNum = Integer.parseInt(pack[0]);

                    if (testNum > 0) {

                        validPack = true;
                        packHolder.add(testNum);

                    } else {
                        System.out.println("*** Invalid number (negative or zero) in pack");
                        packHolder.clear();
                        validPack = false;
                        break;
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


    private static ArrayList<Player> distributePlayerCards(ArrayList<Integer> pack, int totalNumPlayers, ArrayList<CardDeck> deckArray) {

        ArrayList<Player> playerArray = new ArrayList<>();

        for (int i=0; i < totalNumPlayers; i++) {

            ArrayList<Card> hand = new ArrayList<>();

            for (int j = i; j < (4*totalNumPlayers); j+=totalNumPlayers) {
                Card nextCard = new Card(pack.get(j));
                hand.add(nextCard);
            }

            ArrayList<String> cardValues = new ArrayList<>();

            for (Card card: hand) {
                cardValues.add(Integer.toString(card.getValue()));
            }

            Player player = new Player(hand, i+1, totalNumPlayers, deckArray);
            player.addToOutput("Player " + player.getPlayerNum() + " initial hand: " + cardValues);

            playerArray.add(player);
        }

        return playerArray;
    }


    private static ArrayList<CardDeck> distributeDeckCards(ArrayList<Integer> pack, int totalNumPlayers) {

        ArrayList<CardDeck> deckArray = new ArrayList<>();

        for (int i = 0; i < totalNumPlayers; i++) {

            int deckCardsStartIndex = (totalNumPlayers * 4);

            ArrayList<Card> deck = new ArrayList<>();

            for (int j = deckCardsStartIndex + i; j < (8 * totalNumPlayers); j += totalNumPlayers) {
                Card nextCard = new Card(pack.get(j));
                deck.add(nextCard);
            }

            deckArray.add(new CardDeck(deck));
        }

        return deckArray;
    }


    /**
     * Displays a prompt and awaits next string user input.
     * @param prompt The prompt to be given.
     * @return The String the user returned.
     */
    private static String getInput(String prompt){

        System.out.println(prompt);
        Scanner inputScanner = new Scanner(System.in);

        return inputScanner.nextLine();
    }
}


