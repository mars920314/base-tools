/** 
 * 通联数据机密
 * --------------------------------------------------------------------
 * 通联数据股份公司版权�?�? © 2013-1016
 * 
 * 注意：本文所载所有信息均属于通联数据股份公司资产。本文所包含的知识和�?术概念均属于
 * 通联数据产权，并可能由中国�?�美国和其他国家专利或申请中的专利所覆盖，并受商业秘密或
 * 版权法保护�??
 * 除非事先获得通联数据股份公司书面许可，严禁传播文中信息或复制本材料�??
 * 
 * DataYes CONFIDENTIAL
 * --------------------------------------------------------------------
 * Copyright © 2013-2016 DataYes, All Rights Reserved.
 * 
 * NOTICE: All information contained herein is the property of DataYes 
 * Incorporated. The intellectual and technical concepts contained herein are 
 * proprietary to DataYes Incorporated, and may be covered by China, U.S. and 
 * Other Countries Patents, patents in process, and are protected by trade 
 * secret or copyright law. 
 * Dissemination of this information or reproduction of this material is 
 * strictly forbiddecom.datayes.utilsritten permission is obtained from DataYes.
 */
package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ListUtils {
	/* 没有副作用的列表移除元素代码 */
	public static <T> ArrayList<T> copyAndRemove(List<T> list, int index) {
		ArrayList<T> newList = new ArrayList<T>(list);
		newList.remove(index);
		return newList;
	}

	/* 没有副作用的列表增加元素代码 */
	public static <T> ArrayList<T> copyAndAdd(List<T> list, T ele) {
		ArrayList<T> newList = new ArrayList<T>(list);
		newList.add(ele);
		return newList;
	}

	/* 没有副作用的retain方法 */
	public static <T> ArrayList<T> copyAndRetain(List<T> list1, List<T> list2) {
		if (list1 == null)
			return new ArrayList<T>();
		if (list2 == null)
			return new ArrayList<T>(list1);
		ArrayList<T> newList = new ArrayList<T>(list1);
		newList.retainAll(list2);
		return newList;
	}

	/**
	 * 通用的list查找,从第几个位置起查找某个元素的方法第几次出现的方法
	 * The returned index is the smallest value k for which: 
	 * k >= fromIndex && this.startsWith(str, k)
	 * @param list	查找list
	 * @param predicate	判断条件
	 * @param index	search from index
	 * @param countDefault	search by appear times
	 * @return
	 */
	public static <T> int getElementIndex(List<T> list, Predicate<T> predicate,
			int index, int countDefault) {
		int pos = 0, count = 0;
		for (T element : list)
			if (pos>=index && predicate.test(element) && countDefault == count++)
				return pos;
			else
				pos++;
		return -1;
	}

	/* 通用的list查找，从第几个位置起的某个元素的方法 */
	public static <T> int getElementIndexFromIndex(List<T> list, Predicate<T> predicate,
			int index) {
		return getElementIndex(list, predicate, index, 0);
	}
	
	/* 通用的list查找，从第几个位置起的某个元素的方法 */
	public static <T> T findElementFromIndex(List<T> list, Predicate<T> predicate,
			int countDefault) {
		int pos = getElementIndexFromIndex(list, predicate, countDefault);
		if (pos == -1)
			return null;
		return list.get(pos);
	}
	
	/* 通用的list查找，查找某个元素的方法第几次出现的方法 */
	public static <T> int getElementIndexByCount(List<T> list, Predicate<T> predicate,
			int countDefault) {
		return getElementIndex(list, predicate, 0, countDefault);
	}

	/* 通用的list查找，查找某个元素的方法第几次出现的方法 */
	public static <T> T findElementByCount(List<T> list, Predicate<T> predicate,
			int countDefault) {
		int pos = getElementIndexByCount(list, predicate, countDefault);
		if (pos == -1)
			return null;
		return list.get(pos);
	}
		 

	/* 通用的list统计满足条件的元素个数的方法 */
	public static <T> int countElement(List<T> list, Predicate<T> predicate) {
		int count = 0;
		for (T element : list)
			if (predicate.test(element))
				count++;
		return count;
	}

	/* 通用的list获取有满足条件元素的方法 */
	public static <T> List<T> findAllElement(List<T> list,
			Predicate<T> predicate) {
		List<T> eleList = new LinkedList<T>();
		for (T element : list)
			if (predicate.test(element))
				eleList.add(element);
		return eleList;
	}
}
