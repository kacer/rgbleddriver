package rgbleddriver.tools;

import java.util.List;

import rgbleddriver.model.LedStrip;
import rgbleddriver.model.UserConfig;

public class ConfigService {
	
	private static volatile ConfigService instance;
	private final ConfigLoader config;
	private final List<UserConfig> userConfigs;
	private final LedStrip leds;
	
	private ConfigService() {
		config = new ConfigLoader();
		userConfigs = config.load();
		leds = new LedStrip(config.getCountOfLed(), config.getGpioNum());
	}
	
	public ConfigLoader getConfig() {
		return config;
	}
	
	public List<UserConfig> getUserConfigs() {
		return userConfigs;
	}
	
	public LedStrip getLeds() {
		return leds;
	}
	
	public static ConfigService getInstance() {
		if (instance == null) {
            synchronized (ConfigService.class) {
                if (instance == null) {
                    instance = new ConfigService();
                }
            }
		}
        return instance;
	}
}
