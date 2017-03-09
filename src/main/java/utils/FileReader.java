package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class FileReader {
	
	public static String readProperty(String propertyFile, String propertyName) {
		String propertyVal = "";
		Properties dbProperty = new Properties();
		try {
			dbProperty.load(new FileInputStream(propertyFile));
			propertyVal = dbProperty.getProperty(propertyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertyVal;
	}
	
	public static List<String> readListFile(String fileName, String encodings){
		List<String> names = new LinkedList<String>();
		String data = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encodings));
			while((data = br.readLine())!=null)
				if (data.length()>0)
					names.add(data);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return names;
	}
	
}
