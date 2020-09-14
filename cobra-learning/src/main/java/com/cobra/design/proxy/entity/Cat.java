package com.cobra.design.proxy.entity;


public class Cat implements Animal
{
    @Override
    public void eat(String food)
    {
        System.out.println("猫吃："+food);

        sleep();
    }

    @Override
    public void sleep()
    {
        System.out.println("cat sleep ...");
    }
}
