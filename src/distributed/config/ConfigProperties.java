package distributed.config;

import java.util.Properties;

public class ConfigProperties {
	private final Properties properties;
	
	public ConfigProperties(Properties properties) {
		this.properties = properties;
	}
	
	public int getInt(String property) {
		var obj = properties.get(property);
		var port = Integer.valueOf((String) obj).intValue();
		return port;
	}
}
