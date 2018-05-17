package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.FluentIterable;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

public class MyFileReader {
	static Logger logger = LoggerFactory.getLogger(MyFileReader.class);
	
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
			FileInputStream fi = new FileInputStream(propertyFile);
			InputStreamReader isr = new InputStreamReader(fi, "UTF-8");
			dbProperty.load(isr);
			propertyVal = dbProperty.getProperty(propertyName);
			isr.close();
			fi.close();
		} catch (Exception e) {
			logger.info("Error: " + e.toString());
		}
		return propertyVal;
	}
	
	public static List<String> readFileList(String fileName, String encodings){
		List<String> names = new LinkedList<String>();
		String data = "";
		try {
			FileInputStream fi = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fi, encodings);
			BufferedReader br = new BufferedReader(isr);
			while((data = br.readLine())!=null)
				if (data.length()>0)
					names.add(data);
			br.close();
			isr.close();
			fi.close();
		} catch (Exception e) {
			logger.info("Error: " + e.toString());
		}
		return names;
	}

	public static List<String> loadFileList(String fileName, String encodings) {
		List<String> lines = new LinkedList<String>();
		String data = "";
		try {
			InputStreamReader isr = new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(fileName), encodings);
			BufferedReader br = new BufferedReader(isr);
			while ((data = br.readLine()) != null)
				if (data.length() > 0)
					lines.add(data);
			br.close();
			isr.close();
		} catch (Exception e) {
			logger.info("Error: " + e.toString());
		}
		return lines;
	}
	/**
	 * 读超大文件，只返回行的指针，没有全部放入内存。有三种方法：
	 * BufferedReader 基于Buffered缓冲类
	 * Scanner 基于Scanner类文件流
	 * LineIterator 基于Apache Commons IO流
	 * @param fileName
	 * @param encodings
	 * @return
	 */
	public static void readFileScanner(String fileName, String encodings) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
		    inputStream = new FileInputStream(fileName);
		    sc = new Scanner(inputStream, "UTF-8");
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		    }
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    if (sc != null)
		        sc.close();
		}
	}
	public static void readFileIterator(String fileName, String encodings) {
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(new File(fileName), encodings);
		    while (it.hasNext()) {
		        String line = it.nextLine();
		        // do something with line
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(it!=null)
				LineIterator.closeQuietly(it);
		}
	}
	/**
	 * 读文件夹下有超多子文件的情况，只返回文件队列的迭代器
	 * @param foldName
	 * @return
	 */
	public static FluentIterable<File> iterateAllFilesWithFormat(String foldName){
		File file = new File(foldName);
		FluentIterable<File> ite = com.google.common.io.Files.fileTreeTraverser().breadthFirstTraversal(file);
		return ite;
	}
	
	/**
	 * 读文件夹下有超多子文件的情况，只返回文件队列的路径流
	 * @param foldName
	 * @return
	 */
	public static DirectoryStream<Path> iterateAllPathWithFormat(String foldName){
		Path path = Paths.get(foldName);
		try {
			DirectoryStream<Path> paths = java.nio.file.Files.newDirectoryStream(path);
			return paths;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
