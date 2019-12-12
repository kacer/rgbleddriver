package rgbleddriver.tools;


import java.util.List;

import rgbleddriver.model.UserConfig;

public interface UserConfigManager {
	
	boolean save(List<UserConfig> config);
	
	List<UserConfig> load();

}
