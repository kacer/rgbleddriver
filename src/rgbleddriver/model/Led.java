package rgbleddriver.model;

public class Led {
	
	private Color color;
	private int brightness;
	private final int order;
	
	public Led(Color color, int brightness, final int order) {
		this.color = color;
		this.brightness = brightness;
		this.order = order;
	}
	
	public Led(Color color, final int order) {
		this(color, 255, order);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getOrder() {
		return order;
	}

}
