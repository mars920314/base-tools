package list;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 从小到大排序输出。依次比较references的每个字段的值。若不存在这个字段，则排在后面
 * @author lingjun.gao
 *
 */
public class ListComparator implements Comparator<Map<String, Object>>{
	
	public List<String> references;
	
	public ListComparator(List<String> references){
		this.references = references;
	}

	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		if(references==null)
			return 0;
		int index = 0;
		while(index<references.size()){
			String reference = references.get(index);
			Object obj1 = o1.get(reference);
			Object obj2 = o2.get(reference);
			int result = compareReference(obj1, obj2);
			if(result==0)
				index++;
			else
				return result;
		}
		return 0;
	}
	
	public int compareReference(Object obj1, Object obj2){
		if(obj1!=null && obj2==null)
			return 1;
		else if(obj2!=null && obj1==null)
			return -1;
		else if(obj1==null && obj2==null)
			return 0;
		else{
			if(obj1.equals(obj2))
				return 0;
			else if(obj1 instanceof Double && obj2 instanceof Double)
				return ((Double) obj1).compareTo((Double) obj2);
			else if(obj1 instanceof Integer && obj2 instanceof Integer)
				return ((Integer) obj1).compareTo((Integer) obj2);
			else if(obj1 instanceof BigDecimal && obj2 instanceof BigDecimal)
				return ((BigDecimal) obj1).compareTo((BigDecimal) obj2);
			else if(obj1 instanceof String && obj2 instanceof String)
				return ((String) obj1).compareTo((String) obj2);
		}
		return 0;
	}

}
