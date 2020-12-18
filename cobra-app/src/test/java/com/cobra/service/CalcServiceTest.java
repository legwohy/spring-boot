package com.cobra.service;

import com.cobra.AbstractTest;
import com.cobra.domain.pojo.DataVO;
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
 * 参数化测试 普通参数
 * tip:
 * 1、Parameterized 只能支持一个构造 因此 add1和add2方法 请选择对应的构造方法
 */
@Slf4j
@RunWith(Parameterized.class)
public class CalcServiceTest extends AbstractTest {
    /** 参数1*/
    private int num1;
    /** 参数2*/
    private int num2;

    /** 计算结果*/
    private int result;

    /**
     * 参数化测试需要依赖注入
     * @throws Exception
     */
    @Before
    public void init() throws Exception{
        // 依赖注入
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    /**
     * 参数化测试 只能支持一个有参构造
     * @param num1
     * @param num2
     * @param result
     */
    public CalcServiceTest(Integer num1, Integer num2,Integer result){
        this.num1 = num1;
        this.num2 = num2;

        this.result = result;
    }

    @Autowired
    private CalcService calcService;

    /**
     * 返回list 多组参数
     * 连个参数 放在 Object[] 中
     *
     * name 可以省略
     *  {index}  循环执行的索引
     *  {0} 第一个参数的值
     *  {1} 第二个参数值
     * @return
     */
    @Parameterized.Parameters(name = "{index}:{0},{1}")
    public static List<Object[]> getParameter(){
        Object[] arr1 = new Object[] {1, 2,3};
        Object[] arr2 = new Object[] {3, 8,11};
        Object[] arr3 = new Object[] {4, 9,13};

        List<Object[]> result = new ArrayList<>();
        result.add(arr1);
        result.add(arr2);
        result.add(arr3);

        return result;
    }

    @Test
    public void add() throws Exception{
        Assert.assertEquals(result, calcService.add(num1, num2));
    }

}