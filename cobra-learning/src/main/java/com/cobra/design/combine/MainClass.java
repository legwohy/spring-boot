package com.cobra.design.combine;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainClass
{
    public static void main(String[] args)
    {
     /*   List<String> strings = Arrays.asList("bhs","azs","32sf","cd","bc","ds","sd","hsb","ass");
        Collections.sort(strings);
        strings.stream().forEach(str->{System.out.println("==>"+str);});*/

        BigDecimal bigDecimal = new BigDecimal("0.98");
        if(bigDecimal.compareTo(new BigDecimal("0.6"))>0)
        {
            System.out.println("==================");
        }

    }
}
