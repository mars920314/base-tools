package file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFileReaderLock {
	static Logger logger = LoggerFactory.getLogger(MyFileReaderLock.class);
	
	/**
	 * FileInputStream获取只读文件，FileLock不能对只读文件获取锁
	 * 需要用RandomAccessFile获取文件
	 * @param propertyFile
	 * @param propertyName
	 * @return
	 */
	@Deprecated
	public static String readProperty(String propertyFile, String propertyName) {
		String propertyVal = "";
		Properties dbProperty = new Properties();
		try {
			FileInputStream fi = new FileInputStream(propertyFile);
			FileChannel fci = fi.getChannel();
			FileLock fli = fci.lock();
			InputStreamReader isr = new InputStreamReader(fi, "UTF-8");
			dbProperty.load(isr);
			propertyVal = dbProperty.getProperty(propertyName);
			fli.release();
			fci.close();
			isr.close();
			fi.close();
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
		}
		return propertyVal;
	}

	@Deprecated
	public static List<String> readListFile(String fileName, String encodings){
		List<String> names = new LinkedList<String>();
		String data = "";
		try {
			FileInputStream fi = new FileInputStream(fileName);
			FileChannel fci = fi.getChannel();
			FileLock fli = fci.lock();
			InputStreamReader isr = new InputStreamReader(fi, encodings);
			BufferedReader br = new BufferedReader(isr);
			while((data = br.readLine())!=null)
				if (data.length()>0)
					names.add(data);
			fli.release();
			fci.close();
			br.close();
			isr.close();
			fi.close();
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
		}
		return names;
	}
	
}
