/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 *
 * @{#} @MBeanServerUtil.java Create on @2013-7-17 @下午1:53:21 
 */
package com.github.runner.util;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @author bsli@ustcinfo.com
 * 
 */
public class MBeanServerUtil {
	public static void registMBean(Object o, String name) {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		if (null != mbs) {
			try {
				mbs.registerMBean(o, new ObjectName(o.getClass().getPackage()
						.getName()
						+ ":type="
						+ o.getClass().getSimpleName()
						+ (null == name ? ",id=" + o.hashCode() : ",name="
								+ name + "-" + o.hashCode())));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
