package com.cobra.util;


import java.io.File;

public class FileUtils {
    /**
     * 获取当前项目根目录
     * @author Administrator
     * @date 2018年3月22日
     * @return
     */
    public static String getRootPath() {
        String classPath = FileUtilsBean.class.getClassLoader().getResource("").getPath();
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
        return rootPath;
    }

}
