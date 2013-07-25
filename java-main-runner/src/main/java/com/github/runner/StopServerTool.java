/**
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.
 *
 * StopServerTool.java Create on 2013-7-11 上午10:14:05
 */
package com.github.runner;

import com.github.runner.support.AbstractShellTool;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
public class StopServerTool extends AbstractShellTool {
	private static String brokerName = "";
	static {
		String className = ServerStartup.readClassName();
    	int index = className.lastIndexOf(".");
    	String packageName = className.substring(0, index);
    	String simpleName = className.substring(index+1);
    	brokerName = packageName + ":type=" + simpleName + ",*";
	}
	
    public static void main(String[] args) throws Exception {
        new StopServerTool().doMain("stop");
    }
    
    @Override
	public String getBrokerName() {
		return brokerName;
	}
}
