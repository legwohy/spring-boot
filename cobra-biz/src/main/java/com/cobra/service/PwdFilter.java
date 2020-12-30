package com.cobra.service;

import com.cobra.domain.EncryptBO;

/**
 * @author admin
 * @date 2020/12/30 15:21
 * @desc
 */
public interface PwdFilter {

    String name();
    void before(EncryptBO encryptBO);
    void handle(EncryptBO encryptBO);
    void after(EncryptBO bo);

}
