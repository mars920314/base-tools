package segment;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

import file.MyFileReader;

public class JiebaSegment extends BaseSegment {
	
	private static JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
	
	private static class JiebaSegmentHolder{
	    private static final JiebaSegment instance = new JiebaSegment();
	}
	  
    public static final JiebaSegment getInstance() {  
    	return JiebaSegmentHolder.instance;  
    }
	
	@Override
	public void addUserLibraryPath(String userLibraryPath) {
		List<File> files = MyFileReader.listAllFilesWithFormat(userLibraryPath, ".dict");
		for(File file : files){
			Path userDict = Paths.get(file.getAbsolutePath());
			WordDictionary.getInstance().loadUserDict(userDict);
		}
	}
	
	@Override
	public void addDynamicWord(String[] words){
		
	}
	
	@Override
	public void deleteDynamicWord(String word) {
		
	}

    /**
     * @param content
     * @param model  choose a parse method 1.normal 3.Index
     * @return
     */
    @Override
    protected List<SegToken> parseSentence(String content, int model) {
		List<SegToken> parsedTokenList = new ArrayList<SegToken>();
        if (model == 1) {
        	parsedTokenList = jiebaSegmenter.process(content, SegMode.SEARCH);
        }
        else if (model == 3) {
        	parsedTokenList = jiebaSegmenter.process(content, SegMode.INDEX);
        }
        return parsedTokenList;
    }
	
	@Override
	/**
	 * 
	 * @param nature is useless here
	 */
	public List<String> segSentence2List(String content, Boolean nature, int option) {
        List<String> tokenList = new ArrayList<String>();
        List<SegToken> segList = parseSentence(content, option);
        try {
            for (SegToken token : segList) {
            	tokenList.add(token.word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenList;
	}

}
