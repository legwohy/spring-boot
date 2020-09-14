package com.cobra.design.proxy.entity;



public class Dog implements Animal
{
    @Override
    public void eat(String food)
    {
        System.out.println("狗吃："+food);

        sleep();

    }

    @Override
    public void sleep()
    {
        System.out.println("dog sleep...");
    }
}
