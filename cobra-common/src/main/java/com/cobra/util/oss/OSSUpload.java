package com.cobra.util.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class OSSUpload
{

    private Logger logger = LoggerFactory.getLogger(OSSUpload.class);

    private OSSOperation ossOperation = null;

    public OSSUpload()
    {
        ossOperation = new OSSOperation(null);
    }

    public OSSUpload(OSSOperation ossOperation)
    {
        this.ossOperation = ossOperation;
    }

    /**
     * sample content upload
     *
     * @param bucketName
     * @param key
     * @param content
     * @return
     */
    public void sampleUploadWithContent(String bucketName, String key, String content)
    {
        try
        {
            if (StringUtils.isBlank(bucketName) || StringUtils.isBlank(key) || StringUtils.isBlank(content))
            {
                logger.info("sampleUploadWithContent: bucketName or key or content is null, return.");
                return;
            }

            if (!ossOperation.isOSSClientExists())
            {
                logger.info("sampleUploadWithContent: ossClient is null, return.");
                return;
            }

            boolean exists = ossOperation.doesBucketExist(bucketName);
            if (!exists)
            {
                ossOperation.createBucket(bucketName);
            }

            ossOperation.putObject(bucketName, key, content);
        } catch (OSSException oe)
        {
            setOSSException(oe);
        } catch (ClientException ce)
        {
            setClientException(ce);
        } finally
        {
            ossOperation.shutDownOssClient();
        }

    }

    /**
     * sample file upload with inputStream
     *
     * @param bucketName
     * @param key
     * @param inputStream
     * @return
     */
    public OSSModel sampleUploadWithInputStream(String bucketName, String key, InputStream inputStream)
    {
        OSSModel model = new OSSModel();
        model.setResultCode("error");
        model.setUrl("");

        try
        {
            if (StringUtils.isBlank(bucketName) || StringUtils.isBlank(key) || inputStream == null)
            {
                logger.info("sampleUploadWithFile: bucketName or key or filePath is null, return.");
                model.setResultMessage("bucketName or key or filePath is null or filePath is error.");
                return model;
            }
            return sampleUpload(bucketName, key, inputStream);
        } catch (Exception e)
        {
            model.setResultMessage("error.");
            logger.info("error.");
        }

        return model;
    }

    /**
     * sample get file
     *
     * @param bucketName
     * @param key
     * @param outTime    毫秒
     * @return file URL
     */
    public URL sampleGetFileUrl(String bucketName, String key, Long outTime)
    {
        OSSModel model = new OSSModel();
        URL url = null;
        try
        {
            if (!ossOperation.isOSSClientExists())
            {
                logger.info("sampleGetFileUrl: ossClient is null, return.");
                model.setResultMessage("system error: ossClient is null.");
            }
            model.setResultCode("success");
            model.setResultMessage("");
            // 设置URL过期时间
            Date expiration = new Date(System.currentTimeMillis() + outTime);
            url = ossOperation.generatePresignedUrl(bucketName, key, expiration);
        } catch (OSSException oe)
        {
            model.setResultMessage("error.");
            setOSSException(oe);
        } catch (ClientException ce)
        {
            model.setResultMessage("error.");
            setClientException(ce);
        } finally
        {
            ossOperation.shutDownOssClient();
        }
        return url;
    }

    /**
     * sample Download file
     *
     * @param bucketName
     * @param key
     * @param FileInputStream
     * @return file URL
     */
    public InputStream sampleDownloadFile(String bucketName, String key)
    {
        OSSModel model = new OSSModel();
        InputStream in = null;
        try
        {
            if (!ossOperation.isOSSClientExists())
            {
                logger.info("sampleDownloadFile: ossClient is null, return.");
                model.setResultMessage("system error: ossClient is null.");
            }
            model.setResultCode("success");
            model.setResultMessage("");
            try
            {
                in = ossOperation.getObject(bucketName, key);
            } catch (IOException e)
            {
                model.setResultMessage("error.");
            }
        } catch (OSSException oe)
        {
            model.setResultMessage("error.");
            setOSSException(oe);
        } catch (ClientException ce)
        {
            model.setResultMessage("error.");
            setClientException(ce);
        } finally
        {

        }
        return in;
    }


    /**
     * shutDownOssClient
     */
    public void shutDownOssClient()
    {
        ossOperation.shutDownOssClient();
    }

    /**
     * sample file upload
     *
     * @param bucketName
     * @param key
     * @param filePath
     * @return
     */
    public OSSModel sampleUploadWithFile(String bucketName, String key, String filePath)
    {
        OSSModel model = new OSSModel();
        model.setResultCode("error");
        model.setUrl("");

        try
        {
            String fileName = getFileNameByPath(filePath);
            if (StringUtils.isBlank(bucketName) || StringUtils.isBlank(key) || StringUtils.isBlank(filePath) || StringUtils.isBlank(fileName))
            {
                logger.info("sampleUploadWithFile: bucketName or key or filePath is null, return.");
                model.setResultMessage("bucketName or key or filePath is null or filePath is error.");
                return model;
            }
            InputStream inputStream = new FileInputStream(filePath);
            return sampleUpload(bucketName, key, inputStream);
        } catch (FileNotFoundException e)
        {
            model.setResultMessage("fielPath is not exists.");
            logger.info("file is not exists.");
        }

        return model;
    }

    private OSSModel sampleUpload(String bucketName, String key, InputStream inputStream)
    {
        OSSModel model = new OSSModel();
        model.setResultCode("error");
        model.setUrl("");

        try
        {

            if (!ossOperation.isOSSClientExists())
            {
                logger.info("sampleUploadWithFile: ossClient is null, return.");
                model.setResultMessage("system error: ossClient is null.");
                return model;
            }

            boolean exists = ossOperation.doesBucketExist(bucketName);
            if (!exists)
            {
                ossOperation.createBucket(bucketName);
            }

            ossOperation.putObject(bucketName, key, inputStream);

            model.setResultCode("success");
            model.setResultMessage("");
            model.setUrl(ossOperation.getResourceUrl(bucketName) + "/" + key);

        } catch (OSSException oe)
        {
            model.setResultMessage("error.");
            setOSSException(oe);
        } catch (ClientException ce)
        {
            model.setResultMessage("error.");
            setClientException(ce);
        } finally
        {
            ossOperation.shutDownOssClient();
        }

        return model;
    }

    private String getFileNameByPath(String filePath)
    {
        if (StringUtils.isBlank(filePath))
        {
            logger.info("filePath is null or blank.");
            return "";
        }

        String[] strs = filePath.split("/");
        return (strs.length <= 0) ? "" : strs[strs.length - 1];
    }

    /**
     * judge url is exists
     *
     * @param urlStr
     * @return
     */
    @SuppressWarnings("unused")
    private boolean judgeUrlIsExists(String urlStr)
    {
        try
        {
            URL url = new URL(urlStr);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            if (in != null)
            {
                return true;
            }
        } catch (Exception e)
        {
           e.printStackTrace();
        }

        return false;
    }

    private void setOSSException(OSSException oe)
    {
        logger.info("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.");
        logger.info("Error Message: " + oe.getErrorCode());
        logger.info("Error Code:       " + oe.getErrorCode());
        logger.info("Request ID:      " + oe.getRequestId());
        logger.info("Host ID:           " + oe.getHostId());
    }

    private void setClientException(ClientException ce)
    {
        logger.info("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.");
        logger.info("Error Message: " + ce.getMessage());
    }


}
