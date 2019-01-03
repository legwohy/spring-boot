package com.cobra.jvm;

import java.util.Map;
import java.util.Properties;

/**
 * 获取系统配置信息
 *
 */
public class SystemPropertyTest {

    public static void main(String[] args) {

        String[] keys = { "java.version", "java.vendor", "java.vendor.url", "java.home",
                        "java.vm.specification.version", "java.vm.specification.vendor", "java.vm.specification.name",
                        "java.vm.version", "java.vm.vendor", "java.vm.name", "java.specification.version",
                        "java.specification.vendor", "java.specification.name", "java.class.version", "java.class.path",
                        "java.library.path", "java.io.tmpdir", "java.compiler", "java.ext.dirs", "os.name", "os.arch",
                        "os.version", "file.separator", "path.separator", "line.separator", "user.name", "user.home",
                        "user.dir" };

      /*  for (String key : keys) {
            String value = System.getProperty(key);
            System.out.println(key + " : " + value);
        }*/


      System.out.println("=================system properties================");
        Properties properties = System.getProperties();
        for (String key:properties.stringPropertyNames())
        {
            System.out.println(key+"\t=\t"+properties.getProperty(key));

        }

        System.out.println("====================system env================\r\n\r\n\r\n");

        for (Map.Entry<String,String> en:System.getenv().entrySet())
        {
            System.out.println(en.getKey()+"\t=\t"+en.getValue());
        }

        System.out.println("====================system securityManager================\r\n\r\n\r\n");

        SecurityManager security = System.getSecurityManager();
        if(null == security)
        {
            security.checkRead("");

        }

        // 删除自己


    }
}