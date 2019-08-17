package thread;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MultiTask {

	public void run(String[] args){
		Runnable taskRunnable = new TaskRunnable(args);
		(new Thread(taskRunnable)).start();
		
		Runnable taskThread = new TaskThread(args);
		(new Thread(taskThread)).start();
		
		Callable<Object> taskCallable = new TaskCallable(args);
		FutureTask<Object> task = new FutureTask<Object>(taskCallable);
		(new Thread(task)).start();
		try {
			Object result = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run_demo(String[] args){
		//Runnable
		Runnable taskRunnable = new TaskRunnable(args);
		(new Thread(taskRunnable)).start();
		//Thread
		Runnable taskThread = new TaskThread(args);
		(new Thread(taskThread)).start();
		//Callable
		Callable<Object> taskCallable = new TaskCallable(args);
		FutureTask<Object> task = new FutureTask<Object>(taskCallable);
		(new Thread(task)).start();
		try {
			Object result = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class TaskRunnable implements Runnable {

	String[] args;
	
	public TaskRunnable(String[] args){
		this.args = args;
	}
	
	@Override
	public void run() {
		System.out.print(args);
	}
	
}

/**
 * Thread类也是实现Runnable接口
 * Thread中的run方法其实就是调用的是Runnable接口的run方法。如果一个类继承Thread，则不适合资源共享。但是如果实现了Runable接口的话，则突破了Java中单继承的限制，很容易的实现资源共享。
 */
class TaskThread extends Thread {

	String[] args;
	
	public TaskThread(String[] args){
		this.args = args;
	}
	
	public void run() {
		System.out.print(args);
	}
	
}

/**
 * 实现Callable接口的任务线程能返回执行结果；而实现Runnable接口的任务线程不能返回结果；
 * Callable接口的call()方法允许抛出异常；而Runnable接口的run()方法的异常只能在内部消化，不能继续上抛；
 */
class TaskCallable implements Callable<Object> {

	String[] args;
	
	public TaskCallable(String[] args){
		this.args = args;
	}

	@Override
	public Object call() throws Exception {
		System.out.print(args);
		return args;
	}
	
}

/**
 * parallelStream其实就是一个并行执行的流.它通过默认的ForkJoinPool,可能提高你的多线程任务的速度.
 * 设置线程数量：java -classpath $CLASSPATH -Djava.util.concurrent.ForkJoinPool.common.parallelism=4 $CLASSNAME
 */
class StreamThread {
	
	public void run(List<Object> list){
		list.parallelStream().forEach(a -> System.out.println(a));
	}
}