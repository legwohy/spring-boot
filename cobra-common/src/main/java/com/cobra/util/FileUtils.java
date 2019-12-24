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
        String classPath = FileUtils.class.getClassLoader().getResource("").getPath();
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

    /**
     * 创建图片路径
     * @param path
     */
    public static void mkdirsFile(String path){
        try {
            path =  path.replaceAll("\\\\", "/");
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
