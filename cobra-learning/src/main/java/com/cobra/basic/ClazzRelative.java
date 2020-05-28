package com.cobra.basic;

import lombok.Data;

import java.util.Arrays;

/**
 * 类引用
 * 1、子类继承父类 子类的入参必须与父类入参完全一致(继承的入参)
 * 2、父类引用子类的方法 以最终的入参和实例化对象为主 与等号左边的或（）转换的为最终
 */
public class ClazzRelative {
    public static void main(String[] args) {
        Sp sp = new Sp();
        sp.setName("test");
        sp.setId(100);

        Father father = new Son();
        // 1、这里调用以实际入参为准 与(Fp) 无关
        // 2、是son调用 不是 father调用
        father.test((Fp) sp);


    }


}

class Father{
    private String f1;
    private String f2;
    public void test(Fp fp){
        System.out.println("F+"+fp.toString());
        // this指当前实际调用的类 这里是子类调用 所以打印子类的属性 s1和s2
        System.out.println(Arrays.toString(this.getClass().getDeclaredFields()));
    }
}

class Son extends Father{
    private String s1;
    private String s2;

    public void test2(Fp spp) {
        // toString方法是不会打印继承类的属性的 这里的Fp只是形式 可以传 Fp的子类 最终以实际传入的为准
        System.out.println("S="+spp.toString()+","+spp.getId());
    }
}

class Fp{
    private Integer id;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Fp{" +
                "id=" + id +
                '}';
    }
}
class Sp extends Fp{
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sp{" +
                "name='" + name + '\'' +
                '}';
    }
}

