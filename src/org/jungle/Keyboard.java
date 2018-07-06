package org.jungle;

import java.util.ArrayList;

public class Keyboard {

	static ArrayList<Integer> pressedKeys;
	
	static {
		pressedKeys = new ArrayList<Integer>();
	}
	
	public static boolean isKeyPressed(int key) {
		return pressedKeys.contains(key);
	}

}
