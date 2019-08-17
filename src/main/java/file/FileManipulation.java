package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManipulation {
	
	/**
	 * 删除文件，可以是文件或文件夹
	 * 要利用File类的delete()方法删除目录时，必须保证该目录下没有文件或者子目录，否则删除失败。必须利用递归删除该目录下的所有子目录和文件，然后再删除该目录。  
	 * @param fileName 要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		try{
			if (!file.exists()) {
				throw new IOException(fileName + "is not exist");
			} else {
				if (file.isFile())
					return deleteFile(fileName);
				else if(file.isDirectory())
					return deleteDirectory(fileName);
				else
					return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除单个文件
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		try{
			// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
			if (file.exists() && file.isFile()) {
				return file.delete();
			} else {
				throw new IOException(fileName + "is not exist");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除目录及目录下的文件
	 * @param dir 要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		try{
			// 如果dir对应的文件不存在，或者不是一个目录，则退出
			if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
				throw new IOException(dir + "is not exist");
			}
			// 删除文件夹中的所有文件包括子目录
			File[] files = dirFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 删除子文件
				if (files[i].isFile()) {
					if(!deleteFile(files[i].getAbsolutePath())){
						System.out.println("删除目录" + files[i].getAbsolutePath() + "失败！");
						return false;
					}
				}
				// 删除子目录
				else if (files[i].isDirectory()) {
					if(!deleteDirectory(files[i].getAbsolutePath())){
						System.out.println("删除目录" + files[i].getAbsolutePath() + "失败！");
						return false;
					}
				}
			}
			// 删除当前目录
			return dirFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 文件流的方式复制文件
	 * @param src
	 * @param dest
	 * @return
	 */
	public static boolean copyFile(String src, String dest) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try{
			in = new FileInputStream(src);
			File file = new File(dest);
			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
			return true;
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
		finally{
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件流的方式复制文件夹
	 * @param src
	 * @param dest
	 * @return
	 */
	public static boolean copyDirectory(String src, String dest) {
		File fileSrc = new File(src);
		String[] filePath = fileSrc.list();
		File fileDest = new File(dest);
		if (!fileDest.exists())
			fileDest.mkdirs();
        for (int i = 0; i < filePath.length; i++) {
        	if ((new File(src + File.separator + filePath[i])).isDirectory())
        		copyDirectory(src + File.separator  + filePath[i], dest  + File.separator + filePath[i]);
            if (new File(src  + File.separator + filePath[i]).isFile())
            	copyFile(src + File.separator + filePath[i], dest + File.separator + filePath[i]);
        }
        return true;
	}
	
	/**
	 * 文件重命名
	 * @param path
	 * @param oldname
	 * @param newname
	 * @return
	 */
	public static boolean renameFile(String path, String oldname, String newname) {
		// 新的文件名和以前文件名不同时,才有必要进行重命名
		if (!oldname.equals(newname)) {
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			try{
				// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				if (newfile.exists()){
					throw new IOException(newname + "is already exist");
				}
				else {
					oldfile.renameTo(newfile);
					return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 转移文件目录
	 * 转移文件目录不等同于复制文件，复制文件是复制后两个目录都存在该文件，而转移文件目录则是转移后，只有新目录中存在该文件。
	 * @param filename
	 * @param oldpath
	 * @param newpath
	 * @param cover
	 * @return
	 */
	public static boolean changeDirectory(String filename, String oldpath, String newpath, boolean cover) {
		if (!oldpath.equals(newpath)) {
			File oldfile = new File(oldpath + "/" + filename);
			File newfile = new File(newpath + "/" + filename);
			try{
				// 若在待转移目录下，已经存在待转移文件
				if (newfile.exists()) {
					// 覆盖
					if (cover){
						oldfile.renameTo(newfile);
						return true;
					}
					else{
						throw new IOException(newpath + "/" + filename + "is already exist");
					}
				} else {
					oldfile.renameTo(newfile);
					return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
