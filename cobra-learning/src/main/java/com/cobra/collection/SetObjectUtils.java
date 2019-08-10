package com.cobra.collection;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 集合操作
 * set集合 操作对象
 * 这种对象很难运算 只能使用单个字符串比如包装成uuid做运算
 */
public class SetObjectUtils
{
    public static void main(String[] args)
    {
        Set<User> setA = new HashSet<>();

        setA.add(new User(1,"jack"));
        setA.add(new User(1,"jack"));
        setA.add(new User(2,"jack"));
        setA.add(new User(3,"rose"));

        Set<User> setB = new HashSet<>();
        setB.add(new User(4,"rose"));
        setB.add(new User(5,"nike"));

        // 1、set集合去重
        System.out.println("去重:"+Arrays.toString(setA.toArray()));

        // 2、集合的算法 差集有顺序 setA-setB 取正值
       // Set<User> diff = getDifferent(setA,setB);// 差集
        //Set<String> intersection = getIntersection(setA,setB);// 交集
        //Set<String> collection = getCollection(setA,setB);// 并集

        //System.out.println("差集:"+Arrays.toString(diff.toArray()));
        //System.out.println("交集:"+Arrays.toString(intersection.toArray()));
//        System.out.println("并集:"+Arrays.toString(collection.toArray()));

    }

    /**
     * 获取交集
     */
    public static Set<User> getIntersection(Set<User> setA,Set<User> setB){
        setA.retainAll(setB);
        return setA;
    }

    /**
     * 获取并集
     */
     static Set<User> getCollection(Set<User> setA,Set<User> setB){
        setA.addAll(setB);
        return setA;
    }

    /**
     * 获取差集
     */
     static Set<User> getDifferent(Set<User> setA,Set<User> setB){
        setA.removeAll(setB);
        return setA;
    }

    static class User{
        private String name;
        private Integer id;
        public User(Integer id,String name){
            this.name = name;
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

        public Integer getId()
        {
            return id;
        }

        public void setId(Integer id)
        {
            this.id = id;
        }

        @Override
        public String toString()
        {
            return "User{" +
                            "name='" + name + '\'' +
                            ", id=" + id +
                            '}';
        }
    }


}
