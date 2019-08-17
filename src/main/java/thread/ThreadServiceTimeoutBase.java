package thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 多线程任务池，线程任务用Callable实现，线程超时后结束。
 * 任务线程数：poolsize：实际执行并行度：建议等于cpu核数
 * 整个任务队列长度为currentQueue.length()。可以监控队列长度，任务过多则不提交任务；任务执行完成则获取执行结果。
 * 适用场景：任务提交后，每个任务完成后将结果返回，如果超时还未返回结果，则触发该线程的中断，将该线程杀死。
 * 主要有两种原理实现，具体方式有五种：https://blog.csdn.net/wonking666/article/details/64907397
 * 1. 内部中断机制。使用Thread.join，将定时器与任务线程融合，触发中断，并主动退出。
 * 2. 外部监控机制。使用Future.get，主线程监控线程任务在一定时间未完成则强制结束。
 * 主要有两种应用场景：
 * 1. 实时请求任务，从请求发出，到等待时间内未完成，则超时结束。如果任务超时，则认为返回结果错误，不需要再占用池子。实现时，对队列里所有任务分别计时，轮询每个任务是否完成和是否超时（这里只要任务进来了就会开始计时，没有从获得计算资源开始计时）。
 * 2. 批量处理任务，任务池堆满，有任务超时则及时清除，并推入新任务。如果任务超时未结束，则会占用浪费资源，需要让任务池一直满负荷执行。实现时，对队列最前面的任务监控是否超时，完成后对第二个任务重新监控是否超时（这里没有排除第二个任务的已执行时间）。
 * 这里实现的是基于外部监控机制的实现批量处理任务。
 * PackageName: thread
 * Description:
 * author: lingjun.gao
 * email: Lingjun.gao@datayes.com
 * date: 2018年12月18日
 * @param <T>
 */
public class ThreadServiceTimeoutBase<T> {
	
	private ExecutorService executor;

	private Integer poolsize;
	private LinkedList<Future<T>> currentQueue;
	private Long timeout;
	
	/**
	 * 
	 * @param poolsize 线程池大小
	 * @param timeout 超时时间(ms)
	 */
	public ThreadServiceTimeoutBase(Integer poolsize, Long timeout){
		this.poolsize = poolsize;
		this.currentQueue = new LinkedList<Future<T>>();
		this.timeout = timeout;
		
//	    executor = Executors.newSingleThreadExecutor();
	    executor = Executors.newFixedThreadPool(this.poolsize);
//	    executor = Executors.newCachedThreadPool();
//	    executor = Executors.newScheduledThreadPool(this.poolsize);
	}
    
    public Future<T> threadSubmit(Callable<T> thread){
    	Future<T> future = this.executor.submit(thread);
    	this.currentQueue.add(future);
		return future;
    }
    
    /**
     * 获取任务返回结果
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException 
     */
    public T threadResult() throws InterruptedException, ExecutionException, TimeoutException{
    	Future<T> future = this.currentQueue.poll();
		if(future!=null)
	    	return future.get(this.timeout, TimeUnit.MILLISECONDS);
		else
			return null;
    }

    public boolean isTerminated() {
        return this.executor.isTerminated();
    }

    public boolean isShutdown() {
        return this.executor.isShutdown();
    }
    
    public void shutdown(){
    	this.executor.shutdown();
    }
    
    public List<Runnable> shutdownNow(){
    	return this.executor.shutdownNow();
    }

    public boolean isQueueEmpty() {
        return this.currentQueue.isEmpty();
    }

    /**
     * define queue full when queue is currentQueueLen of poolsize
     * @return
     */
    public boolean isQueueFull() {
        return this.currentQueue.size()>poolsize*2;
    }

    public boolean isThreadFull() {
        return this.currentQueue.size()>=poolsize;
    }
    
    public Integer getCurrentQueueLen(){
    	return this.currentQueue.size();
    }

}

class TestThread {
	
	public static void main(String[] args){
		ThreadServiceTimeoutBase<Long> service = new ThreadServiceTimeoutBase<Long>(2, 8000L);
		service.threadSubmit(new TaskCallableTest(10L));
		service.threadSubmit(new TaskCallableTest(9L));
		service.threadSubmit(new TaskCallableTest(8L));
		while(true){
			try {
				Long result = service.threadResult();
				System.out.println(result);
				if(result==null)
					break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("InterruptedException");
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("ExecutionException");
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("TimeoutException");
			}
		}
	}
	
	static class TaskCallableTest implements Callable<Long> {

		Long args;
		
		public TaskCallableTest(Long args){
			this.args = args;
		}

		@Override
		public Long call() throws Exception {
			Thread.sleep(args*1000);
			return args;
		}
		
	}

}