package com.cobra.test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2018/10/15.
 */
public class TestParallel
{
    public static void main(String[] args){
        System.out.println("=========>"+TestParallel.class.getResource("/"));
        //List<String> names = Arrays.asList("jack","rose","lucy");

    }

    private static void check(List<String> names){
        names.parallelStream().forEach(name->{
            if("lucy".equalsIgnoreCase(name)){
                return ;
            }
            System.out.println("-->"+name);

        });

    }
}
