package trie;

/**
 * Trie树的基类
 * 
 * @author lingjun.gao
 *
 */
public class Trie {

	private TrieNode root;
	
	public Trie(){
		this.root = new TrieNode((byte) 0);
	}
	
	/**
	 * 给trie插入节点，在nameCode位置插入value
	 * 
	 * @param nameCode
	 * @param value	接受任意类
	 * @return	返回true说明插入正常，返回false说明插入时，已经存在值且值与要插入值相等
	 */
	public boolean insertTrieNode(byte[] nameCode, Object value){
		TrieNode dummpy = root;
		for(int i=0; i<nameCode.length*2; i++){
			byte val;
			if(i%2 == 0)
				val = (byte) (nameCode[i/2] & 0x0F);
			else
				val = (byte) ((nameCode[i/2] >>4) & 0x0F);
			if(dummpy.childrenArray[val]!=null){
				dummpy = (TrieNode) dummpy.childrenArray[val];
			}
			else{
				TrieNode nextnode = new TrieNode(val);
				dummpy.childrenArray[val] = nextnode;
				dummpy = nextnode;
			}
		}
		if(dummpy.getObject()!=null && dummpy.getObject().equals(value)){
			return false;
		} else {
			dummpy.setObject(value);
			return true;
		}
	}
	
	/**
	 * 查找nameCode的节点，不检查是否为叶节点
	 * 
	 * @param nameCode
	 * @return 若节点存在，无论是否为叶节点，都返回此节点。若节点不存在，则返回null
	 */
	public TrieNode searchTrieNode(byte[] nameCode){
		TrieNode dummpy = root;
		int i = 0;
		for(; i<nameCode.length*2; i++){
			byte val;
			if(i%2 == 0)
				val = (byte) (nameCode[i/2] & 0x0F);
			else
				val = (byte) ((nameCode[i/2] >>4) & 0x0F);
			if(dummpy.childrenArray[val]==null)
				break;
			dummpy = (TrieNode) dummpy.childrenArray[val];
		}
		if(i==nameCode.length*2)
			return dummpy;
		else
			return null;
	}
	
	/**
	 * 查找nameCode的节点，并检查是否为叶节点
	 * 
	 * @param nameCode
	 * @return 若节点存在且为叶节点，则返回此叶节点。否则返回null
	 */
	public TrieNode searchLeaf(byte[] nameCode){
		TrieNode node = searchTrieNode(nameCode);
		if(node!=null && node.isLeaf()){
			return node;
		}
		else{
			return null;
		}
	}
	
	/**
	 * 查找nameCode的节点，
	 * 
	 * @param nameCode
	 * @return 若节点存在且为叶节点则返回此节点的Object，若不为叶节点则返回new Object()，若节点不存在则返回null
	 */
	public Object searchValue(byte[] nameCode){
		TrieNode node = searchTrieNode(nameCode);
		if(node==null){
			return null;
		} else if(!node.isLeaf()){
			return new Object();
		} else {
			return node.getObject();
		}
	}
	
	/**
	 * 删除节点
	 * 
	 * @param nameCode
	 * @return -1, if there is no node found or result node is not leaf. 0, if delete successfully
	 */
	public int delete(byte[] nameCode){
		TrieNode node = searchLeaf(nameCode);
		if(node==null)
			return -1;
		else{
			node.removeValue();
			return 0;
		}
	}
	
	/**
	 * 更新节点
	 * 
	 * @param nameCode
	 * @param obj
	 * @return -1, if there is no node found or result node is not leaf. 0, if update successfully
	 */
	public int update(byte[] nameCode, Object obj){
		TrieNode node = searchLeaf(nameCode);
		if(node==null)
			return -1;
		else{
			node.setObject(obj);
			return 0;
		}
	}
	
}
