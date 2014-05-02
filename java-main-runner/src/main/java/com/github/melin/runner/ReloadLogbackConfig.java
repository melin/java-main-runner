package com.github.melin.runner;

import com.github.melin.runner.support.AbstractShellTool;
import com.github.melin.runner.util.SysProperties;

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
		int port = SysProperties.getInt("port", 4001);
		return "ch.qos.logback.classic:Name=logback-" + port + ",*";
	}

}
