package com.cobra.service;

import com.cobra.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContextManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 非参数化 无需 TestContextManager
 */
@Slf4j
public class CalcServiceTest3 extends AbstractTest {

    @Autowired
    private CalcService calcService;

    @Test
    public void add() throws Exception{
        Assert.assertEquals(8, calcService.add(3, 5));
    }

}