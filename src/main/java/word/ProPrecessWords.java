package word;

import java.io.UnsupportedEncodingException;

public class ProPrecessWords {
	
	/**
	 * 文档预处理的标准流程
	 */
    public static String preProcess(String content){
    	if(content==null){
    		return null;
    	}
    	content = toDBC(content);
    	content = content.toLowerCase().trim();
        return content;
    }
    
    /**
     * 全角转半角
     */
    public static String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000')
                c[i] = ' ';
            else if (c[i] > '\uFF00' && c[i] < '\uFF5F')
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    
    /**
     * 半角转全角
     */
    public static String ToSBC(String input) {
    	char c[] = input.toCharArray();
    	for (int i = 0; i < c.length; i++) {
    		if (c[i] == ' ')
    			c[i] = '\u3000';
    		else if (c[i] < '\177')
    			c[i] = (char) (c[i] + 65248);
    	}
        return new String(c);
    }
    
    /**
     * 去掉停用词
     */
    public static String replaceStopword(String name){
    	name = name.replaceAll(" ", "");
    	return name;
    }
    
    /**
     * 去掉所有标点符号
     */
    public static String replacePunctuation(String name){
    	name = name.replaceAll("\\[|\\]|\\(|\\)|\\{|\\}|-|,|\\.|\\?|!|:|;|'|\"|`|>|<|≥|≤|@|#|\\$|%|&|\\*|、|\\+|/|\\\\|【|】|。|‘|“|·|》|《|�??", "");
    	return name;
    }
    /**
     * 去掉所有unicode编码
     * Correct regex is [\\ud800-\\udbff][\\udc00-\\udfff], but java is not support
     */
    public static String replaceUnicodeDouble(String name){
    	name = name.replaceAll("[\uD800\udc00-\uDBFF\udfff]", "");
    	return name;
    }
    /**
     * 去掉汉字的unicode编码
     * https://www.qqxiuzi.cn/zh/hanzi-unicode-bianma.php
     */
    public static String replaceUnicodeChinese(String name){
    	name = name.replaceAll("[\u4E00-\u9FA5]", "");
    	return name;
    }
    
    /**
     * 去掉Emoji表情字符
     * https://www.cnblogs.com/junrong624/p/6073700.html
     */
    public static String replaceEmoji(String name){
    	name = name.chars().filter(c -> isNotEmojiCharacter((char) c)).
    			collect(StringBuilder::new, (sb, i) -> sb.append((char) i), StringBuilder::append).toString();
    	return name;
    }
    
    private static boolean isNotEmojiCharacter(char codePoint){
    	return (codePoint==0x0) || (codePoint==0x9) || (codePoint==0xA) || (codePoint==0xD) || 
    			((codePoint>=0x20) && (codePoint<=0xD7FF)) || 
    			((codePoint>=0xE000) && (codePoint<=0xFFFD)) || 
    			((codePoint>=0x10000) && (codePoint<=0x10FFFF));
    }
    
    /**
     * 统一转编码
     */
    public static String formatCode(String name){
    	try {
			return new String(name.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return name;
    }
    
}
