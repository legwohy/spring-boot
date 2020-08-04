package com.cobra.service;

import com.cobra.AbstractTest;
import com.cobra.entity.DataVO;
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
 * tip:
 * 1、Parameterized 只能支持一个构造 因此 add1和add2方法 请选择对应的构造方法
 * 2、若是
 *      springboot项目 请使用注解 @SpringbootTest 配置文件默认 aoolication.yml或application.properties
 *      mvc项目 使用@ContextConfiguration加载上下文 并指定配置文件
 */
@Slf4j
@RunWith(Parameterized.class)
//@ContextConfiguration(locations = {"classpath:application.properties"})

public class CalcServiceTest extends AbstractTest{

    private int  num1;
    private int num2;
   /* public CalcServiceTest(Integer num1,Integer num2){
        this.num1 = num1;
        this.num2 = num2;
    }*/

    private DataVO data;
    public CalcServiceTest(DataVO data){
        this.data = data;
    }
    @Before
    public void setUp() throws Exception {
        // 依赖注入
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }
    @Autowired private CalcService calcService;

    /**
     * 返回list 多组参数
     * 连个参数 放在 Object[] 中
     * @return
     */
    @Parameterized.Parameters(name = "{index}:{0},{1}")
    public static List<Object[]> getParameter(){
        Object[] arr1 = new Object[]{1,2};
        Object[] arr2 = new Object[]{3,8};
        Object[] arr3 = new Object[]{4,9};

        List<Object[]> result = new ArrayList<>();
        result.add(arr1);
        result.add(arr2);
        result.add(arr3);

        return result;
    }
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
    public void add() throws Exception {
       int result = calcService.add(num1,num2);
       log.info(".......result="+result);
    }

    @Test
    public void add2() throws Exception {
        int result = calcService.add2(data);
        log.info("...............result2="+result);
    }

}