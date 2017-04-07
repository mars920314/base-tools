package crawler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import file.FileReader;
import file.MyFileWriter;
import http.HttpGetter;

public class Crawler {
	
	public static List<String> get(String url, List<String> parserXpathList){
		List<String> contents = new ArrayList<String>();
    	String toParseHtml = null;
		try {
			toParseHtml = HttpGetter.getInstance().crawl(url, "gbk");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
		    Parser thisParser = new Parser();
			thisParser.initialize(toParseHtml);
	    	for (String thisXpath : parserXpathList) {
	    	    String parseContent = thisParser.parseXML(thisXpath);
	    	    contents.add(parseContent);
	    	}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return contents;
	}

    public static void main(String[] args){
    	List<String> tickers = FileReader.readListFile("./etc/tickers", "gbk");
    	List<String> parserXpathList = new ArrayList<String>();
    	parserXpathList.add(".//*[@class=\"main_intro_list\"]");
    	for(String ticker : tickers){
        	String url = "http://basic.10jqka.com.cn/" + ticker + "/operate.html";
        	List<String> contents = Crawler.get(url, parserXpathList);
        	if(contents.size()==0)
        		continue;
        	String[] segments = contents.get(0).replaceAll(" |\r|\n|", "").trim().split("产品类型：|产品名称：|经营范围：");
        	if(segments.length!=4)
        		continue;
        	String type = segments[segments.length-3].trim().replaceAll("、", ",");
        	String name = segments[segments.length-2].trim().replaceAll("、", ",");
        	String range = segments[segments.length-1];
        	MyFileWriter.writeFile(ticker + "\t" + type + "\t" + name + "\t" + range + "\r\n", "./etc/operate");
        	try {
				Thread.sleep(400L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
