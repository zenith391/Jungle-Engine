package test.org.jungle;

import org.jungle.game.Game;

public class JungleTest extends Game {

	public JungleTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		JungleTest test = new JungleTest();
		test.logic = new DummyLogic();
		test.start();
	}
}
