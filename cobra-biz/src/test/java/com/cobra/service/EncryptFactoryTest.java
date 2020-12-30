package com.cobra.service;

import com.BaseTest;
import com.cobra.domain.FactoryBO;
import com.cobra.domain.FormatBO;
import com.cobra.enums.EncryptEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2020/12/30 15:57
 * @desc
 */
public class EncryptFactoryTest extends BaseTest {
    @Autowired
    private EncryptFactory factory;
    @Test
    public void exccute() throws Exception{
        FactoryBO bo = new FactoryBO();
        bo.setSrc("123456");
        List<FormatBO> encList = new ArrayList<>();
        FormatBO MD5BO = new FormatBO();
        MD5BO.setEncType(EncryptEnum.MD5.getCode());
        MD5BO.setIsTransBigAfter(true);
        encList.add(MD5BO);

        bo.setFormatBOS(encList);

        factory.execute(bo);

        Assert.assertEquals("E10ADC3949BA59ABBE56E057F20F883E",bo.getSrc());
    }

}