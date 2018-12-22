package com.cobra.design.proxy;

/**
 * Created by legwo on 2018/12/22.
 */
public class Dog implements Animal
{
    private String type;
    private Integer id;
    @Override
    public void eat(String food)
    {
        System.out.println("狗吃屎======="+food);

    }
}
