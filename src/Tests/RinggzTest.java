package Tests;

import org.junit.Before;
import Game.Ringgz;
import org.junit.Test;

/**
 * Created by Thomas Stouten on 24-Jan-18.
 */
public class RinggzTest {
    private String[] args;
    @Before
    public void setUp() {
        String[] args = {"a", "b"};
    }


    @Test
    public void testMain(){
        Ringgz.main(args);

    }
}
