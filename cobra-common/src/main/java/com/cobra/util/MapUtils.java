package com.cobra.util;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @author admin
 * @date 2021/1/11 20:37
 * @desc
 */
public class MapUtils {
    static String separation = ".";

    public static void main(String[] args){

        // 测试
        Map<String, Object> container = new HashMap<>();
        getMapData().forEach((key, value) -> {
            Map<String, Object> data = change(new HashMap<>(), key, value);
            merge(container, data);
        });

        // 将toString方法覆盖,输出想要的json格式

        // String json = jsonStyleFormat(container);
        System.out.println(JSON.toJSONString(container));

    }

    /**
     * 将toString方法覆盖,输出想要的json格式
     *
     * @param container 要格式化的的数据
     */
    private static String jsonStyleFormat(Map<String, Object> container){
        String mapString = container.toString();
        System.out.println(mapString);
        return mapString.replaceAll("=", ":");
    }

    /**
     * 多个map有相同的key进行合并
     *
     * @param container 装载遍历之后的数据
     * @param data      要遍历查看的数据
     * @return 合并之后的数据
     */
    private static Map<String, Object> merge(Map<String, Object> container, Map<String, Object> data){
        // 遍历map中的数据
        data.forEach((sonKey, sonValue) -> {
            // 如果容器中包含了目前遍历的key,就代表这个key已经存在,需要覆盖处理
            if (container.containsKey(sonKey)) {
                // 获取覆盖之前的数据
                Object oldValue = container.get(sonKey);
                // 如果不是map,就不需要再次判断了
                if (!(sonValue instanceof Map)) {
                    return;
                }
                // 将新的合并的值放入容器
                container.put(sonKey, merge((Map<String, Object>)sonValue, (Map<String, Object>)oldValue));
            } else {
                // 如果没有冲突,直接存入map
                container.put(sonKey, sonValue);
            }
        });
        return container;
    }

    /**
     * 根据"."的key进行解析成单独的Map
     *
     * @param map   装有解析数据的容器
     * @param key   每一次截取的key
     * @param value key对应的值
     * @return 解析之后的map
     */
    private static Map<String, Object> change(Map<String, Object> map, String key, Object value){
        // 判断字符串是否有.号
        if (key.contains(separation)) {
            // 只要有.号,就代表有子map
            Map<String, Object> sonMap = new HashMap<>();
            // 获取第一个.的位置方便获取key
            int index = key.indexOf(separation);
            // 获取第一个key
            String firstKey = key.substring(0, index);
            // 获取了第一个key之后,剩下的单独作为key在存入map,+2是因为+1是这个字符串的下一个
            int beginIndex = key.indexOf(firstKey);
            String sonKey = key.substring(beginIndex + firstKey.length() + 1);
            // 将截取的第一个字符串重新装配
            map.put(firstKey, change(sonMap, sonKey, value));
        } else {
            // 没有点号,代表只有一个
            map.put(key, value);
        }
        return map;
    }

    /**
     * 提供测试数据
     */
    private static Map<String, Object> getMapData(){
        List<String> initList = new ArrayList<>();
        initList.add("credit.header.mobile");
        initList.add("credit.body");
        initList.add("credit.header.version_fix");
        initList.add("credit.header.flag_system");

        initList.add("credit.mac");
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : initList) {
            map.put(key, key);
        }

        return map;
    }
}
