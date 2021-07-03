import controller.Controller;
import org.junit.Test;
import view.GameUI;

import javax.swing.*;

import static components.Mark.*;
import static org.junit.Assert.assertEquals;

public class ViewTest {
    @Test
    public void testGameState() {
        Controller c = new Controller();
        JFrame gui = new JFrame();
        GameUI v = new GameUI(gui);
        v.setActionListener(c);

        v.update(1, 1, X, O + ":  Player 2");
        assertEquals("x", v.getButtonText(1, 1));

    }

}
