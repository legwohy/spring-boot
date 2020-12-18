package com.cobra.jvm.clsloader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 1、编译 .java  -- .class  (手动)
 * 2、加载.class 为class对象
 * 3、实例化class对象
 *
 * @author admin
 * @date 2020/12/18 11:17
 * @desc 类加载
 */
public class CompileClassLoaderTest {

    CompileClassLoader loader = null;
    String _rootPath = "D:\\code_repository\\cobra\\cobra-learning\\src\\main\\java\\";
    String _classFile = "D:\\code_repository\\cobra\\cobra-learning\\src\\main\\java\\com\\cobra\\jvm\\clsloader\\Hello.class";
    String className = "com.cobra.jvm.clsloader.Hello";

    @Before
    public void init(){
        loader = new CompileClassLoader(_rootPath);
    }

    /**
     * 编译文件
     * @throws Exception
     */
    @Test
    public void compile() throws Exception{
        File file = new File(_classFile);
        if (file.exists()) {
            file.delete();
        }

        loader.compile(className);// 编译

        Assert.assertTrue(new File(_classFile).exists());
    }

    @Test
    public void findClass()throws Exception{
        Class<?> clazz = loader.findClass(className);

        Method method = clazz.getMethod("test", null);
        method.invoke(clazz.newInstance(), null);
    }



}