package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

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

	public static List<String> loadListFile(String fileName, String encodings) {
		List<String> lines = new LinkedList<String>();
		String data = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					ClassLoader.getSystemClassLoader().getResourceAsStream(fileName), encodings));
			while ((data = br.readLine()) != null)
				if (data.length() > 0)
					lines.add(data);
			br.close();
		} catch (Exception e) {
			logger.info("Error: " + e.toString());
		}
		return lines;
	}
	
	public static List<File> listAllFilesWithFormat(String foldName, String includePostfix) {
		if (foldName == null) {
			logger.error("Loading dict occurs an error.");
			return null;
		}
		Queue<File> filePathQueue = new LinkedList<File>();
		List<File> fileList = new LinkedList<File>();
		filePathQueue.offer(new File(foldName));
		while (!filePathQueue.isEmpty()) {
			File file = filePathQueue.poll();
			if (!file.exists())
				continue;
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				assert fileArray != null;
				for (File subFile : fileArray)
					if (subFile.getName().endsWith(includePostfix))
						filePathQueue.offer(subFile);
			} else
				fileList.add(file);
		}
		return fileList;
	}

	public static List<File> listAllFilesWithFilter(String foldName, String noIncludePostfix) {
		if (foldName == null) {
			logger.error("Loading dict occurs an error.");
			return null;
		}
		Queue<File> filePathQueue = new LinkedList<File>();
		List<File> fileList = new LinkedList<File>();
		filePathQueue.offer(new File(foldName));
		while (!filePathQueue.isEmpty()) {
			File file = filePathQueue.poll();
			if (!file.exists())
				continue;
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				assert fileArray != null;
				for (File subFile : fileArray)
					if (!subFile.getName().endsWith(noIncludePostfix))
						filePathQueue.offer(subFile);
			} else
				fileList.add(file);
		}
		return fileList;
	}
	
}
