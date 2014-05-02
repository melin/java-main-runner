/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.github.melin.runner;

import java.io.PrintStream;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Locale;

import com.github.melin.runner.util.JMXClient;
import com.github.melin.runner.util.JMXClientException;
import com.github.melin.runner.util.SystemPropertyUtil;

/**
 * dump thread info
 * 
 * Create on @2013-11-12 @上午10:59:52 
 * @author bsli@ustcinfo.com
 */
public class DumpThreadTool {
	
	private static ThreadMXBean threadBean;

	public static void main(String[] args) {
		final PrintStream writer = System.out;
		
		String host = SystemPropertyUtil.get("host", "127.0.0.1");
        int port = SystemPropertyUtil.getInt("port", 4001);

		try {
			JMXClient jmxClient = JMXClient.getJMXClient(host, port);
			writer.println("connected to " + jmxClient.getAddressAsString());
			
			threadBean = jmxClient.getThreadMXBean();
			
			processDeadlocks(writer);
            processAllThreads(writer);
		} catch (JMXClientException e) {
			e.printStackTrace();
		}
	}
	
	private static void processDeadlocks(PrintStream dump) {
        dump.println("=====  Deadlocked Threads =====");
        long deadlockedThreadIds[] = findDeadlockedThreads();
        if (deadlockedThreadIds != null)
            dumpThreads(dump, getThreadInfo(deadlockedThreadIds));
    }

    private static void processAllThreads(PrintStream dump) {
        dump.println();
        dump.println("===== All Threads =====");
        dumpThreads(dump, dumpAllThreads());
    }

    private static void dumpThreads(PrintStream dump, ThreadInfo infos[]) {
        for (ThreadInfo info : infos) {
            dump.println();
            write(info, dump);
        }
    }

    private static ThreadInfo[] dumpAllThreads() {
        return threadBean.dumpAllThreads(true, true);
    }

    private static long[] findDeadlockedThreads() {
        return threadBean.findDeadlockedThreads();
    }

    private static ThreadInfo[] getThreadInfo(long[] threadIds) {
        return threadBean.getThreadInfo(threadIds, true, true);
    }

    private static void write(ThreadInfo threadInfo, PrintStream writer) {
        writer.print(String.format(Locale.ROOT, "\"%s\" Id=%s %s", threadInfo.getThreadName(), threadInfo.getThreadId(), threadInfo.getThreadState()));
        if (threadInfo.getLockName() != null) {
            writer.print(String.format(Locale.ROOT, " on %s", threadInfo.getLockName()));
            if (threadInfo.getLockOwnerName() != null)
                writer.print(String.format(Locale.ROOT, " owned by \"%s\" Id=%s", threadInfo.getLockOwnerName(), threadInfo.getLockOwnerId()));
        }
        if (threadInfo.isInNative())
            writer.println(" (in native)");
        else
            writer.println();
        MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
        StackTraceElement stackTraceElements[] = threadInfo.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            writer.println("    at " + stackTraceElement);
            MonitorInfo lockedMonitor = findLockedMonitor(stackTraceElement, lockedMonitors);
            if (lockedMonitor != null)
                writer.println(("    - locked " + lockedMonitor.getClassName() + "@" + lockedMonitor.getIdentityHashCode()));
        }

    }

    private static MonitorInfo findLockedMonitor(StackTraceElement stackTraceElement, MonitorInfo lockedMonitors[]) {
        for (MonitorInfo monitorInfo : lockedMonitors) {
            if (stackTraceElement.equals(monitorInfo.getLockedStackFrame()))
                return monitorInfo;
        }

        return null;
    }
}
