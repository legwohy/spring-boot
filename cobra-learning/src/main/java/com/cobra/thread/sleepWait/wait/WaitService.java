package com.cobra.thread.sleepWait.wait;

/**
 * 先调用 SleepService#mSleep() 在调用 mWait()
 * 由于 mSleep 休眠 不是放当前对象锁 mWait 将值等待
 */
public class WaitService {

    public void mSleep(){
        synchronized(this){
            try{
                Thread.sleep(3*1000);
                this.notifyAll();
                System.out.println(" Sleep 。唤醒等待："+System.currentTimeMillis());
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public void mWait(){
        synchronized(this){
            try {
                System.out.println(" Wait 。等待开始："+System.currentTimeMillis());

                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
