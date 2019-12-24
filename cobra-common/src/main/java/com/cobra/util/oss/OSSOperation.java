package com.cobra.util.oss;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;


public class OSSOperation
{

    private static OSSPropertiesUtil util = null;
    private static String URL_REGION_PREFIX = "oss-cn-";
    private static String URL_REGION_SUFFIX = ".aliyuncs.com";
    private static String URL_PREFIX = "http://";

    static
    {
        util = new OSSPropertiesUtil();
    }

    private Logger logger = LoggerFactory.getLogger(OSSOperation.class);
    private OSSClient ossClient;
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketAcl;
    private String bucketStorage;

    public OSSOperation(OSSClient ossClient)
    {
        this.ossClient = ossClient;
    }

    protected static String getEndPointUrl()
    {
        return URL_PREFIX + URL_REGION_PREFIX + util.getOssRegion() + URL_REGION_SUFFIX;
    }

    public OSSClient instance()
    {
        if (ossClient == null)
        {
            ossClient = new OSSClient(getEndPointUrl(), util.getOssAccessKeyId(), util.getOssAccessKeySecret());
        }

        return ossClient;
    }

    public OSSClient getInstance()
    {
        if (ossClient == null)
        {
            ossClient = instance();
        }

        return ossClient;
    }

    //
    protected boolean isOSSClientExists()
    {
//		ossClient = ;

        if (ossClient == null)
        {
            logger.info("ossClient is null, return false");
            return false;
        }

        return true;

    }

    /**
     * create bucket
     *
     * @param bucketName
     * @return
     */
    protected boolean createBucket(String bucketName)
    {
        if (StringUtils.isBlank(bucketName))
        {
            logger.info("bucketName is null or blank.");
            return false;
        }

        if (!isOSSClientExists())
        {
            return false;
        }

        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setCannedACL(getBucketACL());
        createBucketRequest.setStorageClass(getStorageType());
        ossClient.createBucket(createBucketRequest);

        return true;
    }

    /**
     * get bucket list
     * prefix     限定返回的bucket name必须以prefix作为前缀，可以不设定，不设定时不过滤前缀信息
     * marker     设定结果从marker之后按字母排序的第一个开始返回，可以不设定，不设定时从头开始返回
     * maxKeys    限定此次返回bucket的最大数，如果不设定，默认为100，max-keys取值不能大于1000
     */
    protected List<Bucket> getBucketList(String prefix, String marker, Integer maxKeys)
    {
        if (!isOSSClientExists())
        {
            return null;
        }

        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        if (StringUtils.isNotBlank(prefix)) listBucketsRequest.setPrefix(prefix);
        if (StringUtils.isNotBlank(marker)) listBucketsRequest.setMarker(marker);
        if (maxKeys > 0) listBucketsRequest.setMaxKeys(maxKeys);

        return ossClient.listBuckets();
    }

    /**
     * get bucket ACL
     *
     * @return
     */
    protected CannedAccessControlList getBucketACL()
    {
        if (this.getBucketAcl() != null)
        {
            if (this.getBucketAcl().equalsIgnoreCase("private"))
            {
                return CannedAccessControlList.Private;
            } else if (this.getBucketAcl().equalsIgnoreCase("public-read"))
            {
                return CannedAccessControlList.PublicRead;
            } else if (this.getBucketAcl().equalsIgnoreCase("public-read-write"))
            {
                return CannedAccessControlList.PublicReadWrite;
            }
        }

        return CannedAccessControlList.Default;
    }

    /**
     * get storage type
     */
    protected StorageClass getStorageType()
    {
        if (this.getBucketStorage() != null)
        {
            if (this.getBucketStorage().equalsIgnoreCase("Standard"))
            {
                return StorageClass.Standard;
            } else if (this.getBucketStorage().equalsIgnoreCase("IA"))
            {
                return StorageClass.IA;
            } else if (this.getBucketStorage().equalsIgnoreCase("Archive"))
            {
                return StorageClass.Archive;
            } else if (this.getBucketStorage().equals("Unknown"))
            {
                return StorageClass.Unknown;
            }
        }

        return StorageClass.Standard;
    }

    /**
     * get bucket secret
     */
    protected AccessControlList getBucketSecret(String bucketName)
    {
        if (StringUtils.isBlank(bucketName))
        {
            logger.info("bucketName is null or blank, return.");
            return null;
        }

        if (!isOSSClientExists())
        {
            return null;
        }

        return ossClient.getBucketAcl(bucketName);
    }

    /**
     * get bucket location
     */
    protected String getBucketLocation(String bucketName)
    {
        if (StringUtils.isBlank(bucketName))
        {
            logger.info("bucketName is null or blank, return.");
            return null;
        }

        if (!isOSSClientExists())
        {
            return null;
        }

        return ossClient.getBucketLocation(bucketName);
    }

    /**
     * get bucket info
     */
    protected BucketInfo getBucketInfo(String bucketName)
    {
        if (StringUtils.isBlank(bucketName))
        {
            logger.info("bucketName is null or blank, return.");
            return null;
        }

        if (!isOSSClientExists())
        {
            return null;
        }

        return ossClient.getBucketInfo(bucketName);
    }

    /**
     * delete bucket
     */
    protected boolean deleteBucket(String bucketName)
    {
        boolean flag = doesBucketExist(bucketName);
        if (!flag)
        {
            ossClient.deleteBucket(bucketName);
            return true;
        }

        return false;
    }

    protected String getResourceUrl(String backetName)
    {
        return URL_PREFIX + backetName + "." + URL_REGION_PREFIX + util.getOssRegion() + URL_REGION_SUFFIX;
    }

    /**
     * get bucket is exists
     */
    protected boolean doesBucketExist(String bucketName)
    {
        if (StringUtils.isBlank(bucketName))
        {
            logger.info("bucketName is null or blank, return.");
            return true;
        }

        if (!isOSSClientExists())
        {
            getInstance();
        }

        boolean exists = ossClient.doesBucketExist(bucketName);

        if (exists)
        {
            logger.info(bucketName + " is exists.");
        }

        return exists;
    }

    protected void putObject(String bucketName, String key, String content)
    {
        ossClient.putObject(bucketName, key, new ByteArrayInputStream(content.getBytes()));
    }

    protected void putObject(String bucketName, String key, InputStream inputStream)
    {
        ossClient.putObject(bucketName, key, inputStream);
    }

    protected URL generatePresignedUrl(String bucketName, String key, Date expiration)
    {
        return ossClient.generatePresignedUrl(bucketName, key, expiration);
    }

    protected InputStream getObject(String bucketName, String key) throws IOException
    {
        OSSObject ossObject = ossClient.getObject(bucketName, key);
        return ossObject.getObjectContent();
    }

    /**
     * shutdown ossClient
     */
    public void shutDownOssClient()
    {
        if (ossClient == null)
        {
            return;
        }

        ossClient.shutdown();
    }

    public OSSClient getOssClient()
    {
        return ossClient;
    }

    public void setOssClient(OSSClient ossClient)
    {
        this.ossClient = ossClient;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getAccessKeyId()
    {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId)
    {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret()
    {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret)
    {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketStorage()
    {
        return bucketStorage;
    }

    public void setBucketStorage(String bucketStorage)
    {
        this.bucketStorage = bucketStorage;
    }


    public String getBucketAcl()
    {
        return bucketAcl;
    }

    public void setBucketAcl(String bucketAcl)
    {
        this.bucketAcl = bucketAcl;
    }
}
