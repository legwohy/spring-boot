package com.cobra.thread.threadTime.poll;

/**
 * 超时线程
 */
public class CobraTimeoutThread implements Runnable {

    /** 超时时间*/
    private long timeout;

    /** 是否执行完成状态*/
    private boolean isCancel;

    public CobraTimeoutThread(long timeout) {
        super();
        this.timeout = timeout;
    }

    /**
     * 设定监听是否取消
     */
    public void isCancel() {
        this.isCancel = true;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeout);
            if (!isCancel) {
                throw new CobraTimeoutException("thread is timeout");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
