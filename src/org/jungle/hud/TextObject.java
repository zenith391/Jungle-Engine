package org.jungle.hud;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jungle.Mesh;
import org.jungle.Spatial;
import org.jungle.Texture;
import org.jungle.Window;
import org.jungle.util.Material;
import org.jungle.util.Utils;

public class TextObject extends HudItem {

	private static final String DEFAULT_FONT_FILE = "example/assets/fonts/opensans/OpenSans-Bold.ttf";
	private String fontFile;
	
	public TextObject() {
		
	}
	
	public TextObject(Font font) {
		this.fontFile = fontFile;
	}
	
	@Override
	public void render(long nvg, Window win) {
		
	}

	
    
}