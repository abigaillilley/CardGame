import FileIO.Reader;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        boolean validPlayerNum = false;
        ArrayList<Integer> packHolder = new ArrayList<>();

        while (!validPlayerNum) {

            try {

                String playerNumStr = getInput("*** Please input the number of players:");
                int playerNum = Integer.parseInt(playerNumStr);

                if (playerNum > 1) {

                    System.out.println("*** Your game has " + playerNum + " players");
                    validPlayerNum = true;

                    packHolder = getPack(playerNum);

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
            System.out.println(fullPath);

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


