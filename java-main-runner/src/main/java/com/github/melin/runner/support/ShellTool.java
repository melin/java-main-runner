package com.github.melin.runner.support;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
public abstract class ShellTool {

    abstract public Object doMain(String methodName) throws Exception;

    abstract public String getBrokerName();

}