package com.cobra.util.oss;

import com.aliyun.oss.OSSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * @auther: leigang
 * @date: 2018/12/13 15:05
 * @description:
 */
public class MainClass
{
    public final static long expire = 1000 * 3600 * 24 * 15L;
    public static void main(String[] args) throws Exception
    {

        // 存储空间名
        String bucketName = "jinlihua-bank-logo";
        String ossKey = "files/bank/";

        String filePath = "F:\\项目\\保险\\银行缩图\\";


        File file = new File(filePath);
        if(file.isDirectory())
        {
            String[] fileNameList = file.list();
            Arrays.stream(fileNameList).forEach(fileName->{

                    if(fileName.contains(".png"))
                    {
                        try
                        {
                            File file1 = new File(file.getAbsolutePath()+File.separator+fileName);
                            FileInputStream inputStream = new FileInputStream(file1);
                            getOSSUpload().sampleUploadWithInputStream(bucketName,ossKey+fileName,inputStream);

                            System.out.println(fileName+"上传成功");

                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }

            });




        }


    }

    public static OSSUpload getOSSUpload(){
        String region = "shanghai";// 区域
        String keyId = "LTAIi1kpSZhwlene";
        String secret = "8RcUl9BMahCRjLglRhwpt6Whi80rfg";
        String bucketAcl = "public-read";// 读写权限
        String bucketStorage = "standard";// 存储配置

        OSSClient ossClient = new OSSClient("http://oss-cn-" + region + ".aliyuncs.com", keyId, secret);
        OSSOperation ossOperation = new OSSOperation(ossClient);
        ossOperation.setBucketAcl(bucketAcl);
        ossOperation.setBucketStorage(bucketStorage);

        OSSUpload ossUpload = new OSSUpload(ossOperation);

        return ossUpload;
    }
}
