package com.cobra.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CountNumber
{
    @Async
    public void printNumber(){
        for (int i = 10;i>0;i--){
            log.info("----------->printNumber "+Thread.currentThread().getName()+"\ti = "+i);
        }
    }
}
