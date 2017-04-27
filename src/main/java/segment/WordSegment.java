package segment;

public class WordSegment {
	//fold is accepted, but only file end with .dict is accepted
	private static String userLibraryPathDefault;

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

	public static String getUserLibraryPathDefault() {
		return userLibraryPathDefault;
	}

	public static void setUserLibraryPathDefault(String userLibraryPathDefault) {
		WordSegment.userLibraryPathDefault = userLibraryPathDefault;
		if(WordSegment.segment!=null){
			WordSegment.segment.addUserLibraryPath(userLibraryPathDefault);
		}
	}

}
