package FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reader creates a buffered reader to take input from a text file, reading one row at a time.
 *
 * @author 670032202
 * @author 670048585
 */
public class Reader{

    private final BufferedReader reader;

    /**
     * Reader constructor.
     * @param filePath          Path of file to read
     * @throws IOException
     */
    public Reader(Path filePath) throws IOException{
        reader = Files.newBufferedReader(filePath);
    }


    /**
     * @return A String array containing the next row of text in the file
     * @throws IOException
     */
    public String[] next() throws IOException{

        String line = reader.readLine();
        if (line != null){

            return line.split("\n");    //Split by each row in the file
        }
        return null;
    }


    /**
     * Closes reader
     * @throws IOException
     */
    public void close() throws IOException{
        reader.close();
    }
}