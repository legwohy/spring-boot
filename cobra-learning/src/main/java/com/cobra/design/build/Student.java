package com.cobra.design.build;

import java.util.Date;

/**
 * 多个构造器参数时可以考虑此种模式
 * 构造器模式
 *  1、构造器私有
 *  2、通过构造器实例化
 *  3、相当于注释
 *  4、不允许set方法修改参数
 *
 * @auther: leigang
 * @date: 2019/1/22 11:59
 * @description:
 */
public class Student
{
    /**
     * final 保证线程安全
     */
    private final String name;
    private final String gender;
    private final String address;
    private final  Date date;

    /**
     * 私有化构造并通过builder对象初始化参数
     */
    private Student(Builder builder)
    {
        name = builder.name;
        gender = builder.gender;
        address = builder.address;
        date = builder.date;
    }

    public String getName()
    {
        return name;
    }

    public String getGender()
    {
        return gender;
    }

    public String getAddress()
    {
        return address;
    }

    public Date getDate()
    {
        return date;
    }

    public static class Builder
    {
        /**
         * 必要参数设置为final类型
         */
        private final String name;
        /**
         * 非必要参数
         */
        private String gender;
        private String address;
        private Date date;

        /**
         * 构造传入必须的参数
         * @param name
         */
        public Builder(String name)
        {
            // this 是指内部类builder
            // Student.this.name;指外部类
           this.name = name;
        }

        public Builder gender(String gender)
        {
            this.gender = gender;
            return this;
        }
        public Builder address(String address)
        {
            this.address = address;
            return this;
        }

        public Builder date(Date date)
        {
            this.date = date;
            return this;
        }

        /**
         * 实例化外部类
         * @return
         */
        public Student builder()
        {
            return new Student(this);
        }

    }
}

class MainClass
{
    public static void main(String[] args)
    {
        Student student = new Student.Builder("super")
                        .gender("female")
                        .address("LA")
                        .date(new Date())
                        .builder();

        System.out.println(student.getName()+","+student.getGender()+","+student.getAddress()+student.getDate());

    }

}
