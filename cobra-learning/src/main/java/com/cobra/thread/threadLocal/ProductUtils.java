package com.cobra.thread.threadLocal;

/**
 * 静态与非静态
 * @auther: leigang
 * @date: 2018/12/28 11:31
 * @description:
 */
public class ProductUtils
{
    private  static ThreadLocal<Product> threadLocal = new ThreadLocal<>();


    public  static void set(Product product)
    {
        threadLocal.set(product);
    }

    public  static Product get(){
        return threadLocal.get();
    }

}
