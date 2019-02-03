package org.jungle.hud;

import org.lwjgl.nanovg.NVGColor;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jungle.Window;
import org.jungle.util.Utils;

public class Hud {

	public long nvg;
	
	public void init(Window window) throws Exception {
	    this.nvg = window.getOptions().antialiasing ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES) : nvgCreate(NVG_STENCIL_STROKES);
	    if (this.nvg == NULL) {
	        throw new Exception("Could not init nanovg");
	    }
	}
	
	public void registerFont(String name, String file) throws IOException {
		Font.register(name, file, nvg);
	}
	
	int i = 0;
	
	public void render(Window window) {
//		Default HUD does nothing
    }

    public static NVGColor rgba(int r, int g, int b, int a, NVGColor colour) {
        colour.r(r / 255.0f);
        colour.g(g / 255.0f);
        colour.b(b / 255.0f);
        colour.a(a / 255.0f);

        return colour;
    }

    public void cleanup() {
        nvgDelete(nvg);
    }
	
}
