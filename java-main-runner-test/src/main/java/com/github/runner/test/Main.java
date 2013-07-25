/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.test;

import java.util.concurrent.TimeUnit;

import com.github.runner.util.MBeanServerUtil;

/**
 * Create on @2013-7-24 @下午4:18:51 
 * @author bsli@ustcinfo.com
 */
public class Main {
    public static void main(String[] args) throws Exception{   
        init();   
    }   
    private static void init() throws Exception{   
    	AplicationServer demoService = new AplicationServer();   
    	MBeanServerUtil.registMBean(demoService, null);
        for(;;) {
        	TimeUnit.SECONDS.sleep(1);
        }
    }   
}
