package CDM_Automation.Build_hql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import FileUtils.ExcelUtils;
import FileUtils.PropertiesFile;

public class CreateHist {
	static StringBuffer content = null;
	public static Logger log = Logger.getLogger(CreateHist.class.getName());

	public static StringBuffer getDomain(String tableName) {
		StringBuffer domain = null;
		String xlFile = PropertiesFile.readProperty("wbfilename");
		log.info("Reading Excel: " + xlFile);
		String parts[] = xlFile.split("/");
		String fileName = parts[parts.length - 1];
		String fileExtension = fileName.split("\\.")[1];
		Constants.file = new File(xlFile);
		try {
			Constants.fis = new FileInputStream(Constants.file.getAbsolutePath());
			Constants.xlsxBook = new XSSFWorkbook(Constants.fis);
			Constants.xlsxSheet = Constants.xlsxBook.getSheet("Source Table Information");
			if (Constants.xlsxSheet.equals(null)) {
				log.error("There is no sheet with name Source Table Information exist in workbook. Please add it");
			} else {
				log.info("Reading sheet: Source Table Information");
				int lastRow = Constants.xlsxSheet.getLastRowNum();
				System.out.println("Last Row: " + lastRow);
				Row row = Constants.xlsxSheet.getRow(0);
				int expectedColumn = 1;
				for (int j = 1; j <= lastRow; j++) {
					row = Constants.xlsxSheet.getRow(j);

					if (row.getCell(0).getStringCellValue().equals(tableName)) {
						domain = ExcelUtils.getValueFromRow(row, expectedColumn);
						break;
					}
				}
			}

		} catch (IOException e) {
			log.error("Unable to read file: " + xlFile);
		}
		return domain;
	}

	/*
	 * Below function is master function to write hql statements to history load
	 * file
	 * 
	 */
	public static void writeHist() {
		content = new StringBuffer("");
		log.info("Creating a history file");
		Constants.file = new File(Constants.outputdir + "\\" + Constants.property + "\\" + Constants.property + "_"
				+ Constants.tables + "_hist" + ".hql");
		try {
			Constants.file.getParentFile().mkdir();
			Constants.file.createNewFile();
			log.info("file \'" + Constants.property + "_" + Constants.tables + "_hist" + ".hql"
					+ "\' created successfully");
			Constants.writer = new FileWriter(Constants.file);

			if (Constants.data_construct.equalsIgnoreCase("table")) {
				content.append("DROP TABLE " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + "_" + Constants.yyyymmdd_tag.toUpperCase() + ";\n");
				content.append("\n");
				content.append("INSERT OVERWRITE TABLE " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + "_" + Constants.yyyymmdd_tag.toLowerCase() + "_temp\n");

			} else {
				content.append("ALTER TABLE " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + " ADD IF NOT EXISTS PARTITION " + "(year = '"
						+ Constants.yyyy_tag + "', month = '" + Constants.mm_tag + ", day = '" + Constants.dd_tag
						+ "', partition_type = '" + Constants.base_tag + "', seqnum='48');\n");
				content.append("\n");
				content.append("INSERT OVERWRITE TABLE " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + " PARTITION " + "(year = '" + Constants.yyyy_tag
						+ "', month = '" + Constants.mm_tag + ", day = '" + Constants.dd_tag + "', partition_type = '"
						+ Constants.base_tag + "', seqnum='48')\n");

			}
			content.append("\t SELECT \n");
			List<String> computation_calculation = ExcelUtils.getEntireColumn("Computation / Calculation",
					Constants.tables);
			List<String> column_name = ExcelUtils.getEntireColumn("L1 Column Name", Constants.tables);
			List<String> dataType = ExcelUtils.getEntireColumn("L1 Data Type", Constants.tables);
			List<String> joinConditions = ExcelUtils.getEntireColumn("Join Condition", Constants.tables);
			List<String> dataLakeTables = ExcelUtils.getEntireColumn("Data Lake Table Name", Constants.tables);
			List<String> dataLakeColumns = ExcelUtils.getEntireColumn("Data Lake Column Name", Constants.tables);
			System.out.println("Length of DataLakeTables: " + dataLakeTables.size());
			System.out.println("Length of DataLakeColumns: " + dataLakeColumns.size());
			Iterator computationIterator = computation_calculation.iterator();
			Iterator columnNameIterator = column_name.iterator();
			Iterator dataTypeIterator = dataType.iterator();
			for (int i = 0; i < computation_calculation.size(); i++) {
				if (computation_calculation.get(i).equals("")) {
					if (dataType.get(i).contains("decimal")) {
						content.append("\t" + "\t" + Constants.decimal_default + " as " + column_name.get(i) + "\n");
					} else if (dataType.get(i).contains("varchar")) {
						content.append("\t" + "\t" + Constants.string_default + " as " + column_name.get(i) + "\n");
					} else if (dataType.get(i).contains("timestamp")) {
						content.append("\t" + "\t" + Constants.timestamp_default + " as " + column_name.get(i) + "\n");
					} else if (dataType.get(i).contains("data")) {
						content.append("\t" + "\t" + Constants.date_default + " as " + column_name.get(i) + "\n");
					}

				} else {
					content.append("\t" + "\t" + computation_calculation.get(i) + " as " + column_name.get(i) + "\n");

				}
			}
			content.append("FROM \n");
			String domain = new String(getDomain(Constants.tables));
			if (domain.equals(null)) {
				log.error("No domain found for table: " + Constants.tables);
			} else {
				content.append("\t" + Constants.domain_db_prefix_tag + domain.toUpperCase()
						+ Constants.domain_db_suffix_tag + "." + Constants.tables + " " + Constants.tables + "\n");
			}
			content.append("LEFT OUTER JOIN \n");

			// Below code will check whether joining condition is empty.
			// for(String joinCondition:joinConditions) {
			// if (joinCondition.length()!=0) {
			generateLeftOuterJoin(joinConditions, dataLakeTables, dataLakeColumns);
			// }
			// }

			Constants.writer.write(new String(content));
			Constants.writer.close();
		} catch (IOException e) {
			log.error("Problem creating a file: " + e.getMessage());
		}
		log.info("File " + Constants.property + "_" + Constants.tables + "_hist.hql" + " created successfully");

	}

	/*
	 * Below function generates multiple left outer join statements, based on Join
	 * Conditions and Data Lake Tables present in Mapping Document.
	 */
	public static StringBuffer generateLeftOuterJoin(List<String> joinConditions, List<String> dataLakeTables,
			List<String> dataLakeColumns) {

		StringBuffer leftOuterJoin = new StringBuffer("");
		ArrayList<String> joinInfo=new ArrayList<String>();
		Map<String, Integer> frequencyMap = new LinkedHashMap<String, Integer>();
		int offset=0, arrOffset=0;
		for (int i = 0; i < joinConditions.size(); i++) {

			if (joinConditions.get(i).length() != 0) {

				if (frequencyMap.containsKey(dataLakeTables.get(i))) {
					frequencyMap.put(dataLakeTables.get(i), frequencyMap.get(dataLakeTables.get(i))+1);
					Iterator itr=joinInfo.iterator();
					while(itr.hasNext()) {
						String[] parts=String.valueOf(itr.next()).split("#");
						//for(int j=0; j<parts.length;j++) {
							if(parts[1].equals(dataLakeTables.get(i))) {
								parts[0]=parts[0].concat(","+dataLakeColumns.get(i));
								System.out.println("Joined columns: "+parts[0]);
							}
							
						//}
						System.out.println("*************************************");
					}

				} else {
					frequencyMap.put(dataLakeTables.get(i), 1);
					String joiningInfo=dataLakeColumns.get(i)+"#"+dataLakeTables.get(i)+"#"+joinConditions.get(i);
					joinInfo.add(offset, joiningInfo);
					offset+=1;
					joiningInfo="";
				}
				//leftOuterJoin.append(dataLakeColumns.get(i));

			}
		}
		System.out.println("joining info"+joinInfo);

		return leftOuterJoin;

	}

}
