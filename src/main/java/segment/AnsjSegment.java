package segment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;

public class AnsjSegment extends BaseSegment {
	
	private static class AnsjSegmentHolder{
	    private static final AnsjSegment instance = new AnsjSegment();
	}
	  
    public static final AnsjSegment getInstance() {  
    	return AnsjSegmentHolder.instance;  
    }
	
	public void init(){
		//有配置的初始化过程
		if(true){//NER
			MyStaticValue.isNameRecognition=true;
			MyStaticValue.isNumRecognition=true;
			MyStaticValue.isQuantifierRecognition=true;
		}
	}

	/**
	 * 分词默认把大写都转换为小写了.所以添加新词的时候要求必须是小写
	 * 字典文件格式为词语，词性，词频
	 */
	@Override
	public void addDynamicWord(String[] words){
		if(words.length==1){
			UserDefineLibrary.insertWord(words[0].toLowerCase(), "userDefine", 1000);
		}
		else if(words.length==2 && words[1].matches("\\d+")){
			UserDefineLibrary.insertWord(words[0].toLowerCase(), "userDefine", Integer.valueOf(words[1]));
		}
		else if(words.length==2 && !words[1].matches("\\d+")){
			UserDefineLibrary.insertWord(words[0].toLowerCase(), words[1], 1000);
		}
		else if(words.length==3){
            UserDefineLibrary.insertWord(words[0].toLowerCase(), words[1], Integer.valueOf(words[2]));
		}
	}
	
	@Override
	public void deleteDynamicWord(String word){
		UserDefineLibrary.removeWord(word);
	}

	@Override
    public List<String> segSentence2List(String content, Boolean nature, int option) {
        List<String> termList = new ArrayList<String>();
        List<Term> parsedTermList = parseSentence(content, option);
        try {
            for (Term term : parsedTermList) {
                String word = null;
                if (nature) {
                    word = term.toString();
                } else {
                    word = term.getName();
                }
                termList.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return termList;
    }

    /**
     * @param content
     * @param model  choose a parse method 0.Base 1.normal 2.Nlp 3.Index
     * @return
     */
    @Override
	protected List<Term> parseSentence(String content, int model) {
		Result result = null;
        if (model == 0) {
        	result = BaseAnalysis.parse(content);
        }
        else if (model == 1) {
        	result = ToAnalysis.parse(content);
        }//NLP分词，会执行全部命名实体识别和词性标注
        else if (model == 2) {
        	result = NlpAnalysis.parse(content);
        }
        else if (model == 3) {
        	result = IndexAnalysis.parse(content);
        }
		List<Term> parsedTermList = StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());
        return parsedTermList;
    }
    
    //https://github.com/NLPchina/ansj_seg/wiki/%E8%AF%8D%E6%80%A7%E6%A0%87%E6%B3%A8%E5%B7%A5%E5%85%B7%E7%B1%BB
    @Deprecated
    public List<String> segSentence2RecognitionListByAnsj(String content, Boolean nature, int option) {
        List<String> termList = new ArrayList<String>();
        List<Term> parsedTermList = parseSentence(content, option);
        try {
            for (Term term : parsedTermList) {
                String word = null;
                if (nature) {
                    word = term.toString();
                } else {
                    word = term.getName();
                }
                termList.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return termList;
    }
	
	public List<String> KeyWordExtract(String title, String content, int num) {
		List<String> keywordList = new ArrayList<String>();
		KeyWordComputer kwc = new KeyWordComputer(num);
		List<Keyword> tfidfList = kwc.computeArticleTfidf(title, content);
		for(Keyword tfidf : tfidfList){
			keywordList.add(tfidf.getName());
		}
		return keywordList;
	}

}
