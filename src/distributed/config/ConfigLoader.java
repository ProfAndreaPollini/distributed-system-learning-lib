package distributed.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ConfigLoader {
	
	public static ConfigProperties loadFromResource(InputStream configStream) throws IOException {
		Properties props = new Properties();
		props.load(configStream);
		System.out.println(props);
		return new ConfigProperties(props);
	}
	
//	public static Properties loadFromClassPath(String fileName) throws Exception {
//		Properties props = new Properties();
//		URL url = ClassLoader.getSystemResource(fileName);
//		props.load(url.openStream());
//		System.out.println(props);
//		return props;
//	}
//
//	public static Properties loadFromFile(File propsFile) throws IOException {
//		Properties props = new Properties();
//		FileInputStream fis = new FileInputStream(propsFile);
//		props.load(fis);
//		fis.close();
//		System.out.println(props);
//		return props;
//	}
	
	
}

