package org.jungle.hud;

import org.lwjgl.nanovg.NVGColor;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.util.ArrayList;

import org.jungle.Window;

public class Hud {

	private long vg;
	private ArrayList<HudItem> items = new ArrayList<>();
	
	public void init(Window window) throws Exception {
	    this.vg = window.getOptions().antialiasing ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES) : nvgCreate(NVG_STENCIL_STROKES);
	    if (this.vg == NULL) {
	        throw new Exception("Could not init nanovg");
	    }
	}
	
	public void registerFont(String name, String file) throws IOException {
		Font.register(name, file, vg);
	}
	
	public void render(Window window) {
        nvgBeginFrame(vg, window.getWidth(), window.getHeight(), 1);

        for (HudItem item : items) {
        	item.render(vg, window);
        }

        nvgEndFrame(vg);

        // Restore state
        window.restoreState();
    }

    public static NVGColor rgba(int r, int g, int b, int a, NVGColor colour) {
        colour.r(r / 255.0f);
        colour.g(g / 255.0f);
        colour.b(b / 255.0f);
        colour.a(a / 255.0f);

        return colour;
    }

    public void cleanup() {
        nvgDelete(vg);
    }
	
}
