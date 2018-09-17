package org.jungle.hud;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.nanovg.NanoVG.*;

import org.jungle.util.Utils;

public class Font {
	
	private int fontID;
	
	private String name;
	private int size;
	
	public static void register(String name, String file, long nvg) throws IOException {
		ByteBuffer fontBuffer = Utils.ioResourceToByteBuffer("example/assets/fonts/opensans/OpenSans-Bold.ttf", 150 * 1024);
	    int font = nvgCreateFontMem(nvg, name, fontBuffer, 0);
	    if (font == -1) {
	        throw new IllegalStateException("Could not add font");
	    }
	}
	
	/**
	 * Create a new font from a arleady registered font name.
	 * @param name
	 */
	public Font(String name) {
		
	}
	
	public int getNVGId() {
		return fontID;
	}
	
}
