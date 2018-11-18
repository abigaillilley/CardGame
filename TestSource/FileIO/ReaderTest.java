package FileIO;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ReaderTest {

    @Test
    public void next() {

        ObjectToTest ott = new ObjectToTest() {

            StringWriter output = new StringWriter();

            protected getReader()
            {
                return new StringReader("Here is my test data!");
            }

            protected getWriter()
            {
                return output;
            }
        };
        ott.DoInputAndOutput();
        String outputdata = ott.getWriter().toString();
        assertEquals("This is thenexpected output.", outputdata);





//
//            Reader reader = new StringReader("apple\norange\nbanana");
//            assertEquals(Arrays.asList("apple", "orange", "banana"), showListOfCourses(reader));
    }

    @Test
    public void close() {
    }
}