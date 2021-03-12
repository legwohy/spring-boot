package com.cobra.script.groovy

import com.cobra.script.jgroove.utils.CipherCommonUtils

/**
 * @author admin
 * @date 2021/2/2 18:03
 * @desc
 */
class CipherForGroovy {
    /**
     * md5签名
     * @param data
     * @return
     */
    def md5(data){
        return CipherCommonUtils.doMD5(data)
    }

    /**
     * aes加密
     * @param data
     * @return
     */
    def aes(data){
        return CipherCommonUtils.doAesEncrypt(data,"1234567890123456")
    }


}
