package com.cobra.thread.sleepWait.sleep;

/**
 * 先调用 SleepService#mSleep() 在调用 mWait()
 * 由于 mSleep 休眠 不是放当前对象锁 mWait 将值等待
 */
public class SleepService {

    public void mSleep(){
        synchronized(this){
            try{
                System.out.println(" Sleep 。当前时间："+System.currentTimeMillis());
                Thread.sleep(3*1000);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public void mWait(){
        synchronized(this){
            System.out.println(" Wait 。结束时间："+System.currentTimeMillis());

        }
    }

}
