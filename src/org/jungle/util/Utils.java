package org.jungle.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
	
	public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
	}
	
	public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(Utils.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
	}

}
