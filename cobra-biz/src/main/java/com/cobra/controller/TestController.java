package com.cobra.controller;

import com.cobra.util.HttpClientUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
     https://blog.csdn.net/a351945755/article/details/22120139
 keytool生成证书 jdk默认证书密码 changeit
 客户端：密钥库（包含证书和私钥）、信任库（保存证书的数据库）
 服务端：私钥库、信任库
# 单向服务器
 1、创建服务端密钥库(存放服务端) 主题备用名称必须给,访问时使用备用名称 https://test.ddd.com/test
 keytool -genkey -ext SAN=dns:test.ddd.com,ip:127.0.0.1 -dname " CN=keytool_local_server, OU=TY, O=TY, L=SH, S=SH, C=CN" -storepass 123456 -keystore keytool_local_server.keystore -keyalg RSA -keypass 123456 -validity 3600

 2、生成客户端信任库(导出服务端的证书、存放客户端)
 #从密钥库中导出证书
 keytool -export -file keytool_local_server.cer -storepass 123456 -keystore keytool_local_server.keystore
 # 将证书放在信任库
 keytool -import -file keytool_local_server.cer -storepass 123456 -keystore client.truststore -alias serverkey -noprompt

 # 双向 客户端
 3、创建客户端的密钥库(存放客户端)
 keytool -genkey -dname " CN=test-Client, OU=TY, O=TY, L=SH, S=SH, C=CN" -storepass 123456 -keystore client.keystore -keyalg RSA -keypass 123456

 4、生成服务端的信任库(存放服务端)
 keytool -export -file test_client.cer -storepass 123456 -keystore client.keystore
 keytool -import -file test_client.cer -storepass 123456 -keystore server.truststore -alias clientkey -noprompt

 5、tomcat
 存放 server.keystore(密钥库)、server.truststore(信任库)
 * </p>
 * <p>
 *     springboot项目 server.keystoreh或者.p12文件 存放在项目的根目录下(非子项目),.crt证书文件安装在客户端
 * </p>
 * <p>
 *     keytool与openssl对比
 *     https://blog.xujiuming.com/ming/7a196489.html
 * </p>
 * <p>
 *     openssl生成证书(需要对配置文件openssl.cnf 配置主题名称)
 *     # 1、生成密钥
 *     openssl genrsa -out openssl_private.key 2048
 *     # 2、证书请求文件
 *     openssl req -new -nodes -keyout openssl_local_pri.key  -out openssl_local_req.csr -days 3650 -subj "/C=CN/ST=SH/L=SH/O=TY/OU=ZX/CN=openssl_local_server" -config openssl.cnf
 *     #3、 生成证书
 *     openssl x509 -req -days 365 -in openssl_local_req.csr -signkey openssl_local_pri.key -out openssl_local_crt.crt -extfile openssl.cnf -extensions v3_req
 *     #4、 生成.p12文件
 *     openssl pkcs12 -export -out openssl_local_p12.p12 -inkey openssl_local_pri.key -in openssl_local_crt.crt
 * </p>

 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public Object test(){
        return "ok";
    }

    public static void main(String[] args){
        String url = "https://test.ddd.com/test";
        String result = HttpClientUtil.sendByGet(url,3000,3000,new HashMap<>());
        System.out.println(result);
    }
}
