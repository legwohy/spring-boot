package com.cobra.jvm.clsloader;


import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;
import java.lang.reflect.Method;

public class FileClassLoader extends ClassLoader {
    private String rootDir;

    public FileClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * TODO 注意 这里是重写 findClass 方法 而不是 loadClass
     * findClass 查找二进制文件的位置并加载里面
     * @param name 全类名
     */
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // 1、获取.class二进制文件
        byte[] bytes = loadDataByName(name);
        if (null == bytes) {
            throw new ClassNotFoundException("未读取到数据");
        }

        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] loadDataByName(String name) {
        String path = classNameToPath(name);
        System.out.println("文件路径:"+path);
        try (InputStream is = new FileInputStream(path);
             ByteOutputStream bos = new ByteOutputStream()) {
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer)) != -1) {
                bos.write(buffer,0,read);
            }

            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String classNameToPath(String name) {
        return rootDir + File.separatorChar + name.replace('.', File.separatorChar) + ".class";
    }

    public static void main(String[] args) throws Exception {
        // 直接调用.clazz文件
        callClazz();
    }

    private static void callClazz()throws Exception{
        String rootDir = FileClassLoader.class.getResource("/").getFile();
        FileClassLoader loader = new FileClassLoader(rootDir);
        String name = "com.cobra.jvm.clsloader.TestObj";
        //TODO 这里调用loadClass非findClass
        // loadClass 双亲模型加载
        Class clazz = loader.loadClass(name);
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod("test",null);
        method.invoke(obj,null);

    }


}
