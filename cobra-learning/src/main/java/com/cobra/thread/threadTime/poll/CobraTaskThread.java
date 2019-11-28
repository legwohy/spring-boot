package com.cobra.thread.threadTime.poll;

/**
 * 任务线程 当前线程 设置为守护线程
 */
public class CobraTaskThread extends Thread {

    /** 超时线程*/
    private CobraTimeoutThread tt;

    /**
     * 需要注入TimeoutThread对象
     * 可根据不同的场景，注入不同的对象，完成任务的执行
     */
    public CobraTaskThread(CobraTimeoutThread tt) {
        this.setDaemon(true);
        this.tt = tt;
    }

    @Override
    public void run() {
        try {
            // 这里是任务的执行主体，为了简单示例，只用sleep方法演示
            Thread.sleep(1000);

            //执行任务完成后，更改状态
            tt.isCancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
