package com.cobra.script.java;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 动态加载
 * @date 2021/3/12 16:13
 * @desc
 */
public class DynamsticClassLoader {
    private ClassLoader loader;

    /**
     * jar包路径
     * @see URLClassLoader 动态加载类
     * @param jarPath
     * @throws Exception
     */
    public DynamsticClassLoader(String jarPath) throws Exception{
        File file = new File(jarPath);
        loader = new URLClassLoader(new URL[] {file.toURL()});
    }

    public Object invoke(String clazzName, String methodName, String[] parmaters) throws Exception{
        Class clazz = loader.loadClass(clazzName);
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod(methodName, String.class);
        return method.invoke(obj, parmaters[0]);
    }

    public static void main(String[] args) throws Exception{
        String jarPath = "D:\\code_repository\\cobra\\cobra-learning\\src\\lib\\shangtang-1.0.jar";
        DynamsticClassLoader callJar = new DynamsticClassLoader(jarPath);
        System.out.println(callJar.invoke("com.xue.GenerateString", "genOriString", new String[] {"test"}));
    }
}