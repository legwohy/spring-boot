package com.cobra.jvm.inst;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * JDK5提供
 * premain代理必须满足条件
 *      1、代理类必须提供premain方法
 *      2、必须在manifest指定代理jar Premain-class:代理类全类名
 *      3、代理类必须在代理程序启动之前启动
 */
public class AgentForPreAgent {
    /**
     *
     * @param args 自定义传入的代理参数 String类型
     * @param inst vm自动传入的实例 inst实力可以自定义用于转换类的行为
     */
    public static void premain(String args, Instrumentation inst) {
        System.out.println("running agent");

        inst.addTransformer(new TestTransformer());

    }

    /**
     *
     * 对字节码文件解析和组装
     */
    private static class TestTransformer implements ClassFileTransformer {
        /**
         *
         * ClassFileTransformer 只有一个方法
         *
         * @param loader 类加载器
         * @param arg4 参数名
         * @param classBeingRedefined 字节码流
         * @param protectionDomain
         * @param classfileBuffer
         * @return byte[] 返回被转换后的字节码流
         * @throws IllegalClassFormatException
         */
        @Override
        public byte[] transform
                (
                    ClassLoader loader, // 类加载器
                    String arg4, // 参数
                    Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain,
                    byte[] classfileBuffer
                )
                throws IllegalClassFormatException
        {
            ClassReader cr = null;
            try {cr = new ClassReader(arg4);} catch (IOException e) {e.printStackTrace();}

            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            for (Object obj : cn.methods) {
                MethodNode md = (MethodNode) obj;
                if ("<init>".endsWith(md.name) || "<clinit>".equals(md.name)) {
                    continue;
                }
                InsnList insns = md.instructions;
                InsnList il = new InsnList();
                il.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System",
                        "out", "Ljava/io/PrintStream;"));
                il.add(new LdcInsnNode("Enter method-> " + cn.name+"."+md.name));
                il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                        "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
                insns.insert(il);
                md.maxStack += 3;

            }
            ClassWriter cw = new ClassWriter(0);
            cn.accept(cw);


            return cw.toByteArray();
        }
    }
}
