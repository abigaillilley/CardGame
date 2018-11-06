import FileIO.Reader;

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
        // STEP1: initiate the library by loading data from 3 text files
        System.out.println("**************************************************");
        System.out.println("***          Welcome to the Card Game          ***");
        System.out.println("**************************************************");
        //System.out.println("*** Please input the number of players:");

        boolean validPlayerNum = false;

        while (!validPlayerNum) {

            try {

                //Scanner input = new Scanner(System.in);
                //int playerNum = input.nextInt();
                //input.nextLine();

                String playerNumStr = getInput("*** Please input the number of players:");
                int playerNum = Integer.parseInt(playerNumStr);

                if (playerNum > 1) {

                    System.out.println("*** Your game has " + playerNum + " players.");
                    //input.close();
                    validPlayerNum = true;

                    getPack();

                } else {

                    System.out.println("*** Invalid number of players (must be an integer greater than or equal to 2)");
                }

            } catch (InputMismatchException | NumberFormatException e) {

                System.out.println("*** Invalid input, input must be an integer greater than or equal to 2");
            }
        }
    }

    private static void getPack() {

        String fileIn = getInput("*** Please input the pack file:");

        validatePack(fileIn);
    }

    private static Boolean validatePack(String inputPath) {
        Path fullPath;
        String userDir = System.getProperty("user.dir");
        fullPath = Paths.get(userDir, inputPath);

        try {
            Reader packReader = new Reader(fullPath);
            String[] pack = packReader.next();
            Boolean validPack = true;


            while (pack != null) {
                int testNum = Integer.parseInt(pack[0]);

                if (testNum > 0) {

                } else {
                    System.out.println("Invalid number (negative or zero) in pack");
                    validPack = false;
                    break;
                }

                pack = packReader.next();
            }
            packReader.close();

            return validPack;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //dont want to catch
            System.out.println("Non integer value found in pack");
        }

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

    /*public static int[] validatePack(String filepath) {

        try {
            FileIO.Reader packReader = new FileIO.Reader(filePath);
            int pack = packReader.next();
            int[] packArray;

            while (pack != null) {

                if (pack > 0) {

                    packArray.add(pack);
                    pack = packReader.next();

                } else {

                    packReader.close();
                    System.out.println("Invalid pack, please input the path to a file without negative integers.");
                    break;

                }
            }
            packReader.close();
        } catch (IOException e) {
            // TODO catch exception where input isn't a single integer i.e. 'abc' or '6 8'
            e.printStackTrace();
        }
    }*/

