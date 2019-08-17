package mixcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 可以运行含有python第三方库的程序，调用本地python执行 但不能实现java和python的多次交互，不能保留python执行时的中间结果
 */
public class JavaCallPythonByRuntime {

	public static void main(String[] args) {
		runScript();
	}

	public static void runScript() {
		Process proc;
		try {
			// 用command执行py文件
			proc = Runtime.getRuntime().exec("python etc/ScriptMethod.py");
			// 用args执行py文件
//			String[] args = new String[] { "python", "etc/ScriptMethod.py", "1", "1" };
//			proc = Runtime.getRuntime().exec(args);
			// 用输入输出流来截取结果
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null)
				System.out.println(line);
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
