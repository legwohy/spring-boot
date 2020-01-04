package com.cobra.thread.sleepWait.sleep;



public class SWThread implements Runnable{

    private SleepService service;

    public SWThread(SleepService service){
        this.service = service;
    }

    @Override
    public void run(){
        service.mWait();
    }

}
