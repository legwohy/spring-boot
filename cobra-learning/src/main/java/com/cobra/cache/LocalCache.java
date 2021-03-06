package com.cobra.cache;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 * 1、缓存对象的序列化处理
 * 2、缓存的存前检测容量
 * 3、缓存的读取
 * 4、缓存的清理(异步线程)
 */
public class LocalCache {
    /** 默认容量 */
    private static int DEFAULT_CAPACITY = 2 << 9;
    /** 最大容量*/
    private static int MAX_CAPACITY = 2 << 20;
    /** 刷新频率 2秒*/
    private static int MONITOR_DURATION_SEC = 2;

    /** 默认容器*/
    private static ConcurrentHashMap<String, CacheEntity> cacheMap = new ConcurrentHashMap<String, CacheEntity>(DEFAULT_CAPACITY);

    public Boolean putValue(String key, Object value, int expireTime){
        return putCloneValue(key, value, expireTime);
    }

    /**
     * 从本地缓存中读取key对应的值
     * @param key
     * @return
     */
    public Object getValue(String key){
        Object obj = cacheMap.get(key);
        if(null == obj){
            return null;
        }
        return cacheMap.get(key).getValue();
    }

    /** 情况缓存*/
    public void clear(){
        cacheMap.clear();
    }

    static{
        // 异步线程 清理过期的废物
        new Thread(new TimeoutTimerThread()).start();
    }

    /** 过期线程*/
    static class TimeoutTimerThread implements Runnable {

        @Override
        public void run(){
            System.out.println("Cache monitor start");
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(MONITOR_DURATION_SEC);// TimeUnit 并发包

                    checkTime();
                } catch (Exception e) {
                    System.out.println("Cache monitor end");
                    throw new RuntimeException("监控异常 退出");

                }
            }


        }
    }

    /** 过期缓存处理*/
    private static void checkTime() throws Exception{
        // 时间比较
        for (String key : cacheMap.keySet()) {
            CacheEntity entity = cacheMap.get(key);
            // 当前时间-修改时间  与过期时间比较
            Long timeOut = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()- entity.getGmtModify()) ;

            if (entity.getExpireTime() > timeOut) {
                continue;
            }
            System.out.println("清除过期缓存key=" + key);
            cacheMap.remove(key);
        }

    }

    /**
     * 深复制 序列化处理 对象可以随便改
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    private Boolean putCloneValue(String key, Object value, int expireTime){
        try {
            // 容量校验
            if (cacheMap.size() > MAX_CAPACITY) {
                return false;
            }

            // 赋值 对象序列化处理后解决对象的值引用
            CacheEntity cacheClone = clone(new CacheEntity(value, System.nanoTime(), expireTime));
            cacheMap.put(key, cacheClone);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 序列化克隆对象
     * 对象流 只能将序列化过的对象写入流中
     * ObjectOutputStream 将java对象写入OutputStream
     * ObjectInputStream 读取java对象
     * 对象---流---对象
     * */
    @SuppressWarnings({"unchecked"})
    private <T extends Serializable> T clone(T object){
        T cloneObject = null;

        try {
            // 临时字节流 可以使用文件流(FileOutputStream)代替
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);// 将对象输出到流中 或写到文件中
            oos.close();

            // 从临时字节流中读取数据 可以只用文件输入流(FileInputStream)代替
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            try {
                cloneObject = (T)ois.readObject();// 将输入流转换为对象
                ois.close();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("类找不到\r\n" + e);
            }

        } catch (IOException e) {
            throw new RuntimeException("io异常\r\n" + e);
        }

        return cloneObject;
    }


    private static class CacheEntity implements Serializable {
        /** 序列化和反序列化时，若版本不一致 将无法反序列化 读取对象*/
        private static final long serialVersionUID = 27654135929937151L;

        /** 缓存的值*/
        private Object value;

        /** 当前时间戳 纳秒 */
        private Long gmtModify;

        /** 过期时间 单位秒*/
        private int expireTime;

        public Object getValue(){
            return value;
        }

        public void setValue(Object value){
            this.value = value;
        }

        public Long getGmtModify(){
            return gmtModify;
        }

        public void setGmtModify(Long gmtModify){
            this.gmtModify = gmtModify;
        }

        public int getExpireTime(){
            return expireTime;
        }

        public void setExpireTime(int expireTime){
            this.expireTime = expireTime;
        }

        public CacheEntity(Object value, Long gmtModify, int expireTime){
            this.value = value;
            this.gmtModify = gmtModify;
            this.expireTime = expireTime;
        }
    }

}
