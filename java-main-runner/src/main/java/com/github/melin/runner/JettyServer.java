package com.github.melin.runner;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.melin.runner.support.AbstractAplicationServer;
import com.github.melin.runner.util.SystemPropertyUtil;

/**
 *
 * @author bsli@ustcinfo.com
 */
public class JettyServer extends AbstractAplicationServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);
	
	private Server server = null;

	@Override
	public void start() {
		System.out.println(this.getClass().getClassLoader());
		String basePath = SystemPropertyUtil.get("BASE_HOME");
		try {
			Resource resource = Resource.newResource(basePath + File.separator + "conf" + File.separator + "jetty.xml");
			XmlConfiguration configuration = new XmlConfiguration(resource.getInputStream());  
			
			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			server = (Server) configuration.configure();  
	        server.start();  
	        server.join();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}
	}
	
	@Override
	public void stop() {
		if(server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				System.exit(1);
			}
		}
	}
}
