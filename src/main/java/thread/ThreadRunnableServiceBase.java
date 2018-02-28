package thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ThreadRunnableServiceBase {
	
	private ExecutorService executor;
	private Integer poolsize;
	
	public ThreadRunnableServiceBase(Integer poolsize){
		this.poolsize = poolsize;
//	    executor = Executors.newSingleThreadExecutor();
	    executor = Executors.newFixedThreadPool(this.poolsize);
//	    executor = Executors.newCachedThreadPool();
//	    executor = Executors.newScheduledThreadPool(this.poolsize);
	}
    
    public void threadSubmit(Runnable thread){
		executor.submit(thread);
    }

    public boolean isTerminated() {
        return executor.isTerminated();
    }

    public boolean isShutdown() {
        return executor.isShutdown();
    }
    
    public void shutdown(){
    	executor.shutdown();
    }
    
    public List<Runnable> shutdownNow(){
    	return executor.shutdownNow();
    }
	
}
