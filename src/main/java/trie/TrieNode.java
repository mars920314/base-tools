package trie;

/**
 * Trie树的节点类
 * 
 * @author lingjun.gao
 *
 */
public class TrieNode {

	private byte value = 0;
    private boolean isLeaf = false;
    private Object object;
    TrieNode[] childrenArray;
    
    public TrieNode(byte value){
    	this.value = value;
    	childrenArray = new TrieNode[16];
    }

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		setLeaf(true);
		this.object = object;
	}
	
	public void removeValue(){
		setLeaf(false);
		this.object = null;
	}
}
