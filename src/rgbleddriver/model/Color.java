package rgbleddriver.model;

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

}
