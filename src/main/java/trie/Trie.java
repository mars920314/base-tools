package trie;

/**
 * Trie���Ļ���
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
	 * ��trie����ڵ㣬��nameCodeλ�ò���value
	 * 
	 * @param nameCode
	 * @param value	����������
	 * @return	����true˵����������������false˵������ʱ���Ѿ�����ֵ��ֵ��Ҫ����ֵ���
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
	 * ����nameCode�Ľڵ㣬������Ƿ�ΪҶ�ڵ�
	 * 
	 * @param nameCode
	 * @return ���ڵ���ڣ������Ƿ�ΪҶ�ڵ㣬�����ش˽ڵ㡣���ڵ㲻���ڣ��򷵻�null
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
	 * ����nameCode�Ľڵ㣬������Ƿ�ΪҶ�ڵ�
	 * 
	 * @param nameCode
	 * @return ���ڵ������ΪҶ�ڵ㣬�򷵻ش�Ҷ�ڵ㡣���򷵻�null
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
	 * ����nameCode�Ľڵ㣬
	 * 
	 * @param nameCode
	 * @return ���ڵ������ΪҶ�ڵ��򷵻ش˽ڵ��Object������ΪҶ�ڵ��򷵻�new Object()�����ڵ㲻�����򷵻�null
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
	 * ɾ���ڵ�
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
	 * ���½ڵ�
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
