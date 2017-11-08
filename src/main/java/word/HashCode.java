package word;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import file.MyFileReader;
import file.MyFileWriter;

public class HashCode {
	
	public static String hashText(String text) {
		//MD5编码
	    byte[] thedigest = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(text.getBytes());
			thedigest = md5.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    return convertToHexString(thedigest);
	}

	/**
	 * 中文hash函数，最少冲突
	 * @param text
	 * @return
	 */
	public static String ChineseHash(String text) {
		Long value = 0L;
		try {
			byte[] test = text.getBytes("GBK");
			for(int i=0;i<test.length;i=i+2){
				byte b1 = test[i];
				if(i+1<test.length){
					byte b2 = test[i+1];
					value = value + (b1+128)*190 +  (b2+128-64) - (b2+128)/128;
				}
				else{
					value = value + (b1+128)*190;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value.toString();
	}

	/**
	 * Unicode的每两个bit相加
	 * @param text
	 * @return
	 */
	public static String UnicodeAdd(String text) {
		Long value = 0L;
		try {
			byte[] test = text.getBytes("Unicode");
			for(int i=2;i<test.length;i=i+2){
				byte b1 = test[i];
				byte b2 = test[i+1];
				value = (value << 1) + (b1 << 8) + b2;
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return value.toString();
	}
	
	public static String Base64(byte[] bytes){
		return new sun.misc.BASE64Encoder().encode(bytes);
	}
	
	public static byte[] Base64Decode(String text){
		try {
			return new sun.misc.BASE64Decoder().decodeBuffer(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String convertToHexString(byte data[]) {
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			strBuffer.append(Integer.toHexString(0xff & data[i]));
		}
		return strBuffer.toString();
	}
	
	public static HashMap<String, String> putHash(String word, HashMap<String, String> map, Long start){
		if(map==null){
			map = new HashMap<String, String>();
		}
		String text = HashCode.hashText(word);
		for(int i=0;map.containsKey(text);i++){
			text = HashCode.hashText(word + i);
		}
//		map.put(String.valueOf(start + Long.valueOf(text)), word);
		map.put(text, word);
		return map;
	}
	
	/**
	 * 混合了加法和一位操作的字符串哈希算法
	 * @param str
	 * @return
	 */
	public static String DJBHash(String str){
		Long hash = 5381L;
		for(int i = 0; i < str.length(); i++)
			hash = ((hash << 5) + hash) + str.charAt(i);
		return hash.toString();
	}
	
//	public static void main(String[] args){
//		try {
//			System.out.println(HashCode.ChineseHash("瑞声科技(02018)：声学业务升级不止，非声学业务不断突破"));
//			System.out.println(HashCode.ChineseHash("图17：公司固体废物处理业务（固废处理+再生资源）营业情"));
//			List<String> entityList = MyFileReader.readListFile("./etc/entityName", "utf-8");
//			HashMap<Long, HashMap<String, String>> map = new HashMap<Long, HashMap<String, String>>();
//			for(String entity : entityList){
//				Long start = Long.valueOf(entity.split("\t")[1])/1000000*1000000;
//				map.put(start, putHash(entity.split("\t")[0], map.get(start), start));
//			}
//			for(Entry<Long, HashMap<String, String>> mapEntry : map.entrySet()){
//				for(Entry<String, String> entry : mapEntry.getValue().entrySet()){
//					MyFileWriter.writeFile(entry.getValue() + "\t" + entry.getKey() + "\r\n", "./etc/entityNameNew");
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
