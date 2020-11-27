package com.cobra;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {
    public static void main(String[] args){

        try {
            String runKey = "k";
            Map<String, Long> map = new HashMap<>();
            map.put("k", 100L);
            map.put("b", 20L);
            map.put("c", 20L);
            map.put("d", 20L);
               /* for (Map.Entry<String, Long> entry : map.entrySet()) {
                        map.remove(entry.getKey());
                }*/
//            System.out.println("map = " + map);
            Set<String> keySet = map.keySet();
            for (String mapKey : keySet) {
                // map.remove(mapKey);
            }
            String a = "1";
            String b = "1";
            String c = "1";
            String a1 = a.intern();
            String b1 = b.intern();
            String c1 = c.intern();
            List<String> arrList = new ArrayList<>();
            arrList.add(a1);
            arrList.add(b1);
            arrList.add(c1);

            List<String> newArr = new ArrayList<>(arrList);

            for (String s : newArr) {
                System.out.println(111);
                arrList.remove(s);
                arrList.add(s);

            }
           /* Iterator<String> iter = arrList.iterator();
            while (iter.hasNext()) {
                String param=iter.next();
                arrList.remove(param);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeLog(String path) throws Exception{

        Date now = new Date();
        //String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now);
        String date = "2020-10-23 17:05:14.003";
        File file = new File(path);
        String exe1 = "[" + date
                        + "][WARN][中文+CobraHttp555][org.springframework.boot.web.support.ErrorPageFilter][176][http-nio-8083-exec-2]  call [org.springframework.boot.web.support.ErrorPageFilter][forwardToErrorPage] TxId:[] SpanId:[] Forwarding to error page from request [/] due to exception [-1]\n"
                        + "java.lang.ArrayIndexOutOfBoundsException: -1\n"
                        + "\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n"
                        + "\tat java.lang.Thread.run(Thread.java:741)\n";

        exe1 = "a b c";

        for (int i = 0; i < 1; i++) {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(exe1.getBytes());
            fos.flush();
            fos.close();
        }

    }

}
