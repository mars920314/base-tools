package list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetOperation {
	
	/**
	 * return set1 or set2
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static <T> Set<T> Union(Set<T> set1, Set<T> set2){
		Set<T> setUnion = new HashSet<T>();
		setUnion.addAll(set1);
		setUnion.addAll(set2);
		return setUnion;
	}
	
	/**
	 * return set1 and set2
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static <T> Set<T> Intersection(Set<T> set1, Set<T> set2){
		Set<T> setIntersection = new HashSet<T>();
		for(T t : Union(set1, set2)){
			if(set1.contains(t) && set2.contains(t))
				setIntersection.add(t);
		}
		return setIntersection;
	}
	
	/**
	 * return complement of set2 which is in set1 but not in set2
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static <T> Set<T> Complement(Set<T> set1, Set<T> set2){
		Set<T> set2Complement = new HashSet<T>();
		for(T t : set1)
			if(!set2.contains(t))
				set2Complement.add(t);
		return set2Complement;
	}
	
	public static <T extends Comparable<? super T>> boolean isEqual(Set<T> set1, Set<T> set2){
		return isEqual(new ArrayList<T>(set1), new ArrayList<T>(set2));
	}
	
	public static <T extends Comparable<? super T>> boolean isEqual(List<T> list1, List<T> list2){
		if(list1.size()!=list2.size())
			return false;
		Collections.sort(list1);
		Collections.sort(list2);
		for(int i=0; i<list1.size() && i<list2.size(); i++)
			if(list1.get(i)==null && list2.get(i)==null)
				continue;
			else if(list1.get(i)==null && list2.get(i)!=null)
				return false;
			else if(list1.get(i)!=null && list2.get(i)==null)
				return false;
			else if(!list1.get(i).equals(list2.get(i)))
				return false;
			else
				continue;
		return true;
	}

}
