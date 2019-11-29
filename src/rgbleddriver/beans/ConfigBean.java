package rgbleddriver.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import rgbleddriver.tools.ConfigLoader;

@ManagedBean(name = "configBean", eager = true)
@ApplicationScoped
public class ConfigBean {
	
	protected ConfigLoader config;
	
	private int gpioNum;
	private int countOfLed;
	
	
	@PostConstruct
	public void init() {
		config = new ConfigLoader();
		gpioNum = config.getGpioNum();
		countOfLed = config.getCountOfLed();
	}
	
	public String save() {
		if(countOfLed <= 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Po�et LED mus� b�t kladn� ��slo!"));
			return null;
		}
		
		config.setCountOfLed(countOfLed);
		config.setGpioNum(gpioNum);
		
		if(config.save()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Konfigurace byla �sp�n� zm�n�na. Zm�na se projev� po restartu aplikace."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Z n�jak�ho d�vodu se nepovedlo konfiguraci ulo�it. Zkuste to pozd�ji."));
		}
		
		return null;
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

}
