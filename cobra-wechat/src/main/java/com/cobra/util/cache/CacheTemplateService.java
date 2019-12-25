package com.cobra.util.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cobra.pojo.User1;
import com.cobra.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 缓存模板 redis部分可以做锁
 */
@Component
public class CacheTemplateService {
    @Autowired
    private RedisUtil redisUtil;

     public <T> T query(String key, int second, CacheLoad<T> cacheLoad) {
         // 先从缓存查询
         Object value = redisUtil.get(key);
         if (null == value) {
             // 双重检查

             // string的intern的作用是：每次取数据的时候，
             // 如果常量池中有，直接拿常量池中的数据；如果常量池中没有，
             // 将数据写入常量池中并返回常量池中的数据，可以达到锁的目的
             synchronized (key.intern()) {
                 value = redisUtil.get(key);
                 if (null != value) {
                     return JSON.parseObject(value.toString(),  cacheLoad.getType());
                 }

                 // 从db查询
                 T t = cacheLoad.load();
                 redisUtil.set(key, JSON.toJSONString(t));
                 redisUtil.expire(key, second);

                 return t;
             }

         }

         return JSON.parseObject(value.toString(), cacheLoad.getType());
     }
    public static void main(String[] args) {
        //jsonList();

        jsonMap();
    }

    static void jsonList(){
        List<User1> list = new ArrayList<User1>();
        User1 j1 = new User1(1001,"J",new Date());
        User1 j2 = new User1(1002,"Q",new Date());
        User1 j3 = new User1(1003,"K",new Date());
        list.add(j1);
        list.add(j2);
        list.add(j3);

        String jsonString = JSON.toJSONString(list);

        System.out.println("type0:"+jsonString+"\r\n");
        List<User1> types1= JSON.parseObject(jsonString,List.class);
        System.out.println("type1="+types1);


        System.out.println();
        List<User1> types3 = JSON.parseObject(jsonString, new TypeReference<List<User1>>() {});

        System.out.println("type3=" + types3);

    }


    static void jsonMap(){
        List<Map<String,User1>> list = new ArrayList<>();
        User1 j1 = new User1(1001,"J",new Date());
        Map<String,User1> m1 = new HashMap<>();
        m1.put("j1",j1);
        list.add(m1);

        User1 j2 = new User1(1002,"Q",new Date());
        Map<String,User1> m2 = new HashMap<>();
        m2.put("j2",j2);
        list.add(m2);

        User1 j3 = new User1(1003,"K",new Date());
        Map<String,User1> m3 = new HashMap<>();
        m3.put("j3",j3);
        list.add(m3);


        String jsonString = JSON.toJSONString(list);

        System.out.println("type0:"+jsonString+"\r\n");
        List<Map<String,User1>> types1= JSON.parseObject(jsonString,List.class);
        System.out.println("type1="+types1.get(0).get("j1"));


        System.out.println();
        List<Map<String,User1>> types3 =
                JSON.parseObject(jsonString, new TypeReference<List<Map<String,User1>>>() {}.getType());

        System.out.println("type3=" + types3.get(0).get("j1"));

    }

    /**
     * <T>定义泛型
     * T 泛型的类型
     * 无论何时，只要能做到，尽量使用泛型方法 少使用泛型类
     * 如果使用泛型方法可以取代将整个类泛化，那么应该有限采用泛型方法
     *
     * @param args 泛型类型
     * @param <T>  表示该方法为泛型方法
     */
    public static <T> void out(T... args) {
        for (T t : args) {
            System.out.println(t);
        }
    }

    public static <T> void out(T args) {
        System.out.println(args);
    }
}

