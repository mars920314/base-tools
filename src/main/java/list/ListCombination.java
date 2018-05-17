package list;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListCombination {
	
	/**
	 * left join
	 * list是根据name，实现了Comparable，并由小到大排序的。
	 * @param list1
	 * @param list2
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    public static List<Map> LeftJoin(List list1, List list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map> join = new ArrayList<Map>();
        Field field = null;
        String type = null;
        if(list1.size()>0)
    		field = list1.get(0).getClass().getDeclaredField(name);
        else if(list2.size()>0)
    		field = list2.get(0).getClass().getDeclaredField(name);
        if(field==null){
        	type = null;
        	return join;
        } else if(field.getType().getSimpleName().equals("String")){
        	type = "String";
        } else if(field.getType().getSimpleName().equals("Integer")){
        	type = "Integer";
        }
        Collections.sort(list1);
        Collections.sort(list2);
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
            Object obj1 = field.get(list1.get(index1));
            Object obj2 = field.get(list2.get(index2));
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
                entryt.put(name, obj1);
                index1++;
            } else{
                entryt.put(name, obj1);
                index1++;
                index2++;
            }
            if(entryt.size()>0)
                join.add(entryt);
        }
        if(index1<list1.size()){
            for(;index1<list1.size();index1++){
            	Object obj1 = field.get(list1.get(index1));
                Map<String, Object> entryt = new HashMap<String, Object>();
                entryt.put(name, obj1);
                join.add(entryt);
            }
        }
        return join;
    }

    /**
     * inner join
	 * list是根据name，实现了Comparable，并由小到大排序的。
     * @param list1
     * @param list2
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Map> Intersection(List list1, List list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map> join = new ArrayList<Map>();
        Field field = null;
        String type = null;
        if(list1.size()>0)
    		field = list1.get(0).getClass().getDeclaredField(name);
        else if(list2.size()>0)
    		field = list2.get(0).getClass().getDeclaredField(name);
        if(field==null){
        	type = null;
        	return join;
        } else if(field.getType().getSimpleName().equals("String")){
        	type = "String";
        } else if(field.getType().getSimpleName().equals("Integer")){
        	type = "Integer";
        }
        Collections.sort(list1);
        Collections.sort(list2);
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
            Object obj1 = field.get(list1.get(index1));
            Object obj2 = field.get(list2.get(index2));
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
                entryt.put(name, obj1);
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
	 * list是根据name，实现了Comparable，并由小到大排序的。
     * @param list1
     * @param list2
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Map> UnionJoin(List list1, List list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map> join = new ArrayList<Map>();
        Field field = null;
        String type = null;
        if(list1.size()>0)
    		field = list1.get(0).getClass().getDeclaredField(name);
        else if(list2.size()>0)
    		field = list2.get(0).getClass().getDeclaredField(name);
        if(field==null){
        	type = null;
        	return join;
        } else if(field.getType().getSimpleName().equals("String")){
        	type = "String";
        } else if(field.getType().getSimpleName().equals("Integer")){
        	type = "Integer";
        }
        Collections.sort(list1);
        Collections.sort(list2);
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
            Object obj1 = field.get(list1.get(index1));
            Object obj2 = field.get(list2.get(index2));
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
                entryt.put(name, obj2);
                index2++;
            }
            else if(result<0){
                entryt.put(name, obj1);
                index1++;
            }
            else{
                entryt.put(name, obj1);
                index1++;
                index2++;
            }
            if(entryt.size()>0)
                join.add(entryt);
        }
        if(index1<list1.size()){
            for(;index1<list1.size();index1++){
            	Object obj1 = field.get(list1.get(index1));
                Map<String, Object> entryt = new HashMap<String, Object>();
                entryt.put(name, obj1);
                join.add(entryt);
            }
        }
        if(index2<list2.size()){
            for(;index2<list2.size();index1++){
            	Object obj2 = field.get(list2.get(index2));
                Map<String, Object> entryt = new HashMap<String, Object>();
                entryt.put(name, obj2);
                join.add(entryt);
            }
        }
        return join;
    }

    /**
     * complement
	 * list是根据name，实现了Comparable，并由小到大排序的。
     * @param list1
     * @param list2
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<Map> Complement(List list1, List list2, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Map> join = new ArrayList<Map>();
        Field field = null;
        String type = null;
        if(list1.size()>0)
    		field = list1.get(0).getClass().getDeclaredField(name);
        else if(list2.size()>0)
    		field = list2.get(0).getClass().getDeclaredField(name);
        if(field==null){
        	type = null;
        	return join;
        } else if(field.getType().getSimpleName().equals("String")){
        	type = "String";
        } else if(field.getType().getSimpleName().equals("Integer")){
        	type = "Integer";
        }
        Collections.sort(list1);
        Collections.sort(list2);
        int index1 = 0;
        int index2 = 0;
        while(index1<list1.size() && index2<list2.size()){
            Object obj1 = field.get(list1.get(index1));
            Object obj2 = field.get(list2.get(index2));
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
                entryt.put(name, obj1);
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
            	Object obj1 = field.get(list1.get(index1));
                Map<String, Object> entryt = new HashMap<String, Object>();
                entryt.put(name, obj1);
                join.add(entryt);
            }
        }
        return join;
    }

}
