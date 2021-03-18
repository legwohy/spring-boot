package com.cobra.util;

import lombok.extern.slf4j.Slf4j;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author
 *
 */
@Slf4j
public class Serial {
    // 最大数字
    private static final int MAX = 999999;
    // 数字长度
    private static final int NUMLEN = 6;

    private static Serial instance = null;
    private String lastTime = "";
    private int lastNo = 0;
    private int baseNum = 1000000;
    // 表示实时流水
    private static final int serialCat = 10;
    // 生成序列长度
    private static final int serialLen_30 = 30;
    private static final int serialLen_32 = 32;

    private static final Lock lock = new ReentrantLock();

    private static String  address = "0.0.0.0";

    public Serial() {

    }

    private static String getSerialStringByNum(int base, int value) {
        String num = new Integer(base + value).toString();

        return num.substring(1);
    }

    /**
     * 根据当前时间产生一个新的序列号
     *
     * @param sourceId
     * @return
     * @throws Exception
     */
    public static  String genSerialNo_30(String sourceId)
            throws RuntimeException {

        // synchronized (this) {
        lock.lock();
        try {

            Date curTime = new Date();
            SimpleDateFormat s = new SimpleDateFormat("yyMMddHHmmss");

            if (instance == null) {
                instance = new Serial();
                instance.lastNo = 0;
                instance.baseNum = (int) Math.pow(10, NUMLEN);
                instance.lastTime = s.format(curTime);
            }
            String now = s.format(curTime);
            // 前面添加主机ip地址最后3位
            // now = HostMsgCache.getHostIP4Part() + now;
            if (now.compareTo(instance.lastTime) > 0) {
                // 当前时间大于上一次记录时间，表示可以开始新的计数
                instance.lastNo = 1;
                instance.lastTime = now;
                return jointRule_30(sourceId, now);
            }

            if (now.compareTo(instance.lastTime) < 0) {
                // 当前时间小于上一次的记录时间，表示上一秒有超过10000个流水号生成
                now = instance.lastTime;
            }

            int serialNos = instance.lastNo + 1;
            if (serialNos <= MAX) {
                // 当前没有超过最大允许流水号，返回上一个流水号+1作为新的流水号
                instance.lastNo = serialNos;
                return jointRule_30(sourceId, now);
            }

            Date last = s.parse(now);
            Calendar cal = Calendar.getInstance();
            cal.setTime(last);
            cal.add(Calendar.SECOND, 1);
            Date endTime = cal.getTime();
            String endStr = s.format(endTime);
            instance.lastNo = 1;
            instance.lastTime = endStr;
            return jointRule_30(sourceId, endStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            lock.unlock();
        }
        // }
    }


    /**
     * 根据当前时间产生一个内部操作流水号
     * yyMMddHHmmss+
     *
     * @param sourceId
     * @return
     * @throws Exception
     */
    public static String genSerialNo_32(String sourceId) throws RuntimeException {
//		 synchronized (this) {
        lock.lock();
        try {
            Date curTime = new Date();
            SimpleDateFormat s = new SimpleDateFormat("yyMMddHHmmss");

            if (instance == null) {
                instance = new Serial();
                instance.lastNo = 0;
                instance.baseNum = (int) Math.pow(10, NUMLEN);
                instance.lastTime = s.format(curTime);
            }
            String now = s.format(curTime);
            // 前面添加主机ip地址最后3位
            // now = HostMsgCache.getHostIP4Part() + now;
            if (now.compareTo(instance.lastTime) > 0) {
                // 当前时间大于上一次记录时间，表示可以开始新的计数
                instance.lastNo = 1;
                instance.lastTime = now;
                return jointRule_32(sourceId, now);
            }

            if (now.compareTo(instance.lastTime) < 0) {
                // 当前时间小于上一次的记录时间，表示上一秒有超过10000个流水号生成
                now = instance.lastTime;
            }

            int serialNo = instance.lastNo + 1;
            if (serialNo <= MAX) {
                // 当前没有超过最大允许流水号，返回上一个流水号+1作为新的流水号
                instance.lastNo = serialNo;
                return jointRule_32(sourceId, now);
            }

            Date last = s.parse(now);
            Calendar cal = Calendar.getInstance();
            cal.setTime(last);
            cal.add(Calendar.SECOND, 1);
            Date endTime = cal.getTime();
            String endStr = s.format(endTime);
            instance.lastNo = 1;
            instance.lastTime = endStr;
            return jointRule_32(sourceId, endStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }finally {
            lock.unlock();
        }

//		 }
    }

    public static String jointRule_30(String sourceId, String now) throws RuntimeException {
        return generateSerial(sourceId, now,serialLen_30);
    }

    public static String jointRule_32(String sourceId, String now) throws RuntimeException {
        return generateSerial(sourceId, now,serialLen_32);
    }

    private static String generateSerial(String sourceId, String now,int length){
        String addr;
        try {
            addr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
//			throw new RuntimeException(e);
            log.error("", e);
            addr = address;
        }
        String hostIP = addr.split("\\.")[2] + addr.split("\\.")[3];
        if (null == sourceId || "".equals(sourceId)) {
            sourceId = String.valueOf(serialCat);
        }
        String str = sourceId + now
                + getSerialStringByNum(instance.baseNum, instance.lastNo)
                + hostIP;
        int len = length - str.length();

        str = str + generateRandomString(len);
        return str;
    }

    public static String generateRandomString(int len) {
        final char[] mm = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9' };

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            sb.append(mm[random.nextInt(mm.length)]);
        }
        return sb.toString();

    }

    public static  String genSerialNo(String sourceId){
        return genSerialNo_30(sourceId);
    }
    public static String genSerialNos(String sourceId){
        return genSerialNo_32(sourceId);
    }

    public static void main(String[] args) throws Exception {
        String addr = InetAddress.getLocalHost().getHostAddress();
        System.out.println(addr);
    }


}

