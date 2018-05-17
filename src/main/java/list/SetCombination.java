package list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class SetCombination {
	
	public static List<Map<String, Object>> orderByName(List<Map<String, Object>> list, String name){
//		Collections.sort(list, new MapComparator(name));
		if(list.size()==0){
			return list;
		}
		if(list.get(0).get(name) instanceof String){
			Collections.sort(list, (Map<String, Object> o1, Map<String, Object> o2) -> ((String) o1.get(name)).compareTo((String) o2.get(name)));
		}
		else if(list.get(0).get(name) instanceof Integer){
			Collections.sort(list, (Map<String, Object> o1, Map<String, Object> o2) -> ((String) o1.get(name)).compareTo((String) o2.get(name)));
		}
		return list;
	}
 
	/**
	 * 使用前，需要将list1和list2对name从小到大排序，使用orderByName
	 * @param list1
	 * @param list2
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    public static List<Map<String, Object>> LeftJoin(List<Map<String, Object>> list1, List<Map<String, Object>> list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map<String, Object>> join = new ArrayList<Map<String, Object>>();
        String type = null;
        if(list1.size()>0)
        	type = list1.get(0).get(name).getClass().getSimpleName();
        else if(list2.size()>0)
        	type = list2.get(0).get(name).getClass().getSimpleName();
        if(type==null){
        	return join;
        }
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
        	Object obj1 = list1.get(index1).get(name);
        	Object obj2 = list2.get(index2).get(name);
        	Integer result = null;
            Map<String, Object> entryt = new HashMap<String, Object>();
            if(type.equals("String")){
            	result = ((String) obj1).compareTo((String) obj2);
            } else if(type.equals("Integer")){
            	result = ((Integer) obj1).compareTo((Integer) obj2);
            }
            if(result==null){
            	return join;
            } else if(result>0){
                index2++;
            } else if(result<0){
                entryt.putAll(list1.get(index1));
                index1++;
            } else{
                entryt.putAll(list1.get(index1));
                entryt.putAll(list2.get(index2));
                index1++;
                index2++;
            }
            if(entryt.size()>0)
                join.add(entryt);
        }
        if(index1<list1.size()){
            for(;index1<list1.size();index1++){
                join.add(list1.get(index1));
            }
        }
        return join;
    }

    /**
     * inner join
	 * 使用前，需要将list1和list2对name从小到大排序，使用orderByName
     * @param list1
     * @param list2
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> Intersection(List<Map<String, Object>> list1, List<Map<String, Object>> list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map<String, Object>> join = new ArrayList<Map<String, Object>>();
        String type = null;
        if(list1.size()>0)
        	type = list1.get(0).get(name).getClass().getSimpleName();
        else if(list2.size()>0)
        	type = list2.get(0).get(name).getClass().getSimpleName();
        if(type==null){
        	return join;
        }
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
        	Object obj1 = list1.get(index1).get(name);
        	Object obj2 = list2.get(index2).get(name);
            Integer result = null;
            Map<String, Object> entryt = new HashMap<String, Object>();
            if(type.equals("String")){
            	result = ((String) obj1).compareTo((String) obj2);
            } else if(type.equals("Integer")){
            	result = ((Integer) obj1).compareTo((Integer) obj2);
            }
            if(result==null){
            	return join;
            } else if(result>0){
                index2++;
            }
            else if(result<0){
                index1++;
            }
            else{
                entryt.putAll(list1.get(index1));
                entryt.putAll(list2.get(index2));
                index1++;
                index2++;
            }
            if(entryt.size()>0)
                join.add(entryt);
        }
        return join;
    }

    /**
     * union join
	 * 使用前，需要将list1和list2对name从小到大排序，使用orderByName
     * @param list1
     * @param list2
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> UnionJoin(List<Map<String, Object>> list1, List<Map<String, Object>> list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map<String, Object>> join = new ArrayList<Map<String, Object>>();
        String type = null;
        if(list1.size()>0)
        	type = list1.get(0).get(name).getClass().getSimpleName();
        else if(list2.size()>0)
        	type = list2.get(0).get(name).getClass().getSimpleName();
        if(type==null){
        	return join;
        }
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
        	Object obj1 = list1.get(index1).get(name);
        	Object obj2 = list2.get(index2).get(name);
            Integer result = null;
            Map<String, Object> entryt = new HashMap<String, Object>();
            if(type.equals("String")){
            	result = ((String) obj1).compareTo((String) obj2);
            } else if(type.equals("Integer")){
            	result = ((Integer) obj1).compareTo((Integer) obj2);
            }
            if(result==null){
            	return join;
            } else if(result>0){
                entryt.putAll(list2.get(index2));
                index2++;
            }
            else if(result<0){
                entryt.putAll(list1.get(index1));
                index1++;
            }
            else{
                entryt.putAll(list1.get(index1));
                index1++;
                index2++;
            }
            if(entryt.size()>0)
                join.add(entryt);
        }
        if(index1<list1.size()){
            for(;index1<list1.size();index1++){
                join.add(list1.get(index1));
            }
        }
        if(index2<list2.size()){
            for(;index2<list2.size();index1++){
                join.add(list2.get(index2));
            }
        }
        return join;
    }

    /**
     * complement
	 * 使用前，需要将list1和list2对name从小到大排序，使用orderByName
     * @param list1
     * @param list2
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> Complement(List<Map<String, Object>> list1, List<Map<String, Object>> list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map<String, Object>> join = new ArrayList<Map<String, Object>>();
        String type = null;
        if(list1.size()>0)
        	type = list1.get(0).get(name).getClass().getSimpleName();
        else if(list2.size()>0)
        	type = list2.get(0).get(name).getClass().getSimpleName();
        if(type==null){
        	return join;
        }
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
        	Object obj1 = list1.get(index1).get(name);
        	Object obj2 = list2.get(index2).get(name);
            Integer result = null;
            Map<String, Object> entryt = new HashMap<String, Object>();
            if(type.equals("String")){
            	result = ((String) obj1).compareTo((String) obj2);
            } else if(type.equals("Integer")){
            	result = ((Integer) obj1).compareTo((Integer) obj2);
            }
            if(result==null){
            	return join;
            } else if(result>0){
            	index2++;
            }
            else if(result<0){
                entryt.putAll(list1.get(index1));
                index1++;
            }
            else{
            	index1++;
                index2++;
            }
            if(entryt.size()>0)
                join.add(entryt);
        }
        if(index1<list1.size()){
            for(;index1<list1.size();index1++){
                join.add(list1.get(index1));
            }
        }
        return join;
    }
 
}