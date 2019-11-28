package com.cobra.thread.sleepWait.wait;

import com.cobra.thread.sleepWait.sleep.SSThread;
import com.cobra.thread.sleepWait.sleep.SWThread;

/**
 * wait 等待会释放当前锁
 * <a link='https://blog.csdn.net/xyh269/article/details/52613507'/>
 */
public class WaitMainTest {
    public static void main(String[] args){

        //
        WaitService service = new WaitService();

        Thread sleepThread = new Thread(new WSThread(service));
        Thread waitThread = new Thread(new WWThread(service));
        waitThread.start();

        sleepThread.start();

    }
}
