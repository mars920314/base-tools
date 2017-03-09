package segment;

import java.util.List;

import utils.FileReader;

public abstract class BaseSegment {
	
	public static final int optionDefault = 1;

	public void addUserLibraryPath(List<String> userLibraryPath) {
		for(String libraryPath : userLibraryPath){
			List<String> lines = FileReader.readListFile(libraryPath, "UTF-8");
			for(String line : lines){
				String[] words = line.trim().split("\\s+");
				addDynamicWord(words);
			}
		}
	}
	
	public abstract void addDynamicWord(String[] words);
	
	public abstract void deleteDynamicWord(String word);
	
	protected abstract List<?> parseSentence(String content, int model);

    /**
     * @param text
     * @param nature whether the word nature is needed
     * @param model  choose a parse method 0.Base 1.normal 2.Nlp 3.Index 4.traditionally Chinese 5.fast
     * @return
     */
	public abstract List<String> segSentence2List(String content, Boolean nature, int option);
	
    public List<String> segSentence2List(String content, int option) {
    	return segSentence2List(content, false, option);
    }
	
	public List<String> segSentence2List(String text) {
		return segSentence2List(text, optionDefault);
	}
	
	public String segSentence2String(String content, Boolean nature, int option){
		return List2String(segSentence2List(content, nature, option));
	}
	
	public String segSentence2String(String content, int option){
		return segSentence2String(content, false, option);
	}
	
	public String segment2String(String text) throws Exception{
		return segSentence2String(text, optionDefault);
	}

	public static String List2String(List<String> termList){
		StringBuffer contentBuffer = new StringBuffer();
		for(String term : termList){
            if (term.length() > 0) {
                contentBuffer.append(term + " ");
            }
		}
		return contentBuffer.toString().trim();
	}

}
