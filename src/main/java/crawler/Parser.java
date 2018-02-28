package crawler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Parser {
    private TagNode tn;
    private Document dom;
    private XPath xPath;
    private HtmlCleaner hc;

    public Parser() {
        hc = new HtmlCleaner();
        xPath = XPathFactory.newInstance().newXPath();
    }

    /**
     * 为每个html构造dom树
     * @param pageHTML, original page
     * @throws ParserConfigurationException
     */
    public  void initialize(String pageHTML) throws ParserConfigurationException {
        tn = hc.clean(pageHTML);
        dom = new DomSerializer(new CleanerProperties()).createDOM(tn);
    }

    /**
     * 解析xml页面，得到符合xpath的内容string
     * @param exp, xpath expression
     * @return processed string
     * @throws XPathExpressionException
     */
    public  String parseXML(String exp) throws XPathExpressionException {
        Object result = xPath.evaluate(exp, dom, XPathConstants.NODESET);
        String content = "";
        if (result instanceof NodeList) {
            NodeList nodeList = (NodeList) result;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                content += node.getNodeValue() == null ? node.getTextContent() : node.getNodeValue();
            }
        }
        //System.out.println("content: " + content);
        return content;
    }

    /**
     * 解析xml页面，得到符合xpath的内容string
     * @param exp, xpath expression
     * @return processed string
     * @throws XPathExpressionException
     */
    public List<String> parseXMLtoList(String exp) throws XPathExpressionException {
        List<String> contentList = new ArrayList<String>();
        Object result = xPath.evaluate(exp, dom, XPathConstants.NODESET);
        String content = "";
        if (result instanceof NodeList) {
            NodeList nodeList = (NodeList) result;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                content = node.getNodeValue() == null ? node.getTextContent() : node.getNodeValue();
                contentList.add(content);
            }
        }
        //System.out.println("content: " + contentList.size() + "---" +  contentList);
        return contentList;
    }

}
