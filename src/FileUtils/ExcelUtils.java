package FileUtils;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.XLSBUnsupportedException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import CDM_Automation.Build_hql.Constants;
import CDM_Automation.Build_hql.CreateHQL;

public class ExcelUtils {
	static Logger log = Logger.getLogger(CreateHQL.class);
	
	//log.info("Entering getEntireColumn for column: " + columnName);
	static String xlFile = PropertiesFile.readProperty("wbfilename");
	//log.info("Reading Excel: " + xlFile);
	static String parts[] = xlFile.split("/");
	static String fileName = parts[parts.length - 1];
	static String fileExtension = fileName.split("\\.")[1];

	public static boolean isCellContainsValue(int fcell, int lcell, Row row) {
		log.info("Checking whether cell contains value or not");
		boolean flag = false;
		for (int i = fcell; i < lcell; i++) {
			if (//StringUtils.isEmpty(String.valueOf(row.getCell(i))) == true
					// StringUtils.isWhitespace(String.valueOf(row.getCell(i))) == true
					 StringUtils.isBlank(String.valueOf(row.getCell(i))) == true
					|| String.valueOf(row.getCell(i)).length() == 0 || row.getCell(i) == null) {
				// Do nothing
			} else {
				log.info("Cell contains: " + getValueFromRow(row, fcell));
				flag = true;
			}
		}
		return flag;
	}
	public static String getValueFromCell() {
		log.info("reading value from cell");
		String cellValue=null;
		
		return cellValue;

	}
	public static List<String> getEntireColumn(String columnName, String sheetName) {
		List<String> entireColumn = new ArrayList<String>(); //Keep this as local so that we can have separate copy for separate column when we will call this
		//entireColumn = new ArrayList<String>();
		log.info("Entering getEntireColumn for column: " + columnName);
		//xlFile = PropertiesFile.readProperty("wbfilename");
		log.info("Reading Excel: " + xlFile);
		//parts[] = xlFile.split("/");
		//String fileName = parts[parts.length - 1];
		//String fileExtension = fileName.split("\\.")[1];
		switch (fileExtension) {
		case "xls":
			System.out.println("Thi is xls");
			/*
			 * This code need to be written later on as it has low priority
			 */

			break;
		case "xlsx":
					//System.out.println("File name: "+xlFile);
					Constants.file = new File(xlFile);
					try {
						Constants.fis = new FileInputStream(Constants.file.getAbsolutePath());
						Constants.xlsxBook = new XSSFWorkbook(Constants.fis);
					} catch (FileNotFoundException e) {
						System.out.println("File " + xlFile + " not found");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("Problem in reading " + xlFile);
						e.printStackTrace();
					}
					
					Constants.xlsxSheet = Constants.xlsxBook.getSheet(sheetName);
					if (Constants.xlsxSheet.equals(null)) {
						log.error("There is no sheet with name " + sheetName + " exist in workbook");
					} 
					else {
						log.info("Reading sheet: " + sheetName);

						int lastRow = Constants.xlsxSheet.getLastRowNum();
						System.out.println("Last Row: "+lastRow);
						Row row = Constants.xlsxSheet.getRow(0);
						int expectedColumn = 0;
						while (!row.getCell(expectedColumn).getStringCellValue().equalsIgnoreCase(columnName)) {
							expectedColumn++;
						}
						System.out.println("Column "+columnName+" found at: "+expectedColumn);
						for (int j = 1; j <=lastRow; j++) {
							row = Constants.xlsxSheet.getRow(j);

							//if (ExcelUtils.isCellContainsValue(expectedColumn, lastRow, row)) {
								StringBuffer value = getValueFromRow(row, expectedColumn);
								//System.out.println("Line "+j+": "+value);
								entireColumn.add(new String(value));
							//}
						}
					}
					break;

		default:
				log.error("Invalid Excel file name");
				break;
		}//End of Switch Case
		
		log.info("Leaving getEntireColumn for " + columnName);
		return entireColumn;

	}

	public static StringBuffer getValueFromRow(Row row, int ColumnNumber) {
		Cell cell = row.getCell(ColumnNumber);
		StringBuffer value = new StringBuffer();
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
										value.append("");
										break;
		case Cell.CELL_TYPE_NUMERIC:
										value.append(cell.getNumericCellValue());
										break;
		case Cell.CELL_TYPE_STRING:
										value.append(cell.getStringCellValue());
										break;

		default:
										log.error("Invalid value found in cell");
										break;
		}//End of Switch
		return value;
	}

}
