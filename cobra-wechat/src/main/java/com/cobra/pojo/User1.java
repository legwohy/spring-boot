package com.cobra.pojo;

import org.springframework.beans.factory.BeanNameAware;

import java.util.Date;

/**
 *
 */
public class User1 implements BeanNameAware
{
    private String id;
    private String name;
    private String address;

    private Integer order;
    private Date date;
    public User1()
    {
    }

    public User1(Integer order, String name, Date date) {
        this.order = order;
        this.name = name;
        this.date = date;
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
    public String toString() {
        return "User1{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", order=" + order +
                ", date=" + date +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
