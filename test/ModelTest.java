import org.junit.Test;

import static org.junit.Assert.*;

import view.*;
import model.*;

import javax.swing.*;

public class ModelTest {
    @Test
    public void testGameState() {
        JFrame gui = new JFrame();
        GameUI v = new GameUI(gui);
        GameModel m = new GameModel();
        m.registerView(v);
        // Make a move by player 1 on the center tile
        m.PlayMove(1, 1);
        // check if movesCount has reduced from 9 to 8
        assertEquals(8, m.getMovesCount());
        // check that the center tile now contains 'x'
        assertEquals('X', m.getBoard()[1][1]);

        // check if reset is working
        m.ResetModel();
        // count of moves should be reset to 9
        assertEquals(9, m.getMovesCount());

    }

}
