package mixcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class RunShell {

	public static String exec(String[] cmdarray, String[] envp) {
		Process process = null;
		Runtime runTime = Runtime.getRuntime();
		if (runTime == null) {
			System.err.println("Create runtime false!");
			return null;
		}
		try {
			StringBuilder returnString = new StringBuilder();
			if(cmdarray.length==1){
				if(envp==null)
					process = runTime.exec(cmdarray[0]);
				else
					process = runTime.exec(cmdarray[0], envp);
			}
			else{
				if(envp==null)
					process = runTime.exec(cmdarray);
				else
					process = runTime.exec(cmdarray, envp);
			}
			int status = process.waitFor();
			if (status != 0)
				System.out.println("Failed to call shell's command: " + String.join(" ", cmdarray));
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			PrintWriter output = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				returnString.append(line + "\n");
			}
			input.close();
			output.close();
			process.destroy();
			return returnString.toString();
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		String stdout = RunShell.exec(args, null);
//		String stdout = RunShell.exec(new String[]{"sh", "/home/qstream/guoxin-automl/bin/TrainingSubmit.sh", "xgboost", "path"}, null);
//		String stdout = RunShell.exec(new String[]{"ssh", "qstream@10.24.51.72", "\"bash /datayes/puma/news_guoxin/bin/restart.sh\""}, null);
		System.out.println(stdout);
	}

}
