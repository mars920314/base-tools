package thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程任务池，线程任务用Runnable实现，不带返回结果。
 * 任务线程数：poolsize：实际执行并行度：建议等于cpu核数。
 * 对整个任务队列没有约束，没有监控队列数量，可以提交任意多的任务到队列中，前面的任务获得资源执行，等待全部任务执行完毕。
 * 适用场景：一次性提交所有任务，然后等待全部完成，不需要有返回结果。
 * PackageName: thread
 * Description:
 * author: lingjun.gao
 * email: Lingjun.gao@datayes.com
 * date: 2018年12月18日
 */
public class ThreadServiceRunnableBase {
	
	private ExecutorService executor;
	
	private Integer poolsize;
	
	public ThreadServiceRunnableBase(Integer poolsize){
		this.poolsize = poolsize;
		
//	    this.executor = Executors.newSingleThreadExecutor();
		this.executor = Executors.newFixedThreadPool(this.poolsize);
//	    this.executor = Executors.newCachedThreadPool();
//	    this.executor = Executors.newScheduledThreadPool(this.poolsize);
	}
    
    public void threadSubmit(Runnable thread){
		this.executor.submit(thread);
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
	
}
