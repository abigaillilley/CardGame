package FileIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Writer {

        private final BufferedWriter writer;

        Writer(Path path) throws IOException{
            writer = Files.newBufferedWriter(path);
        }

        void add (String[] data) throws IOException{
            writer.write(String.join(",", data));
            writer.newLine();
        }

        void close() throws IOException{
            writer.close();
        }



}
