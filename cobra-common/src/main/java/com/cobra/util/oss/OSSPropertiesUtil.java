package com.cobra.util.oss;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件
 *
 * @author zhangtao
 */
public class OSSPropertiesUtil
{

    private Logger logger = LoggerFactory.getLogger(OSSPropertiesUtil.class);
    private String fileName = "/ossConfig.properties";

    /**
     * endPoint
     */
    private String ossRegion = "";

    /**
     * accesskeyId
     */
    private String ossAccessKeyId = "";

    /**
     * accessKeySecret
     */
    private String ossAccessKeySecret = "";

    /**
     * bucket ACL
     * public-read-write  公共读写
     * public-read        公共读，私有写
     * private            私有读写
     */
    private String bucketACL = "";

    /**
     * bucket storage
     * Standard           标准类型
     * IA                 低频访问类型
     * Archive            归档类型
     * Unknown            未知
     */
    private String bucketStorage = "";


    public OSSPropertiesUtil()
    {
        try
        {
            Properties prop = new Properties();
            InputStream in = getClass().getResourceAsStream(fileName);
            prop.load(in);
            ossRegion = prop.getProperty("region").trim();
            ossAccessKeyId = prop.getProperty("accessKeyId").trim();
            ossAccessKeySecret = prop.getProperty("accessKeySecret").trim();
            bucketACL = prop.getProperty("bucketACL").trim();
            bucketStorage = prop.getProperty("bucketStorage").trim();
        } catch (Exception e)
        {
            logger.error("read property file failed.");
        }
    }

    public static void main(String[] args)
    {
        OSSPropertiesUtil util = new OSSPropertiesUtil();
        System.out.println(util.getOssRegion());
        System.out.println(util.getOssAccessKeyId());
        System.out.println(util.getOssAccessKeySecret());
        System.out.println(util.getBucketACL());
        System.out.println(util.getBucketStorage());
    }

    public String getOssRegion()
    {
        return ossRegion;
    }

    public void setOssRegion(String ossRegion)
    {
        this.ossRegion = ossRegion;
    }

    public String getOssAccessKeyId()
    {
        return ossAccessKeyId;
    }

    public void setOssAccessKeyId(String ossAccessKeyId)
    {
        this.ossAccessKeyId = ossAccessKeyId;
    }

    public String getOssAccessKeySecret()
    {
        return ossAccessKeySecret;
    }

    public void setOssAccessKeySecret(String ossAccessKeySecret)
    {
        this.ossAccessKeySecret = ossAccessKeySecret;
    }

    public String getBucketACL()
    {
        return bucketACL;
    }

    public void setBucketACL(String bucketACL)
    {
        this.bucketACL = bucketACL;
    }

    public String getBucketStorage()
    {
        return bucketStorage;
    }

    public void setBucketStorage(String bucketStorage)
    {
        this.bucketStorage = bucketStorage;
    }

}
