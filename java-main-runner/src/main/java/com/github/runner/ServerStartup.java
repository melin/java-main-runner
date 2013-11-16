/**

 * Copyright (c) 2012,USTC E-BUSINESS TECHNOLOGY CO.LTD All Rights Reserved.
 */

package com.github.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.runner.support.AbstractAplicationServer;
import com.github.runner.util.MBeanServerUtil;

/**
 * @author bsli@starit.com.cn
 * @date 2012-9-11 上午9:11:31
 */
public class ServerStartup {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartup.class);
	
	public static void main(String[] args) {
		String className = readClassName();
		try {
			Class<?> clazz = Class.forName(className);
			Object object = clazz.newInstance();
			if(object instanceof AbstractAplicationServer) {
				AbstractAplicationServer server = ((AbstractAplicationServer)object);
				server.start();
				
				MBeanServerUtil.registMBean(object, clazz.getSimpleName());
				
				LOGGER.info("应用服务启动成功。");
				for(;;) {
		        	try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						;
					}
		        }
			} else {
				throw new RuntimeException(className + " 没有继承 " + AbstractAplicationServer.class.getName() + ", 启动失败。");
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(className + " 类不存在，请确认名称是否正确");
		} catch (InstantiationException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
	}
	
	/**
	 * 读取conf/main-class文件中启动类名称，文件名称包含类包路径，并且放在文件第一行。
	 * 
	 * @return
	 */
	public static String readClassName() {
		String fileName = ServerStartup.class.getClassLoader().getResource(".").getPath();
		fileName += File.separator + "META-INF" + File.separator + "main-class";
		LOGGER.debug("read main-class file path: {}", fileName);
		
		BufferedReader bufferedReader = null;
		try {
			FileInputStream input = new FileInputStream(fileName);
			InputStreamReader reader = new InputStreamReader(input, "UTF-8");
			bufferedReader = new BufferedReader(reader);
	        return bufferedReader.readLine().trim();
		} catch(IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					;
				}
			}
		}
	}

}
