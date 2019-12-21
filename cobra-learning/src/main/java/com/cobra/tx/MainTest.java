package com.cobra.tx;

import com.alibaba.druid.pool.DruidDataSource;
import com.cobra.tx.service.UserService;

/**
 *
 * <a link='https://www.jianshu.com/p/1becdc376f5d'/>
 * 同一个事务里面保证同一个连接
 */
public class MainTest
{
    static String driver = "com.mysql.jdbc.Driver";
    static String userName ="cobra";
    static String pwd = "t!Wi@haj*Sf0yd$L";
    static String url = "jdbc:mysql://192.168.109.224:3306/test-bank-db?";

    public static void main(String[] args)
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(userName);
        dataSource.setPassword(pwd);
        dataSource.setUrl(url);

        final UserService userService = new UserService(dataSource);

        for (int i = 0;i<10;i++){
            new Thread(()->{userService.action();}).start();
        }

        try {Thread.sleep(1000);}catch (InterruptedException e) {e.printStackTrace();}
    }
}
