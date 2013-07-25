/**
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.
 *
 * AbstractShellTool.java Create on 2013-7-11 上午10:13:29
 */
package com.github.runner.support;

import javax.management.ObjectInstance;

import com.github.runner.util.JMXClient;
import com.github.runner.util.SysProperties;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
abstract public class AbstractShellTool extends ShellTool {
	@Override
    public void doMain(String methodName) throws Exception {
        String host = SysProperties.getString("host", "127.0.0.1");
        int port = SysProperties.getInt("port", 9999);

        JMXClient jmxClient = JMXClient.getJMXClient(host, port);
        System.out.println("connected to " + jmxClient.getAddressAsString());
        ObjectInstance brokerInstance = jmxClient.queryMBeanForOne(this.getBrokerName());

        if (brokerInstance != null) {
            jmxClient.invoke(brokerInstance.getObjectName(), methodName, new Object[0], new String[0]);
            jmxClient.close();
            System.out.println("invoke " + brokerInstance.getClassName() + "#" + methodName + " success");
        }
        else {
        	System.out.println("没有找到 " + this.getBrokerName());
        }
    }

}
