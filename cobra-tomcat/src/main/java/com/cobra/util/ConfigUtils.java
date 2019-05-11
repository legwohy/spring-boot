package com.cobra.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本文使用 dom4j 解析xml
 *  SAXReader ===> Document ===> Element(elements,element("key).getText())
 * 解析web.xml文件
 */
public class ConfigUtils
{
    public Map<String,String> getClassName(String path) throws Exception
    {
        Map<String,String> handlerMap = new HashMap<>();
        SAXReader reader = new SAXReader();
        File file = new File(path);
        Document document = reader.read(file);// 文档 节点
        Element rootElement = document.getRootElement();
        List<Element> childElements = rootElement.elements();
        for (Element childElement:childElements){
            String key = childElement.element("url-pattern").getText();
            String value = childElement.element("servlet-class").getText();
            handlerMap.put(key,value);
        }


        return handlerMap;

    }

    public static void main(String[] args) throws Exception
    {
        ConfigUtils configUtils = new ConfigUtils();
        String path = configUtils.getClass().getResource("/").getPath()+"WEB-INF/web.xml";

        System.out.println("path="+path);
        Map<String,String> handlerMap = configUtils.getClassName(path);
        handlerMap.forEach((key,value)->{System.out.println("key="+key+",value="+value);});
    }
}
