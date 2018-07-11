package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FtpUtil {

	FTPClient ftpClient = new FTPClient();

	class Account {
		public Account(String server, int port, String user, String pwd) {
			super();
			this.server = server;
			this.port = port;
			this.user = user;
			this.pwd = pwd;
		}
		public String server;
		public int port = 21;
		public String user;
		public String pwd;
	}
	
	public Account getAccount(String server, int port, String user, String pwd){
		return new Account(server, port, user, pwd);
	}

	/*
	 * FTP远程命令列表 
	 * USER PORT RETR ALLO DELE SITE XMKD CDUP FEAT PASS PASV STOR
	 * REST CWD STAT RMD XCUP OPTS ACCT TYPE APPE RNFR XCWD HELP XRMD STOU AUTH
	 * REIN STRU SMNT RNTO LIST NOOP PWD SIZE PBSZ QUIT MODE SYST ABOR NLST MKD
	 * XPWD MDTM PROT
	 */

	public boolean connectFTP(Account account) {
		try {
			ftpClient.connect(account.server, account.port);
			ftpClient.login(account.user, account.pwd);
			
			// 开启主动模式或被动模式。http://blog.51cto.com/hover/197729
			// 每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据。
			// 因为ftp server可能每次开启不同的端口来传输数据，但是在linux上，由于安全限制，可能某些端口没有开启，所以就出现阻塞。
			ftpClient.enterLocalPassiveMode();
			// 设置缓冲区大小，未设置则是每比特传输
			ftpClient.setBufferSize(1024);
			// 设置编码类型（gbk）
			ftpClient.setControlEncoding("UTF-8");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean closeFTP() {
		try {
			ftpClient.disconnect();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ftp上传单个文件
	 * 上传的文件名不能包含中文
	 * @param directory 上传至ftp的路径名不包括ftp地址
	 * @param localName 要上传的文件全路径名
	 * @param remoteName 上传至ftp后存储的文件名
	 * @throws IOException
	 */
	public boolean upload(String directory, String localName, String remoteName) {
		FileInputStream fis = null;
		try {
			File srcFile = new File(localName);
			if(!srcFile.exists())
				throw new IOException(localName + "is not exist");
			fis = new FileInputStream(srcFile);
			// 设置上传目录
			ftpClient.changeWorkingDirectory(directory);

			return ftpClient.storeFile(remoteName, fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * FTP单个文件下载
	 * 
	 * @param directory 要下载的文件所在ftp的路径名不包含ftp地址
	 * @param remoteName 要下载的文件名
	 * @param localName 下载后锁存储的文件名全路径
	 */
	public boolean download(String directory, String remoteName, String localName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(localName);
			// 设置上传目录
			ftpClient.changeWorkingDirectory(directory);

			return ftpClient.retrieveFile(remoteName, fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 如果用sendCommand来执行远程命令(不能执行本地FTP命令)的话，所有FTP命令都要加上\r\n
	 * @param command
	 * @return
	 */
	public int command(String command) {
		try {
			return ftpClient.sendCommand(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}