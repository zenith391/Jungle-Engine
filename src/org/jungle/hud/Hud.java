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
	    this.nvg = window.getOptions().antialiasing ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES | NVG_DEBUG) : nvgCreate(NVG_STENCIL_STROKES | NVG_DEBUG);
	    if (this.nvg == NULL) {
	        throw new Exception("Could not init nanovg");
	    }
	}
	
	public void registerFont(String name, String file) throws IOException {
		Font.register(name, file, nvg);
	}
	
	int i = 0;
	
	public void render(Window window) {
//		try {
//        nvgBeginFrame(nvg, window.getWidth(), window.getHeight(), 1);
//
////        for (HudItem item : items) {
////        	item.render(vg, window);
////        }
//        
//        nvgBeginPath(nvg);
//        nvgFontSize(nvg, font.getSize());
//        nvgFontFace(nvg, Font.DEFAULT_FONT_FILE);
//        nvgTextAlign(nvg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
//        nvgFillColor(nvg, rgba(0xe6, 0xea, 0xed, 255, colour));
//        nvgText(nvg, window.getWidth() - 150, window.getHeight() - 95, dateFormat.format(new Date()));
//
//        nvgEndFrame(nvg);
//
//        // Restore state
//        window.restoreState();
//        i++;
//		} catch (Throwable t) {
//			t.printStackTrace();
//			System.out.println(i);
//			System.exit(0);
//		}
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
