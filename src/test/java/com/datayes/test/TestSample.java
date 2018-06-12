package com.datayes.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSample {
	
	private Function func = null;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		func = new Function();
	}

	/**
	 * 参数测试
	 * @param url
	 */
	@Test(groups = "func", dataProvider = "WordProvider")
	public void testFunction(String str){
		String word = func.uppercase(str);
		assert(word.equals(str.toUpperCase()));
	}
	
	/**
	 * 也可以适用于双参数，返回类型为Object[][]
	 * @return
	 */
	@DataProvider(name = "WordProvider")
	public Object[] WordProvider(){
		List<Object> urls = new ArrayList<Object>();
		urls.add("aBc");
		return urls.toArray(new Object[]{});
	}

}

class Function {
	public String uppercase(String str){
		return str.toUpperCase();
	}
}