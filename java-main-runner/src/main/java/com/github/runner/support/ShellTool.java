/**
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.
 *
 * ShellTool.java Create on 2013-7-11 上午10:13:05
 */
package com.github.runner.support;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
public abstract class ShellTool {


    /** 主功能入口 */
    abstract public void doMain(String methodName) throws Exception;

    abstract public String getBrokerName();

}