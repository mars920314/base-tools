package xml;

public class test {
	
	public static void main(String[] args){
		XPathParser parser = new XPathParser("/new.xml");
		String classification = parser.evalString("classification");
		String type = parser.evalString("type");
		for(XNode node : parser.evalNode("patterns").getChildren()){
			ReadPattern(node);
		}
	}
	
	public static void ReadPattern(XNode pattern){
		String GID = pattern.evalString("GID");
		for(XNode rule : pattern.evalNode("rules").getChildren()){
			String formular = pattern.evalString("formular");
			
		}
	}

}
