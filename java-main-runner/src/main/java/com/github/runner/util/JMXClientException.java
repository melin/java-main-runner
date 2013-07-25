/**
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.
 *
 * JMXClientException.java Create on 2013-7-11 上午10:25:02
 */
package com.github.runner.util;

/**
 *
 * @author <a href="mailto:bsli@ustcinfo.com">li.binsong</a>
 *
 */
public class JMXClientException extends Exception {

    private static final long serialVersionUID = -7410016800727397507L;


    public JMXClientException(String message) {
        super(message);
    }


    public JMXClientException(Throwable cause) {
        super(cause);
    }


    public JMXClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
