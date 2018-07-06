package org.jungle.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static String loadResource(String path) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path), 4096);
		StringBuilder b = new StringBuilder();
		while (bis.available() != 0) {
			char c = (char) bis.read();
			b.append(c);
		}
		b.append((char) '\0');
		bis.close();
		//System.out.println("Res " + path + " == " + b.toString());
		return b.toString();
	}
	
	public static List<String> getAllLines(String path) throws IOException {
		return List.of(Utils.loadResource(path).split("\n"));
	}

}
