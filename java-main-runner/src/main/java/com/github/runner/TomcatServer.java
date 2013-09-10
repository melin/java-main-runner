/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardThreadExecutor;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.coyote.AbstractProtocol;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.github.runner.support.AbstractAplicationServer;
import com.github.runner.util.SysProperties;

/**
 * Create on @2013-8-2 @上午10:24:02 
 * @author bsli@ustcinfo.com
 */
public class TomcatServer extends AbstractAplicationServer {
	private static final Log log = LogFactory.getLog(TomcatServer.class);
	
	private Tomcat tomcat = null;
	private Properties properties = new Properties();

	@Override
	public void start() {
		loadProperties();
		
		int port = Integer.valueOf(properties.getProperty("server.port"));
		int connectorPort = Integer.valueOf(properties.getProperty("connector.port"));
		String contextPath = properties.getProperty("server.contextPath");
		String basePath = SysProperties.getString("BASE_HOME");
		System.setProperty(Globals.CATALINA_BASE_PROP, basePath);
		try {
			tomcat = new Tomcat();
			tomcat.getServer().addLifecycleListener(new AprLifecycleListener());
			
			Service service = tomcat.getService();
			
			Connector connector = new Connector("AJP/1.3");
	        connector.setPort(connectorPort);
	        service.addConnector( connector );
	        
	        connector = new Connector("HTTP/1.1");
	        connector.setPort(port);
	        AbstractProtocol protocol = (AbstractProtocol) connector.getProtocolHandler();
	        StandardThreadExecutor executor = new StandardThreadExecutor();
			executor.setName("tomcat-executor");
			protocol.setExecutor(executor);
			service.addExecutor(executor);
	        service.addConnector( connector );
	        tomcat.setConnector(connector);
	        
			AccessLogValve logValve = new AccessLogValve();
			logValve.setPattern("%I %h %l %u %t &quot;%r&quot; %s %b");
			tomcat.getHost().getPipeline().addValve(logValve);
			tomcat.setPort(8009);
			log.info("tomcat context path : " + contextPath);
			tomcat.addWebapp(contextPath, basePath + "/webapp");
			tomcat.start();
			
			new Thread() {
				@Override
				public void run() {
					tomcat.getServer().await();
					while(true) {
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		if(tomcat != null) {
			try {
				tomcat.stop();
			} catch (LifecycleException e) {
				;
			}
		}
	}
	
	private void loadProperties() {
		try {
			InputStream inputStream = TomcatServer.class.getClassLoader().getResourceAsStream("tomcat.properties");
			properties.load(inputStream);
			log.info("load tomcat.properties.");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	} 

}
