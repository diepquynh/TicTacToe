import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;


import model.*;
import controller.*;
import view.*;

import javax.swing.*;

public class ControllerTest {
    @Test
    public void testGameState() {
		Controller c = new Controller();
		GameModel m = new GameModel();
		JFrame gui = new JFrame();
		GameUI v = new GameUI(gui);
		m.registerView(v);
		c.setGameModel(m);
		
		ArrayList<Integer> pos = new ArrayList<Integer>();
		pos.add(1);
		pos.add(1);
		
		// ask the model to update its board depending on the request
		c.setGameRequest(pos);
		
		// check if the requested operation updated the model successfully
		assertEquals ('X', m.getBoard()[1][1]);
		
    }

}
