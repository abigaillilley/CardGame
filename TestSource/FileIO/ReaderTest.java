package FileIO;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ReaderTest {

    @Test
    public void nextWithData() {

        Path fullPath;
        String userDir = System.getProperty("user.dir");
        fullPath = Paths.get(userDir, "TestSource/FileIO/readerTestWithData");

        String[] expected = {"First test line", "Second test line", "Third test line"};
        ArrayList<String> input = new ArrayList<>();

        try {
            Reader testReader = new Reader(fullPath);

            String[] pack = testReader.next();

            while (pack != null) {

                input.add(pack[0]);
                pack = testReader.next();
            }

            String[] actual = new String[input.size()];
            actual = input.toArray(actual);

            assertArrayEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();        //no error expected here, no testing for IOException
        }
    }


    @Test
    public void nextWithoutData() {

        Path fullPath;
        String userDir = System.getProperty("user.dir");
        fullPath = Paths.get(userDir, "TestSource/FileIO/readerTestWithoutData");

        try {
            Reader testReader = new Reader(fullPath);

            String[] pack = testReader.next();
            String data = pack[0];

            fail("NullPointerException not caught");

        } catch (IOException e) {
            e.printStackTrace();        //no error expected here, no testing for IOException
        }
        catch (NullPointerException n) {
            assertTrue("NullPointerException caught, method returns correct (null) value when " +
                    "there's not data",true);
        }
    }

    @Test
    public void nextNoFile() {

        Path fullPath;
        String userDir = System.getProperty("user.dir");
        fullPath = Paths.get(userDir, "TestSource/FileIO/thisFileDoesNotExist");

        try {
            Reader testReader = new Reader(fullPath);
            fail("IOException not caught");
        } catch (IOException e) {
            assertTrue("IOException caught", true);
        }
    }


    @Test
    public void close() {

        Path fullPath;
        String userDir = System.getProperty("user.dir");
        fullPath = Paths.get(userDir, "TestSource/FileIO/readerTestWithData");

        try {
            Reader testReader = new Reader(fullPath);
            testReader.close();
            testReader.next();
        } catch (IOException e) {
            assertEquals("Stream successfully closed", "Stream closed", e.getLocalizedMessage());
        }
    }
}