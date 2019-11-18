package com.cobra.design.listener;

/**
 * 监听测试
 * <a link='https://segmentfault.com/a/1190000013240470'/>
 *
 */
public class MainListenTest {

    public static void main(String[] args) {

        Person person = new Person();

        //注册监听器()
        person.registerLister(new PersonListener() {
            @Override
            public void doEat(Event event) {
                Person person1 = event.getResource();
                System.out.println(person1 + "正在吃饭呢！");
            }

            @Override
            public void doSleep(Event event) {
                Person person1 = event.getResource();
                System.out.println(person1 + "正在睡觉呢！");
            }
        });


        //当调用eat方法时，触发事件，将事件对象传递给监听器，最后监听器获得事件源，对事件源进行操作
        person.Eat();
    }


}
