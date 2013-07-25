/**
 * Copyright (c) 2012,USTC E-BUSINESS TECHNOLOGY CO.LTD All Rights Reserved.
 */

package com.github.runner.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

import com.github.runner.util.SysProperties;

/**
 * @author bsli123@ustcinfo.com
 * @date 2012-12-31 上午10:49:26
 */
public abstract class AbstractAplicationServer implements DynamicMBean {
	
	private List<MBeanAttributeInfo> attributes = new ArrayList<MBeanAttributeInfo>();
	private List<MBeanOperationInfo> operations = new ArrayList<MBeanOperationInfo>();  
	
	private MBeanInfo mBeanInfo;  
	
	public AbstractAplicationServer() {  
        try{  
            init();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	private void init() throws Exception{  
        buildDynamicInfo();  
        mBeanInfo = createMBeanInfo();  
    }  
	
	public abstract void stop();
	
	public abstract void start();

	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if("baseHome".equals(attribute)) 
			return SysProperties.getString("BASE_HOME");
		else
			return null;
	}

	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		return null;
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		return null;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		try {
			Method methods[] = this.getClass().getMethods();
			Object result = null;
			for (Method method : methods) {
				String name = method.getName();
				if (name.equals(actionName)) {
					result = method.invoke(this, params);
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return mBeanInfo;
	}
	
	private void buildDynamicInfo() throws Exception{  
		attributes.add(new MBeanAttributeInfo("baseHome", "java.lang.String", "安装路径", true, false, false));  
        operations.add(new MBeanOperationInfo("stop method", this.getClass().getMethod("stop")));  
    }  
	
	private MBeanInfo createMBeanInfo(){  
		return new MBeanInfo(this.getClass().getName(), this.getClass().getSimpleName() + "Dynamic", 
				attributes.toArray(new MBeanAttributeInfo[attributes.size()]), null,
                operations.toArray(new MBeanOperationInfo[operations.size()]), null);  
    }  
}
