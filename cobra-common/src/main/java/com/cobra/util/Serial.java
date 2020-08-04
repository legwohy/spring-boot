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
     *
     * @param sourceId
     * @return
     * @throws Exception
     */
    public static String genSerialNo_32(String sourceId)
            throws RuntimeException {
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

    public static String jointRule_30(String sourceId, String now)
            throws RuntimeException {
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
        int len = serialLen_30 - str.length();
        str = str + generateRandomString(len);
        return str;
    }

    public static String jointRule_32(String sourceId, String now)
            throws RuntimeException {
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
        int len = serialLen_32 - str.length();

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

    /**
     * 左补全sequence 第一位1 其他位为0
     *
     * @param seq
     * @param fixLen
     * @return
     */
    public static Long padSeq(Long seq, int fixLen) {
        if (seq == null || seq == 0) {
            return null;
        }
        int len = seq.toString().length();

        if (len >= fixLen) {
            return seq;
        } else if (len == fixLen + 1) {
            return Long.valueOf("1".concat(seq.toString()));
        } else {
            String revSeq = "1"
                    + StringCommonUtils.fillLeft(seq.toString(), '0', fixLen - 1);
            return Long.valueOf(revSeq);
        }
    }

    public static void main(String[] args) throws Exception {
        String addr = InetAddress.getLocalHost().getHostAddress();
        System.out.println(addr);
    }


    /**
     * 获取随机数(包括字母,数字,或特殊字符)
     * @param len
     * @return
     */
    public static String getRandom(int len){

        final char[] num = new char[] {'0','1','2','3','4','5','6','7','8','9'};
        final char[] charLow = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        final char[] charUpp = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        final char[] sign = new char[]{'!','@','#','$','&','?'};

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            boolean flag = random.nextInt(2) % 2 == 0;
            if(flag){
                boolean choice = random.nextInt(2) % 2 == 0;
                if(choice){
                    sb.append(charLow[random.nextInt(charLow.length)]);
                }else{
                    sb.append(charUpp[random.nextInt(charUpp.length)]);
                }
            }else{
                boolean choice = random.nextInt(2) % 2 == 0;
                if(choice){
                    sb.append(num[random.nextInt(num.length)]);
                }else{
                    sb.append(sign[random.nextInt(sign.length)]);
                }
            }
        }
        return sb.toString();
    }


    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}

