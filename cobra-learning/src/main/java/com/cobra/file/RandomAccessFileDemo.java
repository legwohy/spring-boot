package com.cobra.file;

import org.junit.Test;

import java.io.RandomAccessFile;

/**
 * RandomAccessFile 随机操作文件位置的类
 */
public class RandomAccessFileDemo {

    @Test
    public void writeByte() throws Exception {
        RandomAccessFile file = new RandomAccessFile("temp.txt", "rw");
        file.write(97);// a
        file.close();

    }

    @Test
    public void writeString() throws Exception {
        // 会直接覆盖上文的文件
        RandomAccessFile file = new RandomAccessFile("temp.txt", "rw");
        file.write("ABCDEFGH".getBytes());
        file.close();

    }
    @Test
    public void writeStringWithOffset() throws Exception {
        // 会直接覆盖上文的文件
        RandomAccessFile file = new RandomAccessFile("temp.txt", "rw");
        // 指定下标开始写 写入多少个长度 注意这不是插入 它会覆盖源位置的数据
        file.write("12345677".getBytes(),1,3);
        System.out.println("length:"+file.length());
        file.close();

    }

    public static void main(String[] args) {
        byte[] bytes = new byte[3];

        System.out.println("length:"+bytes.length);
    }
}
