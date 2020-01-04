package com.cobra.util.pdf;

import java.io.File;


public class PathUtils {

    private PathUtils(){throw new AssertionError("can not instantiate class");}

    public static String getRootClassPath(String fileName){

        return PathUtils.class.getClassLoader().getResource("").getFile()+fileName;
    }

    public static String getCurrentClassPath(String fileName){

        String classPath = PathUtils.class.getClassLoader().getResource("").getPath();
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator))
        {
            rootPath = classPath.replace("/", "\\");
        }
        //linux下
        if ("/".equals(File.separator))
        {
            rootPath = classPath.replace("\\", "/");
        }

        return rootPath + fileName;
    }



}
