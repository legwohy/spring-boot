package com.cobra.groove

/**
 * @author admin
 * @date 2021/2/4 10:37
 * @desc
 */
class GroovyMainTest {
     static void main(String[] args) {

         GroovyMainTest test = new GroovyMainTest()

         //test.bibao()

         // 封装方法调用
         println (QxSignUtils.getTimestamp())


    }

    /**
     * map
     */
    def map(){
        def mp = ["k1":"v1","k2":"v2"]
        mp.each {println(it.key+":"+it.value)}
    }


    def bibao(){
        int item = 1;
        // def result = {item++}// 只有一个返回值
        {->item++}// 无参

        println(item)
        println(item)


        def outParams = '外部参数'
        // 必报带传参
        def tmp = {params->println("hello ${params},外部参数：${outParams}")}
        tmp.call("闭包参数")

        // it 是关键字
        def tmp1 = {println("hello ${it}")}
        tmp1.call("小鬼")
    }



    def fileOperate(){
        // 读取文件
        new File("E:\\test\\tmp.txt").eachLine {
            line->println(line)
        }

    }
}

import javax.crypto.*
import javax.crypto.spec.DESedeKeySpec
import java.security.*
import java.security.spec.InvalidKeySpecException
import org.apache.commons.codec.binary.Base64
import java.text.SimpleDateFormat

class QxSignUtils {
    static String getTimestamp(){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS")
        Calendar calendar = Calendar.getInstance()
        return myFormat.format(calendar.getTime())
    }

    static String desEncrypt(String content, String sKey){
        try {
            Key secretKey = generateDesKey(sKey)
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            byte[] bytes = cipher.doFinal(content.getBytes())
            return Base64.encodeBase64String(bytes).trim()
        } catch (Exception e) {
            e.printStackTrace()
            throw new RuntimeException("3des加密失败")
        }
    }

    private static Key generateDesKey(String strKey) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException{
        KeyGenerator generator = KeyGenerator.getInstance("DESede")
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG")
        sr.setSeed(strKey.getBytes("UTF-8"))
        generator.init(168, sr)
        Key key = generator.generateKey()

        DESedeKeySpec dks = new DESedeKeySpec(key.getEncoded())

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede")
        return keyFactory.generateSecret(dks)
    }
}
