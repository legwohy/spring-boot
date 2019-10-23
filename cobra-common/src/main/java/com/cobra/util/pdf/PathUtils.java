package com.cobra.util.pdf;

/**
 * Created by leigang on 2019/10/22.
 */
public class PathUtils {

    public static String getRootClassPath(String fileName){

        return PathUtils.class.getClassLoader().getResource("").getFile()+fileName;
    }

    public static String getCurrentClassPath(String fileName){

        return PathUtils.class.getResource("").getFile()+fileName;
    }



}
