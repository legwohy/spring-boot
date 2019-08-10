package com.cobra.springCore.resLoader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * spring 资源管理器
 *      URL 统一资源定位符
 *      classLoader（classPath） 根路径加载
 */
public class DefaultResourceLoaderDemo
{
    public static void main(String[] args) throws IOException
    {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        resourceLoader.addProtocolResolver((location,loader)->{
            return new ClassPathResource(location);
        });
        Resource resource = resourceLoader.getResource("applicationTest.yml");
        InputStream inputStream = resource.getInputStream();
        String content = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));

        System.out.println(content);
    }
}
