package file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Properties;

public class MyFileWriter {
	static Logger logger = LoggerFactory.getLogger(MyFileWriter.class);
	
	static public void writeProperty(String propertyFile, String propertyName, String propertyVal) {
		Properties dbProperty = new Properties();
		try {
			dbProperty.load(new FileInputStream(propertyFile));
			dbProperty.setProperty(propertyName, propertyVal);
			dbProperty.store(new FileOutputStream(propertyFile), "auto-gen properties");
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}
	
	static public void writeFile(String content, String fileName) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			fw.write(content);
			fw.close();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}

	static public void clearFile(String fileName) {
		// clear ori file
		try {
			FileWriter fw = new FileWriter(fileName, false);
			fw.close();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}
	
}
