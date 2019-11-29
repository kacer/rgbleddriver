package rgbleddriver.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
public abstract class AbstractBean {
	
	@ManagedProperty("#{configBean}")
	protected ConfigBean configBean;
	
	
	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}
	
}
