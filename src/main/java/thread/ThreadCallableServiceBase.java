package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadCallableServiceBase<T> {
	
	private ExecutorService executor;
	//是一个将线程池执行结果放入到一个Blockqueueing的类。多线程的执行时间是未知的，不用通过循环遍历线程池取得结果，直接去queue中取即可。
	private CompletionService<T> completionService;
	private Integer currentQueueLen = 0;
	private Integer poolsize;
	
	public ThreadCallableServiceBase(Integer poolsize){
		this.poolsize = poolsize;
//	    executor = Executors.newSingleThreadExecutor();
	    executor = Executors.newFixedThreadPool(this.poolsize);
//	    executor = Executors.newCachedThreadPool();
//	    executor = Executors.newScheduledThreadPool(this.poolsize);
	    completionService = new ExecutorCompletionService<T>(executor);
	}
    
    public Future<T> threadSubmit(Callable<T> thread){
		currentQueueLen++;
		return completionService.submit(thread);
    }
    
    public T threadResult() throws InterruptedException{
    	try {
        	Future<T> future = completionService.take();
        	if(future!=null)
        		currentQueueLen--;
    		return future.get();
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (ExecutionException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return null;
    }

    public boolean isQueueEmpty() {
        return currentQueueLen==0;
    }

    /**
     * define queue full when queue is currentQueueLen of poolsize
     * @return
     */
    public boolean isQueueFull() {
        return currentQueueLen>poolsize*2;
    }

    public boolean isThreadFull() {
        return currentQueueLen>=poolsize;
    }
    
    public Integer getCurrentQueueLen(){
    	return currentQueueLen;
    }
    
    public void shutdown(){
    	executor.shutdown();
    }
    
    public void shutdownNow(){
    	executor.shutdownNow();
    }
	
}
