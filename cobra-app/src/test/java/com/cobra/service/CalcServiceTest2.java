package com.cobra.service;

import com.cobra.AbstractTest;
import com.cobra.domain.pojo.DataVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContextManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数化测试 对象参数
 */
@RunWith(Parameterized.class)
@Slf4j
public class CalcServiceTest2 extends AbstractTest {

    private DataVO data;


    public CalcServiceTest2(DataVO data){
        this.data = data;
    }

    /**
     * 参数化必须依赖注入
     * @throws Exception
     */
    @Before
    public void init() throws Exception{
        // 依赖注入
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    @Autowired
    private CalcService calcService;

    @Parameterized.Parameters(name = "{index}:params{0}")
    public static List<DataVO> getParameter2(){
        DataVO data1 = new DataVO();
        data1.setNum1(1);
        data1.setNum2(2);

        DataVO data2 = new DataVO();
        data2.setNum1(10);
        data2.setNum2(5);

        DataVO data3 = new DataVO();
        data3.setNum1(11);
        data3.setNum2(11);

        List<DataVO> result = new ArrayList<>();
        result.add(data1);
        result.add(data2);
        result.add(data3);

        return result;
    }

    @Test
    public void add2() throws Exception{
        int result = calcService.addForObject(data);
        log.info("...............result2=" + result);
    }

}