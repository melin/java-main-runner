/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.melin.runner.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;

/**
 * Create on @2013-11-15 @下午4:01:12 
 * @author bsli@ustcinfo.com
 */
public class ThreadAbortPolicy implements XRejectedExecutionHandler {
	private final LongAdder rejected = new LongAdder();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof AbstractRunnable) {
            if (((AbstractRunnable) r).isForceExecution()) {
                BlockingQueue<Runnable> queue = executor.getQueue();
                if (!(queue instanceof SizeBlockingQueue)) {
                    throw new ThreadPoolRuntimeException("forced execution, but expected a size queue");
                }
                try {
                    ((SizeBlockingQueue) queue).forcePut(r);
                } catch (InterruptedException e) {
                    throw new ThreadPoolRuntimeException(e.getMessage(), e);
                }
                return;
            }
        }
        rejected.increment();
        throw new ThreadPoolRuntimeException("rejected execution of [" + r.getClass().getName() + "]");
    }

    @Override
    public long rejected() {
        return rejected.longValue();
    }
}
