package list;

import java.util.Comparator;
import java.util.Map;

public class MapComparator implements Comparator<Map<String, Object>>{
	
	String name;
	
	public MapComparator(String name){
		this.name = name;
	}

	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		if(o1.get(name) instanceof String){
			return ((String) o1.get(name)).compareTo((String) o2.get(name));
		}
		else if(o1.get(name) instanceof Integer){
			return ((Integer) o1.get(name)).compareTo((Integer) o2.get(name));
		}
		else
			return 0;
	}
	
}
