package com.cobra.file;


import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * https://zhuanlan.zhihu.com/p/30669938
 * 文件的随机访问操作类
 * RandomAccessFile(path,mode) path 文件路径 mode r只读 rw读写
 */
public class DownLoader {
    String url;
    int num;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNum(int num) {
        this.num = num;
    }

    /**
     * 获取响应数据大小
     * @param url 网络地址
     * @return 文件名称、文件大小
     */
    public ArrayList getResponseDataLength(String url) throws Exception {
        ArrayList<Object> arr = new ArrayList<Object>();
        String[] names = url.split("/");
        String name = names[names.length - 1];
        URL urls = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
        int length = conn.getContentLength();

        arr.add(name);
        arr.add(length - 1);

        return arr;
    }

    /**
     * 拆解文件
     * 文件大小为5 拆分为三部分
     * 5/2 = 【2】+1
     * 0，2，5
     *
     * 文件大小为6 拆分为四部分
     * 6/2 = 【3】+1
     * 0，2，4，6
     *
     * @param length 文件总大小
     * @param mun 线程数
     */
    public int[] getIndex(int length, int mun) {
        int index = length / mun;
        int[] a = new int[mun + 1];
        for (int i = 0; i <= mun; i++) {
            if (i == (mun)) {
                a[i] = length;
            } else {
                a[i] = index * i;
            }
        }
        return a;
    }

    /**
     * 多线程下载
     * @param mun 线程数
     * @param url 地址
     * @throws Exception
     */
    public void mut_thread_donwloader(int mun, String url) throws Exception {
        setNum(mun);
        setUrl(url);

        // 1、响应数据的大小
        ArrayList arr = getResponseDataLength(this.url);
        String name = (String) arr.get(0);
        int length = (int) arr.get(1);

        // 2、根据线程数拆分数据
        int[] index = getIndex(length, this.num);
        System.out.println("开始下载" + name);

        // 3、先在磁盘中建立空文件 设置文件名、文件读写权限以及文件大小 rw:可读可写
        RandomAccessFile raf = new RandomAccessFile(name, "rw");
        raf.setLength(length);
        raf.close();

        // 4、开启线程写入数据
        for (int i = 0; i < this.num; i++) {
            new Thread(new WorkThread(index[i] + 1, index[i + 1], this.url, name)).start();
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "http://c.hiphotos.baidu.com/image/pic/item/060828381f30e924aa20564a46086e061c95f7c1.jpg";
        DownLoader m = new DownLoader();
        m.mut_thread_donwloader(5, url); //这里我设置为5线程下载图片 基本秒下
    }
}

class WorkThread implements Runnable {
    //private RandomAccessFile raf;
    private int start, end;
    private String url;
    private URL urls;
    private String path;
    int buf = 1;
    int length;

    /**
     *
     * [start,end) --> [0,200) [201,300)
     * @param start 开始位置
     * @param end   结束位置
     * @param url
     * @param path
     */
    public WorkThread(int start, int end, String url, String path) {
        this.path = path;
        this.start = start;
        this.end = end;
        this.url = url;
    }

    @Override
    public void run() {
        if (start == 1) {
            start = 0;
        }
        System.out.println(start + "...." + end);
        try {
            //raf = new RandomAccessFile(path, "rwd");
            urls = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setRequestProperty("Range", "bytes=" + start + "-" + end);// 断点传续
            if (conn.getResponseCode() == 206) {// 206 服务器成功处理了部分get内容
                InputStream is = conn.getInputStream();
                byte[] b = new byte[buf];

                // FileChannel只能通过get方法获取 不能直接new
                FileChannel fileChannel = new RandomAccessFile(path, "rw").getChannel();

                // position类似seek方法 指定文件光标位置 文件位置的定位
                fileChannel.position(start);

                // 缓冲区:在内存中预留指定大小的存储空间来对数据的存储
                // 减小实际物理的读写次数(缓冲区可伸缩)、减小动态分配和回收内存的次数
                // 设置缓冲区大小 一般以 1024为单位
                // NIO中缓冲区直接为channel服务
                // allocate 从堆空间中分配 allocateDirect不使用堆栈分配 使用操作系统的内存 缓冲区大且常用
                // wrap 将数据放在byte数组中
                ByteBuffer bb = ByteBuffer.allocateDirect(buf * 1024);
                length = end - start;

                // 在文件中将一段区域锁定 true/false 是否共享
                fileChannel.lock(start, length, false);
                while (length - (b.length * 1024) > 0 && is.read(b) != -1) {
                    length = length - b.length * 1024;
                    bb.flip();// 通知缓存 让其进入激活状态
                    fileChannel.write(bb.wrap(b));
                    bb.clear();// 清空缓存 将buffer内的指针回到0
                }
                // 最后读的数据小于缓存的数据
                ByteBuffer bb2 = ByteBuffer.allocateDirect(length);
                while (is.read(b) != -1) {
                    bb2.flip();
                    fileChannel.write(bb2.wrap(b));
                    bb2.clear();
                }
                System.out.println(Thread.currentThread() + "下载结束");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}