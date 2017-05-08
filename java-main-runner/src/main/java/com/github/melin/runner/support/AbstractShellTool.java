package com.github.melin.runner.support;

import javax.management.ObjectInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.melin.runner.ServerStartup;
import com.github.melin.runner.util.JMXClient;
import com.github.melin.runner.util.SystemPropertyUtil;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
abstract public class AbstractShellTool extends ShellTool {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartup.class);
	
	@Override
    public Object doMain(String methodName) throws Exception {
        String host = SystemPropertyUtil.get("host", "127.0.0.1");
        int port = SystemPropertyUtil.getInt("port", 4001);

        JMXClient jmxClient = JMXClient.getJMXClient(host, port);
        LOGGER.debug("connected to " + jmxClient.getAddressAsString());
        ObjectInstance brokerInstance = jmxClient.queryMBeanForOne(this.getBrokerName());
        
        Object result = null;
        if (brokerInstance != null) {
        	result = jmxClient.invoke(brokerInstance.getObjectName(), methodName, new Object[0], new String[0]);
            jmxClient.close();
            LOGGER.debug("invoke " + brokerInstance.getClassName() + "#" + methodName + " success");
        }
        else {
        	LOGGER.warn("没有找到 " + this.getBrokerName());
        }
        return result;
    }

}
