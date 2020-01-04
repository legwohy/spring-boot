package com.cobra.thread.sleepWait.wait;



public class WWThread implements Runnable{

    private WaitService service;

    public WWThread(WaitService service){
        this.service = service;
    }

    @Override
    public void run(){
        service.mWait();
    }

}
