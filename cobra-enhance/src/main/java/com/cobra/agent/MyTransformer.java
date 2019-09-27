package com.cobra.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 *
 <dependency>
     <groupId>javassist</groupId>
     <artifactId>javassist</artifactId>
     <version>3.12.1.GA</version>
 </dependency
 *
 */
public class MyTransformer implements ClassFileTransformer {
    /**
     *
     * @param loader 加载器
     * @param className
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("加载器:"+loader.toString());
        System.out.println("正在加载类："+ className);
        if (!"com/cobra/Person".equals(className)){
            return classfileBuffer;
        }

        CtClass cl = null;
        try {
            // 1、获取加载类池
            ClassPool classPool = ClassPool.getDefault();
            // 2、从池中取出代理类
            cl = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            // 3、获取方法
            CtMethod ctMethod = cl.getDeclaredMethod("test");
            System.out.println("获取方法名称："+ ctMethod.getName());

            ctMethod.insertBefore("System.out.println(\" 动态插入的打印语句 \");");
            // $_ 是 javassist 特定符
            ctMethod.insertAfter("System.out.println($_);");

            byte[] transformed = cl.toBytecode();
            return transformed;
        }catch (Exception e){
            e.printStackTrace();

        }
        return classfileBuffer;
    }
}
