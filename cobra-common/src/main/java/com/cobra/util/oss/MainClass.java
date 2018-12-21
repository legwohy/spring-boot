package com.cobra.util.oss;

import com.aliyun.oss.OSSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @auther: leigang
 * @date: 2018/12/13 15:05
 * @description:
 */
public class MainClass
{
    public final static long expire = 1000 * 3600 * 24 * 15L;
    public static void main(String[] args) throws FileNotFoundException
    {
        String region = "hangzhou";// 区域
        String keyId = "";
        String secret = "";
        String bucketAcl = "public-read";// 读写权限
        String bucketStorage = "standard";// 存储配置
        String bucketName = "banner-test";// 存储空间名
        OSSClient ossClient = new OSSClient("http://oss-cn-" + region + ".aliyuncs.com", keyId, secret);
        OSSOperation ossOperation = new OSSOperation(ossClient);
        ossOperation.setBucketAcl(bucketAcl);
        ossOperation.setBucketStorage(bucketStorage);

        OSSUpload ossUpload = new OSSUpload(ossOperation);

        String ossKey = "file/sb/test/"+ UUID.randomUUID().toString()+".png";
        File file = new File("F:\\0 手持.png");
        FileInputStream inputStream = new FileInputStream(file);

        // 上传
        OSSModel model = ossUpload.sampleUploadWithInputStream(bucketName,ossKey,inputStream);


        String url = ossUpload.sampleGetFileUrl(bucketName, ossKey, expire).toString();
        System.out.println("url="+url);

    }
}
