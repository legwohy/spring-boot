package com.cobra.jvm.clsloader;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 本类 需提前编译为.class文件方可找得到
 */
class TestClassLoad {
    public static void test() {
        System.out.println("测试方法...OOOO");
    }
    @Override
    public String toString() {
        return "类加载成功。";
    }
}

public class PathClassLoader extends ClassLoader {
    private String classPath;

    public PathClassLoader(String classPath) {
        this.classPath = classPath;
    }

    /**
     *
     * @param name 全类名
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    /**
     * 1、读取 .class文件
     * 2、将.class文件转换为 byte[]
     * @param className 全类名
     * @return
     */
    private byte[] getData(String className) {
        String path = classPath + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
        try {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int num = 0;
            while ((num = is.read(buffer)) != -1) {
                stream.write(buffer, 0, num);
            }
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String args[]) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException ,NoSuchMethodException,InvocationTargetException{

        String path = PathClassLoader.class.getResource("/").getPath();
        ClassLoader pcl = new PathClassLoader(path);// 字节码(.class文件)文件所在的路径  不包含 包名
        Class c = pcl.loadClass("com.cobra.jvm.clsloader.TestClassLoad");//注意要包括包名
        Object obj = c.newInstance();// 实例化
        Method method = c.getMethod("test",null);// 查找方法
        method.invoke(obj,null);

    }


}
