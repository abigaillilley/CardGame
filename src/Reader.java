import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class Reader{

    private final BufferedReader reader;
    private String line;
    private String[] info;

    Reader(Path filePath) throws IOException{
        reader = Files.newBufferedReader(filePath);
    }

    String[] next() throws IOException{
        line = reader.readLine();
        if (line != null){
            info = line.split(" ");
            //info.add(line);
            return info;
        }
        return null;
    }

    void close() throws IOException{
        reader.close();
    }
}
