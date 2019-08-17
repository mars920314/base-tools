package thread;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 多线程任务池，线程任务用Callable实现，带返回结果，返回结果用Future包装。
 * 任务线程数：poolsize：实际执行并行度：建议等于cpu核数
 * 整个任务队列长度为currentQueueLen。可以监控队列长度，任务过多则不提交任务；任务执行完成则获取执行结果。
 * 适用场景：实时提交任务，并监控队列长度，每个任务完成后将结果返回。
 * 如果需要实时提交，但没有返回结果：则将Callable替换为Runnable，或者将Callable返回值为空null或Boolean。
 * 如果需要一次性提交，但需要返回结果，则将currentQueueLen不设置限制，队列可以接收无限多任务。
 * 获取返回结果有两种方式：
 * 1. 异步阻塞方式：对提交的任务队列Callable，查询是否执行完成。（也可以依次轮询任务队列，优化为非阻塞方式）
 * 2. 异步非阻塞方式：利用CompletionService实现，只要有任务完成则将结果推导结果队列。
 * PackageName: thread
 * Description:
 * author: lingjun.gao
 * email: Lingjun.gao@datayes.com
 * date: 2018年12月18日
 * @param <T>
 */
public class ThreadServiceCallableBase<T> {
	
	private ExecutorService executor;
	//是一个将线程池执行结果放入到一个Blockqueueing的类。多线程的执行时间是未知的，不用通过循环遍历线程池取得结果，直接去queue中取即可。
	private CompletionService<T> completionService;

	private Integer poolsize;
	private Integer currentQueueLen = 0;
	
	public ThreadServiceCallableBase(Integer poolsize){
		this.poolsize = poolsize;
		
//	    this.executor = Executors.newSingleThreadExecutor();
		this.executor = Executors.newFixedThreadPool(this.poolsize);
//	    this.executor = Executors.newCachedThreadPool();
//	    this.executor = Executors.newScheduledThreadPool(this.poolsize);
		this.completionService = new ExecutorCompletionService<T>(executor);
	}
    
    public Future<T> threadSubmit(Callable<T> thread){
    	this.currentQueueLen++;
		return this.completionService.submit(thread);
    }
    
    /**
     * 获取任务返回结果
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public T threadResult() throws InterruptedException, ExecutionException{
		this.currentQueueLen--;
		Future<T> future = this.completionService.take();
		if(future!=null)
			return future.get();
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
        return this.currentQueueLen==0;
    }

    /**
     * define queue full when queue is currentQueueLen of poolsize
     * @return
     */
    public boolean isQueueFull() {
        return this.currentQueueLen>poolsize*2;
    }

    public boolean isThreadFull() {
        return this.currentQueueLen>=poolsize;
    }
    
    public Integer getCurrentQueueLen(){
    	return this.currentQueueLen;
    }
	
}
