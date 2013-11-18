/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.runner.support;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 提供实例占用内存大小的计算功能. 内部借助JVM的{@link Instrumentation}实现.
 * 参考：http://jloser.com/Java对象占用内存大小的分析与计算
 *  
 * Create on @2013-11-18 @下午8:43:35 
 * @author bsli@ustcinfo.com
 */
public class MemoryCalculator {

	/**
	 * JVM在初始化后在调用应用程序main方法前将调用本方法, 本方法中可以写任何main方法中可写的代码.
	 *
	 * @param agentArgs 命令行传进行来的代理参数, 内部需自行解析.
	 * @param inst JVM注入的句柄.
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		instrumentation = inst;
	}
	
	/**
	 * 计算实例本身占用的内存大小. 注意:
	 * 1. 多次调用可能结果不一样, 主要跟实例的状态有关
	 * 2. 实例中成员变量如果是reference类型, 则reference所指向的实例占用内存大小不统计在内
	 *
	 * @param obj 待计算内存占用大小的实例.
	 * @return 内存占用大小, 单位为byte.
	 */
	public static long shallowSizeOf(Object obj) {
		if (instrumentation == null) {
			throw new IllegalStateException("Instrumentation initialize failed");
		}
		if (isSharedObj(obj)) {
			return 0;
		}
		return instrumentation.getObjectSize(obj);
	}
	
	/**
	 * 计算实例占用的内存大小, 含其成员变量所引用的实例, 递归计算.
	 *
	 * @param obj 待计算内存占用大小的实例.
	 * @return 内存占用大小, 单位为byte.
	 */
	public static long deepSizeOf(Object obj) {
		Map calculated = new IdentityHashMap();
		Stack unCalculated = new Stack();
		unCalculated.push(obj);
		long result = 0;
		do {
			result += doSizeOf(unCalculated, calculated);
		} while (!unCalculated.isEmpty());
		return result;
	}
	
	/**
	 * 判断obj是否是共享对象. 有些对象, 如interned Strings, Boolean.FALSE和Integer#valueOf()等.
	 *
	 * @param obj 待判断的对象.
	 * @return true, 是共享对象, 否则返回false.
	 */
	private static boolean isSharedObj(Object obj) {
		if (obj instanceof Comparable) {
			if (obj instanceof Enum) {
				return true;
			} else if (obj instanceof String) {
				return (obj == ((String) obj).intern());
			} else if (obj instanceof Boolean) {
				return (obj == Boolean.TRUE || obj == Boolean.FALSE);
			} else if (obj instanceof Integer) {
				return (obj == Integer.valueOf((Integer) obj));
			} else if (obj instanceof Short) {
				return (obj == Short.valueOf((Short) obj));
			} else if (obj instanceof Byte) {
				return (obj == Byte.valueOf((Byte) obj));
			} else if (obj instanceof Long) {
				return (obj == Long.valueOf((Long) obj));
			} else if (obj instanceof Character) {
				return (obj == Character.valueOf((Character) obj));
			}
		}
		return false;
	}
	
	/**
	 * 确认是否需计算obj的内存占用, 部分情况下无需计算.
	 *
	 * @param obj 待判断的对象.
	 * @param calculated 已计算过的对象.
	 * @return true, 意指无需计算, 否则返回false.
	 */
	private static boolean isEscaped(Object obj, Map calculated) {
		return obj == null || calculated.containsKey(obj)
				|| isSharedObj(obj);
	}
	
	/**
	 * 计算栈顶对象本身的内存占用.
	 *
	 * @param unCalculated 待计算内存占用的对象栈.
	 * @param calculated 对象图谱中已计算过的对象.
	 * @return 栈顶对象本身的内存占用, 单位为byte.
	 */
	private static long doSizeOf(Stack unCalculated, Map calculated) {
		Object obj = unCalculated.pop();
		if (isEscaped(obj, calculated)) {
			return 0;
		}
		Class clazz = obj.getClass();
		if (clazz.isArray()) {
			doArraySizeOf(clazz, obj, unCalculated);
		} else {
			while (clazz != null) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (!Modifier.isStatic(field.getModifiers())
							&& !field.getType().isPrimitive()) {
						field.setAccessible(true);
						try {
							unCalculated.add(field.get(obj));
						} catch (IllegalAccessException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		calculated.put(obj, null);
		return shallowSizeOf(obj);
	}
	
	/**
	 * 将数组中的所有元素加入到待计算内存占用的栈中, 等待处理.
	 *
	 * @param arrayClazz 数组的型别.
	 * @param array 数组实例.
	 * @param unCalculated 待计算内存占用的对象栈.
	 */
	private static void doArraySizeOf(Class arrayClazz, Object array,
			Stack unCalculated) {
		if (!arrayClazz.getComponentType().isPrimitive()) {
			int length = Array.getLength(array);
			for (int i = 0; i < length; i++) {
				unCalculated.add(Array.get(array, i));
			}
		}
	}
	
	/** JVM将在启动时通过{@link #premain}初始化此成员变量. */
	private static Instrumentation instrumentation = null;
}
