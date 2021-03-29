package com.cobra.other;

import java.lang.ref.WeakReference;

/**
 * @auther: admin
 * @date: 2018/12/29 11:46
 * @description:而SoftReference

就是和WeakReference有一点点的不同，SoftReference比较大方，
在内存快用尽的时候才会回收这个对象。而Weak会很快就回收，
强引用是就算内存不足了，也不会回收，这就是他的危险之处
 */
public class WeakReferenceMainClass
{
    public static void main(String[] args)
    {
        Car car = new Car(1.12D,"silever");

        WeakReference<Car> carWeakReference = new WeakReference<Car>(car);

        int i = 0;
        while (true){
            //System.out.println("strong reference running..."+car);
            if(null != carWeakReference.get())
            {
                i++;
                System.out.println("weakReference running\t"+i+",\t weak:"+carWeakReference.get());

            }else {
                System.out.println("gc collect weakReference...");
                break;
            }
        }

    }
}

class Car{
    private Double price;
    private String color;
    public Car(){}
    public Car(Double price,String color){
        this.price = price;
        this.color = color;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }
}
