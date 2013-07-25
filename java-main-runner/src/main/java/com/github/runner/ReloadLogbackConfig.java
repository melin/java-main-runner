package com.github.runner;

import com.github.runner.support.AbstractShellTool;

/**
 * 
 * @author bsli123@ustcinfo.com
 * @date 2012-12-31 下午2:13:37
 */
public class ReloadLogbackConfig extends AbstractShellTool {
	
    public static void main(String[] args) throws Exception {
        new ReloadLogbackConfig().doMain("reloadDefaultConfiguration");
    }

	@Override
	public String getBrokerName() {
		return "ch.qos.logback.classic:Name=default,*";
	}

}
