package rgbleddriver.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigLoader {
	
	private static final String CONFIG_FILE = "/WEB-INF/config.json";
	
	private static final String KEY_COUNT_OF_LED = "countOfLed";
	private static final String KEY_GPIO_NUM = "gpioNum";
	
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
	
	@SuppressWarnings("unchecked")
	public void setGpioNum(int gpioNum) {
		if(gpioNum < 0 || gpioNum > 40) 
			throw new IllegalArgumentException("Invalid gpio num: " + gpioNum + "!");
		
		config.put(KEY_GPIO_NUM, gpioNum);
	}
	
	@SuppressWarnings("unchecked")
	public void setCountOfLed(int countOfLed) {
		if(countOfLed <= 0)
			throw new IllegalArgumentException("Total count of led (" +
							countOfLed + ") should be positive number!");
		
		config.put(KEY_COUNT_OF_LED, countOfLed);
	}
	
	public int getGpioNum() {
		return Integer.parseInt(String.valueOf(config.get(KEY_GPIO_NUM)));
	}
	
	public int getCountOfLed() {
		return Integer.parseInt(String.valueOf(config.get(KEY_COUNT_OF_LED)));
	}

}
