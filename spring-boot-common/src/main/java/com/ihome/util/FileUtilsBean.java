package com.ihome.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Component
public class FileUtilsBean {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtilsBean.class);

    // 失效时间15天
    public final static long expire = 1000l * 3600l * 24l * 15l;
    public final static long fileSize = 2 * 1024 * 1024;
    public final static String[] fileType = {"png", "jpg", "jpeg"};
    /** 上传附件地址 */
    public static final String FILEPATH = "files";



    /**
     * 上传成功则返回ossKey值 
     * @author Administrator
     * @date 2018年4月26日
     * @param file
     * @param uniqueId 文件唯一名称
     * @param type oss文件夹 路径
     * @return
     */
   /* public String fileUpload(MultipartFile file, String uniqueId, String type) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 限制文件大小和格式
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.asList(fileType).contains(suffix)) {
            throw new RuntimeException("图片格式不正确!");
        }

        String ossKey = null;
        File zipFile = null;
        String zipFilePath = null;
        ZipOutputStream zipOut = null;
        OutputStream localImgStream = null;
        File localFile = null;
        String localFileName = Uploader.getQuickPathnameNew("");// 不带后缀的文件名
        String rootPath = getRootPath();
        String fileNamePath = rootPath + FILEPATH + localFileName + "." + suffix;// 带后缀的全

        try {

            // 上传到本地
            try {
                localFile = new File(fileNamePath);
                localImgStream = FileUtils.openOutputStream(localFile);// 自动生成文件 不能关闭

                int t1 = 0;
                InputStream imgStream = file.getInputStream();
                while ((t1 = imgStream.read()) != -1) {
                    localImgStream.write(t1);
                }
                localImgStream.flush();
                localImgStream.close();// 关闭
                imgStream.close();
            }catch (IOException e){
                throw new RuntimeException("上传文件失败!");
            }

            try {

                ossKey = FILEPATH + localFileName + ".zip";
                zipFilePath = rootPath + ossKey;
                zipFile = new File(zipFilePath);

                zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
                zipOut.putNextEntry(new ZipEntry(localFile.getName()));

                // 设置注释
                zipOut.setComment("test img");

                int temp = 0;
                InputStream zipStream = new FileInputStream(new File(fileNamePath));
                while ((temp = zipStream.read()) != -1) {
                    zipOut.write(temp);
                }

                zipOut.flush();
                zipOut.close();// 关闭压缩流
                zipStream.close();

            } catch (Exception e) {
                throw new RuntimeException("压缩图片错误!");
            }



        }catch (Exception e){
            throw new RuntimeException("文件处理错误!");
        }finally {
            if(localFile != null && localFile.exists()){
                localFile.delete();// 删除本地图片
            }
        }



        return null;
    }
*/

    public String fileUpload(MultipartFile file, String uniqueId, String type) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 限制文件大小和格式
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.asList(fileType).contains(suffix)) {
            throw new RuntimeException("图片格式不正确!");
        }

        String ossKey = null;
        File zipFile = null;
        ZipOutputStream zipOut = null;
        File localFile = null;
        String rootPath = getRootPath();
        String localFileName = FILEPATH +"/IMG"+  Uploader.getQuickPathnameNew(suffix);// 带后缀的全

        try {

            // 上传到本地

            localFile = new File(rootPath +localFileName);
            FileUtils.openOutputStream(localFile).close();// 自动生成文件 不能关闭

            ossKey = localFileName.substring(0,localFileName.lastIndexOf(".")) + ".zip";
            System.out.println("ossKey---------->"+ossKey);
            zipFile = new File(rootPath + ossKey);
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(localFile.getName()));

            // 设置注释
            zipOut.setComment("test img");

            int temp = 0;
            InputStream zipStream = file.getInputStream();
            while ((temp = zipStream.read()) != -1) {
                zipOut.write(temp);
            }

            zipOut.flush();
            zipOut.close();// 关闭压缩流
            zipStream.close();

        } catch (Exception e) {
            throw new RuntimeException("压缩图片错误!");
        }finally {
            localFile.delete();
        }

        return null;
    }


    /**
     * 获取当前项目根目录 
     * @author Administrator
     * @date 2018年3月22日
     * @return
     */
    public String getRootPath() {
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
