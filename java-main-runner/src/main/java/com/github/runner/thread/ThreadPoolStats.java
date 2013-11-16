/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.thread;

import java.io.Serializable;
import java.util.List;

/**
 * Create on @2013-11-15 @下午8:52:50 
 * @author bsli@ustcinfo.com
 */
public class ThreadPoolStats implements Serializable {
	private static final long serialVersionUID = 6865092538998974883L;

	public static class Stats implements Serializable {

		private static final long serialVersionUID = 3734391203270045584L;
		
		private String name;
        private int threads;
        private int queue;
        private int active;
        private long rejected;
        private int largest;
        private long completed;

        Stats() {

        }

        public Stats(String name, int threads, int queue, int active, long rejected, int largest, long completed) {
            this.name = name;
            this.threads = threads;
            this.queue = queue;
            this.active = active;
            this.rejected = rejected;
            this.largest = largest;
            this.completed = completed;
        }

        public String getName() {
            return this.name;
        }

        public int getThreads() {
            return this.threads;
        }

        public int getQueue() {
            return this.queue;
        }

        public int getActive() {
            return this.active;
        }

        public long getRejected() {
            return rejected;
        }

        public int getLargest() {
            return largest;
        }

        public long getCompleted() {
            return this.completed;
        }
        public String stats() {
        	return String.format("%14s%10s%8s%8s%11s%10s%12s\r\n", name, threads, queue, active, rejected, largest, completed);
        }
    }

    private List<Stats> stats;

    ThreadPoolStats() {
    }

    public ThreadPoolStats(List<Stats> stats) {
        this.stats = stats;
    }

	public List<Stats> getStats() {
		return stats;
	}

	public void setStats(List<Stats> stats) {
		this.stats = stats;
	}

	public String stats() {
		StringBuilder builder = new StringBuilder();
		builder.append("===============thread pool stats=========================================\r\n");
		builder.append(String.format("%14s%10s%8s%8s%11s%10s%12s\r\n", "name", "threads", "queue", "active", "rejected", "largest", "completed"));
		for(Stats stat : stats) {
			builder.append(stat.stats());
		}
		return builder.toString();
	}
}
