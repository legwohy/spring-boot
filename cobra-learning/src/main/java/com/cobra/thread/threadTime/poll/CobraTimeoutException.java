package com.cobra.thread.threadTime.poll;

/**
 * 运行时异常
 */
public class CobraTimeoutException extends RuntimeException{
    /**
     * 抛出异常信息
     */
    public CobraTimeoutException(String msg) {
        super(msg);
    }

}
