import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({

        Cards.CardTest.class,
        Cards.CardDeckTest.class,
        FileIO.ReaderTest.class,
        //Player.class
})
public class TestSuite {

}
