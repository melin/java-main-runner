/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.runner.support.AbstractAplicationServer;


/**
 * Create on @2013-7-24 @下午4:18:35 
 * @author bsli@ustcinfo.com
 */
public class AplicationServer extends AbstractAplicationServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AplicationServer.class);

	@Override
	public void stop() {
		LOGGER.info("stop======");
	}
	
	@Override
	public void start() {
		LOGGER.info("start======");
	}
}
