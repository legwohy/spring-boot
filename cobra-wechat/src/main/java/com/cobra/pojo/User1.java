package com.cobra.pojo;

import org.springframework.beans.factory.BeanNameAware;

/**
 *
 */
public class User1 implements BeanNameAware
{
    private String id;
    private String name;
    private String address;
    public User1()
    {
    }

    @Override
    public void setBeanName(String beanName)
    {
        this.id = beanName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Override
    public String toString()
    {
        return "User2{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", address='" + address + '\'' +
                        '}';
    }


}
