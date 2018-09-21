package CDM_Automation.Build_hql;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import FileUtils.ExcelUtils;

public class CreateDDL {
	static StringBuffer content = null;
	static Logger log = Logger.getLogger(CreateDDL.class.getName());

	public static void writeDDL() {

		log.info("Creating a DDL file");
		Constants.file = new File(Constants.outputdir + "\\" + Constants.property + "\\" + Constants.property + "_"
				+ Constants.tables + "_ddl" + ".hql");

		content = new StringBuffer("");
		try {
			Constants.file.getParentFile().mkdir();
			Constants.file.createNewFile();
			log.info("file \'" + Constants.property + "_" + Constants.tables + "_ddl" + ".hql"
					+ "\' created successfully");
			Constants.writer = new FileWriter(Constants.file);
			// System.out.println("File created successfuly "+Constants.file.getName());
			if (Constants.data_construct.equalsIgnoreCase("table")) {
				content.append("DROP TABLE IF EXIST " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + "_" + Constants.yyyymmdd_tag + " PURGE;" + "\n");
				content.append("CREATE EXTERNAL TABLE " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + "_" + Constants.yyyymmdd_tag + " (" + "\n");
			} else {
				content.append("DROP TABLE IF EXISTS " + Constants.table_db_tag.toUpperCase() + "."
						+ Constants.tables.toUpperCase() + ";" + "\n");
				content.append("\n" + "CREATE EXTERNAL TABLE IF NOT EXISTS " + Constants.table_db_tag.toUpperCase()
						+ "." + Constants.tables.toUpperCase() + " (" + "\n");

			}

			List<String> l1_column_name = ExcelUtils.getEntireColumn("L1 Column Name", Constants.tables);
			List<String> l1_data_type = ExcelUtils.getEntireColumn("L1 Data Type", Constants.tables);
			Iterator itr = l1_column_name.iterator();
			Iterator itr1 = l1_data_type.iterator();
			while (itr.hasNext()) {
				content.append("\t" + String.valueOf(itr.next()).toUpperCase() + "\t \t "
						+ String.valueOf(itr1.next()).toUpperCase() + "\n");
			}
			content.append(") \n");
			if (Constants.data_construct.equalsIgnoreCase("table")) {
				content.append("STORED AS PARQUET" + "\n" + "LOCATION \'hdfs://" + Constants.cluster_tag + "/sys/"
						+ Constants.datalake_dir_tag + "/" + Constants.property + "/data/managed_zone/"
						+ Constants.domain_tag + "/" + Constants.tables.toUpperCase() + "_" + Constants.yyyymmdd_tag
						+ "\';" + "\n");
			} else {
				content.append(
						"PARTITIONED BY (year string, month string, day string, partition_type string, seqnum string) STORED AS PARQUET \n"
								+ "LOCATION 'hdfs://" + Constants.cluster_tag + "/sys/" + Constants.datalake_dir_tag
								+ "/" + Constants.property + "/data/managed_zone/" + Constants.domain_tag + "/"
								+ Constants.tables.toUpperCase() + "';\n" + "\n" + "ALTER TABLE "
								+ Constants.table_db_tag.toUpperCase() + "." + Constants.tables.toUpperCase()
								+ " DROP IF EXISTS PARTITION " + "(year = '" + Constants.year_tag + "\', month = \'"
								+ Constants.month_tag + "\', " + " day = \'" + Constants.day_tag
								+ "\', partition_type=\'" + Constants.base_tag + "', seqnum=\'48\';\n" + "\n"
								+ "ALTER TABLE " + Constants.table_db_tag.toUpperCase() + "."
								+ Constants.tables.toUpperCase() + " ADD PARTITION " + "(year = '" + Constants.year_tag
								+ "', month = '" + Constants.month_tag + "', " + " day = \'" + Constants.day_tag
								+ "', partition_type='" + Constants.base_tag + "', seqnum='48';\n");
			}
			Constants.writer.write(new String(content));
			Constants.writer.close();
		} catch (IOException e) {
			log.error("Cannot create file" + Constants.file.getAbsolutePath());
			e.printStackTrace();
		}
		log.info("File " + Constants.property + "_" + Constants.tables + "_ddl.hql" + " created successfully");

	}

}
