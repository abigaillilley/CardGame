package FileIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


/**
 * Writer outputs the content of a String ArrayList to a specified file with each element of the list on a new line.
 *
 * @author 670032202
 * @author 670048585
 */
public class Writer {

    private final BufferedWriter writer;

    /**
     * Writer constructor.
     * @param path              Path of file to write to
     * @throws IOException
     */
    public Writer(Path path) throws IOException{
        writer = Files.newBufferedWriter(path);
    }

    /**
     * Writes the contents of an ArrayList, one element on each line
     * @param data          ArrayList containing string to be written to the output file
     * @throws IOException
     */
    public void add (ArrayList<String> data) throws IOException{
        for (String line : data) {
            writer.write(line);
            writer.newLine();
        }
    }

    /**
     * Closes writer
     * @throws IOException
     */
    public void close() throws IOException{
        writer.close();
    }
}
