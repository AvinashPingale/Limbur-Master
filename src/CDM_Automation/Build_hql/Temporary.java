package CDM_Automation.Build_hql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.dev.XSSFSave;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import FileUtils.PropertiesFile;

public class Temporary {
	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream("D:\\Limbur\\Limbur\\src\\Input\\Paypal_L1_Data_Mappings_SOR_to_CDM_V05_v4.xlsx");
		XSSFWorkbook book = new XSSFWorkbook(fis);
		XSSFSheet sheet = book.getSheet("Account");
		if (sheet==null) {
			System.out.println("No sheet found");
		} else {
			int lastRowNum = sheet.getLastRowNum();
			System.out.println("Last Row: "+lastRowNum);
				int i=0;
				Row row = sheet.getRow(i);
				int lastCellNum = row.getLastCellNum();
				while (!row.getCell(i).getStringCellValue().equals("Computation / Calculation")) {
					i++;

				}
				System.out.println("Computation / Calculaation found at: "+i);
				for (int j = 1; j < lastRowNum; j++) {
					row=sheet.getRow(j);
					Cell cell = row.getCell(i);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						System.out.println(j + " value: " + "Blank");
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.println(j + " value: " + cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.println(j + " value: " + cell.getStringCellValue());
						break;
					default:
						System.out.println("Invalid value");
						break;
					}
				}
			}

		}
	}


