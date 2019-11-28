package com.cobra.thread.sleepWait.sleep;



public class SSThread implements Runnable{

    private SleepService service;

    public SSThread(SleepService service){
        this.service = service;
    }

    @Override
    public void run(){
        service.mSleep();
    }

}
