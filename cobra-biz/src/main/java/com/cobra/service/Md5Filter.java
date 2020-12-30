package com.cobra.service;

import com.cobra.domain.EncryptBO;
import com.cobra.enums.EncryptEnum;
import com.cobra.util.MD5Util;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @date 2020/12/30 15:26
 * @desc
 */
@Component
public class Md5Filter implements PwdFilter {
    @Override
    public String name(){
        return EncryptEnum.MD5.getCode();
    }

    @Override
    public void before(EncryptBO encryptBO){
        if (null != encryptBO.getIsTransBigBefore()) {
            if (encryptBO.getIsTransBigBefore()) {
                encryptBO.setSrc(encryptBO.getSrc().toUpperCase());
            } else {
                encryptBO.setSrc(encryptBO.getSrc().toLowerCase());
            }
        }

    }

    @Override
    public void handle(EncryptBO encryptBO){
        encryptBO.setSrc(MD5Util.MD5(encryptBO.getSrc()));
    }

    @Override
    public void after(EncryptBO encryptBO){
        if (null != encryptBO.getIsTransBigAfter()) {
            if (encryptBO.getIsTransBigAfter()) {
                encryptBO.setSrc(encryptBO.getSrc().toUpperCase());
            } else {
                encryptBO.setSrc(encryptBO.getSrc().toLowerCase());
            }
        }
    }
}
