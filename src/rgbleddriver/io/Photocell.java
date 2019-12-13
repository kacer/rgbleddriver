package rgbleddriver.io;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;

public class Photocell {
	
	private static final int MAX_VALUE = 30000;
	
	private GpioPinDigitalMultipurpose pin;
	
	public Photocell(GpioPinDigitalMultipurpose pin) {
		this.pin = pin;
		pin.setMode(PinMode.DIGITAL_OUTPUT);
	}
	
	public int readValue() {
		int value = 0;
		
		pin.setMode(PinMode.DIGITAL_OUTPUT);
		pin.low();
		
		pin.setMode(PinMode.DIGITAL_INPUT);
		while(pin.isLow()) {
			if(++value == MAX_VALUE) {
				break;
			}
		}
		
		return value;
	}
	
}
