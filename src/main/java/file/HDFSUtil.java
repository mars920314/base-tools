package file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSUtil {

	public static InputStream readHDFS(String path) throws IOException {
		Configuration configuration = new Configuration();
		configuration.set("fs.defaultFS", "hdfs://sh-puma-storm01.wmcloud-qa.com:54310");
		FileSystem fileSystem = FileSystem.get(URI.create(path), configuration);
		FSDataInputStream in = fileSystem.open(new Path(path));
		return in;
	}
	
	public static byte[] readHDFS(String path, int initialSize) throws IOException {
		Configuration configuration = new Configuration();
		configuration.set("fs.defaultFS", "hdfs://sh-puma-storm01.wmcloud-qa.com:54310");
		FileSystem fileSystem = FileSystem.get(URI.create(path), configuration);
		FSDataInputStream in = fileSystem.open(new Path(path));
	    // 预估消息大小
		if(initialSize<0)
			initialSize = in.available();
		byte[] bytes = MyFileReader.readFileByte(in, initialSize);
		in.close();
		return bytes;
	}
	
	public static String readHDFS(String path, String encodings) throws IOException {
		byte[] bytes = readHDFS(path, -1);
		return new String(bytes, encodings);
	}

}
