package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//阻塞方式获得排它锁
public class MyFileWriterLock {
	static Logger logger = LoggerFactory.getLogger(MyFileWriterLock.class);
	
	static public void writeProperty(String propertyFile, String propertyName, String propertyVal) {
		Properties dbProperty = new Properties();
		try {
			//read
			FileInputStream fi = new FileInputStream(propertyFile);
			InputStreamReader isr = new InputStreamReader(fi, "UTF-8");
			dbProperty.load(isr);
			isr.close();
			fi.close();
			//write
			dbProperty.setProperty(propertyName, propertyVal);
			FileOutputStream fo = new FileOutputStream(propertyFile);
			FileChannel fco = fo.getChannel();
			//重试3次获取锁，否则抛出错误
			FileLock flo = null;
			int retry = 3;
			while (true) {
				try {
					flo = fco.lock();
					break;
				} catch (OverlappingFileLockException e) {
					if (retry > 0) {
						retry--;
						Thread.sleep((long) (Math.random() * 1000));
					} else {
						fo.close();
						throw e;
					}
				}
			}
			OutputStreamWriter ofw = new OutputStreamWriter(fo, "UTF-8");
			dbProperty.store(ofw, "auto-gen properties");
			flo.release();
			fco.close();
			ofw.close();
			fo.close();
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
