package rgbleddriver.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.swing.Timer;

import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.DeviceDiscoveryListener;
import org.sputnikdev.bluetooth.manager.DiscoveredDevice;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiBcmPin;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPinNumberingScheme;

import rgbleddriver.model.Color;
import rgbleddriver.model.UserConfig;
import rgbleddriver.tools.ConfigService;

@WebListener
public class RGBLedDriverApplication implements ServletContextListener {
	
	private static final int BAD_RSSI = -110;
	private static final int REFRESH_RATE = 1000;
	private static final int LIGHT_INTENSITY = 1000;
	private BluetoothManager bluetoothManager;
	private Photocell photocell;
	private ConfigService configService;
	private LinkedHashMap<String, DiscoveredDevice> devices = new LinkedHashMap<>();
	private Timer timer;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		configService = ConfigService.getInstance();
		bluetoothManager = new BluetoothManagerBuilder()
				.withTinyBTransport(true)
				.withDiscoveryRate(1)
				.withRediscover(true)
				.build();
		bluetoothManager.addDeviceDiscoveryListener(new DeviceDiscoveryListener() {
			
			@Override
			public void discovered(DiscoveredDevice discoveredDevice) {
				String mac = discoveredDevice.getURL().getDeviceCompositeAddress();
				if(findUserConfig(mac) == null)
					return;
				
				if(discoveredDevice.getRSSI() > BAD_RSSI) { // Odstranit taky kdyz prejde do stavu offline nebo timeout?
					devices.put(mac, discoveredDevice);
				} else {
					devices.remove(mac);
				}
			}
		});
		
		final GpioController gpio = GpioFactory.getInstance();
		GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
		final GpioPinDigitalMultipurpose photocellPin = gpio.provisionDigitalMultipurposePin(RaspiBcmPin.getPinByAddress(configService.getConfig().getPhotocellGpioNum()), PinMode.DIGITAL_OUTPUT);
		photocell = new Photocell(photocellPin);
		
		if(configService.getConfig().getUseUserConfig()) {
			bluetoothManager.start(true);
			
			timer = new Timer(REFRESH_RATE, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String mac = null;
					Iterator<Entry<String, DiscoveredDevice>> it = devices.entrySet().iterator();
					if(it.hasNext())
						mac = it.next().getValue().getURL().getDeviceCompositeAddress();

					boolean usePhotocell = configService.getConfig().getUsePhotocell();
					if(mac == null || (usePhotocell && photocell.readValue() < LIGHT_INTENSITY)) {
						configService.getLeds().setColorToAllLed(Color.BLACK);
						configService.getLeds().update();
					} else {
						UserConfig uc = findUserConfig(mac);
						Color color = Color.fromString(uc.getColor());
						configService.getLeds().setColorToAllLed(color);
						configService.getLeds().update();
					}
				}
			});
			timer.start();
		}
	}
	
	private UserConfig findUserConfig(String mac) {
		for(UserConfig uc : configService.getUserConfigs()) {
			if(uc.getMac().equals(mac)) {
				return uc;
			}
		}
		
		return null;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		bluetoothManager.dispose();
	}

}
