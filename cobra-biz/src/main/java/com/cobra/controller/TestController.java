package com.cobra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
     https://blog.csdn.net/a351945755/article/details/22120139
 客户端：密钥库（包含证书和私钥）、信任库（保存证书的数据库）
 服务端：私钥库、信任库

 1、创建服务端密钥库(存放服务端)
 keytool -genkey -dname " CN=test-server, OU=TY, O=TY, L=SH, S=SH, C=CN" -storepass 123456 -keystore server.keystore -keyalg RSA -keypass 123456

 2、生成客户端信任库(导出服务端的证书、存放客户端)
 keytool -export -file test_server.cer -storepass 123456 -keystore server.keystore #从密钥库中导出证书
 keytool -import -file test_server.cer -storepass 123456 -keystore client.truststore -alias serverkey -noprompt # 将证书放在信任库

 4、创建客户端的密钥库(存放客户端)
 keytool -genkey -dname " CN=test-Client, OU=TY, O=TY, L=SH, S=SH, C=CN" -storepass 123456 -keystore client.keystore -keyalg RSA -keypass 123456

 5、生成服务端的信任库(存放服务端)
 keytool -export -file test_client.cer -storepass 123456 -keystore client.keystore
 keytool -import -file test_client.cer -storepass 123456 -keystore server.truststore -alias clientkey -noprompt

 6、tomcat
 存放 server.keystore(密钥库)、server.truststore(信任库)
 * </p>
 * <p>
 *     springboot项目 server.keystore 存放在项目的根目录下(非子项目)
 * </p>
 * <p>
 *     keytool与openssl对比
 *     https://blog.xujiuming.com/ming/7a196489.html
 * </p>

 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public Object test(){
        return "ok";
    }
}
