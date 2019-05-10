package com.cobra.thread;

/**
 * 斐波拉契散列  均匀分散 threadLocalMap
 */
public class MainClass
{
    private static final int  HASH_INCREMENT = 0x61c88647;
    public static void main(String[] args)
    {
        magicHash(16);

    }
    private static void magicHash(int size)
    {
        int hashCode = 0;
        for (int i = 0;i<size;i++){
            hashCode = i*HASH_INCREMENT+HASH_INCREMENT;
            System.out.print((hashCode&(size-1))+" ");
        }
        System.out.println();

    }
}
