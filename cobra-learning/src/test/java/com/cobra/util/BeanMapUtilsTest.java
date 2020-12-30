package com.cobra.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/30 15:08
 * @desc
 */
public class BeanMapUtilsTest {
    @Test
    public void transfer() throws Exception{

        TestUser user = new TestUser();
        user.setName("passer");
        user.setDescri("cool");


        Map<Object, Object> map = BeanMapUtils.bean2map(user);

        TestUser obj = (TestUser)BeanMapUtils.map2bean(map, TestUser.class);
        Assert.assertEquals(user.toString(),obj.toString());
    }

}

class TestUser {
    private String name;
    private String descri;

    public TestUser(){
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescri(){
        return descri;
    }

    public void setDescri(String descri){
        this.descri = descri;
    }

    @Override
    public String toString(){
        return "TestUser{" +
                        "name='" + name + '\'' +
                        ", descri='" + descri + '\'' +
                        '}';
    }
}
