package trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trie树的应用工具方法类，供二次开发添加特有访问Trie的功能
 * 
 * @author lingjun.gao
 *
 */
public class TrieApplication {
	
	public static boolean InsertTrieNode(Trie trie, String name, Object obj){
		return trie.insertTrieNode(name.getBytes(), obj);
	}
	
	public static TrieNode SearchTrieNode(Trie trie, String name){
		return trie.searchLeaf(name.getBytes());
	}
	
	/**
	 * 以NoOverLap的方式在trie中搜索，字符串str中存在哪些子字符串匹配了trie的字典
	 * 
	 * @param trie
	 * @param str
	 * @return 返回所有匹配的子字符串及其下挂Object组成的Map
	 */
	public static Map<String, Object> MatchWithoutOverlap(Trie trie, String str){
		Map<String, Object> matchMap = new HashMap<String, Object>();
		if(str.equals("")){
			return matchMap;
		}
		int begin = 0;
		int end = 1;
		TrieNode nodeMax = null;
		String strMatchMax = "";
		str = str + " ";
		while(begin<str.length()){
    		if(end>str.length()){
    			begin++;
    			end = begin+1;
    			continue;
    		}
    		String strMatch = str.substring(begin, end);;
    		TrieNode node = trie.searchTrieNode(strMatch.getBytes());
    		if(node==null){
				if(nodeMax!=null){
					if(nodeMax.getObject()!=null){
						matchMap.put(strMatchMax, nodeMax.getObject());
					}
					begin = begin + strMatchMax.length();
					end = begin + 1;
					nodeMax = null;
					strMatchMax = "";
				} else {
					begin++;
					end = begin+1;
				}
			} else if(node.getObject()==null){
				end++;
			} else {
				end++;
				nodeMax = node;
				strMatchMax = strMatch;
			}
		}
		return matchMap;
	}
	
	/**
	 * 以OverLap的方式在trie中搜索，字符串str中存在哪些子字符串匹配了trie的字典
	 * 
	 * @param trie
	 * @param str
	 * @return 返回所有匹配的子字符串及其下挂Object组成的Map
	 */
	public static Map<String, Object> MatchWithOverlap(Trie trie, String str){
		Map<String, Object> matchMap = new HashMap<String, Object>();
		if(str.equals("")){
			return matchMap;
		}
		int begin = 0;
		int end = 1;
		TrieNode nodeMax = null;
		String strMatchMax = "";
		str = str + " ";
		while(begin<str.length()){
    		if(end>str.length()){
    			begin++;
    			end = begin+1;
    			continue;
    		}
    		String strMatch = str.substring(begin, end);;
    		TrieNode node = trie.searchTrieNode(strMatch.getBytes());
    		if(node==null){
				if(nodeMax!=null){
					if(nodeMax.getObject()!=null){
						matchMap.put(strMatchMax, nodeMax.getObject());
					}
					//复用matchWithoutOverlap，匹配之后只前进一位
//					begin = begin + strMatchMax.length();
					begin++;
					end = begin + 1;
					nodeMax = null;
					strMatchMax = "";
				} else {
					begin++;
					end = begin+1;
				}
			} else if(node.getObject()==null){
				end++;
			} else {
				end++;
				nodeMax = node;
				strMatchMax = strMatch;
			}
		}
		return matchMap;
	}
	
	/**
	 * 在trie中搜索，字符串str中存在哪些子字符串未匹配任何trie的字典
	 * @param trie
	 * @param str
	 * @return 按顺序返回所有未匹配的子字符串
	 */
	public static List<String> MatchNotExist(Trie trie, String str){
		Map<String, Object> matchMap = new HashMap<String, Object>();
		List<String> strLeftList = new ArrayList<String>();
		if(str.equals("")){
			return strLeftList;
		}
		int begin = 0;
		int end = 1;
		int lastMatch = 0;
		TrieNode nodeMax = null;
		String strMatchMax = "";
		str = str + " ";
		while(begin<str.length()){
    		if(end>str.length()){
    			begin++;
    			end = begin+1;
    			continue;
    		}
    		String strMatch = str.substring(begin, end);;
    		TrieNode node = trie.searchTrieNode(strMatch.getBytes());
    		if(node==null){
				if(nodeMax!=null){
					if(nodeMax.getObject()!=null){
						matchMap.put(strMatchMax, nodeMax.getObject());
					}
					String wordLeft = str.substring(lastMatch, begin).trim();
					if(!wordLeft.equals("")){
						strLeftList.add(wordLeft);
					}
					begin = begin + strMatchMax.length();
					lastMatch = begin + 1;
					end = begin + 1;
					nodeMax = null;
					strMatchMax = "";
				} else {
					begin++;
					end = begin+1;
				}
			} else if(node.getObject()==null){
				end++;
			} else {
				end++;
				nodeMax = node;
				strMatchMax = strMatch;
			}
		}
		//process last sentence
		if(begin!=0 && begin==str.length()){
			String wordLeft = str.substring(lastMatch, begin).trim();
			if(!wordLeft.equals("")){
				strLeftList.add(wordLeft);
			}
		}
		return strLeftList;
	}

}
