/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.thread;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * Create on @2013-11-15 @下午5:17:43 
 * @author bsli@ustcinfo.com
 */
public interface XRejectedExecutionHandler extends RejectedExecutionHandler {

    /**
     * The number of rejected executions.
     */
    long rejected();
}
