package segment;

import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.TraditionalChineseTokenizer;

public class HanLPSegment extends BaseSegment {

	private static Segment segment = HanLP.newSegment();
	
	private static class HanLPSegmentHolder{
	    private static final HanLPSegment instance = new HanLPSegment();
	}
	  
    public static final HanLPSegment getInstance() {  
    	return HanLPSegmentHolder.instance;  
    }

	/**
	 * http://hanlp.linrunsoft.com/doc/_build/html/ner.html
	 */
    public void init(){
		//有配置的初始化过程
		if(true){//NER
			segment.enableNameRecognize(true);
			segment.enablePlaceRecognize(true);
			segment.enableOrganizationRecognize(true);
//			segment.enableAllNamedEntityRecognize(true);
		}
	}
	
    @Override
	/**
	 * http://hanlp.linrunsoft.com/doc/_build/html/dictionary.html#id10
	 */
	public void addDynamicWord(String[] words){
		if(words.length==1){
			CustomDictionary.add(words[0]);
		}
		else if(words.length==3){
			CustomDictionary.add(words[0], words[1] + " " + words[2]);
		}
	}

    @Override
	public void deleteDynamicWord(String word){
		CustomDictionary.remove(word);
	}
	
    @Override
	public List<String> segSentence2List(String content, Boolean nature, int option) {
		if(nature)
			segment.enablePartOfSpeechTagging(true);
		else
			segment.enablePartOfSpeechTagging(false);
        List<String> termList = new ArrayList<String>();
        List<Term> parsedTermList = parseSentence(content, option);
        try {
            for (Term term : parsedTermList) {
                String word = null;
                if (nature) {
                    word = term.toString();
                } else {
                    word = term.word;
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
     * @param model  choose a parse method 0.Base 1.normal 2.Nlp 3.Index 4.traditionally Chinese 5.fast
     * @return
     */
    @Override
	protected List<Term> parseSentence(String content, int model) {
		List<Term> parsedTermList = new ArrayList<Term>();
        if (model == 1) {
            parsedTermList = HanLP.segment(content);
        }
        else if (model == 2) {
            parsedTermList = NLPTokenizer.segment(content);
        }
        else if (model == 3) {
            parsedTermList = IndexTokenizer.segment(content);
        }//繁体分词
        else if (model == 4) {
            parsedTermList = TraditionalChineseTokenizer.segment(content);
        }//极速词典分词
        else if (model == 5) {
            parsedTermList = SpeedTokenizer.segment(content);
        }//N-最短路径分词
        //CRF分词
        return parsedTermList;
    }

	//http://hanlp.linrunsoft.com/doc/_build/html/extract.html
	public List<String> KeyWordExtract(String title, String content, int num) {
		List<String> keywordList = HanLP.extractKeyword(title + "\r\n" + content, num);
//		List<String> sentenceList = HanLP.extractSummary(content, 3);
//		List<String> phraseList = HanLP.extractPhrase(content, 10);
		return keywordList;
	}

}
