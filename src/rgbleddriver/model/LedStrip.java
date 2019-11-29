package rgbleddriver.model;

import java.util.LinkedList;
import java.util.List;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.rpiws281x.WS281x;

public class LedStrip {
	
	private static final int BRIGHTNESS = 255;
	
	private List<Led> leds;
	private LedDriverInterface driver;
	
	
	public LedStrip(int ledCount, int gpioNum) {
		if(ledCount <= 0)
			throw new IllegalArgumentException("LED Strip should contain some LED");
		
		leds = new LinkedList<>();
		for(int i = 0; i < ledCount; i++) {
			leds.add(new Led(Color.BLACK, i));
		}
		
		System.out.println("*********CONFIG - LED Count: "+ ledCount + ", GPIO Num: " + gpioNum);
		driver = new WS281x(gpioNum, BRIGHTNESS, ledCount);
	}
	
	public LedStrip(LinkedList<Led> leds, int gpioNum) {
		if(leds.isEmpty())
			throw new IllegalArgumentException("LED Strip should contain some LED");
		
		this.leds = leds;
		driver = new WS281x(gpioNum, BRIGHTNESS, leds.size());
	}
	
	public void update() {
		for(int i = 0; i < getLedCount(); i++) {
			driver.setPixelColour(i, leds.get(i).getColor().getRGB());
		}
		
		driver.render();
	}
	
	public void setColorToAllLed(Color color) {
		for(Led led : leds) {
			led.setColor(color);
		}
	}
	
	public void setColorToLed(Color color, int indexOfLed) {
		validateIndexOfLed(indexOfLed);
		leds.get(indexOfLed).setColor(color);
	}
	
	public Color getLedColor(int indexOfLed) {
		validateIndexOfLed(indexOfLed);
		
		return leds.get(indexOfLed).getColor();
	}
	
	private void validateIndexOfLed(int indexOfLed) {
		if(indexOfLed < 0 || indexOfLed > getLedCount() - 1)
			throw new IllegalArgumentException("Invalid index (" + indexOfLed + ")"
					+ " of LED. Index should be in range 0 .. " + (getLedCount() - 1));
	}
	
	public int getLedCount() {
		return leds.size();
	}

	public List<Led> getLeds() {
		return leds;
	}

}
