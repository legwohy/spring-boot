package com.cobra.basic;

import lombok.Data;

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
        // 这里调用以实际入参为准 与(Fp) 无关
        father.test((Fp) sp);


    }


}

class Father{
    public void test(Fp fp){
        System.out.println("F+"+fp.toString());
    }
}

class Son extends Father{
    @Override
    public void test(Fp spp) {
        // toString方法是不会打印继承类的属性的 这里的Fp只是形式 可以传 Fp的子类 最终以实际传入的为准
        System.out.println("S="+spp.toString()+","+spp.getId());
    }
}

@Data
class Fp{
    private Integer id;
}
@Data
class Sp extends Fp{
    private String name;
}

