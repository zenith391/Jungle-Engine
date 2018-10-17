package org.jungle.hud;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.nanovg.NanoVG.*;

import org.jungle.util.Utils;

public class Font {
	
	private int fontID;
	
	public static final String DEFAULT_FONT_FILE = "example/assets/fonts/opensans/OpenSans-Bold.ttf";
	
	private float size;
	
	private static Map<String, Integer> idRegister = new HashMap<>();
	
	public static void register(String name, String file, long nvg) throws IOException {
		ByteBuffer fontBuffer = Utils.ioResourceToByteBuffer(file, 150 * 1024);
	    int font = nvgCreateFontMem(nvg, DEFAULT_FONT_FILE, fontBuffer, 0);
	    System.out.println("gen: " + font);
	    if (font == -1) {
	        throw new IllegalStateException("Could not add font");
	    }
	    idRegister.put(DEFAULT_FONT_FILE, font);
	}
	
	/**
	 * Create a new font from a arleady registered font name.
	 * @param name
	 */
	public Font(String name, float size) {
		this.fontID = idRegister.get(name);
		System.out.println(fontID);
		this.size = size;
	}

	public float getSize() {
		return size;
	}
	
	public int getNVGId() {
		return fontID;
	}
	
}
