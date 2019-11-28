package com.cobra.jvm.definclsloader;


import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 为什么要自定义类加载器
 *  1、类不在classpath下
 *  2、实现热部署（相同的class文件通过不同的类加载器产生不同的class对象）
 *  3、网络传输过程中class文件需要解密才能加载到jvm中
 *
 * 自定义类加载器 只需要复写findClass(name)和findResource(name)
 */
public class FileClassLoader extends ClassLoader
{
    private String rootDir;
    public FileClassLoader(String rootDir){

        this.rootDir = rootDir;
    }
    /**
     * 通过全类名查找类
     * @param name 全类名
     */
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException
    {
        // 1、class文件转换为二进制文件
        byte[] bytes = loadByDataNew(name);
        if(null == bytes){
            throw new ClassNotFoundException();
        }

        // 2、通过此defineClass方法加载二进制文件转换为class对象
        return defineClass(name,bytes,0,bytes.length);
    }

    private byte[] loadByDataNew(String name)
    {
        // 1、查找class文件
        String path = this.getClassFile(name);
        // 2、读取文件
        try(
                        InputStream fis = new FileInputStream(new File(path));
                        ByteArrayOutputStream bas = new ByteArrayOutputStream()
        )
        {
            // 每次读取1k
            byte[] bytes = new byte[1024];
            int byteNameRead = 0;
            while ((byteNameRead = fis.read(bytes)) != -1){
                bas.write(bytes,0,byteNameRead);
            }

            return bas.toByteArray();

        }catch (IOException e){
            e.printStackTrace();
        }


        return null;
    }

    private String getClassFile(String name){
        // com.cobra.Test ==>> com/cobra/Test.class
        return rootDir+File.separatorChar+name.replace('.',File.separatorChar)+".class";
    }

    public static void main(String[] args) throws Exception
    {
        String rootDir = "F:\\testClsLoader";
        FileClassLoader loader = new FileClassLoader(rootDir);
        Class<?> clazz = loader.loadClass("com.test.cls.TestObj");
        Method main = clazz.getDeclaredMethod("print", null);
        main.invoke(clazz.newInstance(), null);


    }
}
