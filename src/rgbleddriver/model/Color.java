package rgbleddriver.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color {
	
	public static final Color BLACK = new Color(0, 0, 0);
	
	private int r;
	private int g;
	private int b;
	
	public Color(int r, int g, int b) {
		validateColor("red", r);
		validateColor("green", g);
		validateColor("blue", b);
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(int color) {
		if(color > 16777215)
			throw new IllegalArgumentException("Illegal color value: " + color + ". "
					+ "Color value should be 0 .. 16 777 215");
		r = (color & 0xFF0000) >> 16;
		g = (color & 0x00FF00) >> 8;
		b =  color & 0x0000FF;
	}
	
	private void validateColor(String component, int c) {
		if(c < 0 || c > 255)
			throw new IllegalArgumentException("Illegal color value: " + c + " for " + 
									component + " component - value must be 0 .. 255");
	}
	
	public Color lerp(Color targetColor, float t) {
		return new Color(Math.round(r + (targetColor.r - r) * t),
						 Math.round(g + (targetColor.g - g) * t),
						 Math.round(b + (targetColor.b - b) * t));
	}
	
	public int getRGB() {
		return r << 16 | g << 8 | b;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		validateColor("red", r);
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		validateColor("green", g);
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		validateColor("blue", b);
		this.b = b;
	}
	
	public String getRGBString() {
		return "rgb(" + r +", " + g + ", "+ b + ")";
	}
	
	public static Color fromString(String hexColor) {
		Pattern p = Pattern.compile("^#(?:[0-9a-fA-F]{3}){1,2}$");
		Matcher m = p.matcher(hexColor);
		if(!m.find())
			throw new IllegalArgumentException("Invalid hex color string: " + hexColor);
		
		String safeStr = m.group();
		int r = Integer.valueOf(safeStr.substring(1, 3), 16);
		int g = Integer.valueOf(safeStr.substring(3, 5), 16);
		int b = Integer.valueOf(safeStr.substring(5, 7), 16);
		
		return new Color(r, g, b);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Color))
			return false;
		
		Color c = (Color) obj;
		return (r == c.r) && (g == c.g) && (b == c.b);
	}
	
	

}
