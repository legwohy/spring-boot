package com.cobra.design.proxy;

/**
 * Created by legwo on 2018/12/22.
 */
public class Cat implements Animal
{
    @Override
    public void eat(String food)
    {
        System.out.println("==========<猫吃老鼠\t"+food);
    }
}
