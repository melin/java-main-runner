/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.melin.runner.thread;

/**
 * Create on @2013-11-15 @下午4:06:47 
 * @author bsli@ustcinfo.com
 */
public abstract class AbstractRunnable implements Runnable {

    /**
     * Should the runnable force its execution in case it gets rejected?
     */
    public boolean isForceExecution() {
        return false;
    }
}
