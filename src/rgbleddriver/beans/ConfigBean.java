package rgbleddriver.beans;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import rgbleddriver.model.UserConfig;
import rgbleddriver.tools.ConfigLoader;
import rgbleddriver.tools.UserConfigManager;

@ManagedBean(name = "configBean", eager = true)
@ApplicationScoped
public class ConfigBean {
	
	protected ConfigLoader config;
	protected UserConfigManager userConfigManager;
	
	private int gpioNum;
	private int countOfLed;
	private int photocellNum;
	private boolean usePhotocell;
	private boolean useUserConfig;
	private String name;
	private String mac;
	private String color;
	
	private List<UserConfig> userConfigs;
	
	
	@PostConstruct
	public void init() {
		config = new ConfigLoader();
		userConfigManager = config;
		gpioNum = config.getGpioNum();
		countOfLed = config.getCountOfLed();
		photocellNum = config.getPhotocellGpioNum();
		usePhotocell = config.getUsePhotocell();
		useUserConfig = config.getUseUserConfig();
		userConfigs = userConfigManager.load();
	}
	
	public String save() {
		if(countOfLed <= 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Poèet LED musí být kladné èíslo!"));
			return null;
		}
		
		config.setCountOfLed(countOfLed);
		config.setGpioNum(gpioNum);
		config.setPhotocellGpioNum(photocellNum);
		config.setUsePhotocell(usePhotocell);
		config.setUseUserConfig(useUserConfig);
		
		if(config.save()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Konfigurace byla úspìšnì zmìnìna. Zmìna se projeví po restartu aplikace."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Z nìjakého dùvodu se nepovedlo konfiguraci uložit. Zkuste to pozdìji."));
		}
		
		return null;
	}
	
	public void delete(String id) {
		UUID uuid = UUID.fromString(id);
		UserConfig config = null;
		for(UserConfig uc : userConfigs) {
			if(uc.getId().equals(uuid))
				config = uc;
		}
		userConfigs.remove(config);
		userConfigManager.save(userConfigs);
	}
	
	public void add() {
		UserConfig userConfig = new UserConfig();
		userConfig.setId(UUID.randomUUID());
		userConfig.setName(name);
		userConfig.setMac(mac);
		userConfig.setColor(color);
		userConfigs.add(userConfig);
		
		name = null;
		mac = null;
		color = null;
		
		userConfigManager.save(userConfigs);
	}
	
	public ConfigLoader getConfig() {
		return config;
	}

	public int getGpioNum() {
		return gpioNum;
	}

	public void setGpioNum(int gpioNum) {
		this.gpioNum = gpioNum;
	}

	public int getCountOfLed() {
		return countOfLed;
	}

	public void setCountOfLed(int countOfLed) {
		this.countOfLed = countOfLed;
	}

	public List<UserConfig> getUserConfigs() {
		return userConfigs;
	}

	public void setUserConfigs(List<UserConfig> userConfigs) {
		this.userConfigs = userConfigs;
	}

	public int getPhotocellNum() {
		return photocellNum;
	}

	public void setPhotocellNum(int photocellNum) {
		this.photocellNum = photocellNum;
	}

	public boolean isUsePhotocell() {
		return usePhotocell;
	}

	public void setUsePhotocell(boolean usePhotocell) {
		this.usePhotocell = usePhotocell;
	}

	public boolean isUseUserConfig() {
		return useUserConfig;
	}

	public void setUseUserConfig(boolean useUserConfig) {
		this.useUserConfig = useUserConfig;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
