/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.thread;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.runner.common.unit.TimeValue;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Create on @2013-11-15 @下午1:13:11 
 * @author bsli@ustcinfo.com
 */
public class ThreadPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
	
	private static final ThreadPool THREAD_POOL = new ThreadPool();
	
	private final ConcurrentHashMap<String, ExecutorHolder> executors = new ConcurrentHashMap<String, ExecutorHolder>();
	
	private ThreadPool() {};
	
	public static ThreadPool getInstance() {
		return THREAD_POOL;
	}
	
	public Executor buildCached(String name) {
		return buildCached(name, TimeValue.timeValueMinutes(1l));
	}
	
	public Executor buildCached(String name, TimeValue keepAlive) {
		ExecutorHolder holder = build(name, PoolType.CACHED, -1, -1, keepAlive, -1);
		return holder.executor;
	}
	
	public Executor buildFixed(String name, int poolSize) {
		return buildFixed(name, poolSize, TimeValue.timeValueMinutes(1l), -1);
	}
	
	public Executor buildFixed(String name, int poolSize, TimeValue keepAlive) {
		return buildFixed(name, poolSize, keepAlive, -1);
	}
	
	public Executor buildFixed(String name, int poolSize, TimeValue keepAlive, int queueSize) {
		ExecutorHolder holder = build(name, PoolType.FIXED, poolSize, poolSize, keepAlive, queueSize);
		return holder.executor;
	}
	
	private synchronized ExecutorHolder build(String name, PoolType type, int min, int max, TimeValue keepAlive, int queueSize) {
		if(name == null || "".equals(name.trim()))
			throw new IllegalArgumentException("线程池名称不能为空");
		
		if(executors.containsKey(name))
			throw new IllegalArgumentException("线程池 [" + name + "] 已经存在");
			
		ThreadFactory threadFactory = new NamedThreadFactory(name);
		
		ExecutorHolder holder = null;
		if(PoolType.CACHED == type) {
			return new ExecutorHolder(MoreExecutors.sameThreadExecutor(), new Info(name, type));
		} else if(PoolType.CACHED == type) {
			LOGGER.info("creating thread_pool [{}], type [cached], keep_alive [{}]", name, keepAlive);
			Executor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, keepAlive.getSeconds(), TimeUnit.SECONDS, 
					new SynchronousQueue<Runnable>(), threadFactory, new ThreadAbortPolicy());
			holder = new ExecutorHolder(executor, new Info(name, type, -1, -1, keepAlive, -1));
			
		} else if(PoolType.FIXED == type) {
			BlockingQueue<Runnable> workQueue;
			int size = max;
			if (queueSize < 0) {
				workQueue = new LinkedBlockingQueue<Runnable>();
	        } else {
	        	workQueue = new SizeBlockingQueue<Runnable>(new LinkedBlockingQueue<Runnable>(), queueSize);
	        }
			
			LOGGER.info("creating thread_pool [{}], type [fixed], size [{}], queue_size [{}]", name, size, queueSize);
			Executor executor = new ThreadPoolExecutor(size, size, keepAlive.getSeconds(), TimeUnit.SECONDS, 
					workQueue, threadFactory, new ThreadAbortPolicy());
			holder = new ExecutorHolder(executor, new Info(name, type, size, size, null, queueSize));
			
		} else {
			throw new ThreadPoolRuntimeException("不支持线程池类型：" + type);
		}
		executors.put(name, holder);
		
		return holder;
    }
	
	public ThreadPoolInfo info() {
        List<Info> infos = new ArrayList<Info>();
        for (ExecutorHolder holder : executors.values()) {
        	if (PoolType.SAME == holder.info.getType()) {
                continue;
            }
            infos.add(holder.info);
        }
        return new ThreadPoolInfo(infos);
    }
	
	public ThreadPoolStats stats() {
        List<ThreadPoolStats.Stats> stats = new ArrayList<ThreadPoolStats.Stats>();
        for (ExecutorHolder holder : executors.values()) {
            if (PoolType.SAME == holder.info.getType()) {
                continue;
            }
            
            int threads = -1;
            int queue = -1;
            int active = -1;
            long rejected = -1;
            int largest = -1;
            long completed = -1;
            if (holder.executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) holder.executor;
                threads = threadPoolExecutor.getPoolSize();
                queue = threadPoolExecutor.getQueue().size();
                active = threadPoolExecutor.getActiveCount();
                largest = threadPoolExecutor.getLargestPoolSize();
                completed = threadPoolExecutor.getCompletedTaskCount();
                RejectedExecutionHandler rejectedExecutionHandler = threadPoolExecutor.getRejectedExecutionHandler();
                if (rejectedExecutionHandler instanceof XRejectedExecutionHandler) {
                    rejected = ((XRejectedExecutionHandler) rejectedExecutionHandler).rejected();
                }
            }
            stats.add(new ThreadPoolStats.Stats(holder.info.getName(), threads, queue, active, rejected, largest, completed));
        }
        return new ThreadPoolStats(stats);
    }
	
	public void shutdown() {
        for (ExecutorHolder executor : executors.values()) {
            if (executor.executor instanceof ThreadPoolExecutor) {
                ((ThreadPoolExecutor) executor.executor).shutdown();
            }
        }
    }

    public void shutdownNow() {
        for (ExecutorHolder executor : executors.values()) {
            if (executor.executor instanceof ThreadPoolExecutor) {
                ((ThreadPoolExecutor) executor.executor).shutdownNow();
            }
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        boolean result = true;
        for (ExecutorHolder executor : executors.values()) {
            if (executor.executor instanceof ThreadPoolExecutor) {
                result &= ((ThreadPoolExecutor) executor.executor).awaitTermination(timeout, unit);
            }
        }
        return result;
    }
	
	static class ExecutorHolder {
        public final Executor executor;
        public final Info info;

        ExecutorHolder(Executor executor, Info info) {
            this.executor = executor;
            this.info = info;
        }
    }
	
	public static enum PoolType {	
		SAME, CACHED, FIXED, SCALING;
	}
	public static class Info implements Serializable {
		private static final long serialVersionUID = 2855544286528862160L;
		
		private String name;
        private PoolType type;
        private int min;
        private int max;
        private TimeValue keepAlive;
        private int queueSize;

        Info() {}

        public Info(String name, PoolType type) {
            this(name, type, -1);
        }

        public Info(String name, PoolType type, int size) {
            this(name, type, size, size, null, 0);
        }

        public Info(String name, PoolType type, int min, int max, TimeValue keepAlive, int queueSize) {
            this.name = name;
            this.type = type;
            this.min = min;
            this.max = max;
            this.keepAlive = keepAlive;
            this.queueSize = queueSize;
        }

        public String getName() {
            return this.name;
        }

        public PoolType getType() {
            return this.type;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public TimeValue getKeepAlive() {
            return this.keepAlive;
        }

        public int getQueueSize() {
            return this.queueSize;
        }
        
        public String info() {
        	return String.format("%14s%8s%6s%6s%12s%10s\r\n", name, type, min, max, keepAlive, queueSize);
        }
    }
}
