/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.melin.runner.thread;


/**
 * Create on @2013-11-15 @下午4:02:31 
 * @author bsli@ustcinfo.com
 */
public class ThreadPoolRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ThreadPoolRuntimeException(String msg) {
        super(msg);
    }
    
    public ThreadPoolRuntimeException(Throwable cause) {
        super(cause);
    }

    public ThreadPoolRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
