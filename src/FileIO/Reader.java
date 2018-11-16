package FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Reader{

    private final BufferedReader reader;
    //private String line;
    //private String[] info;

    public Reader(Path filePath) throws IOException{
        reader = Files.newBufferedReader(filePath);
    }

    public String[] next() throws IOException{

        String line = reader.readLine();
        if (line != null){
            return line.split(" ");
            //return info;
        }
        return null;
    }

    public void close() throws IOException{
        reader.close();
    }
}