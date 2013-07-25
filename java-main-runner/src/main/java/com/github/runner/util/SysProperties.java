/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 *
 * @{#} @SysProperties.java Create on @2013-7-16 @下午4:09:10 
 */    
package com.github.runner.util;

/**
 * @author bsli@ustcinfo.com
 *
 */
public class SysProperties {
	public static String getString(String key) {
		String value = System.getProperty(key);
		return value;
	}
	
	public static String getString(String key, String defaultValue) {
		String value = System.getProperty(key);
		if(value != null && !"".equals(value))
			return value;
		else 
			return defaultValue;
	}
	
	public static int getInt(String key) {
		return getInt(key, 0);
	}
	
	public static int getInt(String key, int defaultValue) {
		String value = System.getProperty(key);
		if(value != null && !"".equals(value))
			return Integer.valueOf(value);
		else 
			return defaultValue;
	}
}
