package com.cobra.inout;

/**
 * @auther: leigang
 * @date: 2018/12/28 14:16
 * @description:
 */
public class OuterClass
{
    private String name;


     public class InnerClass{
        private String id;
        public InnerClass(){
            System.out.println("内部类被调用"+name);
        }

    }
}
