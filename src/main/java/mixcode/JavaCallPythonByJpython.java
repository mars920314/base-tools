package mixcode;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * Jpython的这四个方法虽然都可以调用python程序，但是使用Jpython调用的python库不是很多，如果你用以上两个方法调用，
 * 而python的程序中使用到第三方库， 这时就会报错java ImportError: No module named
 * xxx。遇到这种情况推荐使用其他方法解决该问题。
 */
public class JavaCallPythonByJpython {

	public static void main(String[] args) {
		execClass();
	}

	public static void execCommand() {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.exec("a=[5,2,3,9,4,0]; ");
		interpreter.exec("print(sorted(a));"); // 此处python语句是3.x版本的语法
		interpreter.exec("print sorted(a);"); // 此处是python语句是2.x版本的语法

		interpreter.close();
	}

	public static void execScript() {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.execfile("etc/ScriptMethod.py");

		interpreter.close();
	}

	public static void execMethod() {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.execfile("etc/ScriptMethod.py");
		// 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
		PyFunction pyFunction_add = interpreter.get("add", PyFunction.class);
		int a = 5;
		int b = 10;
		// 调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
		PyObject pyobj = pyFunction_add.__call__(new PyInteger(a), new PyInteger(b));
		System.out.println("the anwser is: " + pyobj);

		interpreter.close();
	}

	public static void execClass() {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.execfile("etc/ScriptClass.py");
		// 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
		PyObject pyobj = interpreter.get("script");
		int a = 5;
		// 调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
		pyobj.invoke("add", new PyInteger(a));
		PyObject result = pyobj.invoke("get");
		System.out.println("the anwser is: " + result);

		interpreter.close();
	}

}
