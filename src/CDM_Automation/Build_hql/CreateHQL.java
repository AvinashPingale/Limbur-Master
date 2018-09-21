package CDM_Automation.Build_hql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.print.DocFlavor.URL;

import org.apache.commons.logging.impl.ServletContextCleaner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.xml.DOMConfigurator;

import FileUtils.ExcelUtils;
import FileUtils.PropertiesFile;

public class CreateHQL {

	static Logger log = Logger.getLogger(CreateHQL.class);
	static StringBuffer content;

	public static void main(String[] args) {
		
		//CreateDDL.writeDDL();
		//System.out.println("DDL created successfully");
		CreateHist.writeHist();
		System.out.println("Hist created successfully");
		CreateHist.getDomain("account");
	}

}
