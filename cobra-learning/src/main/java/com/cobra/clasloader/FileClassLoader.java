package com.cobra.clasloader;


import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;

public class FileClassLoader extends ClassLoader {
    private String rootDir;

    public FileClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * @param name 全类名
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // 1、获取.class二进制文件
        byte[] bytes = loadDataByName(name);
        if (null == bytes) {
            throw new ClassNotFoundException("未读取到数据");
        }

        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] loadDataByName(String name) {
        String path = classNameToPath(name);
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
        String rootDir = "F:\\temp";
        FileClassLoader loader = new FileClassLoader(rootDir);
        Class clazz = loader.loadClass("com.test.TestObj");


        //System.out.println(clazz.newInstance());
    }


}
