package com.cobra.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * @auther: sh-zxgs-chenx
 * @date: 2020/7/15 10:18
 * @description:
 */
@Slf4j
public class LogUtils
{
    public static final String NL = System.getProperty("line.separator");

    /**
     * 打印map日志
     * @param params
     */
    public  void logMap(Map<String,Object> params) {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getSimpleName());
        b.append(" params: ");
        b.append(NL);// 空白行

        for (Map.Entry<String, Object> entry : new TreeMap<>(params).entrySet()) {
            b.append('\t');
            b.append(entry.getKey());
            b.append(" = ");
            b.append(entry.getValue());
            b.append(NL);
        }
        log.info(b.toString());
    }

    public static void logModel(Object object){
        Class clazz = object.getClass();
        StringBuilder b = new StringBuilder();
        b.append(clazz.getSimpleName());
        b.append(" params: ");
        b.append(NL);// 空白行

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            b.append('\t');
            b.append(field.getName());
            b.append(" = ");
            Object value = null;
            try
            {
                value = field.get(object);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            if(null == value){
                value = "";
            }
            b.append(value.toString());
            b.append(NL);
        }
        System.out.println(b.toString());
        //log.info(b.toString());

    }

    public static void main(String[] args) throws Exception
    {
        TempDTO tempDTO = new TempDTO();
        tempDTO.setId(1);
       //tempDTO.setName("test");

        // 打印日志
        logModel(tempDTO);
        System.out.println("输出完成");

    }


}

@Data
class TempDTO{
    private Integer id;
    private String name;
}