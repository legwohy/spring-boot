package com.cobra.thread.sleepWait.wait;


import com.cobra.thread.sleepWait.sleep.SleepService;

public class WSThread implements Runnable{

    private WaitService service;

    public WSThread(WaitService service){
        this.service = service;
    }

    @Override
    public void run(){
        service.mSleep();
    }

}
