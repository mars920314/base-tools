package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharSource;
import com.google.common.io.Files;

public class FileReader {
	static Logger logger = LoggerFactory.getLogger(FileReader.class);
	
	public static String readSchema(String schemaFile){
		String schemaStr = "";
		try {
			CharSource mapping = Files.asCharSource(new File(schemaFile),
					Charset.forName("utf-8"));
			schemaStr = mapping.read();
		} catch (Exception e) {
			logger.info("Error: " + e.toString());
		}
		return schemaStr;
	}
	
	public static String readProperty(String propertyFile, String propertyName) {
		String propertyVal = "";
		Properties dbProperty = new Properties();
		try {
			dbProperty.load(new FileInputStream(propertyFile));
			propertyVal = dbProperty.getProperty(propertyName);
		} catch (Exception e) {
			logger.info("Error: " + e.toString());
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
			logger.info("Error: " + e.toString());
		}
		return names;
	}
	
}
