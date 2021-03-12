package com.cobra.script.jpython;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>
 *     windows 下调用需要配置python环境变量
 *     参考 https://www.cnblogs.com/lizm166/p/8092719.html
 *
 * </p>
 *
 * java 调用python
 *
 * @author admin
 * @date 2021/3/11 16:16
 * @desc
 */
public class RuntimeFunction {
    private final static String PYTHON_PATH = "C:\\Users\\sh-zxgs-chenx\\AppData\\Local\\Programs\\Python\\Python39\\python.exe";
    public static void main(String[] args) {
        Process proc;
        try {
            String pythonFilePath = "D:\\code_repository\\cobra\\cobra-learning\\src\\main\\java\\com\\cobra\\script\\python\\Hello.py";
            System.out.println("-------begin------------");
            proc = Runtime.getRuntime().exec(PYTHON_PATH+" "+pythonFilePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
            System.out.println("-------end------------");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
