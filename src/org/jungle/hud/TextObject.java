package org.jungle.hud;

import org.jungle.Window;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

@Deprecated
public class TextObject extends HudItem {

	private Font font;
	private String text;
	private float x, y;
	private NVGColor color;
	
	public TextObject() {
		
	}
	
	public TextObject(String text, Font font, int x, int y) {
		this.text = text;
		this.font = font;
		this.x = x;
		this.y = y;
		color = NVGColor.create();
	}
	
	@Override
	public void render(long nvg, Window win) {
		nvgFontSize(nvg, font.getSize());
		nvgTextAlign(nvg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP); // default align
		//nvgFillColor(nvg, Hud.rgba(0xff, 0xff, 0xff, 255, color));
		nvgText(nvg, x + 90f, y + 90f, text);
		
	}

	
    
}