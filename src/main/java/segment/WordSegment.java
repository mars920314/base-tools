package segment;

import java.util.ArrayList;
import java.util.List;

public class WordSegment {
	
	private static List<String> userLibraryPathDefault = new ArrayList<String>();

	private static BaseSegment segment;
	
	public WordSegment(){
		
	}
	
	public static BaseSegment segmentGetter(String method) throws Exception{
		if(method.equalsIgnoreCase("ansj")){
			WordSegment.segment = AnsjSegment.getInstance();
		}
		else if(method.equalsIgnoreCase("jieba")){
			WordSegment.segment = JiebaSegment.getInstance();
		}
		else if(method.equalsIgnoreCase("hanlp")){
			WordSegment.segment = HanLPSegment.getInstance();
		}
		else{
			throw new Exception("Method is illegal. Available value is ansj, jieba, hanlp");
		}
		return segment;
	}

	public static List<String> getUserLibraryPathDefault() {
		return userLibraryPathDefault;
	}

	public static void setUserLibraryPathDefault(List<String> userLibraryPathDefault) {
		WordSegment.userLibraryPathDefault = userLibraryPathDefault;
		if(WordSegment.segment!=null){
			WordSegment.segment.addUserLibraryPath(userLibraryPathDefault);
		}
	}

}
