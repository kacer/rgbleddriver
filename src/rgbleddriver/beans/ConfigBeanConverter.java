package rgbleddriver.beans;

import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = UUID.class)
@RequestScoped
public class ConfigBeanConverter extends AbstractBean implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || value.isEmpty())
			return null;
		
		return UUID.fromString(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null)
			return null;
		
		if(value instanceof UUID) {
			return ((UUID) value).toString();
		}
		
		return null;
	}

}
