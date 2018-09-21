package CDM_Automation.Build_hql;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.formula.functions.IPMT;
import org.apache.poi.ss.usermodel.DataValidation.ErrorStyle;

public class ExecuteHQL {
	public void runCommand() {
		Runtime run=Runtime.getRuntime();
		try {
			ProcessBuilder builder=new ProcessBuilder("putty.exe");
			Process p=builder.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public static void main(String[] args) {
		ExecuteHQL exe=new ExecuteHQL();
		exe.runCommand();
	}

}
