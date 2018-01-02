package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class ThreadServiceBase<T> {
	
	private ExecutorService executor;
	private CompletionService<T> completionService;
	private Integer currentQueueLen = 0;
	private Integer poolsize;
	
	public ThreadServiceBase(Integer poolsize){
		this.poolsize = poolsize;
	    executor = Executors.newFixedThreadPool(this.poolsize);
	    completionService = new ExecutorCompletionService<T>(executor);
	}
    
    public Future<T> threadSubmit(Callable<T> thread){
		currentQueueLen++;
		return completionService.submit(thread);
    }
    
    public Future<T> threadResult() throws InterruptedException{
    	Future<T> future = completionService.take();
    	if(future!=null)
    		currentQueueLen--;
		return future;
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

	public abstract T getThreadResult();

}
