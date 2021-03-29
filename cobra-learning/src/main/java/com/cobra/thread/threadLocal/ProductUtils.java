package com.cobra.thread.threadLocal;


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
