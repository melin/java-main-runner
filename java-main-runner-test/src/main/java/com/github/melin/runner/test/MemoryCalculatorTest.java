/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.melin.runner.test;

import java.util.HashMap;

import com.github.melin.runner.support.MemoryCalculator;

/**
 * -javaagent:F:\codes\opensources\github\java-main-runner\java-main-runner\target\java-main-runner-1.0.0-SNAPSHOT.jar
 * 
 * Create on @2013-11-18 @下午8:56:37 
 * @author bsli@ustcinfo.com
 */
public class MemoryCalculatorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long result = MemoryCalculator.shallowSizeOf(new Object());
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[1000]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[2]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[2][3]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[3][3]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[9][3]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[2][3][3]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[7][3]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[3][7]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new byte[7][6][4]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new HashMap[7][6][4]);
		System.out.println(result);
		result = MemoryCalculator.shallowSizeOf(new HashMap[7]);
		System.out.println(result);
	}

}
