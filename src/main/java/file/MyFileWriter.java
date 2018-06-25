package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFileWriter {
	static Logger logger = LoggerFactory.getLogger(MyFileWriter.class);
	
	static public void writeProperty(String propertyFile, String propertyName, String propertyVal) {
		Properties dbProperty = new Properties();
		try {
			FileInputStream fi = new FileInputStream(propertyFile);
			InputStreamReader isr = new InputStreamReader(fi, "UTF-8");
			dbProperty.load(isr);
			dbProperty.setProperty(propertyName, propertyVal);
			FileOutputStream fo = new FileOutputStream(propertyFile);
			OutputStreamWriter ofw = new OutputStreamWriter(fo, "UTF-8");
			dbProperty.store(ofw, "auto-gen properties");
			isr.close();
			fi.close();
			// close by order
			ofw.close();
			fo.close();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}
	
	static public void writeFile(String content, String fileName, boolean isAdd) {
		try {
			FileWriter fw = new FileWriter(fileName, isAdd);
			fw.write(content);
			fw.close();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}
	
	static public void writeFile(String content, String fileName) {
		writeFile(content, fileName, false);
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
