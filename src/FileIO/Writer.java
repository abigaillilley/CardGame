package FileIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Writer {

    private final BufferedWriter writer;

    public Writer(Path path) throws IOException{
        writer = Files.newBufferedWriter(path);
    }

    public void add (ArrayList<String> data) throws IOException{
        for (String line : data) {
            writer.write(line);
            writer.newLine();
        }
    }

    public void close() throws IOException{
        writer.close();
    }



}
