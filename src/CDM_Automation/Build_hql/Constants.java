package CDM_Automation.Build_hql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import FileUtils.PropertiesFile;

public class Constants {
	public static File file=null;
	public static FileInputStream fis=null;
	public static Properties prop=null;
	public static XSSFWorkbook xlsxBook=null;
	public static XSSFSheet xlsxSheet=null;
	public static Row row=null;
	public static HSSFWorkbook xlsBook=null;
	public static HSSFSheet xlsSheet=null;
	public static Logger logger;
	public static FileWriter writer;
	//Property file content
	static String xlFilePath=PropertiesFile.readProperty("wbfilename");
	static String property          = PropertiesFile.readProperty("property");
	static String table_db_tag      = PropertiesFile.readProperty("table_db_tag");
	static String table_db_view_tag = PropertiesFile.readProperty("table_db_view_tag");
	static String outputdir         = PropertiesFile.readProperty("outputdir");
	static String cluster_tag       = PropertiesFile.readProperty("cluster_tag");
	static String datalake_dir_tag  = PropertiesFile.readProperty("datalake_dir_tag");
	static String  domain_tag       = PropertiesFile.readProperty("domain_tag");
	static String yyyymmdd_tag      = PropertiesFile.readProperty("yyyymmdd_tag");
	static String data_construct    = PropertiesFile.readProperty("data_construct");
	static String delta_tag         = PropertiesFile.readProperty("delta_tag");
	static String base_tag          = PropertiesFile.readProperty("base_tag");
	static String year_tag 			= PropertiesFile.readProperty("year_tag");
	static String month_tag 		= PropertiesFile.readProperty("month_tag");
	static String day_tag 			= PropertiesFile.readProperty("day_tag");
	static String seqnum_tag 		= PropertiesFile.readProperty("seqnum_tag");
	static String tables			=PropertiesFile.readProperty("tables");
	static String yyyy_tag			=PropertiesFile.readProperty("yyyy_tag");
	static String mm_tag			=PropertiesFile.readProperty("mm_tag");
	static String dd_tag			=PropertiesFile.readProperty("dd_tag");
	static String date_default= PropertiesFile.readProperty("date_default");
	static String int_default= PropertiesFile.readProperty("int_default");
	static String string_default= PropertiesFile.readProperty("string_default");
	static String timestamp_default= PropertiesFile.readProperty("timestamp_default");
	static String decimal_default= PropertiesFile.readProperty("decimal_default");
	static String domain_db_prefix_tag= PropertiesFile.readProperty("domain_db_prefix_tag");
	static String domain_db_suffix_tag= PropertiesFile.readProperty("domain_db_suffix_tag");
	


}
