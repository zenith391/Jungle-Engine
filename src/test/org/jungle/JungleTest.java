package test.org.jungle;

import org.jungle.game.Game;
import org.jungle.game.GameOptions;
import org.jungle.game.GameOptionsPrompt;

public class JungleTest extends Game {

	public JungleTest() {
		
	}
	
	public static void main(String[] args) {
		JungleTest test = new JungleTest();
		//GameOptionsPrompt prompt = new GameOptionsPrompt();
		//prompt.setVisible(true);
		GameOptions opts = new GameOptions();
		//opts.antialiasing = true;
		//opts.showTriangles = true;
		test.logic = new DummyLogic();
		test.start(opts);
	}
}
