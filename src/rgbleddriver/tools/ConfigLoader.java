package rgbleddriver.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import rgbleddriver.model.UserConfig;

public class ConfigLoader implements UserConfigManager {
	
	private static final String CONFIG_FILE = "/WEB-INF/config.json";
	private static final String KEY_COUNT_OF_LED = "countOfLed";
	private static final String KEY_GPIO_NUM = "gpioNum";
	private static final String KEY_PHOTOCELL_GPIO_NUM = "photocellGpioNum";
	private static final String KEY_USE_PHOTOCELL = "usePhotocell";
	private static final String KEY_USE_USER_CONFIG = "	";
	
	private static final String USER_CONFIG_FILE = "/WEB-INF/user-config.json";
	
	
	private JSONObject config;
	private File configFile;
	
	public ConfigLoader() {		
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		JSONParser parser = new JSONParser();
		try {
			configFile = new File(context.getResource(CONFIG_FILE).toURI());
			//Osetrit nullovou hodnotu
			config = (JSONObject) parser.parse(new FileReader(configFile));
		} catch (IOException | ParseException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public boolean save() {
		try {
			FileWriter file = new FileWriter(configFile);
			file.write(config.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean save(List<UserConfig> config) {
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		try {
			FileWriter fw = new FileWriter(new File(context.getResource(USER_CONFIG_FILE).toURI()));
			Gson gson = new Gson();
			gson.toJson(config, fw);
			fw.flush();
			fw.close();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}

	@Override
	public List<UserConfig> load() {
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		try {
			FileReader fr = new FileReader(new File(context.getResource(USER_CONFIG_FILE).toURI()));
			Gson gson = new Gson();
			Type type = TypeToken.getParameterized(List.class, UserConfig.class).getType();
			List<UserConfig> userConfig = gson.fromJson(fr, type); 
			fr.close();
			
			return userConfig;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		return new LinkedList<UserConfig>();
	}
	
	private void validateGpioNum(int gpioNum) {
		if(gpioNum < 0 || gpioNum > 40) 
			throw new IllegalArgumentException("Invalid gpio num: " + gpioNum + "!");
	}

	@SuppressWarnings("unchecked")
	public void setGpioNum(int gpioNum) {
		validateGpioNum(gpioNum);
		
		config.put(KEY_GPIO_NUM, gpioNum);
	}
	
	@SuppressWarnings("unchecked")
	public void setCountOfLed(int countOfLed) {
		if(countOfLed <= 0)
			throw new IllegalArgumentException("Total count of led (" +
							countOfLed + ") should be positive number!");
		
		config.put(KEY_COUNT_OF_LED, countOfLed);
	}
	
	@SuppressWarnings("unchecked")
	public void setPhotocellGpioNum(int photocellGpioNum) {
		validateGpioNum(photocellGpioNum);
		
		config.put(KEY_PHOTOCELL_GPIO_NUM, photocellGpioNum);
	}
	
	@SuppressWarnings("unchecked")
	public void setUsePhotocell(boolean usePhotocell) {
		config.put(KEY_USE_PHOTOCELL, usePhotocell);
	}
	
	@SuppressWarnings("unchecked")
	public void setUseUserConfig(boolean useUserConfig) {
		config.put(KEY_USE_USER_CONFIG, useUserConfig);
	}
	
	public int getGpioNum() {
		return Integer.parseInt(String.valueOf(config.get(KEY_GPIO_NUM)));
	}
	
	public int getCountOfLed() {
		return Integer.parseInt(String.valueOf(config.get(KEY_COUNT_OF_LED)));
	}
	
	public int getPhotocellGpioNum() {
		return Integer.parseInt(String.valueOf(config.get(KEY_PHOTOCELL_GPIO_NUM)));
	}
	
	public boolean getUsePhotocell() {
		return Boolean.parseBoolean(String.valueOf(config.get(KEY_USE_PHOTOCELL)));
	}
	
	public boolean getUseUserConfig() {
		return Boolean.parseBoolean(String.valueOf(config.get(KEY_USE_USER_CONFIG)));
	}
	
}
