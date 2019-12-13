package rgbleddriver.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import rgbleddriver.model.LedStrip;
import rgbleddriver.tools.ConfigService;

@ManagedBean(name = "colorBean", eager = true)
@ApplicationScoped
public class ColorBean extends AbstractBean {
	
	protected LedStrip leds;
	
	
	@PostConstruct
	public void init() {
		leds = ConfigService.getInstance().getLeds();
	}
	
	public LedStrip getLeds() {
		return leds;
	}
	
}
