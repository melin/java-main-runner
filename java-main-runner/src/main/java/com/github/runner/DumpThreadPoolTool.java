/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner;

import com.github.runner.support.AbstractShellTool;
import com.github.runner.util.SysProperties;

/**
 * Create on @2013-11-15 @下午8:04:47 
 * @author bsli@ustcinfo.com
 */
public class DumpThreadPoolTool extends AbstractShellTool {
	private static String brokerName = "";
	
	static {
		String className = ServerStartup.readClassName();
    	int index = className.lastIndexOf(".");
    	String packageName = className.substring(0, index);
    	String simpleName = className.substring(index+1);
    	int port = SysProperties.getInt("port", 4001);
    	brokerName = packageName + ":type=" + simpleName + "-" + port + ",*";
	}
	
    public static void main(String[] args) throws Exception {
    	String info = (String)new DumpThreadPoolTool().doMain("threadPool");
    	System.out.println(info);
    }
    
    @Override
	public String getBrokerName() {
		return brokerName;
	}
}
