package com.cobra.btrace;


import java.util.Random;

public class Calculator {

    public int add(int a, int b) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return a + b;
    }
}


 class BTraceDemo {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("add start...");
            System.out.println("结果:"+calculator.add(random.nextInt(10), random.nextInt(10)));
            System.out.println("add end...");
        }
    }
}
