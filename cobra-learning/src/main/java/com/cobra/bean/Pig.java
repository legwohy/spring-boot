package com.cobra.bean;

import org.springframework.stereotype.Service;

/**
 * Created by legwo on 2018/12/6.
 */
@Service
public class Pig implements Animal
{
    @Override
    public void eat()
    {
        System.out.println("🐖吃草");
    }
}
