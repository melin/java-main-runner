/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.thread;

import java.io.Serializable;
import java.util.List;

import com.github.runner.thread.ThreadPool.Info;

/**
 * Create on @2013-11-15 @下午8:12:46 
 * @author bsli@ustcinfo.com
 */
/**
 * Create on @2013-11-15 @下午8:20:01 
 * @author bsli@ustcinfo.com
 */
public class ThreadPoolInfo implements Serializable {

	private static final long serialVersionUID = 2101312432651310442L;
	
	private List<ThreadPool.Info> infos;

    ThreadPoolInfo() {
    }

    public ThreadPoolInfo(List<ThreadPool.Info> infos) {
        this.infos = infos;
    }

	public List<ThreadPool.Info> getInfos() {
		return infos;
	}

	public void setInfos(List<ThreadPool.Info> infos) {
		this.infos = infos;
	}
	
	public String info() {
		StringBuilder builder = new StringBuilder();
		builder.append("===============thread pool info=========================\r\n");
		builder.append(String.format("%14s%8s%6s%6s%12s%10s\r\n", "name", "type", "min", "max", "keepAlive", "queueSize"));
		for(Info info : infos) {
			builder.append(info.info());
		}
		return builder.toString();
	}
}
