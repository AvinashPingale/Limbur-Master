package FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import CDM_Automation.Build_hql.Constants;

public class PropertiesFile {
	public static String readProperty(String propertyName) {
		//System.out.println("Reading properties file");
		try {
			
			Constants.file=new File("src/Config/paypal.properties");
			Constants.fis=new FileInputStream(Constants.file.getAbsolutePath());
			Constants.prop=new Properties();
			Constants.prop.load(Constants.fis);
			}catch (IOException e) {
				e.printStackTrace();
			}
		//System.out.println("Leaving properties file");
		return Constants.prop.getProperty(propertyName);

	}

}
