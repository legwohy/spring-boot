package com.cobra.collection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 集合操作
 * set集合 是可变性 因此在操作之前最好复制一个副本
 * 差集是有顺序、取正值
 */
public class SetStringUtils
{
    public static void main(String[] args)
    {
        Set<String> setA = new HashSet<>();

        setA.add("a");
        setA.add("a");
        setA.add("b");
        setA.add("c");

        Set<String> setB = new HashSet<>();
        setB.add("a");
        setB.add("d");

        // 1、set集合去重


        // 2、集合的算法 差集有顺序 setA-setB 取正值
        Set<String> diff = getDifferent(setA,setB);// 差集
        //Set<String> intersection = getIntersection(setA,setB);// 交集
        //Set<String> collection = getCollection(setA,setB);// 并集
       //  System.out.println("去重:"+Arrays.toString(setA.toArray()));
        System.out.println("差集:"+Arrays.toString(diff.toArray()));
        //System.out.println("交集:"+Arrays.toString(intersection.toArray()));
//        System.out.println("并集:"+Arrays.toString(collection.toArray()));

    }

    /**
     * 获取交集
     */
    public static Set<String> getIntersection(Set<String> setA,Set<String> setB){
        setA.retainAll(setB);
        return setA;
    }

    /**
     * 获取并集
     */
    public static Set<String> getCollection(Set<String> setA,Set<String> setB){
        setA.addAll(setB);
        return setA;
    }

    /**
     * 获取差集
     */
    public static Set<String> getDifferent(Set<String> setA,Set<String> setB){
        setA.removeAll(setB);
        return setA;
    }


}
