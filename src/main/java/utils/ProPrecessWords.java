package utils;

public class ProPrecessWords {

    public static String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);
        return returnString;
    }

    public static String preProcess(String content){
    	if(content==null){
    		return null;
    	}
        String processedContent = toDBC(content);
//        processedContent = processedContent.replaceAll(" ","");
        processedContent = processedContent.toLowerCase().trim();
        return processedContent;
    }
    
    public static String replacePunctuation(String name){
    	name = name.replaceAll("\\[|\\]|\\(|\\)|\\{|\\}|-|,|\\.|\\?|!|:|;|'|\"|`|>|<|≥|≤|@|#|\\$|%|&|\\*|、|\\+|/|\\\\|【|】|。|‘|“|·|》|《|�??", " ");
    	return name;
    }
}
