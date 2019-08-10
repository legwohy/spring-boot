package com.cobra.jvm.clsloader;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * findClass 用户自定义加载类
 * loadClass 双亲加载模型
 * 一般推荐使用findClass加载类 因为findClass是符合双亲委托加载模型的 保证相同限定类型的名不会加载到jvm中
 * 而loadClass可能造成包名重复
 * classNotFoundExcrption 编译时找不到类
 * NoClassDefFoundError 运行时找不到类
 */
public class CompileClassLoader extends ClassLoader
{
    private String fileRootPath;
    private  static final  String class_suffix = ".class";
    private  static final  String java_suffix = ".java";

    public CompileClassLoader(String fileRootPath)
    {
        this.fileRootPath = fileRootPath;
    }


    /**
     * .java文件与.class文件在同一文件下
     * 1、加载.class文件 若.class文件不存在 则编译.java文件
     * 2、将.class文件转换为class对象
     * 3、通过反射调用方法
     * @param className 全类名
     * @return
     */
    @Override
    protected Class<?> findClass(String className)
    {
        String newClassName = className;
        System.out.println("查找类【"+className+"】开始。。。");
        Class clazz = null;

        //将包路径中的点（.）替换成斜线（/）。
        newClassName = newClassName.replace(".", "/");
        String javaFilename =  fileRootPath + newClassName +java_suffix;
        String classFilename =  fileRootPath + newClassName +class_suffix;
        File javaFile = new File(javaFilename);
        File classFile = new File(classFilename);

        try
        {
            // 编译文件 .java --> .class
            if (javaFile.exists() && (!classFile.exists()) || javaFile.lastModified() > classFile.lastModified())
            {
                // 如果编译失败，或者该class文件不存在
                if (!compile(className) || !classFile.exists())
                {
                   throw new CompilerException("编译失败：\t"+className);
                }
            }

            // .class --> class对象
            if (classFile.exists())
            {
                byte[] raw = getBytes(classFilename);
                clazz = defineClass(className, raw, 0, raw.length);
            }

            if(null == clazz)
            {
                throw new ClassNotFoundException(className);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return clazz;

    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        return super.loadClass(name, resolve);
    }

    /**
     * 编译java文件
     * javac .java-->.class文件
     * @param clazzName 全类名
     * @return
     * @throws IOException
     */
    private boolean compile(String clazzName) throws IOException
    {

        clazzName = clazzName.replace(".",File.separator);
        System.out.println("CompileClassLoader正在编译\t" + fileRootPath+clazzName + "...");
        // 调用系统的javac命令
        Process p = Runtime.getRuntime().exec("javac " + fileRootPath+clazzName+java_suffix);
        //其他的线程都在等待这个线程完成
        try
        {
            p.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.out.println("编译错误:" + e.getMessage());
        }
        //获取javac线程的退出值
        int ret = p.exitValue();

        System.out.println("CompileClassLoader编译结束\t" + clazzName );
        return ret == 0;

    }

    /**
     * .class二进制文件转换为class对象
     * @param clazzName 全类名
     *                  fileRootPath 必须是.class文件所在的根目录(不含包名)
     * @return
     */
    private Class<?> getClassObject(String clazzName) throws IOException
    {
        String newClazzName = clazzName;
        newClazzName = dotToSeparator(newClazzName);

        String fileName = fileRootPath+newClazzName+class_suffix;
        System.out.println(".class文件转class对象【"+fileName+"】开始...");

        byte[] raw = getBytes(fileName);

        System.out.println(".class文件转class对象结束");
        return defineClass(clazzName,raw,0,raw.length);

    }

    private String dotToSeparator(String name)
    {
        return name.replace(".",File.separator);
    }

    /**
     * 读取一个文件的内容
     * @param filename
     * @return
     * @throws IOException
     */
    private byte[] getBytes(String filename) throws IOException
    {
        File file = new File(filename);
        long len = file.length();
        byte[] raw = new byte[(int)len];
        FileInputStream fin = new FileInputStream(file);
        //一次读取class文件的全部二进制数据
        int r = fin.read(raw);
        if (r != len)
        {
            throw new IOException("无法读取全部文件：" + r + " != " + len);
        }
        fin.close();
        return raw;
    }



    public static void main(String[] args) throws Exception
    {

        String rootPath = "F:\\workspqce_idea\\cobra\\cobra-learning\\src\\main\\java\\";
        CompileClassLoader loader = new CompileClassLoader(rootPath);
        Class clazz = loader.findClass("com.cobra.jvm.clsloader.Hello");


       //loader.compile("com.ihome.HelloWorld");// 编译


        // Class clazz = loader.getClassObject("com.ihome.HelloWorld");// .class转class对象
        Method method = clazz.getMethod("test",null);
        method.invoke(clazz.newInstance(),null);
    }



}
