package org.jungle.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jungle.Texture;

public class FontTexture {

	private Texture texture;
	private int width, height;
	private Font font;
	private BufferedImage img;
	private int horChars;
	
	private final static String IMAGE_FORMAT = "png";
	
	private HashMap<Character, CharInfo> charMap = new HashMap<>();
	
	public Texture getTexture() {
		return texture;
	}
	
	public int getHorizentalChars() {
		return horChars;
	}
	
	public static class CharInfo {

        private final int startX;

        private final int width;

        public CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
        }

        public int getStartX() {
            return startX;
        }

        public int getWidth() {
            return width;
        }
    }
	
	public FontTexture(Font font) throws Exception {
		this.font = font;
		Charset.availableCharsets().forEach((string, charset) -> {
			System.out.println(string);
		});
		buildTexture("windows-1252");
	}
	
	private String getAllAvailableChars(String charsetName) {
	    CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
	    StringBuilder result = new StringBuilder();
	    for (char c = 0; c < Character.MAX_VALUE; c++) {
	        if (ce.canEncode(c)) {
	            result.append(c);
	        }
	    }
	    return result.toString();
	}
	
	private void buildTexture(String charsetName) throws Exception {
        // Get the font metrics for each character for the selected font by using image
        img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableChars(charsetName);
        this.width = 0;
        this.height = 0;
        
        for (char c : allChars.toCharArray()) {
            // Get the size for each character and update global image size
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            horChars++;
            width += charInfo.getWidth();
            height = Math.max(height, fontMetrics.getHeight());
        }
        g2D.dispose();
        // Create the image associated to the charset
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();
        JFrame frame = new JFrame();
        frame.add(new JPanel() {
        	public void paintComponent(Graphics g) {
        		g.drawImage(img, 0, 0, null);
        	}
        });
        frame.setVisible(true);
       // Dump image to a byte buffer
        InputStream is;
        try (
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, IMAGE_FORMAT, out);
            ImageIO.write(img, IMAGE_FORMAT, new File("temp.png"));
            out.flush();
            is = new ByteArrayInputStream(out.toByteArray());
        }

        texture = new Texture(is);
    }

}
